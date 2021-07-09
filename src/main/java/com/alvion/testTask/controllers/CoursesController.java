package com.alvion.testTask.controllers;


import com.alvion.testTask.models.Coures_Students;
import com.alvion.testTask.models.Courses;
import com.alvion.testTask.models.Professors;
import com.alvion.testTask.models.Students;
import com.alvion.testTask.repo.Coures_StuedntsRepository;
import com.alvion.testTask.repo.CoursesRepository;
import com.alvion.testTask.repo.ProfessorsRepository;
import com.alvion.testTask.repo.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class CoursesController {

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private Coures_StuedntsRepository coures_stuedntsRepository;

    @Autowired
    private ProfessorsRepository professorsRepository;

    @GetMapping("/courses")
    public String showAllCourses(Model model){
        Iterable<Courses> courses = coursesRepository.findAll();
        model.addAttribute("courses", courses);
        return "courses";
    }


    @GetMapping("/courses/add")
    public String addProfessors(Model model){

        return "course-add";
    }

    @PostMapping("/courses/add")
    public String addCourse(@RequestParam String name, @RequestParam String number, @RequestParam double price, @RequestParam Long professor,  Model model){
        if(professorsRepository.existsById(professor)){
            Courses course = new Courses(name, number, price, professor);
            coursesRepository.save(course);
            return "redirect:/courses";
        }
        else{
            model.addAttribute("error", "Выбран несуществующий профессор");
            model.addAttribute("name", name);
            model.addAttribute("number", number);
            model.addAttribute("price", price);
            model.addAttribute("professor", professor);
            return "course-add";
        }

    }

    @GetMapping("/courses/{id}")
    public String showСourse(@PathVariable(value = "id") long id, Model model){
        if (!coursesRepository.existsById(id)){
            return "redirect:/courses";
        }

        Optional<Courses> course = coursesRepository.findById(id);
        ArrayList<Courses> res = new ArrayList<>();
        course.ifPresent(res::add);
        model.addAttribute("course", res);
        return "course-details";
    }

    @GetMapping("/courses/{id}/edit")
    public String courseEdit(@PathVariable(value = "id") long id, Model model){
        if (!coursesRepository.existsById(id)){
            return "redirect:/courses";
        }

        Optional<Courses> course = coursesRepository.findById(id);
        ArrayList<Courses> res = new ArrayList<>();
        course.ifPresent(res::add);
        model.addAttribute("course", res);
        return "course-edit";
    }

    @PostMapping("/courses/{id}/edit")
    public String courseUpdate(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam String number, @RequestParam double price, @RequestParam Long professor, Model model){
        Courses course = coursesRepository.findById(id).orElseThrow();

        if (professorsRepository.existsById(professor)){
            course.setName(name);
            course.setNumber(number);
            course.setPrice(price);
            course.setProfessor(professor);
            coursesRepository.save(course);
            return "redirect:/courses";
        }
        else{
            model.addAttribute("error", "Выбран несуществующий профессор");
            Courses courses = new Courses(name, number, price, professor);
            model.addAttribute("course", courses);
            return "course-edit";
        }

    }

    @PostMapping("/courses/{id}/remove")
    public String courseDelete(@PathVariable(value = "id") long id, Model model){
        Courses course = coursesRepository.findById(id).orElseThrow();

        Iterable<Coures_Students> c_s = coures_stuedntsRepository.findAllByCourse(id);

        for (Coures_Students c_s1: c_s){
            coures_stuedntsRepository.delete(c_s1);
        }
        coursesRepository.delete(course);
        return "redirect:/courses";
    }

    @GetMapping("/courses/{id}/enroll")
    public String studentEnroll(@PathVariable(value = "id") long id, Model model){
        Iterable<Students> students = studentsRepository.findAll();
        model.addAttribute("students", students);
        model.addAttribute("id_course", id);
        return "course-enroll";
    }

    @GetMapping("/courses/{id_course}/enroll/{id_student}")
    public String studentEnrollCoures(@PathVariable(value = "id_course") long id_course, @PathVariable(value = "id_student") long id_student, Model model){
        Iterable<Students> students = studentsRepository.findAll();

        Iterable<Coures_Students> c_s1 = coures_stuedntsRepository.findAllByCourseAndStudent(id_course, id_student);
        for(Coures_Students c_s11: c_s1){
            model.addAttribute("id_course", id_course);
            return "redirect:/courses/" + id_course + "/enroll";
        }
        Coures_Students cs = new Coures_Students(id_student, id_course);
        coures_stuedntsRepository.save(cs);
        model.addAttribute("students", students);
        model.addAttribute("id_course", id_course);
        return "redirect:/courses/" + id_course + "/enroll";
    }

    @GetMapping("/courses/{id_course}/students")
    public String courseStudent(@PathVariable(value = "id_course") long id_course, Model model){
        Iterable<Coures_Students> c_s = coures_stuedntsRepository.findAllByCourse(id_course);

        ArrayList<Students> res = new ArrayList<>();
        for (Coures_Students c_s1: c_s){
            Optional<Students> student = studentsRepository.findById(c_s1.getId_student());
            student.ifPresent(res::add);
        }
        String courseName = "";
        Optional<Courses> course = coursesRepository.findById(id_course);
        ArrayList<Courses> temp = new ArrayList<>();
        course.ifPresent(temp::add);
        for (Courses tmp: temp){
            courseName = tmp.getName();
        }

        model.addAttribute("students", res);
        model.addAttribute("courseName", courseName);
        model.addAttribute("id_course", id_course);
        return "course-students";
    }

    @GetMapping("/courses/{id_course}/{id_student}/marks")
    public String writeMarks(@PathVariable(value = "id_course") long id_course, @PathVariable(value = "id_student") long id_student, Model model){

        Iterable<Coures_Students> c_s = coures_stuedntsRepository.findAllByCourseAndStudent(id_course, id_student);

        String studentName = "";
        Optional<Students> student = studentsRepository.findById(id_student);
        ArrayList<Students> temp = new ArrayList<>();
        student.ifPresent(temp::add);
        for (Students tmp: temp){
            studentName = tmp.getFio();
        }

        model.addAttribute("studentName", studentName);
        model.addAttribute("marks", c_s);
        return "course-marks";
    }

    @PostMapping("/courses/{id_course}/{id_student}/marks")
    public String marksUpdate(@PathVariable(value = "id_course") long id_course, @PathVariable(value = "id_student") long id_student, @RequestParam String marks, Model model){
       Iterable <Coures_Students> c_s = coures_stuedntsRepository.findAllByCourseAndStudent(id_course, id_student);

       for(Coures_Students c_s1: c_s){
           c_s1.setMarks(marks);
           coures_stuedntsRepository.save(c_s1);
       }

       String [] marksArrayTemp = marks.split(",");
       int [] marksArray = new int[marksArrayTemp.length];
       int markCount = 0;
       int sum = 0;
       for (int i = 0; i < marksArrayTemp.length; i++){
            marksArray[i] = Integer.parseInt(marksArrayTemp[i]);
            markCount = i+1;
            sum += marksArray[i];
       }

       double tempAvarageScore = 0;
       if (markCount != 0){
           tempAvarageScore = sum/markCount;
       }

       Students student = studentsRepository.findById(id_student).orElseThrow();

       double curAverageScore = student.getAverage_score();
       if (curAverageScore != 0){
           student.setAverage_score((curAverageScore+tempAvarageScore)/2);;
       }
       else{
           student.setAverage_score(tempAvarageScore);
       }
       studentsRepository.save(student);
        return "redirect:/courses/"+id_course+"/students";
    }

}

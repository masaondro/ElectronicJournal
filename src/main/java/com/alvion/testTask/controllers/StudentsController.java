package com.alvion.testTask.controllers;


import com.alvion.testTask.models.Coures_Students;
import com.alvion.testTask.models.Courses;
import com.alvion.testTask.models.Students;
import com.alvion.testTask.repo.Coures_StuedntsRepository;
import com.alvion.testTask.repo.CoursesRepository;
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
public class StudentsController {

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private Coures_StuedntsRepository coures_stuedntsRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @GetMapping("/students")
    public String showAllStudents(Model model){
        Iterable<Students> students = studentsRepository.findAll();
        model.addAttribute("mainText", "Список студентов");
        model.addAttribute("students", students);
        return "students";
    }


    @GetMapping("/students/add")
    public String addStudents(Model model){
        return "student-add";
    }

    @PostMapping("/students/add")
    public String addStudent(@RequestParam String fio, @RequestParam String email, @RequestParam String adres, @RequestParam String phone, @RequestParam String record_book,  Model model){
        Students student = new Students(fio, email, adres, phone, record_book);
        studentsRepository.save(student);
        return "redirect:/students";
    }

    @GetMapping("/students/{id}")
    public String showStudent(@PathVariable(value = "id") long id, Model model){
        if (!studentsRepository.existsById(id)){
            return "redirect:/students";
        }

        Optional<Students> student = studentsRepository.findById(id);
        ArrayList<Students> res = new ArrayList<>();
        student.ifPresent(res::add);
        model.addAttribute("student", res);
        return "student-details";
    }

    @GetMapping("/students/{id}/edit")
    public String studentEdit(@PathVariable(value = "id") long id, Model model){
        if (!studentsRepository.existsById(id)){
            return "redirect:/students";
        }

        Optional<Students> student = studentsRepository.findById(id);
        ArrayList<Students> res = new ArrayList<>();
        student.ifPresent(res::add);
        model.addAttribute("student", res);
        return "student-edit";
    }

    @PostMapping("/students/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long id, @RequestParam String fio, @RequestParam String email, @RequestParam String adres, @RequestParam String phone, @RequestParam String record_book, Model model){
        Students student = studentsRepository.findById(id).orElseThrow();

        student.setFio(fio);
        student.setAdres(adres);
        student.setEmail(email);
        student.setPhone(phone);
        student.setRecord_book(record_book);
        studentsRepository.save(student);
        return "redirect:/students";
    }

    @PostMapping("/students/{id}/remove")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model){
        Students student = studentsRepository.findById(id).orElseThrow();
        Iterable<Coures_Students> c_s = coures_stuedntsRepository.findAllByStudent(id);

        for (Coures_Students c_s1: c_s){
            coures_stuedntsRepository.delete(c_s1);
        }
        studentsRepository.delete(student);
        return "redirect:/students";
    }

    @GetMapping("/students/{id_student}/courses")
    public String courseStudent(@PathVariable(value = "id_student") long id_student, Model model){
        Iterable<Coures_Students> c_s = coures_stuedntsRepository.findAllByStudent(id_student);

        ArrayList<Courses> res = new ArrayList<>();
        for (Coures_Students c_s1: c_s){
            Optional<Courses> course = coursesRepository.findById(c_s1.getCourse());
            course.ifPresent(res::add);
        }
        String studentName = "";
        Optional<Students> student = studentsRepository.findById(id_student);
        ArrayList<Students> temp = new ArrayList<>();
        student.ifPresent(temp::add);
        for (Students tmp: temp){
            studentName = tmp.getFio();
        }

        model.addAttribute("students", res);
        model.addAttribute("studentName", studentName);
        return "student-courses";
    }
}

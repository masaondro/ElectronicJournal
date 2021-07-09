package com.alvion.testTask.controllers;


import com.alvion.testTask.models.Coures_Students;
import com.alvion.testTask.models.Courses;
import com.alvion.testTask.models.Professors;
import com.alvion.testTask.models.Students;
import com.alvion.testTask.repo.Coures_StuedntsRepository;
import com.alvion.testTask.repo.CoursesRepository;
import com.alvion.testTask.repo.ProfessorsRepository;
import com.alvion.testTask.repo.StudentsRepository;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class ProfessorController {

    @Autowired
    private ProfessorsRepository professorsRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private Coures_StuedntsRepository coures_stuedntsRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    @GetMapping("/professors")
    public String showAllProfessors(Model model){
        Iterable<Professors> professors = professorsRepository.findAll();
        model.addAttribute("professors", professors);
        return "professors";
    }


    @GetMapping("/professors/add")
    public String addProfessors(Model model){
        return "professor-add";
    }

    @PostMapping("/professors/add")
    public String addProfessor(@RequestParam String fio, @RequestParam String adres, @RequestParam String phone, @RequestParam double salary,  Model model){
        Professors professor = new Professors(fio, adres, phone, salary);

        professorsRepository.save(professor);
        return "redirect:/professors";
    }

    @GetMapping("/professors/{id}")
    public String showProfessor(@PathVariable(value = "id") long id, Model model){
        if (!professorsRepository.existsById(id)){
            return "redirect:/professors";
        }

        Optional<Professors> professor = professorsRepository.findById(id);
        ArrayList<Professors> res = new ArrayList<>();
        professor.ifPresent(res::add);
        model.addAttribute("professor", res);
        return "professor-details";
    }

    @GetMapping("/professors/{id}/edit")
    public String professorEdit(@PathVariable(value = "id") long id, Model model){
        if (!professorsRepository.existsById(id)){
            return "redirect:/professors";
        }

        Optional<Professors> professor = professorsRepository.findById(id);
        ArrayList<Professors> res = new ArrayList<>();
        professor.ifPresent(res::add);
        model.addAttribute("professor", res);
        return "professor-edit";
    }

    @PostMapping("/professors/{id}/edit")
    public String professorUpdate(@PathVariable(value = "id") long id, @RequestParam String fio, @RequestParam String adres, @RequestParam String phone, @RequestParam double salary, Model model){
        Professors professor = professorsRepository.findById(id).orElseThrow();

        professor.setFio(fio);
        professor.setAdres(adres);
        professor.setPhone(phone);
        professor.setSalary(salary);
        professorsRepository.save(professor);
        return "redirect:/professors";
    }

    @PostMapping("/professors/{id}/remove")
    public String professorDelete(@PathVariable(value = "id") long id, Model model){
        Professors professor = professorsRepository.findById(id).orElseThrow();

        professorsRepository.delete(professor);
        return "redirect:/professors";
    }

    @GetMapping("/professors/{id}/courses")
    public String professorCourses(@PathVariable(value = "id") long id, Model model){
        if (!professorsRepository.existsById(id)){
            return "redirect:/professors";
        }

        Optional<Professors> professor = professorsRepository.findById(id);
        ArrayList<Professors> res = new ArrayList<>();
        professor.ifPresent(res::add);

        Iterable<Courses> courses = coursesRepository.findAllByProfessor(id);

        model.addAttribute("professor", res);
        model.addAttribute("courses", courses);
        return "professor-courses";
    }

    @GetMapping("/professors/burden")
    public String professorsBurden(Model model) throws IOException {
        //Список нужной нам информации
        ArrayList<ProfessorBurden> professorBurdens = new ArrayList<>();
        //Получаем всех профессоров
        Iterable<Professors> professors = professorsRepository.findAll();
        //Обрабатываем каждого профессора
        for (Professors prof:professors){
            String name = prof.getFio();
            //Ищем количество студентов по всем курсам
            //1.Находим все курсы профессора
            Iterable<Courses> courses = coursesRepository.findAllByProfessor(prof.getId());
            int students = 0;
            double averageScore = 0;
            double sum = 0;
            int studentWithMarks = 0;
            //Ищем количество студентов на каждом курсе
            //И заодно вычисляем средний балл
            for (Courses cor: courses){
                Iterable<Coures_Students> c_s = coures_stuedntsRepository.findAllByCourse(cor.getId());
                for (Coures_Students c_s1: c_s){
                    students += 1;
                    //Optional<Students> student = studentsRepository.findById(c_s1.getId_student());
                    //ArrayList<Students> res = new ArrayList<>();
                    //student.ifPresent(res::add);
                    //for(Students el:res){
                        //if (el.getAverage_score() != 0){
                            //sum += el.getAverage_score();
                            //studentWithMarks += 1;
                        //}
                    if (c_s1.getMarks() != ""){
                        String [] marksArrayTemp = c_s1.getMarks().split(",");
                        int [] marksArray = new int[marksArrayTemp.length];
                        int markCount = 0;
                        int sumMarks = 0;
                        for (int i = 0; i < marksArrayTemp.length; i++){
                            marksArray[i] = Integer.parseInt(marksArrayTemp[i]);
                            markCount = i+1;
                            sumMarks += marksArray[i];
                        }
                        sum += sumMarks/markCount;
                        studentWithMarks += 1;
                    }
                }
            }
            if (students != 0){
                averageScore = sum/studentWithMarks;
            }

            ProfessorBurden pr = new ProfessorBurden(name, students, averageScore);
            professorBurdens.add(pr);
        }

        write("professorsBurdens.xlsx", professorBurdens);
        model.addAttribute("professors", professorBurdens);
        return "professors-burden";
    }


    public void write(String filename, ArrayList<ProfessorBurden> professorBurdens) throws IOException {
        var workbook = new XSSFWorkbook();
        var sheet = createSheet(workbook);
        createHeader(workbook, sheet);
        createCells(workbook, sheet, professorBurdens);
        try (var outputStream = new FileOutputStream(filename)) {
            workbook.write(outputStream);
        }
        workbook.close();
    }

    private Sheet createSheet(XSSFWorkbook workbook) {
        var sheet = workbook.createSheet("Загруженность преподавательского состава");
        sheet.setColumnWidth(0, 9000);
        sheet.setColumnWidth(1, 9000);
        sheet.setColumnWidth(2, 9000);
        return sheet;
    }

    private void createHeader(XSSFWorkbook workbook, Sheet sheet) {
        var header = sheet.createRow(0);

        var headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        var font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);

        var headerCell = header.createCell(0);
        headerCell.setCellValue("ФИО преподавателя");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Общее кол-во студентов");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Средний балл");
        headerCell.setCellStyle(headerStyle);
    }

    private void createCells(XSSFWorkbook workbook, Sheet sheet, ArrayList<ProfessorBurden> professorBurdens) {
        var style = workbook.createCellStyle();
        style.setWrapText(true);
        var createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd.mm.yyyy"));
        sheet.setDefaultColumnStyle(1, style);

        for (var i = 0; i < professorBurdens.size(); i++) {
            var client = professorBurdens.get(i);
            var row = sheet.createRow(i + 1);

            var cell = row.createCell(0);
            cell.setCellValue(client.fio);

            cell = row.createCell(1);
            cell.setCellValue(client.students);

            cell = row.createCell(2);
            cell.setCellValue(client.average_score);
        }
    }
}

class ProfessorBurden{
    public String fio;
    public int students;
    public double average_score;

    public ProfessorBurden(String fio, int students, double average_score){
        this.fio = fio;
        this.students = students;
        this.average_score = average_score;
    }
}

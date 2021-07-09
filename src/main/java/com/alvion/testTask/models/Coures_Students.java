package com.alvion.testTask.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Coures_Students {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long student;

    private Long course;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }



    private String marks;

    public void setId_student(Long id_student) {
        this.student = id_student;
    }

    public void setCourse(Long course_id) {
        this.course = course_id;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public Long getId_student() {
        return student;
    }

    public Long getCourse() {
        return course;
    }

    public String getMarks() {
        return marks;
    }

    public Coures_Students(Long id_student, Long course_id) {
        this.student = id_student;
        this.course = course_id;
    }

    public Coures_Students(Long id_student, Long course_id, String marks) {
        this.student = id_student;
        this.course = course_id;
        this.marks = marks;
    }

    public Coures_Students() {
    }
}

package com.alvion.testTask.models;

import javax.persistence.*;

@Entity
public class Courses {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name, number;

    private double price;

    private Long professor;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public double getPrice() {
        return price;
    }

    public void setProfessor(Long professor) {
        this.professor = professor;
    }

    public Long getProfessor() {
        return professor;
    }

    public Courses(String name, String number, double price, Long professor) {
        this.name = name;
        this.number = number;
        this.price = price;
        this.professor = professor;
    }

    public Courses() {
    }
}
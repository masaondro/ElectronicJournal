package com.alvion.testTask.models;

import javax.persistence.*;

@Entity
public class Professors {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fio, adres, phone;

    private double salary;


    public void setId(Long id) {
        this.id = id;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public String getFio() {
        return fio;
    }

    public String getAdres() {
        return adres;
    }

    public String getPhone() {
        return phone;
    }

    public double getSalary() {
        return salary;
    }

    public Professors() {
    }

    public Professors(String fio, String adres, String phone, double salary) {
        this.fio = fio;
        this.adres = adres;
        this.phone = phone;
        this.salary = salary;
    }

}
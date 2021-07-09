package com.alvion.testTask.models;

import javax.persistence.*;

@Entity
public class Students {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fio, email, adres, phone, record_book;

    private double average_score;

    public Students(String fio, String email, String adres, String phone, String record_book) {
        this.fio = fio;
        this.email = email;
        this.adres = adres;
        this.phone = phone;
        this.record_book = record_book;
    }

    public Students() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRecord_book(String record_book) {
        this.record_book = record_book;
    }

    public void setAverage_score(double average_score) {
        this.average_score = average_score;
    }

    public Long getId() {
        return id;
    }

    public String getFio() {
        return fio;
    }

    public String getEmail() {
        return email;
    }

    public String getAdres() {
        return adres;
    }

    public String getPhone() {
        return phone;
    }

    public String getRecord_book() {
        return record_book;
    }

    public double getAverage_score() {
        return average_score;
    }
}

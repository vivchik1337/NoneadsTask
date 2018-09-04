package com.noneadstask.model;

/**
 * Created by vivchick on 04.09.2018.
 */

public class Person {
    private String id;
    private String firstname;
    private String lastname;
    private String placeOfWork;
    private String position;
    private String linkPDF;

    public Person() {
    }

    public Person(String id, String firstname, String lastname, String placeOfWork, String position, String linkPDF) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.placeOfWork = placeOfWork;
        this.position = position;
        this.linkPDF = linkPDF;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPlaceOfWork() {
        return placeOfWork;
    }

    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLinkPDF() {
        return linkPDF;
    }

    public void setLinkPDF(String linkPDF) {
        this.linkPDF = linkPDF;
    }
}

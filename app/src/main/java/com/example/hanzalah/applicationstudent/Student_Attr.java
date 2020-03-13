package com.example.hanzalah.applicationstudent;

/**
 * Created by Hanzalah on 2/12/2019.
 */

public class Student_Attr {
    private String id;
    private String Student_Name;
    private String Password;
    private String Contact;
    private String Email;

    public Student_Attr() {
    }

    public Student_Attr(String id, String student_Name, String password, String contact, String email) {
        this.id = id;
        Student_Name = student_Name;
        Password = password;
        Contact = contact;
        Email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudent_Name() {
        return Student_Name;
    }

    public void setStudent_Name(String student_Name) {
        Student_Name = student_Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}

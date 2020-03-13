package com.example.hanzalah.applicationstudent;

public class Payment {
    private String id;
    private String HostelName;
    private String StudentName;
    private String Amount;
    private String StudentContact;

    public Payment() {
    }

    public Payment(String id, String hostelName, String studentName, String amount, String studentContact) {
        this.id = id;
        HostelName = hostelName;
        StudentName = studentName;
        Amount = amount;
        StudentContact = studentContact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostelName() {
        return HostelName;
    }

    public void setHostelName(String hostelName) {
        HostelName = hostelName;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getStudentContact() {
        return StudentContact;
    }

    public void setStudentContact(String studentContact) {
        StudentContact = studentContact;
    }
}

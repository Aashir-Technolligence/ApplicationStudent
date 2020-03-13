package com.example.hanzalah.applicationstudent;

/**
 * Created by Hanzalah on 3/2/2019.
 */

public class Rating_Attr {
    private String Id ;
    private String HostelId;
    private String StudentId;
    private Float Building;
    private Float Food;
    private Float Security;
    private Float Staff;
    private Float Total;
    private String Comment;


    public Rating_Attr() {
    }

    public Rating_Attr(String id, String hostelId, String studentId, Float building, Float food, Float security, Float staff, Float total, String comment) {
        Id = id;
        HostelId = hostelId;
        StudentId = studentId;
        Building = building;
        Food = food;
        Security = security;
        Staff = staff;
        Total = total;
        Comment = comment;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getHostelId() {
        return HostelId;
    }

    public void setHostelId(String hostelId) {
        HostelId = hostelId;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public Float getBuilding() {
        return Building;
    }

    public void setBuilding(Float building) {
        Building = building;
    }

    public Float getFood() {
        return Food;
    }

    public void setFood(Float food) {
        Food = food;
    }

    public Float getSecurity() {
        return Security;
    }

    public void setSecurity(Float security) {
        Security = security;
    }

    public Float getStaff() {
        return Staff;
    }

    public void setStaff(Float staff) {
        Staff = staff;
    }

    public Float getTotal() {
        return Total;
    }

    public void setTotal(Float total) {
        Total = total;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}

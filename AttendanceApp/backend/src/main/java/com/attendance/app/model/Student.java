package com.attendance.app.model;
import jakarta.persistence.*;
import java.util.List;   // ← THIS WAS MISSING (important)
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.*;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String batch;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendance;

    public Student() {}

    public Student(String name, String batch) {
        this.name = name;
        this.batch = batch;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBatch() {
        return batch;
    }

    public List<Attendance> getAttendance() {
        return attendance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setAttendance(List<Attendance> attendance) {
        this.attendance = attendance;
    }
}

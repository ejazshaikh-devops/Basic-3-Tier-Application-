package com.attendance.app.controller;

import com.attendance.app.model.Attendance;
import com.attendance.app.model.Student;
import com.attendance.app.repository.AttendanceRepository;
import com.attendance.app.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendance")
@CrossOrigin
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public AttendanceController(AttendanceRepository attendanceRepository,
                                StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    @PostMapping("/{studentId}")
    public Attendance markAttendance(@PathVariable Long studentId,
                                     @RequestParam boolean present,
                                     @RequestParam String date) {

        Student student = studentRepository.findById(studentId).orElseThrow();

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setPresent(present);
        attendance.setDate(LocalDate.parse(date));

        return attendanceRepository.save(attendance);
    }

    @GetMapping
    public List<Attendance> getAll() {
        return attendanceRepository.findAll();
    }

    @GetMapping("/student/{studentId}")
    public List<Attendance> getByStudent(@PathVariable Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
}

package com.attendance.app.controller;

import com.attendance.app.repository.AttendanceRepository;
import com.attendance.app.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin
public class DashboardController {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public DashboardController(StudentRepository studentRepository,
                               AttendanceRepository attendanceRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {

        long totalStudents = studentRepository.count();
        long totalAttendanceRecords = attendanceRepository.count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("classStrength", totalStudents);
        stats.put("totalAttendanceRecords", totalAttendanceRecords);
        stats.put("teacher", "Alpesh");

        return stats;
    }
}

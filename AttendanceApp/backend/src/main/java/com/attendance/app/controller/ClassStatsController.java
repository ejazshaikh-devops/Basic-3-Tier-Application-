package com.attendance.app.controller;

import com.attendance.app.model.Attendance;
import com.attendance.app.repository.AttendanceRepository;
import com.attendance.app.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/class")
@CrossOrigin
public class ClassStatsController {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public ClassStatsController(AttendanceRepository attendanceRepository,
                                StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    // class strength
    @GetMapping("/strength")
    public long classStrength() {
        return studentRepository.count();
    }

    // class average attendance (overall)
    @GetMapping("/average")
    public double classAverage() {

        long total = attendanceRepository.count();
        if (total == 0) return 0;

        long present = attendanceRepository.findAll()
                .stream()
                .filter(Attendance::isPresent)
                .count();

        return (present * 100.0) / total;
    }

    // class weekly average
    @GetMapping("/weekly-average")
    public double weeklyAverage() {

        LocalDate today = LocalDate.now();
        LocalDate start =
                today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate end = start.plusDays(5);

        List<Attendance> records =
                attendanceRepository.findAll()
                        .stream()
                        .filter(a ->
                                !a.getDate().isBefore(start) &&
                                !a.getDate().isAfter(end))
                        .toList();

        if (records.isEmpty()) return 0;

        long present = records.stream()
                .filter(Attendance::isPresent)
                .count();

        return (present * 100.0) / 6.0;
    }

    // performance score out of 10
    @GetMapping("/performance/{studentId}")
    public double performance(@PathVariable Long studentId) {

        LocalDate today = LocalDate.now();
        LocalDate start =
                today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate end = start.plusDays(5);

        List<Attendance> records =
                attendanceRepository.findByStudentIdAndDateBetween(
                        studentId, start, end);

        long present = records.stream()
                .filter(Attendance::isPresent)
                .count();

        return (present / 6.0) * 10.0;
    }
}

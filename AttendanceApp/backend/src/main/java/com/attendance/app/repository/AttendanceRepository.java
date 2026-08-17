package com.attendance.app.repository;

import com.attendance.app.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudentId(Long studentId);

    List<Attendance> findByStudentIdAndDateBetween(
            Long studentId,
            LocalDate startDate,
            LocalDate endDate
    );
}

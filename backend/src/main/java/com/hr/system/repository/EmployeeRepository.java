package com.hr.system.repository;

import com.hr.system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByStatus(String status);

    List<Employee> findByStatusNot(String status);
}

package com.hr.system.service;

import com.hr.system.entity.Employee;
import com.hr.system.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    public Employee createEmployee(Employee employee) {
        return repository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return repository.findByStatusNot("Resigned");
    }

    public List<Employee> getResignedEmployees() {
        return repository.findByStatus("Resigned");
    }

    public Employee getEmployeeById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = getEmployeeById(id);

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhone(employeeDetails.getPhone());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setDesignation(employeeDetails.getDesignation());
        employee.setDateOfJoining(employeeDetails.getDateOfJoining());
        employee.setStatus(employeeDetails.getStatus());
        employee.setResignationDate(employeeDetails.getResignationDate());
        employee.setResignationReason(employeeDetails.getResignationReason());

        return repository.save(employee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        repository.delete(employee);
    }
}

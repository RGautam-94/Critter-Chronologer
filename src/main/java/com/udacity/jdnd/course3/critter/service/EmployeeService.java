package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.Repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.exception.NotFoundException;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(NotFoundException::new);
    }

    public void setEmployeeAvailability(Set<DayOfWeek> dayOfWeeks, long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(NotFoundException::new);
        employee.setDaysAvailable(dayOfWeeks);
        employeeRepository.save(employee);
    }

    public List<Employee> findEmployeesForService(LocalDate date, Set<EmployeeSkill> skills) {
        List<Employee> employees = employeeRepository
                .getAllByDaysAvailableContains(date.getDayOfWeek()).stream()
                .filter(employee -> employee.getSkills().containsAll(skills))
                .collect(Collectors.toList());
        return employees;
    }
}

package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {


    private CustomerService customerService;
    private EmployeeService employeeService;

    @Autowired
    public UserController(CustomerService customerService, EmployeeService employeeService) {
        this.customerService = customerService;
        this.employeeService = employeeService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return getCustomerDTO(customerService.saveCustomer(customer));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        return customerService
                .allCustomers()
                .stream().map(customer -> getCustomerDTO(customer))
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        return getCustomerDTO(customerService.getCustomerByPetId(petId));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employeeService.saveEmployee(employee);
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return getEmployeeDTO(employeeService.getEmployeeById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setEmployeeAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeService.findEmployeesForService(employeeDTO.getDate(), employeeDTO.getSkills());
        return employees.stream().map(this::getEmployeeDTO).collect(Collectors.toList());
    }

    // Customer to CustomerDTO
    private CustomerDTO getCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        List<Long> petIds = new ArrayList<>();
        List<Pet> petList = customer.getPets();

        if (petList != null) {
            for (Pet pet : petList) {
                petIds.add(pet.getId());
            }
            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }

    // Employee to Employee DTO
    private EmployeeDTO getEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;

    }

}

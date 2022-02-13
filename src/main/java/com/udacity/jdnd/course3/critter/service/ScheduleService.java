package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.Repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.Repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.Repository.PetRepository;
import com.udacity.jdnd.course3.critter.Repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class ScheduleService {


    private ScheduleRepository scheduleRepository;
    private CustomerRepository customerRepository;
    private PetRepository petRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, CustomerRepository customerRepository,
                           PetRepository petRepository, EmployeeRepository employeeRepository) {
        this.scheduleRepository = scheduleRepository;
        this.customerRepository = customerRepository;
        this.petRepository = petRepository;
        this.employeeRepository = employeeRepository;
    }


    public Schedule createSchedule(Schedule schedule, List<Long> eIds, List<Long> pIds) {
        List<Employee> employees = employeeRepository.findAllById(eIds);
        List<Pet> pets = petRepository.findAllById(pIds);
        schedule.setEmployees(employees);
        schedule.setPets(pets);
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedule() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleByPetId(long petId) {
        Pet getPetId = petRepository.findById(petId).orElseThrow(NotFoundException::new);
        return scheduleRepository.getAllByPetsContains(getPetId);
    }

    public List<Schedule> getScheduleByEmployeeId(long employeeId) {
        Employee getEmployeeId = employeeRepository.findById(employeeId).orElseThrow(NotFoundException::new);
        return scheduleRepository.getAllByEmployeesContains(getEmployeeId);
    }

    public List<Schedule> getScheduleByCustomerID(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(NotFoundException::new);
        List<Pet> petList = customer.getPets();
        List<Schedule> scheduleList = new ArrayList<>();
        for (Pet pet : petList) {
            scheduleList.addAll(scheduleRepository.getAllByPetsContains(pet));
        }
        return scheduleList;
    }

}

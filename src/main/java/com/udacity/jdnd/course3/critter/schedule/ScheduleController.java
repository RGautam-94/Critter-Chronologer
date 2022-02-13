package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {


    private ScheduleService scheduleService;


    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        List<Long> petIds = scheduleDTO.getPetIds();
        return getScheduleDTO(scheduleService.createSchedule(schedule, employeeIds, petIds));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedule()
                .stream()
                .map(schedule -> getScheduleDTO(schedule))
                .collect(Collectors.toList());

    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable Long petId) {
        return scheduleService.getScheduleByPetId(petId).stream()
                .map(schedule -> getScheduleDTO(schedule))
                .collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable Long employeeId) {
        return scheduleService.getScheduleByEmployeeId(employeeId).stream()
                .map(schedule -> getScheduleDTO(schedule))
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable Long customerId) {
        return scheduleService.getScheduleByCustomerID(customerId)
                .stream()
                .map(schedule -> getScheduleDTO(schedule))
                .collect(Collectors.toList());
    }

    private static ScheduleDTO getScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        List<Long> employeeId = new ArrayList<>();
        for (Employee employee : schedule.getEmployees()) {
            employeeId.add(employee.getId());
        }
        scheduleDTO.setEmployeeIds(employeeId);
        List<Long> petId = new ArrayList<>();
        for (Pet pet : schedule.getPets()) {
            petId.add(pet.getId());
        }
        scheduleDTO.setPetIds(petId);
        return scheduleDTO;
    }
}

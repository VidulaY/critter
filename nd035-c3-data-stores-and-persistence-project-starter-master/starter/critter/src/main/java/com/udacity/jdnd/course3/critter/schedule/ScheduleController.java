package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    PetService petService;
    
    @Autowired
    ScheduleService scheduleService;

    @Autowired
    EmployeeService employeeService;
    
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CustomerService customerService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertToEntity(scheduleDTO);
        Schedule scheduleCreated = scheduleService.saveSchedule(schedule);
        return convertToDto(scheduleCreated);
    }


    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.findAllSchedules();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(Schedule schedule : schedules){
            scheduleDTOS.add(convertToDto(schedule));
        }
        return scheduleDTOS;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        Pet pet = petService.findPetById(petId);
        List<Schedule> schedules = scheduleService.findScheduleByPet(pet);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(Schedule schedule : schedules){
            scheduleDTOS.add(convertToDto(schedule));
        }
        return scheduleDTOS;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        List<Schedule> scheduleList = scheduleService.findScheduleByEmployee(employee);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(Schedule schedule : scheduleList){
            scheduleDTOS.add(convertToDto(schedule));
        }
        return scheduleDTOS;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        List<Schedule> scheduleList = scheduleService.findScheduleByCustomer(customer);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for(Schedule schedule : scheduleList){
            scheduleDTOS.add(convertToDto(schedule));
        }
        return scheduleDTOS;
    }

    private ScheduleDTO convertToDto(Schedule schedule) {
        ScheduleDTO scheduleDTO = modelMapper.map(schedule, ScheduleDTO.class);
        List<Long> petIds = new ArrayList<>();
        List<Pet> pets = schedule.getPets();
        List<Long> employeeIds = new ArrayList<>();
        List<Employee> employees = schedule.getEmployees();
        for(Pet pet : pets){
            petIds.add(pet.getId());
        }
        scheduleDTO.setPetIds(petIds);
        for(Employee employee : employees){
            employeeIds.add(employee.getId());
        }
        scheduleDTO.setEmployeeIds(employeeIds);
        return scheduleDTO;
    }


    private Schedule convertToEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = modelMapper.map(scheduleDTO, Schedule.class);
        List<Pet> pets = new ArrayList<>();
        List<Long> petIds = scheduleDTO.getPetIds();
        List<Employee> employees = new ArrayList<>();
        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        for(Long petId : petIds){
            pets.add(petService.findPetById(petId));
        }
        schedule.setPets(pets);
        for(Long employeeId : employeeIds){
            employees.add(employeeService.getEmployeeById(employeeId));
        }
        schedule.setEmployees(employees);
        return schedule;
    }

}

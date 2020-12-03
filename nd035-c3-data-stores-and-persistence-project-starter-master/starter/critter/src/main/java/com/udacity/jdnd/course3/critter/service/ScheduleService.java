package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PetRepository petRepository;

    public Schedule saveSchedule(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> findAllSchedules(){
        Iterable<Schedule> iterable = scheduleRepository.findAll();
        List<Schedule> result = new ArrayList<>();
        iterable.forEach(result::add);
        return result;

    }
    public List<Schedule> findScheduleByPet(Pet pet){
        List<Schedule> schedules = findAllSchedules();
        List<Schedule> schedulesByPet = new ArrayList<>();
        for(Schedule schedule : schedules){
            if(schedule.getPets().contains(pet))
                schedulesByPet.add(schedule);
        }
        return schedulesByPet;
    }

    public List<Schedule> findScheduleByEmployee(Employee employee){
        List<Schedule> schedules = findAllSchedules();
        List<Schedule> schedulesByEmployee = new ArrayList<>();
        for(Schedule schedule : schedules){
            if(schedule.getEmployees().contains(employee))
                schedulesByEmployee.add(schedule);
        }
        return schedulesByEmployee;
    }

    public List<Schedule> findScheduleByCustomer(Customer customer){
        List<Pet> pets = customer.getPets();
        List<Schedule> schedulesByCustomer = new ArrayList<>();
        for(Pet pet : pets){
            List<Schedule> schedules = findScheduleByPet(pet);
            for(Schedule schedule : schedules){
                schedulesByCustomer.add(schedule);
            }
        }
        return schedulesByCustomer;
    }
}

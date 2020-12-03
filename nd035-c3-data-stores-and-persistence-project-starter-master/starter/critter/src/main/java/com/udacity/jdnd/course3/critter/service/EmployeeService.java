package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);

    }

    public Employee getEmployeeById(Long id){
        return employeeRepository.findById(id).get();
    }

    public List<Employee> getAllEmployees(){
        Iterable<Employee> iterable = employeeRepository.findAll();
        List<Employee> result = new ArrayList<>();
        iterable.forEach(result::add);
        return result;
    }

    public List<Employee> getEmployeeForService(LocalDate requiredDate, Set<EmployeeSkill> requiredSkills){
        List<Employee> employees = getAllEmployees();
        List<Employee> employeesList = new ArrayList<>();
        for(Employee employee : employees){
            if(employee.getSkills().containsAll(requiredSkills) && employee.getDaysAvailable().contains(requiredDate.getDayOfWeek()))
                employeesList.add(employee);
        }
        return employeesList;
    }

}

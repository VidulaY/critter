package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.checkerframework.checker.units.qual.C;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = convertToEntityCustomer(customerDTO);
        Customer customerCreated = customerService.saveCustomer(customer);
        return convertToDtoCustomer(customerCreated);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        List<Customer> customers = customerService.findAllCustomers();
        for(Customer customer : customers)
            customerDTOS.add(convertToDtoCustomer(customer));
        return customerDTOS;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Pet pet = petService.findPetById(petId);
        Customer customer = customerService.getCustomerById(pet.getCustomer().getId());
        return convertToDtoCustomer(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertToEntityEmployee(employeeDTO);
        Employee employeeCreated = employeeService.saveEmployee(employee);
        return convertToDtoEmployee(employeeCreated);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        return convertToDtoEmployee(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeService.saveEmployee(employee);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeService.getEmployeeForService(employeeDTO.getDate(), employeeDTO.getSkills());
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        for(Employee employee : employees){
            employeeDTOS.add(convertToDtoEmployee(employee));
        }
        return employeeDTOS;
    }

    private CustomerDTO convertToDtoCustomer(Customer customer) {
        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        List<Pet> pets = customer.getPets();
        List<Long> petIds = new ArrayList<>();
        if (pets != null) {
            for (Pet pet : pets) petIds.add(pet.getId());
            customerDTO.setPetIds(petIds);
        } else {
            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }
    private Customer convertToEntityCustomer(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        List<Long> petIds = customerDTO.getPetIds();
        List<Pet> pets = new ArrayList<>();
        if (petIds != null) {
            for (Long id : petIds) pets.add(petService.findPetById(id));
            customer.setPets(pets);
        } else {
            customer.setPets(pets);
        }
        return customer;
    }

    private EmployeeDTO convertToDtoEmployee(Employee employee) {
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
        return employeeDTO;
    }

    private Employee convertToEntityEmployee(EmployeeDTO employeeDTO) {
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        return employee;
    }

}

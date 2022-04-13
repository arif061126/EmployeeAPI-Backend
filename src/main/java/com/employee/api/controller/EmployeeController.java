package com.employee.api.controller;

import com.employee.api.exception.ResourceNotFoundException;
import com.employee.api.model.Employee;
import com.employee.api.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1") //standard url end point we typically use in rest api
public class EmployeeController {
    @Autowired //to inject this repository by spring container
    private EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * get all employees
     * returns list of employees to the clients
     */

    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return this.employeeRepository.findAll();
    }

    /**
     * create new employee rest api
     */
    @PostMapping("/add-employee")
    public void addEmployee(@RequestBody Employee employee){
        System.out.println(employee);
        this.employeeRepository.save(employee);
    }

    /**
     * get employee by id
     * @param employeeId employeeId
     * @return optional object
     */
    @GetMapping("/employees/{employeeId}")
    public Optional<Employee> getEmployeeById(@PathVariable Long employeeId){
        boolean exists = this.employeeRepository.existsById(employeeId);

        if(!exists){
            throw new IllegalStateException("Employee with ID: "+employeeId+" does not exist");
        }

        return this.employeeRepository.findById(employeeId);
    }

    /**
     * update employee by id
     * @param employeeId employeeId
     * @return employee
     */
    @PutMapping("employees/{employeeId}")
    public Employee updateEmployeeById(
            @PathVariable Long employeeId,
            @RequestBody Employee employeeDetails){

        /*boolean exists = this.employeeRepository.existsById(employeeId);

        if(!exists){
            throw new IllegalStateException("Employee with ID: "+employeeId+" does not exist");
        }*/

       // as i wanted to set the values to update entities,
       // but in that case optional variable will not work,
        // because in optional variable, setter method does not work
        //that drives me to do the following:

       Employee employee = this.employeeRepository.findById(employeeId)
               .orElseThrow(
                       ()-> new ResourceNotFoundException("Employee with ID: "+employeeId+" does not exist"));

       employee.setEmployeeFirstName(employeeDetails.getEmployeeFirstName());
       employee.setEmployeeLastName(employeeDetails.getEmployeeLastName());
       employee.setEmployeeEmail(employeeDetails.getEmployeeEmail());

       return this.employeeRepository.save(employee);

       //alternatively i can set the return type of this method> ResponseEntity<Employee>
        //in this case: i have to return> ResponseEntity.Ok(this.employeeRepository.save(employee));

    }

    /**
     * delete api rest controller
     * @param employeeId employeeId
     */
    @DeleteMapping(path="/employees/{employeeId}")
    public void deleteEmployeeById(@PathVariable("employeeId") Long employeeId){
        boolean exists = this.employeeRepository.existsById(employeeId);

        if(!exists){
            throw new IllegalStateException("Employee with ID: "+employeeId+" does not exist");
        }

        this.employeeRepository.deleteById(employeeId);
    }

}

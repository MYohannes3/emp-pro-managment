package org.perscholas.epm.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.perscholas.epm.dao.EmployeeRepo;
import org.perscholas.epm.model.Employee;
import org.perscholas.epm.model.Project;
import org.perscholas.epm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
@Service
@Transactional  // we will avoid lazy initialization
@Slf4j
public class  EmployeeService {
    private EmployeeRepo EmployeeRepo;
    private UserService userService;
@Autowired
    public EmployeeService(EmployeeRepo EmployeeRepo, UserService userService){
        this.EmployeeRepo = EmployeeRepo;
        this.userService = userService;
    }

    public Employee loadEmployeeById(int employeeId) {
        return EmployeeRepo.findById(employeeId).orElseThrow(()->new EntityNotFoundException("Employee with id" +employeeId+"Not Found"));
    }

    public List<Employee> findEmployeesByName(String name) {

    return EmployeeRepo.findEmployeesByName(name);
    }

    public Employee loadEmployeeByEmail(String email) {

        return EmployeeRepo.findEmployeeByEmail(email);
    }

    public Employee createEmployee(String firstName, String lastName, String title, String email, String password) {
        User user =userService.createUser(email,password);
        userService.assignRoleToUser(email, "Employee");
        return EmployeeRepo.save(new Employee(firstName, lastName, title, user));
    }

    public Employee updateEmployee(Employee employee) {

        return EmployeeRepo.save(employee);
    }

    public List<Employee> fetchEmployees() {

        return EmployeeRepo.findAll();
    }

    public void removeEmployee(int employeeId) {
        Employee employee= loadEmployeeById(employeeId);
        Iterator<Project> iterator = employee.getProjects().iterator();
        if(iterator.hasNext()){
            Project project =iterator.next();
            project.removeEmployeeFromProject(employee);
        }
        EmployeeRepo.deleteById(employeeId);
    }
}

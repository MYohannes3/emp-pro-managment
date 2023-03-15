package org.perscholas.epm.controllers;

import org.perscholas.epm.model.Employee;
import org.perscholas.epm.model.User;
import org.perscholas.epm.service.EmployeeService;
import org.perscholas.epm.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private EmployeeService employeeService;
    private UserService userService;

    public EmployeeController(EmployeeService employeeService, UserService userService){
        this.employeeService = employeeService;
        this.userService = userService;
    }

    //allows us to view and search for list of employees
    @GetMapping(value = "/index")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String employees(Model model, @RequestParam(name="keyword", defaultValue = "")String keyword){
        List<Employee> employees = employeeService.findEmployeesByName(keyword);
        model.addAttribute("listEmployees", employees);
        model.addAttribute("keyword", keyword);
        return "employees";
    }

    @GetMapping(value = "/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteStudent(int employeeId, String keyword) {
        employeeService.removeEmployee(employeeId);
        return "redirect:/employees/index?keyword=" + keyword;
    }

    @GetMapping(value = "/updateEmployee")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public String updateEmployee(Model model, Principal principal){
        Employee employee=employeeService.loadEmployeeByEmail(principal.getName());
        model.addAttribute("employee", employee);
        return "updateEmployee";
    }


// updating employee
    @PostMapping(value = "/update")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public String update(Employee employee){
        employeeService.updateEmployee(employee);
        return "redirect:/projects/employeeProjects";
    }

    //creating new employee
    @GetMapping(value = "/createEmployee")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String formEmployees(Model model){
        model.addAttribute("employee", new Employee());
        return "createEmployee";
    }

    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String save(Employee employee, BindingResult bindingResult){
        User user= userService.loadUserByEmail(employee.getUser().getEmail());
        if(user != null)
            bindingResult.rejectValue("user.email",null, "There is already an account registered with this email");
        if (bindingResult.hasErrors()) return "createEmployee";
        employeeService.createEmployee(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getTitle(),
                employee.getUser().getEmail(),
                employee.getUser().getPassword());
        return "redirect:/employees/index";
    }
}

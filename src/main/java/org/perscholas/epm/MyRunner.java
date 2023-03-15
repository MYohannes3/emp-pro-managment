package org.perscholas.epm;

import lombok.extern.slf4j.Slf4j;

import org.perscholas.epm.model.*;
import org.perscholas.epm.service.*;
import org.perscholas.epm.service.EmployeeService;
import org.perscholas.epm.service.ProjectService;
import org.perscholas.epm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyRunner implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RoleService roleService;


    public static final String ADMIN = "Admin";
    public static final String MANAGER = "Manager";
    public static final String EMPLOYEE = "Employee";

    @Override
    public void run(String... args) throws Exception {

        User user1 = userService.createUser("user1@gmail.com", "pass1");
        User user2 = userService.createUser("user2@gmail.com", "pass2");

        roleService.createRole("ADMIN");
       roleService.createRole("MANAGER");
        roleService.createRole("EMPLOYEE");

        userService.assignRoleToUser(user1.getEmail(), ADMIN);
        userService.assignRoleToUser(user1.getEmail(), MANAGER);
        userService.assignRoleToUser(user2.getEmail(), EMPLOYEE);

        Manager manager1= managerService.createManager("Chuchu", "Meklit", "Java Developer", "Manager1@gmail.com", "pass1");
        Manager manager2= managerService.createManager("Galila", "Mama", "Python Developer", "Manager2@gmail.com", "pass2");
        Manager manager3= managerService.createManager("Chuchu", "Meklit", "Java Developer", "Manager3@gmail.com", "pass3");
        Manager manager4= managerService.createManager("Galila", "Mama", "Python Developer", "Manager4@gmail.com", "pass4");

        Employee employee1 =employeeService.createEmployee("Dagmawi", "T", "Phd", "emp1@gmail.com", "pass1");
        Employee employee2 =employeeService.createEmployee("stud2", "st222", "Associate", "emp2@gmail.com", "pass2");
        Employee employee3 =employeeService.createEmployee("Dagmawi", "T", "Bachelor degree", "emp3@gmail.com", "pass3");

        Project project1 = projectService.createProject("EMS","4 weeks","attendance control", manager1.getManagerId());
        Project project2 = projectService.createProject("STMS","3 weeks","let the students enrol to courses", manager2.getManagerId());
        Project project3 = projectService.createProject("EMS","4 weeks","attendance control", manager3.getManagerId());
        Project project4 = projectService.createProject("STMS","3 weeks","let the students enrol to courses", manager4.getManagerId());


        projectService.assignEmployeeToProject(project1.getProjectId(),employee1.getEmployeeId());
        projectService.assignEmployeeToProject(project2.getProjectId(),employee2.getEmployeeId());

    }

}

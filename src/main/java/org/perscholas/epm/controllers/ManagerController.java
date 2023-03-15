package org.perscholas.epm.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.perscholas.epm.model.Manager;
import org.perscholas.epm.model.User;
import org.perscholas.epm.service.ManagerService;
import org.perscholas.epm.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping(value = "/managers")
public class ManagerController {
    ManagerService managerService;
    UserService userService;
    public ManagerController(ManagerService managerService, UserService userService){
        this.managerService =managerService;
        this.userService = userService;
    }

    @GetMapping("/index")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String managers(Model model, @RequestParam(name = "keyword", defaultValue = "")String keyword){
        List<Manager> managers= managerService.findManagersByName(keyword);
        model.addAttribute("listManagers", managers);
        model.addAttribute("keyword", keyword);
        return "managerFiles/managers";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteManager(int managerId, String keyword) {
        managerService.removeManager(managerId);
        return "redirect:/managers/index?keyword=" + keyword;
    }

    @GetMapping(value = "/updateManager")
    @PreAuthorize("hasAuthority('MANAGER')")
    public String updateManager(Model model, Principal principal) {
        //current Instructor
        Manager manager = managerService.loadManagerByEmail(principal.getName());
        model.addAttribute("manager", manager);
        return "managerFiles/updateManager";
    }

    @PostMapping(value = "/update")
    @PreAuthorize("hasAuthority('MANAGER')")
    public String update(Manager manager) {
        managerService.updateManager(manager);
        return "redirect:/projects/managerProjects";
    }


    //only admin has the authority to create Managers.
    @GetMapping(value = "/createManager")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createManager(Model model) {
        model.addAttribute("manager", new Manager());
        return "managerFiles/createManager";
    }

    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String save(@Valid Manager manager, Model model) {
        User user = userService.loadUserByEmail(manager.getUser().getEmail());
        if (user != null) {
            model.addAttribute("errorMessage", "There is an account with that email");
            return "createManager";
        }
        managerService.createManager(manager.getFirstName(),manager.getLastName(),manager.getSummary(), manager.getUser().getEmail(), manager.getUser().getPassword());
        return "redirect:/managers/index";
    }
}

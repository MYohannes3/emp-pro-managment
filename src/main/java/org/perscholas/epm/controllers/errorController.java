package org.perscholas.epm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class errorController {
    @GetMapping("/error")
    public String notAuthorized(){

        return "error";
    }

}

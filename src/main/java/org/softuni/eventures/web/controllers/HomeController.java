package org.softuni.eventures.web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController {
    @GetMapping("/")
    public ModelAndView index() {
        return this.view("index");
    }

    @GetMapping("/home")
    public ModelAndView home(Authentication authentication, ModelAndView modelAndView) {
        modelAndView.addObject("username", authentication.getName());

        if(this.getPrincipalAuthority(authentication) != null
                && this.getPrincipalAuthority(authentication).equals("ADMIN")){
            return this.view("admin-home", modelAndView);
        }

        return this.view("user-home", modelAndView);
    }
}

package org.softuni.eventures.web.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.eventures.domain.models.binding.UserRegisterBindingModel;
import org.softuni.eventures.domain.models.service.UserServiceModel;
import org.softuni.eventures.service.MailService;
import org.softuni.eventures.service.RecaptchaService;
import org.softuni.eventures.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController extends BaseController {

    private final UserService userService;

    private final RecaptchaService recaptchaService;

    private final MailService mailService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, RecaptchaService recaptchaService, MailService mailService, ModelMapper modelMapper) {
        this.userService = userService;
        this.recaptchaService = recaptchaService;
        this.mailService = mailService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return this.view("login");
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return this.view("register");
    }

    @PostMapping("/register")
    public ModelAndView registerConfirm(@ModelAttribute UserRegisterBindingModel userRegisterBindingModel,
                                        @RequestParam(name = "g-recaptcha-response") String gRecaptchaResponse,
                                        HttpServletRequest request) {
        if (!userRegisterBindingModel.getPassword()
                .equals(userRegisterBindingModel.getConfirmPassword())) {
            return this.view("register");
        }

        if (this.recaptchaService
                .verifyRecaptcha(request.getRemoteAddr()
                        , gRecaptchaResponse) == null) {
            return this.view("register");
        }

        this.userService
                .createUser(this.modelMapper
                        .map(userRegisterBindingModel, UserServiceModel.class));

        this.mailService.sentRegistrationSuccessMessage(userRegisterBindingModel.getEmail(),
                userRegisterBindingModel.getUsername());

        return this.redirect("/login");
    }
}

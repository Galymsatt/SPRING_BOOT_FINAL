package com.project.demo.controller;

import com.project.demo.entities.Category;
import com.project.demo.entities.Customer;
import com.project.demo.entities.Role;
import com.project.demo.entities.Users;
import com.project.demo.repositories.RoleRepository;
import com.project.demo.services.CategoryService;
import com.project.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
@RequestMapping(value = "/")
public class MainController {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "/auth_reg")//Ne nuzhno
    public String auth_reg(){
//        return "auth_reg";
        return "register";
    }

    //////////////////////////////////////////USER SETTINGS/////////////////////////////////////////////////

    @GetMapping(value = "/pageRegister")
    public String register(){
        return "anonymous/register";
    }

    @GetMapping(value = "/pageLogin")
    public String login(){
        return "anonymous/login";
    }


    @PostMapping(value = "/register")//Users registration
    public String register(@RequestParam(name = "email") String email,
                           @RequestParam(name = "password") String password,
                           @RequestParam(name = "re-password") String re_password,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "surName") String surName){
        System.out.println();
        String redirect = "redirect:/pageRegister?" + userService.register(email, password, re_password, name, surName);

        return redirect;
    }

    @GetMapping(value = "/adminProfile")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String profile(ModelMap model){

        List<Users> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);

        Role moderator = roleRepository.getOne(3L);
        model.addAttribute("moderator", moderator);

        Role admin = roleRepository.getOne(2L);
        model.addAttribute("admin", admin);

        Role user = roleRepository.getOne(1L);
        model.addAttribute("user", user);

        return "admin/profile";
    }

    @GetMapping(value = "/moderatorProfile")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String profile_moderator(ModelMap model){


        return "moderator/profile";
    }

    @GetMapping(value = "/userProfile")//not finished
    @PreAuthorize("hasRole('ROLE_USER')")
    public String userProfile(ModelMap model){

        Users user = userService.getAuthonticatedUser();
        model.addAttribute("user", user);

        return "user/profile";
    }

    @PostMapping(value = "/refresh")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String refreshUser(@RequestParam(name = "act") String act,
                              @RequestParam(name = "id") Long id,
                              @RequestParam(name = "email", required = false, defaultValue = "") String email,
                              @RequestParam(name = "password", required = false, defaultValue = "") String password,
                              @RequestParam(name = "newPassword", required = false, defaultValue = "") String newPassword,
                              @RequestParam(name = "confirmNewPassword", required = false, defaultValue = "") String confirmNewPassword,
                              @RequestParam(name = "name", required = false, defaultValue = "") String name,
                              @RequestParam(name = "surName", required = false, defaultValue = "") String surName){

        String redirect = "redirect:/userProfile?"+userService.refreshData(id, act, email, password, newPassword, confirmNewPassword, name, surName);

        return redirect;
    }


    @PostMapping(value = "/addUserModerator")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addUserModerator(@RequestParam(name = "email") String email,
                                   @RequestParam(name = "password") String password,
                                   @RequestParam(name = "re-password") String re_password,
                                   @RequestParam(name = "name") String name,
                                   @RequestParam(name = "surName") String surName){

        String redirect = "redirect:/adminProfile?" + userService.register(email, password, re_password, name, surName);

        return redirect;
    }

    @PostMapping(value = "/refPassword")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String refPassword(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "password") String password){

        String redirect = "redirect:/adminProfile?"+userService.refreshPassword(id, password);

        return redirect;
    }

    @PostMapping(value = "/blockUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String blockUser(@RequestParam(name = "id") Long id){

        String redirect = "redirect:/adminProfile?"+userService.blockUser(id);





        return redirect;
    }




    ///////////////////////////////END USER//////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////

    @GetMapping(value = "/")
    public String index(ModelMap model){

        List<Category> categories = categoryService.allCategories();
        model.addAttribute("categories", categories);

        return "user/index";
    }
}

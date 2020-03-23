package com.project.demo.services.impl;

import com.project.demo.entities.Role;
import com.project.demo.entities.Users;
import com.project.demo.repositories.RoleRepository;
import com.project.demo.repositories.UserRepository;
import com.project.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @Autowired
    RoleRepository roleRepository;



    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        Users user = userRepository.findByEmail(s);
        Users user = userRepository.findByEmailAndIsActiveIsTrue(s);
        User securityUser = new User(user.getEmail(), user.getPassword(), user.getRoles());
        return securityUser;
    }

    public Users registerUser(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Users getAuthonticatedUser(){
        Users user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            user = userRepository.findByEmail(secUser.getUsername());
//            user = userRepository.findByEmailAndIsActiveIsTrue(secUser.getUsername());
        }



        return user;
    }

    public Users getUserById(Long id){

        Users user = null;
        Optional<Users> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            user = optionalUser.get();
        }

        return user;
    }

    public String refreshData(Long id, String act, String email, String password, String newPassword, String confirmNewPassword, String name, String surName){

        String redirect ="";
        if(act.equals("email")){
            redirect = refreshEmail(id, email);
        }
        else if(act.equals("password")){
            redirect = refreshPassword(id, password, newPassword, confirmNewPassword);
        }
        else if(act.equals("name")){
            redirect = refreshName(id, name);
        }
        else if(act.equals("surName")){
            redirect = refreshSurName(id, surName);
        }

        return redirect;
    }


    public String refreshEmail(Long id, String email){

        String redirect ="email_refresh_error";
        Users user = getUserById(id);
        Users emailUser = userRepository.findByEmail(email);

        if(user!=null) {
            if (emailUser == null) {
                user.setEmail(email);
                userRepository.save(user);

//                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//                System.out.println(authentication.getPrincipal().toString());
                SecurityContextHolder.clearContext();
//                Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getRoles());
//                SecurityContextHolder.getContext().setAuthentication(authentication);
                //SecurityContextHolder.getContext().getAuthentication().getPrincipal()

//                List<Role> autorities = getAuthorities(user.getRoles());
///////////////////////////////////////////////////zdes rabotalo//////////////////////////////////////////////////////////////////////////
//                User secUser = new User(user.getName(), user.getPassword(), getAuthorities(user.getRoles()));
//                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
//                        secUser, null, user.getRoles());
//                SecurityContextHolder.getContext().setAuthentication(authRequest);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////

//                List<GrantedAuthority> authorities = user.getRoles().stream()//Pahet no ne ponial
//                        .map(p -> new SimpleGrantedAuthority(p.getRole()))
//                        .collect(Collectors.toList());

                UserDetails secUser = loadUserByUsername(user.getEmail());//vmesto UserDetails ya pisal user s bibliotecki UserDetails. Vot takaya tupaya owibka
                Authentication authentication = new UsernamePasswordAuthenticationToken(secUser, null, user.getRoles());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Authenticate the user
//                Authentication authentication = authenticationManager.authenticate(authRequest);
//                SecurityContext securityContext = SecurityContextHolder.getContext();
//                securityContext.setAuthentication(authentication);

                redirect = "email_refreshed";
            }
            else {
                redirect = "error?there_is_such_email";
            }
        }

        return redirect;
    }



    public String refreshName(Long id, String name){

        String redirect ="name_refresh_error";
        Users user = getUserById(id);
        if(user!=null) {
            user.setName(name);
            userRepository.save(user);
            redirect = "name_refreshed";

        }

        return redirect;
    }

    public String refreshSurName(Long id, String surName){

        String redirect ="surName_refresh_error";
        Users user = getUserById(id);
        if(user!=null) {
            user.setSurname(surName);
            userRepository.save(user);
            redirect = "surName_refreshed";

        }

        return redirect;
    }

    public String refreshPassword(Long id, String oldPass, String newPass, String confirmNewPass){

        String redirect = "password_refresh_error";
        Users user = getUserById(id);
        System.out.println("refreshPassword: \n Old:"+user.getPassword()+"\n Password: "+passwordEncoder.encode(oldPass));

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        boolean isPasswordMatches=bcrypt.matches(oldPass, user.getPassword());//Sopostovlieam poroli

        if(user!=null){
//            if(true) {//user.getPassword().equals(passwordEncoder.encode(oldPass))//ne pashet
            if(isPasswordMatches) {//Vot pashet
                if(newPass.equals(confirmNewPass)) {
                    user.setPassword(newPass);
                    registerUser(user);
                    userRepository.save(user);
                    redirect = "password_refreshed";
                }
                else {
                    redirect = "error_invalid_new_password";
                }
            }
            else {
                redirect = "error_invalid_old_password";
            }
        }

        return redirect;
    }

    public String refreshPassword(Long id, String newPass){

        String redirect = "password_refresh_error";
        Users user = getUserById(id);

        if(user!=null){
            user.setPassword(newPass);
            registerUser(user);
//            userRepository.save(user);
            redirect = "password_refreshed";
        }


        return redirect;
    }

    ///////////////////////////////////////////////////////////////////////

    public String register(String email, String password, String re_password, String name, String surName){

        String redirect = "registration_error?there_is_such_email";

        Users user = userRepository.findByEmail(email);
        if(user==null){
            if(password.equals(re_password)){
                Set<Role> roles = new HashSet<>();
                Role userRole = roleRepository.getOne(1l);
                roles.add(userRole);


                user = new Users(null, email, password, name, surName, true, roles);
                registerUser(user);//osy zherge kelgen zat kaida ketedi?
                redirect = "registration_success";
            }
            else {
                redirect = "registration_error?password_not_same";
            }
        }

        return redirect;

    }

    public String blockUser(Long id){

        String redirect = "user_block_error";

        Users user = getUserById(id);
        if(user!=null){
            user.setIsActive(false);
            userRepository.save(user);
            redirect = "user_blocked";
        }

        return redirect;
    }

    public List<Users> getAllUsers(){
        List<Users> allUsers = userRepository.findAll();

        return allUsers;
    }
}

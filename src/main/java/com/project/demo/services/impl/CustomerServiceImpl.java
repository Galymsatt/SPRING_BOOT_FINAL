package com.project.demo.services.impl;

import com.project.demo.entities.Customer;
import com.project.demo.entities.Users;
import com.project.demo.repositories.CustomerRepository;
import com.project.demo.repositories.UserRepository;
import com.project.demo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Customer getAuthonticatedCustomer(){
        Customer customer = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
//            customer = customerRepository.findByEmail(secUser.getUsername()).get();
            customer = userRepository.findByEmailQuery(secUser.getUsername());

            System.out.println("sss");
        }

        return customer;
    }
}

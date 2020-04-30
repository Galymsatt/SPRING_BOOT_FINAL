package com.project.demo.repositories;

import com.project.demo.entities.Customer;
import com.project.demo.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer , Long> {

//    Customer findByEmail(String email);//base implementation
    Optional<Customer> findByEmail(String email);//i think by this i will able to get null if there is no such email, i assume
    @Query("SELECT c FROM Users c WHERE c.email=?1")
    Object findByEmailQuery(String email);

}

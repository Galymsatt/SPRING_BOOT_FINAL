package com.project.demo.repositories;

import com.project.demo.entities.Customer;
import com.project.demo.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);
    Users findByEmailAndIsActiveIsTrue(String email);
    Optional<Users> findById(Long id);

//    Customer findByEmail(String email);
    @Query("SELECT c FROM Users c WHERE c.email=?1")
    Customer findByEmailQuery(String email);


}

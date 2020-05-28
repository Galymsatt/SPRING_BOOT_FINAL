package com.project.demo.repositories;

import com.project.demo.entities.Cart;
import com.project.demo.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByCustomer_Id(Long id);
//    Cart findById(Long id);

}

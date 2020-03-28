package com.project.demo.repositories;

import com.project.demo.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    Optional<Product> findById(Long id);
    Page<Product> findAllByCategoryId(Long id, Pageable pageable);

}

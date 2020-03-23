package com.project.demo.repositories;

import com.project.demo.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAll();
    List<Category> findAllByDeletedAtIsNull();
    Optional<Category> findById(Long id);

}

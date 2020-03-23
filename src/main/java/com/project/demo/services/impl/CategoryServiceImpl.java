package com.project.demo.services.impl;

import com.project.demo.entities.Category;
import com.project.demo.repositories.CategoryRepository;
import com.project.demo.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public String addCategory(String name) {

        String response="category_added";
        Category category = new Category(name);
        categoryRepository.save(category);

        return response;

    }

    @Override
    public List<Category> allCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @Override
    public Category categoryById(Long id) {
        Category category = categoryRepository.findById(id).orElse(new Category());
        return category;
    }

    @Override
    public String editCategoryName(Long id, String name) {

        String response="name_editted";

        Category category = categoryRepository.findById(id).orElse(null);
        category.setName(name);
        category.setUpdatedAt(new Date());
        categoryRepository.save(category);

        return response;
    }

    @Override
    public String deleteCategory(Long id) {

        String response = "category_deleted";

        Category category = categoryRepository.findById(id).orElse(null);
        category.setDeletedAt(new Date());
        categoryRepository.save(category);

        return response;
    }

    @Override
    public String restoreCategory(Long id) {

        String response = "category_restored";

        Category category = categoryRepository.findById(id).orElse(null);
        category.setDeletedAt(null);
        categoryRepository.save(category);

        return response;
    }
}

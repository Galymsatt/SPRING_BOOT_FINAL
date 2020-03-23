package com.project.demo.controller;

import com.project.demo.entities.Category;
import com.project.demo.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller//RestController pozvoliet otpravliat zapros s postmana i td
@RequestMapping(value = "category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;


    @GetMapping(value = "/pageAddCategory")
//    @PreAuthorize("hasRole('ROLE_MODERATOR')")//kak zakonchu nuzhno podkluchit
    public String pageAddCategory(){

        return "moderator/addCategory";
    }

    @PostMapping(value = "/addCategory")
//    @PreAuthorize("hasRole('ROLE_MODERATOR')")//kak zakonchu nuzhno podkluchit
    public String addCategory(@RequestParam String name){

        String redirect="redirect:/category/pageAddCategory?";

        redirect+=categoryService.addCategory(name);

        return redirect;
    }

    @GetMapping(value = "/pageCategoriesList")
//    @PreAuthorize("hasRole('ROLE_MODERATOR')")//kak zakonchu nuzhno podkluchit
    public String pageCategoriesList(ModelMap model){

        List<Category> categories = categoryService.allCategories();
        model.addAttribute("categories", categories);

        return "moderator/categoriesList";
    }

    @GetMapping(value = "/pageCategoryEdit/{id}")
    public String pageCategoryEdit(ModelMap model,
                                   @PathVariable(name = "id") Long id){

        Category category = categoryService.categoryById(id);
        model.addAttribute("category", category);

        return "moderator/editCategory";
    }

    @PostMapping(value = "/editCategoryName")
    public String editCategoryName(@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "name") String name){

        String redirect="redirect:/category/pageCategoryEdit/"+id+"?";

        redirect+=categoryService.editCategoryName(id, name);

        return redirect;
    }

    @PostMapping(value = "/deleteCategory")
    public String deleteCategory(@RequestParam(name = "id") Long id){

        String redirect="redirect:/category/pageCategoriesList?";

        redirect+=categoryService.deleteCategory(id);

        return redirect;
    }

    @PostMapping(value = "/restoreCategory")
    public String restoreCategory(@RequestParam(name = "id") Long id){

        String redirect="redirect:/category/pageCategoriesList?";

        redirect+=categoryService.restoreCategory(id);

        return redirect;
    }
}

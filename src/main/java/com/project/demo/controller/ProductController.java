package com.project.demo.controller;


import com.project.demo.entities.Category;
import com.project.demo.entities.Product;
import com.project.demo.services.CategoryService;
import com.project.demo.services.ProductService;
import com.project.demo.services.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller//RestController pozvoliet otpravliat zapros s postmana i td
@RequestMapping(value = "product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @PostMapping(value = "/addProduct")
//    @PreAuthorize("hasRole('ROLE_MODERATOR')")//kak zakonchu nuzhno podkluchit
    public String addProduct(@RequestParam(name = "name") String name,
                             @RequestParam(name = "categoryId") Long categoryId,
                             @RequestParam(name = "content") String content,
                             @RequestParam(name = "price") double price,
                             @RequestParam(name = "image")MultipartFile image){

        String redirect="redirect:/product/pageAddProduct?";

        redirect += productService.addProduct(name, categoryId, content, price, image);


        return redirect;
    }

    @GetMapping(value = "/pageAddProduct")
//    @PreAuthorize("hasRole('ROLE_MODERATOR')")//kak zakonchu nuzhno podkluchit
    public String pageAddProduct(ModelMap model){

        List<Category> categoryList = categoryService.allCategories();
        model.addAttribute("categoryList", categoryList);

//        Map<Long, String> categoryMap = new HashMap<>();//Mozhno bylo cherez map otpravit, no listom mense koda
//        for(Category c : categoryList){
//            categoryMap.put(c.getId(), c.getName());
//        }
//        model.addAttribute("categoryMap", categoryMap);


        return "moderator/addProduct";
    }


    @GetMapping(value = "/pageProductsList")
//    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String pageProductsList(ModelMap model){

        List<Product> products = productService.allProducts();
        model.addAttribute("products", products);

        return "moderator/productsList";
    }

    @GetMapping(value = "/pageProductEdit/{id}")
    public String pageProductEdit(ModelMap model,
                                  @PathVariable(name = "id") Long id){

        Product product = productService.productById(id);
        model.addAttribute("product", product);

        List<Category> categoryList = categoryService.allCategories();
        model.addAttribute("categoryList", categoryList);

        return "moderator/editProduct";
    }

    @PostMapping(value = "/editProductName")
    public String editProductName(@RequestParam(name = "id") Long id,
                                  @RequestParam(name = "name") String name){

        String redirect="redirect:/product/pageProductEdit/"+id+"?";

        redirect+=productService.editProductName(id, name);

        return redirect;
    }

    @PostMapping(value = "/editProductCategory")
    public String editProductCategory(@RequestParam(name = "id") Long id,
                                      @RequestParam(name = "categoryId") Long categoryId){

        String redirect="redirect:/product/pageProductEdit/"+id+"?";

        redirect+=productService.editProductCategory(id, categoryId);

        return redirect;
    }

    @PostMapping(value = "/editProductContent")
    public String editProductContent(@RequestParam(name = "id") Long id,
                                     @RequestParam(name = "content") String content){

        String redirect="redirect:/product/pageProductEdit/"+id+"?";

        redirect+=productService.editProductContent(id, content);

        return redirect;
    }

    @PostMapping(value = "/editProductPrice")
    public String editProductPrice(@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "price") double price){

        String redirect="redirect:/product/pageProductEdit/"+id+"?";

        redirect+=productService.editProductPrice(id, price);

        return redirect;
    }

    @PostMapping(value = "/editProductImage")
    public String editProductImage(@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "image") MultipartFile image){

        String redirect="redirect:/product/pageProductEdit/"+id+"?";

        redirect+=productService.editProductImage(id, image);

        return redirect;
    }

    @PostMapping(value = "/deleteProduct")
    public String deleteProduct(@RequestParam(name = "id") Long id){

        String redirect="redirect:/product/pageProductsList?";

        redirect+=productService.deleteProduct(id);

        return redirect;
    }

    @PostMapping(value = "/restoreProduct")
    public String restoreProduct(@RequestParam(name = "id") Long id){

        String redirect="redirect:/product/pageProductsList?";

        redirect+=productService.restoreProduct(id);

        return redirect;
    }
}

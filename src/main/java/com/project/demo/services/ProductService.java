package com.project.demo.services;


import com.project.demo.entities.Category;
import com.project.demo.entities.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    public String addProduct(String name, Long categoryId, String content, double price, MultipartFile image);
    public List<Product> allProducts();
    public Product productById(Long id);
    public String editProductName(Long id, String name);
    public String editProductCategory(Long id, Long categoryId);
    public String editProductContent(Long id, String content);
    public String editProductPrice(Long id, double price);
    public String editProductImage(Long id, MultipartFile image);
    public String deleteProduct(Long id);
    public String restoreProduct(Long id);


}

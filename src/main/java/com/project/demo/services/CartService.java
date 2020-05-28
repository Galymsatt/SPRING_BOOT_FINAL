package com.project.demo.services;

import com.project.demo.entities.Cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {

    public String addProduct(Long productId, int quantity, HttpServletRequest request);
    public Cart getCustomerCart(Long customerId);
    public Cart getCartById(Long cartId);

}

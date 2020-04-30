package com.project.demo.controller;

import com.project.demo.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping(value = "/addProduct")
    public String addProduct(HttpServletResponse response,
                             @RequestParam(name = "id") Long productId,
                             @RequestParam(name = "quantity") int quantity){

        String redirect = "redirect:/product/pageProduct/"+productId+"?";


        String[] responseFromService = (cartService.addProduct(productId, quantity)).split("-");
//        redirect+=cartService.addProduct(productId, quantity);//previous code
        redirect+=responseFromService[0];

        Cookie cookie = new Cookie("cartId", responseFromService[1]);
        cookie.setMaxAge(60 * 60 * 24 * 30);//1 month
        cookie.setPath("/");
        response.addCookie(cookie);

        return redirect;
    }

}

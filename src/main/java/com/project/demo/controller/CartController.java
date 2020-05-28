package com.project.demo.controller;

import com.project.demo.entities.Cart;
import com.project.demo.entities.CartItem;
import com.project.demo.entities.Users;
import com.project.demo.services.CartService;
import com.project.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    UserService userService;

    @PostMapping(value = "/addProduct")
    public String addProduct(HttpServletResponse response,
                             HttpServletRequest request,
                             @RequestParam(name = "id") Long productId,
                             @RequestParam(name = "quantity") int quantity){

        String redirect = "redirect:/product/pageProduct/"+productId+"?";


        String[] responseFromService = (cartService.addProduct(productId, quantity, request)).split("-");
//        redirect+=cartService.addProduct(productId, quantity);//previous code
        redirect+=responseFromService[0];


        Cookie cookie = new Cookie("cartId", responseFromService[1]);
        cookie.setMaxAge(60 * 60 * 24 * 30);//1 month
        cookie.setPath("/");
        response.addCookie(cookie);

        return redirect;
    }

    @GetMapping(value = "/pageCart")
    public String pageCart(HttpServletRequest request,
                           ModelMap model){

        Users user = userService.getAuthonticatedUser();

        Cart cart = null;
        if(user!=null){
            cart = cartService.getCustomerCart(user.getId());
        }

//        cart.getCartItems();
//        CartItem cartItem;
//        cartItem.getProduct().getName();
//        cartItem.getTotalPrice();
//        cartItem.getQuantity()

        if(cart==null){
            Long cookieCartId;

            Cookie[] cookies = request.getCookies();
            if(cookies!=null){
                for(Cookie c : cookies){
                    if(c.getName().equals("cartId")){

                        cookieCartId = Long.parseLong(c.getValue());

                        cart = cartService.getCartById(cookieCartId);

                        break;
                    }
                }
            }
        }

        model.addAttribute("cart", cart);


        return "user/cart";
    }

}

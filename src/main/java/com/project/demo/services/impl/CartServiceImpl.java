package com.project.demo.services.impl;

import com.project.demo.entities.*;
import com.project.demo.repositories.CartItemRepository;
import com.project.demo.repositories.CartRepository;
import com.project.demo.repositories.CustomerRepository;
import com.project.demo.repositories.UserRepository;
import com.project.demo.services.CartService;
import com.project.demo.services.CustomerService;
import com.project.demo.services.ProductService;
import com.project.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public String addProduct(Long productId, int quantity, HttpServletRequest request) {

        String response = "addToCart_success";

        Product product = productService.productById(productId);


        Cart cart = new Cart();
        Customer customer = customerService.getAuthonticatedCustomer();
        if(customer!=null){
            if(customer.getCart()!=null){
                cart = customer.getCart();
            }
        }
        else {
            Long cookieCartId;

            Cookie[] cookies = request.getCookies();
            if(cookies!=null){
                for(Cookie c : cookies){
                    if(c.getName().equals("cartId")){

                        cookieCartId = Long.parseLong(c.getValue());

                        cart = getCartById(cookieCartId);

                        break;
                    }
                }
            }
        }

        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems==null){
            cartItems = new ArrayList<>();
        }


        boolean productExists = false;
        for(CartItem item : cartItems){
            if(item.getProduct().getId().equals(productId)){
                productExists=true;
                item.setQuantity(item.getQuantity()+quantity);
                item.setTotalPrice(item.getTotalPrice()+(product.getPrice()*quantity));
                break;
            }
        }

        if(!productExists){
            CartItem cartItem = new CartItem(null, product, quantity, product.getPrice()*quantity);
            cartItemRepository.save(cartItem);
            cartItems.add(cartItem);
        }
        cart.setCartItems(cartItems);


        double grandTotal=0;
        for(CartItem item : cartItems){
            grandTotal+=item.getTotalPrice();
        }
        cart.setGrandTotal(grandTotal);


        String cartId="-authorized";//cookie will be saved with authorized value which means cart is users
        if(customer!=null){//saving cart to authorized user
            customer.setCart(cart);
            cart.setCustomer(customer);//i think it is more than needed -> if customer is null, that means there is anonymous user, then also added anonymous field to the cart
            customerRepository.save(customer);
        }
        else {//saving cart to without user, and by specifying "anonymous" true
            cart.setAnonymous(true);
            Cart savedCart = cartRepository.save(cart);//as we not enter to the if, cart remains not saved. Therefor we save it in such way
            cartId="-"+savedCart.getId();//by that way will be sent cart's id, in its turn to be saved on cookie
        }//but we have to save cart of the anonymous user on session. Need to fix above else statement

        return response+cartId;
    }

    @Override
    public Cart getCustomerCart(Long customerId) {

        Cart cart = cartRepository.findByCustomer_Id(customerId);

        return cart;
    }

    @Override
    public Cart getCartById(Long cartId) {

        Cart cart = cartRepository.findById(cartId).orElse(null);

        return cart;
    }
}

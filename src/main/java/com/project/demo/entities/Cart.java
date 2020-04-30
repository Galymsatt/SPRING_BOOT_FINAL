package com.project.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_carts")
public class Cart extends BaseEntity{

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private List<CartItem> cartItems;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)//for what it needed, figure out -> if customer is null, it means that cart is a anonymouses cart
    @JoinColumn(name = "customerId")
    private Customer customer;

    @Column(name = "grandTotal")
    private double grandTotal;

    @Column(name = "anonymous")
    private boolean anonymous;//to identify whether the cart is anonymous

}

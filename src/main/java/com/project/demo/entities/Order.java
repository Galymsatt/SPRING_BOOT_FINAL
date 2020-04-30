package com.project.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="t_orders")
public class Order extends BaseEntity{

    @OneToOne(cascade=CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    private Customer customer;

    @Column(name = "customerName")//for anonymous customer
    private String customerName;

    @Column(name = "customerNum")//for anonymous customer
    private String customerNum;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cartId")
    private Cart cart;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="shippingAddressId")
    private ShippingAddress shippingAddress;

    @Column(name = "paymentType")
    private String paymentType;

    @Column(name = "done")
    private boolean done;


}

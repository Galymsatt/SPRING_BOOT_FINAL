package com.project.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_products")
public class Product extends BaseEntity{

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "content")
    private String content;

    @Column(name = "price")
    private double price;

    @Column(name = "image")
    @Transient
    private MultipartFile image;

    @Column(name = "image_path")
    private String imagePath;


    public Product(Long id, String name, Category category, String content, double price, MultipartFile image, String imagePath) {
//        super(id, createdAt, updatedAt, deletedAt);
        super();
        this.name = name;
        this.category = category;
        this.content = content;
        this.price = price;
        this.image = image;
        this.imagePath = imagePath;
    }



}

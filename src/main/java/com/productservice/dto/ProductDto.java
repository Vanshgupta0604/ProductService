package com.productservice.dto;

import com.productservice.model.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDto {
    private Long id;
    private String title;
    private String description;
    private double price;

    private String image;

    private String category;

    private RatingDto rating;
}
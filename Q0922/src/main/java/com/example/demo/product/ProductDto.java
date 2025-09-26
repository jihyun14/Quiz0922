package com.example.demo.product;

import java.lang.management.MemoryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProductDto {

    private String name;
    private String description;
    private ProductAuthenticityType memoryType;
    private String originalOwner;
    private int emotionLevel;
    private int rarityScore;
    private String magicalPower;
    private int price;
    private int stock;
    private String detailedDescription;

    public Product toEntity() {
    	
    	return new Product(name,description,memoryType,originalOwner,emotionLevel,
    			rarityScore,magicalPower,price,stock,detailedDescription);
    }
    
    
    
    
}
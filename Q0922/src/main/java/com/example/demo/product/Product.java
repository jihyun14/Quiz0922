package com.example.demo.product;



import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private ProductAuthenticityType memoryType;

    private String originalOwner;
    private int emotionLevel;
    private int rarityScore;
    
    
    public Product(String name, String description, ProductAuthenticityType memoryType, String originalOwner, int emotionLevel,
			int rarityScore, String magicalPower, int price, int stock, String detailedDescription) {
		
		this.name = name;
		this.description = description;
		this.memoryType = memoryType;
		this.originalOwner = originalOwner;
		this.emotionLevel = emotionLevel;
		this.rarityScore = rarityScore;
		this.magicalPower = magicalPower;
		this.price = price;
		this.stock = stock;
		this.detailedDescription = detailedDescription;
	}

	private String magicalPower;
    private int price;
    private int stock;
    
    @Lob
    private String detailedDescription;
    
    
}
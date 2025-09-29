package com.example.demo.order;

import java.lang.management.MemoryType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; 

    @Column(nullable = false)
    private int price; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemoryType memoryType; 

    @Column(nullable = false)
    private String originalOwner; 
    
    @Column(nullable = false)
    private int rarityScore;

    @Column(columnDefinition = "TEXT")
    private String description;

    public String getRarityDisplay() {
        return "희귀도 " + rarityScore;
    }
}

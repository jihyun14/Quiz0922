package com.example.demo.order;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

enum DeliveryType {
    NORMAL, 
    EXPRESS,
    NUREONG 
}

enum PaymentMethod {
    CARD, 
    BANK, 
    MEMORY, 
    TOSIM_MOOD 
}

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private LocalDateTime orderDate;
    
    @Column(nullable = false, length = 50)
    private String receiverName; 

    @Column(nullable = false, length = 20)
    private String receiverPhone; 
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String deliveryAddress; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryType deliveryType;
    
    @Column(columnDefinition = "TEXT")
    private String specialRequest; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
    
    private int subtotal; 
    private int storageFee; 
    private int shippingFee; 
    private int totalAmount; 
    
    private boolean termsAgreed;
    private boolean privacyAgreed;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>(); // 카트로 변경예정
    
    @PrePersist
    public void prePersist() {
        this.orderDate = LocalDateTime.now();
    }
    
    
    
    private OrderStatus orderStatus;
    
    public void createOrderItem(OrderItem orderItem) {
    	this.orderItems.add(orderItem);
    }
    
}
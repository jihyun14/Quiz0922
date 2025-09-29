package com.example.demo.order;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(nullable = false)
    private int orderPrice; 
    
    @Column(nullable = false)
    private int quantity;
    
    @Builder
    public OrderItem(Order order, Product product, int orderPrice, int quantity) {
        this.order = order;
        this.product = product;
        this.orderPrice = orderPrice;
        this.quantity = quantity;
        
        
        if (order != null && !order.getOrderItems().contains(this)) {
            order.getOrderItems().add(this);
        }
    }
    
    public int getTotalPrice() {
        return orderPrice * quantity;
    }
  
    public static OrderItem createOrderItem(Product product, int orderPrice, int quantity) {
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .orderPrice(orderPrice)
                .quantity(quantity)
                .build();
        
        return orderItem;
    }
    
}

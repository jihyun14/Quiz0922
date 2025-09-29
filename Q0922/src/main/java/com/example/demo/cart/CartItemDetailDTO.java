package com.example.demo.cart;

import java.lang.management.MemoryType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartItemDetailDTO {
	
	private Long cartItemId;
	private Long productId;
	private String productName;
	private String owner;
	private MemoryType memoryType;
	private int price;
	private int quantity;
	private int subtotal;
	
	public CartItemDetailDTO(CartItem item) {
		this.owner = item.getProduct().getOriginalOwner();
        this.memoryType = item.getProduct().getMemoryType();
        this.price = item.getProduct().getPrice();
        this.quantity = item.getQuantity();
        this.subtotal = item.getTotalPrice();
	}
	
}

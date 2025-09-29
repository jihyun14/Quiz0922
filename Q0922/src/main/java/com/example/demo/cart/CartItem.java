package com.example.demo.cart;

import com.example.demo.order.Product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cart_item")
public class CartItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_item_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product; 
	
	@Column(nullable = false)
	private int quantity;
	
	@Builder
	public CartItem(Cart cart, Product product, int quantity) {
		this.cart = cart;
		this.product = product;
		this.quantity = quantity;
	}
	
	@Enumerated
	public MemoryType memoryType;
	
	@Setter
	private int count;
	private int price;
	
	public int getTotalPrice() {
		return product.getPrice() * quantity;
	}
	
	public void updateQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}

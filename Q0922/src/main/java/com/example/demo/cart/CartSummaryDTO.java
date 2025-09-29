package com.example.demo.cart;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartSummaryDTO {
	private int subtotal;
	private final int storageFee = 3000;
	private final int shippingFee = 5000;
	private int discountAmount; 
	private int totalAmount; // 총 결제금액
}

package com.example.demo.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderSummaryDTO {
	
	private int subtotal;
	private final int storageFee = 3000;
	private int shippingFee;
	private int totalAmount;

	
}

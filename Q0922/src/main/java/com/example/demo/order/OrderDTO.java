package com.example.demo.order;

import java.util.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
	private String receiverName;
	private String receiverPhone;
	private String deliveryAddress;
	private DeliveryType deliveryType;
	private String specialRequest;
	
	private PaymentMethod paymentMethod;
	
	private boolean termsAgreed;
	private boolean privacyAgreed;
	
	private List<OrderItemDTO> items;

}

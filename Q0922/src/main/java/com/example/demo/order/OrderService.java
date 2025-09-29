package com.example.demo.order;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	
	public void createOrder(OrderDTO orderDTO) {
		Order order = Order.builder()
				.receiverName(orderDTO.getReceiverName())
                .receiverPhone(orderDTO.getReceiverPhone())
                .deliveryAddress(orderDTO.getDeliveryAddress())
                .deliveryType(orderDTO.getDeliveryType())
                .specialRequest(orderDTO.getSpecialRequest())
                .paymentMethod(orderDTO.getPaymentMethod())
                .termsAgreed(orderDTO.isTermsAgreed())
                .privacyAgreed(orderDTO.isPrivacyAgreed())
                .orderStatus(OrderStatus.ORDER_IN_PROGRESS)
                .build();
        
        int subtotal = 0;
        List<OrderItemDTO> itemDTOs = orderDTO.getItems(); 
        
        if (itemDTOs == null || itemDTOs.isEmpty()) {
             throw new IllegalArgumentException("주문할 상품 항목이 없습니다.");
        }
        
        for (OrderItemDTO itemDTO : itemDTOs) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다: ID " + itemDTO.getProductId()));
            
            int quantity = itemDTO.getQuantity();


            OrderItem orderItem = OrderItem.createOrderItem(
                    product, 
                    product.getPrice(), 
                    quantity
            );
            
            
            order.createOrderItem(orderItem);

            subtotal += orderItem.getTotalPrice();
        }

        int storageFee = 3000; 
        int shippingFee = calculateShippingFee(orderDTO.getDeliveryType());
        int totalAmount = subtotal + storageFee + shippingFee;

        order.setSubtotal(subtotal);
        order.setStorageFee(storageFee);
        order.setShippingFee(shippingFee);
        order.setTotalAmount(totalAmount);
        
      
        orderRepository.save(order);
        //return order.getId();
    }
    
    
    private int calculateShippingFee(DeliveryType type) {
        return switch (type) {
            case NORMAL, NUREONG -> 5000; 
            case EXPRESS -> 10000; 
            default -> 5000; 
        };
    }
}
	
	


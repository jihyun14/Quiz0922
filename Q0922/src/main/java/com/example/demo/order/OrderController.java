package com.example.demo.order;

import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.cart.MemoryType;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
	
	// 의존성 주입 필요
	private final OrderService orderService;
	
	@GetMapping
	public String orderForm(Model model) {
		List<OrderDisplayItem> orderItems = createDummyOrderItems();
		model.addAttribute("orderItems", orderItems);

		OrderSummaryDTO orderSummary = caculateDummySummary(orderItems);
		model.addAttribute("orderSummary", orderSummary);
		model.addAttribute("orderDTO", new OrderDTO());
		return "order";
	}

	@PostMapping("/process")
	public String processOrder(@ModelAttribute OrderDTO orderDTO) {
		orderService.createOrder(orderDTO); //  PR 해봐야 알듯함, 왜 에러나는지 모르겠음
		Long currentUserId = 1L;
		
		return "redirect:/order/complete/";
	}
	
	private record DummyProduct(Long id, String name, String originalOwner, MemoryType memoryType, int rarityScore, int price) {}
	
	private record OrderDisplayItem(DummyProduct product, int quantity) {}
	
	private List<OrderDisplayItem> createDummyOrderItems() {
		DummyProduct p1 = new DummyProduct(1L, "잊혀진 첫사랑의 편지", "토심이", MemoryType.DREAM, 8, 45000);
        DummyProduct p2 = new DummyProduct(2L, "5차원 우주의 평화로운 기억", "탱고", MemoryType.HAPPY, 5, 10000);
        
        return Arrays.asList(
            new OrderDisplayItem(p1, 1),
            new OrderDisplayItem(p2, 2)
        );
	}
	
	private OrderSummaryDTO caculateDummySummary(List<OrderDisplayItem> orderItems) {
		int subtotal = orderItems.stream()
	            .mapToInt(i -> i.product().price() * i.quantity())
	            .sum(); // 65,000원
	        
	        int storageFee = 3000;
	        int shippingFee = 5000; // HTML의 초기값 5,000원으로 가정
	        int discount = 0; 
	        
	        int totalAmount = subtotal + storageFee + shippingFee - discount;

	        return OrderSummaryDTO.builder()
	            .subtotal(subtotal)
	            .shippingFee(shippingFee)
	            .totalAmount(totalAmount)
	            .build();
	    }
}

package com.example.demo.cart;

import java.lang.management.MemoryType;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
	
	private final CartItemRepository cartItemRepository;
	
	public List<CartItemDetailDTO> getCartItems(Long cartID){
		List<CartItem> cartItems = cartItemRepository.findAll();
		return cartItems.stream()
				.map(CartItemDetailDTO::new)
				.collect(Collectors.toList());	
	}
	
	public CartSummaryDTO caclulatorCartSummary(List<CartItemDetailDTO> cartItems) {
		int subtotal = cartItems.stream()
				.mapToInt(CartItemDetailDTO::getSubtotal)
				.sum();
		
		final int STORAGE_FEE = 3000;
		final int SHIPPING_FEE = 5000;
		
		int discount = 0;
		int totalAmount = subtotal + STORAGE_FEE + SHIPPING_FEE - discount;
		
		return CartSummaryDTO.builder()
				.subtotal(subtotal)
				.discountAmount(discount)
				.totalAmount(totalAmount)
				.build();
	}
	@Transactional
	public void updateItemQuantity(Long cartItemId, int newQuantity) {
		CartItem item = cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new IllegalArgumentException("장바구니 항목을 찾을 수 없습니다."));
				
				if (newQuantity <= 0) {
		            cartItemRepository.delete(item); // 수량이 0 이하면 삭제
		        } else {
		            item.updateQuantity(newQuantity);
		        }
	}
	
	@Transactional
    public void deleteItem(Long cartItemId) {
       cartItemRepository.deleteById(cartItemId);
	}
	    	   
	@Transactional
	public void clearCart(Long cartId) {
       cartItemRepository.deleteAll(); 
	}
	
	public MemoryType getMemoryType(CartItem item) {
	    return item.getProduct().getMemoryType();
	    }

	public boolean addItem(Long userId, Long productId, int quantity) {
		cartItemRepository.findById(userId);
		return false;
	}

	
}

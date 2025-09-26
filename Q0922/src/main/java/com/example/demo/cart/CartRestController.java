package com.example.demo.cart;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartRestController {
	
	private CartService cartService;
	
	@GetMapping("/{userId}")
	public ResponseEntity<List<CartItemDetailDTO>> getCartItems(
            @PathVariable("userId") Long userId){
		List<CartItemDetailDTO> cartItems = cartService.getCartItems(userId);
		return ResponseEntity.ok(cartItems);
	}
	
	public static class CartAddRequst{
		public Long productId;
		public int quantity;
	}
	
	@PostMapping("/{userId}")
	public ResponseEntity<String> addItemToCart(@PathVariable("userId") Long userId, @RequestBody CartAddRequst request){
		
		boolean success = cartService.addItem(userId, request.productId, request.quantity);
		
		if(success) {
			return ResponseEntity.status(HttpStatus.CREATED).body("장바구니에 성공적으로 담겼습니다.");
		}else {
			return ResponseEntity.badRequest().body("장바구니 담기에 실패 했다링");
		}
	}
	
	@DeleteMapping("/{userId}/items/{itemId}")
	public ResponseEntity<Void> removeItemFromCart(@PathVariable("userId") Long userId, @PathVariable("itemId") Long itemId){
		cartService.deleteItem(itemId);
		
		return ResponseEntity.noContent().build();
	}
	
	
	
}

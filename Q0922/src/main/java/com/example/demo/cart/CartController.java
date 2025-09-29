package com.example.demo.cart;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
	
	private final CartService cartService;
	
//	@GetMapping("/cart")
//	public String testHome() {
//		return "cart";
//	}
		
	@GetMapping()
	public String viewCart(Model model) {
		Long currentCartId = 1L;
		
		List<CartItemDetailDTO> cartItems = cartService.getCartItems(currentCartId);
		
		if(cartItems.isEmpty()){
			model.addAttribute("cartItems", cartItems);
			model.addAttribute("isCartEmpty", cartItems);
		}else {
			CartSummaryDTO summary = cartService.caclulatorCartSummary(cartItems);
			model.addAttribute("cartItems", cartItems);
			model.addAttribute("summary", summary);
			model.addAttribute("isCartEmpty", false);
		}
		
		return "cart.html";
	}
	
	@PostMapping("/update/{itemId}")
	@ResponseBody
	public String updateCartItem(@PathVariable Long itemId, @RequestParam int quantity) {
		
		try {
			cartService.updateItemQuantity(itemId, quantity);
			return "SUCCESS";
		} catch (Exception e) {
			return "ERROR :" + e.getMessage();
		}	
		}
	
	@DeleteMapping("/{itemId}")
	@ResponseBody
	public String deleteCartItem(@PathVariable Long itemId) {
		
		try {
			cartService.deleteItem(itemId);
			return "SUCCESS";
		} catch (Exception e) {
			return "ERROR";
		}
	}
	
	@PostMapping("/clear")
	public String clearCart() {
		Long currentCartId = 1L; 
        cartService.clearCart(currentCartId);
        return "redirect:/cart";
    
	}
	
	
	
	
	}



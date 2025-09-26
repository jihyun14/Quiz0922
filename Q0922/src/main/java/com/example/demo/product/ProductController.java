package com.example.demo.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {
	
	private final ProductService productService;
	
	
	//상품목록(list)
	@GetMapping("/products")
	public String products(
	    Model model,
	    @RequestParam(value="page", defaultValue="0") int page,
	    @RequestParam(value="size", defaultValue="12") int size, // products.js와 동일하게 size 추가
	    @RequestParam(value="sort", defaultValue="newest") String sort // 정렬 기준 추가
	) {
	    // service 메서드를 수정하여 정렬 파라미터를 전달합니다.
	    Page<Product> productlist = this.productService.findProduct(page, size, sort);
	    
	    model.addAttribute("productlist", productlist); // 변수명 일관성 유지 (list이므로 productlist로 변경 권장)
	    return "products";
	}
	
	
	
	@GetMapping("/products/new")
	public String create() {
		
		
		return "productcreate";
	}
	
	@PostMapping("/products/new")
	public String create(ProductDto dto) {
		
		this.productService.save(dto);
		
		return "redirect:/products";
	}
	
	@GetMapping(value = "/products/{id}")
	public String show(Model model, @PathVariable("id") Long id) {
		
		Product detail = productService.getDetail(id);
		model.addAttribute("product", detail);
		return "product-detail";
	}
	
	
	
	
	
	
	
}

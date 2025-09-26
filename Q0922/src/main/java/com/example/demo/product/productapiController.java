package com.example.demo.product;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class productapiController {
	private final ProductService productService;
	//상품목록(list)
	@GetMapping
    public Map<String, Object> getProducts(
        @RequestParam(value="page", defaultValue="1") int page, // 프론트엔드는 1부터 시작
        @RequestParam(value="size", defaultValue="12") int size,
        @RequestParam(value="sort", defaultValue="newest") String sort,
        // 필터링을 위한 추가 파라미터 (products.js의 currentFilters)
        @RequestParam(value="category", defaultValue="") String category,
        @RequestParam(value="search", defaultValue="") String search
    ) {
        // 실제로는 category, search 필터링까지 적용된 findProductWithFiltersAndSort()를 호출합니다.
        // 현재는 위의 findProduct() 메서드를 사용한다고 가정
        Page<Product> productPage = productService.findProduct(page - 1, size, sort); // Spring Data는 페이지를 0부터 세므로 page-1
        
        // 프론트엔드가 요구하는 형식에 맞게 JSON 응답을 구성
        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        response.put("totalCount", productPage.getTotalElements());
        response.put("currentPage", productPage.getNumber() + 1);
        response.put("totalPages", productPage.getTotalPages());
        
        return response;
    }

}

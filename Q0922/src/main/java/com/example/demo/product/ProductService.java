package com.example.demo.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	
	private final ProductRepository productRepository;

	
//	public List<Product> findAll() {
//		
//		return productRepository.findAll();
//	}
	

	// 💡 정렬 파라미터를 추가한 findProduct 메서드
    public Page<Product> findProduct(int page, int size, String sort) {
        
        // 1. 정렬 기준을 담을 Sort 객체 생성
        Sort sortCriteria;
        
        // products.js에서 보낸 sort 값에 따라 Sort 객체를 다르게 생성
        switch (sort) {
            case "price_asc":
                // 가격 오름차순 (price 필드를 사용한다고 가정)
                sortCriteria = Sort.by("price").ascending();
                break;
            case "price_desc":
                // 가격 내림차순
                sortCriteria = Sort.by("price").descending();
                break;
            case "rarity_desc":
                // 희귀도 내림차순 (rarityScore 필드를 사용한다고 가정)
                sortCriteria = Sort.by("rarityScore").descending();
                break;
            case "newest":
            default:
                // 기본값: 최신순 (id 또는 createDate 필드를 사용한다고 가정)
                sortCriteria = Sort.by("id").descending(); // 또는 "createDate"
                break;
        }

        // 2. Pageable 객체 생성 시 Sort 객체 적용
        Pageable pageable = PageRequest.of(page, size, sortCriteria);

        // 3. Repository를 사용하여 데이터 조회
        return productRepository.findAll(pageable); // Repository 메서드 사용
    }
	
//	public Page<Product> findProduct(int page, int size, String sort, String category,String search, String priceRange, String owner){
//		
//		// 1. 정렬 기준 설정 (Sorting)
//        // 기본값: 'newest' (최신순 = ID 내림차순이라고 가정)
//		Sort sortCriteria = createSortCriteria(sort);
//		
//		Pageable pageable = PageRequest.of(page-1, size, sortCriteria);
//		
//		
//		
//		
//		
//		return this.productRepository.findAll(spec, pageable);
//	}
//	
	
	


	public void save(ProductDto dto) {
		
		this.productRepository.save(dto.toEntity());
	}




	public Product getDetail(Long id) {
		
		return this.productRepository.findById(id).get();
		
//		Optional<Product> product1 = this.productRepository.findById(id);
//		
//		if(product1.isPresent()) {
//			return product1.get();
//		}else {
//			throw new IllegalArgumentException("해당 상품이 없습니다.");
//		}
		
	
	}

	public Page<Product> findAll(int page) {
		// TODO Auto-generated method stub
		return null;
	}


	
	
	
	
	
	
}

// Memory Treasures - 상품 목록 관리 스크립트
class ProductList {
    constructor() {
        this.currentFilters = {
            memoryType: '', // HTML ID에 맞게 수정
            priceRange: '',
            owner: '',
            emotionLevel: 5, // 기본값 설정
            search: ''
        };
        this.currentSort = 'newest';
        this.currentPage = 1;
        this.pageSize = 12;
        this.initializeFilters();
    }

    // 필터 초기화
    initializeFilters() {

        const searchInput = document.getElementById('searchInput');

        if (searchInput) {
            let debounceTimer;
            searchInput.addEventListener('input', (e) => {
                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(() => {
                    this.currentFilters.search = e.target.value;
                    this.currentPage = 1; // 검색 시 1페이지로 리셋
                    this.loadProducts();
                }, 500);
            });
        }

        const memoryTypeSelect = document.getElementById('memoryTypeFilter');
        if (memoryTypeSelect) {
            memoryTypeSelect.addEventListener('change', (e) => {
                this.currentFilters.memoryType = e.target.value;
                this.currentPage = 1;
                this.loadProducts();
            });
        }

        const priceSelect = document.getElementById('priceFilter');
        if (priceSelect) {
            priceSelect.addEventListener('change', (e) => {
                this.currentFilters.priceRange = e.target.value;
                this.currentPage = 1;
                this.loadProducts();
            });
        }

        const ownerSelect = document.getElementById('ownerFilter');
        if (ownerSelect) {
            ownerSelect.addEventListener('change', (e) => {
                this.currentFilters.owner = e.target.value;
                this.currentPage = 1;
                this.loadProducts();
            });
        }
        
        const emotionRange = document.getElementById('emotionFilter');
        if(emotionRange) {
            emotionRange.addEventListener('change', (e) => {
                this.currentFilters.emotionLevel = e.target.value;
                this.currentPage = 1;
                this.loadProducts();
            });
        }

        const sortSelect = document.getElementById('sortFilter');
        if (sortSelect) {
            sortSelect.addEventListener('change', (e) => {
                this.currentSort = e.target.value;
                this.currentPage = 1;
                this.loadProducts();
            });
        }

        // URL 파라미터에서 초기 필터 설정
        this.setFiltersFromURL();
    }

    // URL 파라미터로부터 필터 설정
    setFiltersFromURL() {
        const urlParams = new URLSearchParams(window.location.search);

        if (urlParams.get('memoryType')) {
            this.currentFilters.memoryType = urlParams.get('memoryType');
            const memoryTypeSelect = document.getElementById('memoryTypeFilter');
            if (memoryTypeSelect) memoryTypeSelect.value = this.currentFilters.memoryType;

        }

        if (urlParams.get('search')) {
            this.currentFilters.search = urlParams.get('search');
            const searchInput = document.getElementById('searchInput');

            if (searchInput) searchInput.value = this.currentFilters.search;
        }
    }

    // 상품 목록 로드
    async loadProducts() {

        const loadingIndicator = document.getElementById('loadingIndicator');
        const productsGrid = document.getElementById('productsGrid'); // HTML ID에 맞게 수정
      
        if (loadingIndicator) loadingIndicator.style.display = 'block';

        try {
            const queryParams = new URLSearchParams({
                page: this.currentPage,
                size: this.pageSize,
                sort: this.currentSort,
                ...this.currentFilters
            });

            const response = await fetch(`/api/products?${queryParams}`);
            const data = await response.json();

            this.renderProducts(data.products);
            this.updateResultsCount(data.totalCount);
            // 페이지네이션 업데이트는 HTML에 해당 요소가 없으므로 주석 처리
            // this.updatePagination(data.currentPage, data.totalPages);

        } catch (error) {
            console.error('상품 로드 실패:', error);
            this.showError('상품을 불러오는데 실패했습니다.');
        } finally {
            if (loadingIndicator) loadingIndicator.style.display = 'none';
        }
    }

    // 상품 목록 렌더링
    renderProducts(products) {

        const container = document.getElementById('productsGrid'); // HTML ID에 맞게 수정
        if (!container) return;
        
        const emptyState = document.getElementById('emptyState');
        const loadMoreContainer = document.getElementById('loadMoreContainer');

        if (products.length === 0) {
            container.innerHTML = '';
            if (emptyState) emptyState.style.display = 'block';
            if (loadMoreContainer) loadMoreContainer.style.display = 'none';
            return;
        }

        if (emptyState) emptyState.style.display = 'none';
        if (loadMoreContainer) loadMoreContainer.style.display = 'block';
        
        container.innerHTML = products.map(product => this.createProductCard(product)).join('');
    }

    // 상품 카드 HTML 생성
	createProductCard(product) {
	    const rarityBadgeClass = product.rarityScore >= 8 ? 'bg-success' : 'bg-warning';
	    const rarityText = product.rarityScore >= 8 ? '매우 희귀' : '희귀';
	    const stockInfo = product.stock > 0 ?
	        `<span class="in-stock">재고 ${product.stock}개</span>` :
	        `<span class="out-of-stock">품절</span>`;
	    const emotionEmoji = this.getEmotionEmoji(product.emotionLevel);

	    return `
	        <div class="product-card" onclick="openProductModal('${product.id}')">
	            <div class="product-image">
	                <span class="no-image">${product.memoryIcon || '🎁'}</span>
	                <span class="rarity-badge" style="background: ${rarityBadgeClass};">
	                    ${rarityText} (${product.rarityScore}/10)
	                </span>
	            </div>
	            <div class="product-info">
	                <span class="owner-tag">${product.originalOwner}</span>
	                <h4 class="product-name">${product.name}</h4>
	                <div class="emotion-level">
	                    <span class="emotion-emoji">${emotionEmoji}</span>
	                    <span class="emotion-text">감정 강도 ${product.emotionLevel}</span>
	                </div>
	                <div class="product-price">${product.price.toLocaleString()}원</div>
	                <div class="stock-info">
	                    ${stockInfo}
	                </div>
	            </div>
	        </div>
	    `;
	}

    // 검색 결과 개수 업데이트
    updateResultsCount(totalCount) {
        const countElement = document.getElementById('resultsCount'); // HTML ID에 맞게 수정

        if (countElement) {
            countElement.textContent = `총 ${totalCount}개의 기억`;
        }
    }

    // 에러 메시지 표시
    showError(message) {
        const container = document.getElementById('productsGrid'); // HTML ID에 맞게 수정
        if (container) {
            container.innerHTML = `
                <div class="col-12 text-center py-5">
                    <div class="alert alert-danger">${message}</div>
                </div>
            `;
        }
    }
    
    // 이 함수는 HTML에 없으므로 주석 처리 또는 제거
    // updatePagination(currentPage, totalPages) {}
    // goToPage(page) {}
    
    // 필터 초기화
    clearFilters() {
        // ... (동일한 로직)
        this.currentFilters = {
            memoryType: '',
            priceRange: '',
            owner: '',
            emotionLevel: 5,
            search: ''
        };
        
        const filterElements = ['memoryTypeFilter', 'priceFilter', 'ownerFilter', 'searchInput'];

        filterElements.forEach(id => {
            const element = document.getElementById(id);
            if (element) element.value = '';
        });

        
        const emotionRange = document.getElementById('emotionFilter');
        if (emotionRange) emotionRange.value = 5;
        
        this.currentPage = 1;
        this.loadProducts();
    }

}

// 전역 상품 목록 인스턴스
const productList = new ProductList();

// DOM 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    productList.loadProducts();
});
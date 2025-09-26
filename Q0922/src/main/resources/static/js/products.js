// Memory Treasures - 상품 목록 관리 스크립트
class ProductList {
    constructor() {
        this.currentFilters = {
            category: '',
            priceRange: '',
            owner: '',
            search: ''
        };
        this.currentSort = 'newest';
        this.currentPage = 1;
        this.pageSize = 12;
        
        this.initializeFilters();
        this.initializeEmotionFilter(); // 감정 강도 필터 초기화 추가
    }

    // 감정 강도 필터 관련 UI 및 이벤트 초기화
    initializeEmotionFilter() {
        const emotionRange = document.getElementById('emotionFilter');
        const emotionDisplay = document.getElementById('emotionDisplay');

        if (emotionRange && emotionDisplay) {
            // 초기 값 표시
            emotionDisplay.textContent = `${emotionRange.value} 이상`;
            
            // onchange 이벤트는 HTML에 있으므로, 여기서 필터 상태만 업데이트합니다.
            emotionRange.addEventListener('change', (e) => {
                this.currentFilters.emotionLevel = e.target.value;
                this.loadProducts();
            });
            // oninput 이벤트는 HTML에 있으므로, 여기서 필터 상태만 업데이트합니다.
            emotionRange.addEventListener('input', (e) => {
                 emotionDisplay.textContent = `${e.target.value} 이상`;
            });
            // 초기 필터 상태 설정 (1 이상의 값만 필터링하도록)
            this.currentFilters.emotionLevel = emotionRange.value;
        }
    }


    // 필터 초기화
    initializeFilters() {
        // 검색 입력 이벤트 (ID: searchInput)
        const searchInput = document.getElementById('searchInput'); // ✨ 수정: 'search-input' -> 'searchInput'
        if (searchInput) {
            let debounceTimer;
            searchInput.addEventListener('input', (e) => {
                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(() => {
                    this.currentFilters.search = e.target.value;
                    this.loadProducts();
                }, 500);
            });
        }

        // 카테고리 필터 (ID: memoryTypeFilter)
        const categorySelect = document.getElementById('memoryTypeFilter'); // ✨ 수정: 'category-filter' -> 'memoryTypeFilter'
        if (categorySelect) {
            categorySelect.addEventListener('change', (e) => {
                this.currentFilters.category = e.target.value;
                this.loadProducts();
            });
        }

        // 가격 범위 필터 (ID: priceFilter)
        const priceSelect = document.getElementById('priceFilter'); // ✨ 수정: 'price-filter' -> 'priceFilter'
        if (priceSelect) {
            priceSelect.addEventListener('change', (e) => {
                this.currentFilters.priceRange = e.target.value;
                this.loadProducts();
            });
        }

        // 기억 주인 필터 (ID: ownerFilter)
        const ownerSelect = document.getElementById('ownerFilter'); // ✨ 수정: 'owner-filter' -> 'ownerFilter'
        if (ownerSelect) {
            ownerSelect.addEventListener('change', (e) => {
                this.currentFilters.owner = e.target.value;
                this.loadProducts();
            });
        }

        // 정렬 옵션 (ID: sortFilter)
        const sortSelect = document.getElementById('sortFilter'); 
        if (sortSelect) {
            sortSelect.addEventListener('change', (e) => {
                this.currentSort = e.target.value;
                this.loadProducts();
            });
        }

        // URL 파라미터에서 초기 필터 설정
        this.setFiltersFromURL();
    }

    // URL 파라미터로부터 필터 설정
    setFiltersFromURL() {
        const urlParams = new URLSearchParams(window.location.search);
        
        if (urlParams.get('category')) {
            this.currentFilters.category = urlParams.get('category');
            // ID 수정 반영
            const categorySelect = document.getElementById('memoryTypeFilter');
            if (categorySelect) categorySelect.value = this.currentFilters.category;
        }

        if (urlParams.get('search')) {
            this.currentFilters.search = urlParams.get('search');
            // ID 수정 반영
            const searchInput = document.getElementById('searchInput'); 
            if (searchInput) searchInput.value = this.currentFilters.search;
        }
    }

    // 상품 목록 로드
    async loadProducts() {
        const loadingIndicator = document.getElementById('loadingIndicator'); // HTML ID에 맞춤
        
        if (loadingIndicator) loadingIndicator.style.display = 'block';

        try {
            // 필터 객체에서 빈 값 제거
            const activeFilters = Object.entries(this.currentFilters)
                .filter(([, value]) => value !== '')
                .reduce((acc, [key, value]) => ({ ...acc, [key]: value }), {});

            const queryParams = new URLSearchParams({
                page: this.currentPage,
                size: this.pageSize,
                sort: this.currentSort,
                ...activeFilters
            });

            const response = await fetch(`/api/products?${queryParams}`);
            
            // HTTP 오류 처리
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            
            this.renderProducts(data.products);
            this.updateResultsCount(data.totalCount);
            this.updatePagination(data.currentPage, data.totalPages);
            
        } catch (error) {
            console.error('상품 로드 실패:', error);
            this.showError('상품을 불러오는데 실패했습니다. 서버 또는 네트워크 연결을 확인해주세요.');
        } finally {
            if (loadingIndicator) loadingIndicator.style.display = 'none';
        }
    }

    // 상품 목록 렌더링
    renderProducts(products) {
        // ✨ 결정적인 수정: products-container 대신 HTML의 'productsGrid' 사용
        const container = document.getElementById('productsGrid'); 
        const emptyState = document.getElementById('emptyState');
        const loadMoreContainer = document.getElementById('loadMoreContainer');

        if (!container) return; // 요소를 찾지 못하면 종료

        // 렌더링 전에 컨테이너 초기화
        container.innerHTML = ''; 

        if (products.length === 0) {
            container.style.display = 'none';
            if (emptyState) emptyState.style.display = 'block';
            if (loadMoreContainer) loadMoreContainer.style.display = 'none';
            return;
        }
        
        // 데이터가 있을 경우
        container.style.display = 'grid'; // 그리드 레이아웃 표시
        if (emptyState) emptyState.style.display = 'none'; // 빈 상태 숨기기

        // HTML 템플릿 생성 및 삽입
        const productHTML = products.map(product => this.createProductCard(product)).join('');
        container.innerHTML = productHTML;
    }

    // 상품 카드 HTML 생성
    createProductCard(product) {
        const stockBadge = product.stock > 0 ? 
            `<span class="badge bg-success">재고 ${product.stock}개</span>` : 
            `<span class="badge bg-danger">품절</span>`;
        
        // HTML 구조가 부트스트랩 카드 형태가 아니므로, HTML 구조에 맞게 수정이 필요할 수 있습니다. 
        // 여기서는 기존 JS 코드를 최대한 유지하고 HTML의 클래스명과 구조에 맞춘다고 가정합니다.
        // HTML의 product-card가 래핑되는 요소가 없으므로 grid item에 직접 적용합니다.

        return `
            <div class="product-card" data-product-id="${product.id}">
                <div class="card-body text-center">
                    <div style="font-size: 4rem; margin: 1rem 0;">
                        ${product.memoryIcon || '🎁'}
                    </div>
                    <span class="badge bg-primary mb-2">희귀도 ${product.rarityScore}/10</span>
                    
                    <h6 class="card-title">${product.name}</h6>
                    <p class="card-text text-muted small">${product.description}</p>
                    
                    <div class="mb-2">
                        <small class="text-muted">${product.originalOwner}의 기억</small>
                    </div>
                    
                    <div class="mb-2">
                        <span class="badge bg-secondary">감정 ${product.emotionLevel}/10</span>
                    </div>
                    
                    ${stockBadge}
                    
                    <h5 class="text-primary mt-3">${product.price.toLocaleString()}원</h5>
                    
                    <div class="mt-3">
                        <button class="btn btn-outline-primary btn-sm me-2" 
                                onclick="viewProductDetail('${product.id}')">
                            상세보기
                        </button>
                        <button class="btn btn-primary btn-sm" 
                                onclick="addToCart('${product.id}')"
                                ${product.stock === 0 ? 'disabled' : ''}>
                            장바구니
                        </button>
                    </div>
                </div>
            </div>
        `;
    }

    // 검색 결과 개수 업데이트 (ID: resultsCount)
    updateResultsCount(totalCount) {
        const countElement = document.getElementById('resultsCount'); // ✨ 수정: 'results-count' -> 'resultsCount'
        if (countElement) {
            countElement.textContent = `총 ${totalCount}개의 기억`;
        }
    }

    // 페이지네이션 업데이트
    updatePagination(currentPage, totalPages) {
        // 'loadMoreContainer'를 사용하므로 페이지네이션 컨테이너 ID는 무시하고 'loadMoreBtn'만 처리
        const loadMoreContainer = document.getElementById('loadMoreContainer');
        const loadMoreBtn = document.getElementById('loadMoreBtn');
        
        if (loadMoreContainer && loadMoreBtn) {
            if (currentPage < totalPages) {
                loadMoreContainer.style.display = 'block';
                loadMoreBtn.onclick = () => this.loadMoreProducts();
            } else {
                loadMoreContainer.style.display = 'none';
            }
        }
    }
    
    // 더 많은 상품 로드 (loadMoreProducts 함수 추가)
    loadMoreProducts() {
        this.currentPage++;
        this.loadProducts(true); // appendMode를 위한 인자 추가 필요 (현재 구조상)
    }

    // 페이지 이동 (현재 loadMoreProducts를 사용하므로 이 함수는 사용되지 않을 수 있음)
    goToPage(page) {
        this.currentPage = page;
        this.loadProducts();
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    // 필터 초기화 (clearAllFilters 함수와 매핑)
    clearAllFilters() {
        this.currentFilters = {
            category: '',
            priceRange: '',
            owner: '',
            search: '',
            emotionLevel: '5' // 감정 강도 초기값
        };
        
        // UI 초기화
        const filterElements = ['memoryTypeFilter', 'priceFilter', 'ownerFilter', 'searchInput']; // ✨ ID 수정 반영
        filterElements.forEach(id => {
            const element = document.getElementById(id);
            if (element) element.value = '';
        });

        // 감정 강도 필터 초기화
        const emotionRange = document.getElementById('emotionFilter');
        const emotionDisplay = document.getElementById('emotionDisplay');
        if (emotionRange) emotionRange.value = '5';
        if (emotionDisplay) emotionDisplay.textContent = '5 이상';


        this.currentPage = 1;
        this.loadProducts();
    }

    // 에러 메시지 표시
    showError(message) {
        const container = document.getElementById('productsGrid');
        if (container) {
            container.innerHTML = `
                <div class="col-12 text-center py-5">
                    <div class="alert alert-danger">${message}</div>
                </div>
            `;
        }
    }
}

// 상품 상세 페이지로 이동
function viewProductDetail(productId) {
    window.location.href = `/products/${productId}`;
}

// 카테고리별 상품 보기 (HTML에서 사용하지 않으므로 주석 처리해도 무방)
// function viewByCategory(category) {
//     window.location.href = `/products?category=${category}`;
// }

// 전역 상품 목록 인스턴스
const productList = new ProductList();

// HTML 버튼 onclick 이벤트와 전역 함수 매핑
function filterProducts() {
    productList.currentPage = 1;
    productList.loadProducts();
}
function sortProducts() {
    productList.currentPage = 1;
    productList.loadProducts();
}
function searchProducts() {
    // searchInput의 'input' 이벤트 리스너가 처리하므로 이 함수는 필요하지 않을 수 있지만, HTML 버튼에 연결되어 있다면 구현해야 합니다.
    productList.currentPage = 1;
    productList.loadProducts();
}
function clearAllFilters() {
    productList.clearAllFilters();
}
function refreshProducts() {
    productList.currentPage = 1;
    productList.loadProducts();
}
function loadMoreProducts() {
    productList.loadMoreProducts();
}
function updateEmotionDisplay(value) {
    const emotionDisplay = document.getElementById('emotionDisplay');
    if(emotionDisplay) emotionDisplay.textContent = `${value} 이상`;
}
// TODO: addToCart, viewProductDetail, closeProductModal 등 HTML에서 사용하는 다른 함수들도 필요합니다.

// DOM 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    productList.loadProducts();
});
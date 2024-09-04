import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ProductDetailsComponent } from './product-details.component';
import { ProductService } from '../services/product.service';
import { WishlistService } from '../services/wishlist.service';
import { CartService } from '../services/cart.service';
import { ReviewService } from '../services/review.service';
import { ComponentDataService } from '../services/component-data.service';

describe('ProductDetailsComponent', () => {
  let component: ProductDetailsComponent;
  let fixture: ComponentFixture<ProductDetailsComponent>;
  let mockProductService: jasmine.SpyObj<ProductService>;
  let mockWishlistService: jasmine.SpyObj<WishlistService>;
  let mockCartService: jasmine.SpyObj<CartService>;
  let mockReviewService: jasmine.SpyObj<ReviewService>;
  let mockDataService: jasmine.SpyObj<ComponentDataService>;
  let mockRouter: jasmine.SpyObj<Router>;

  beforeEach(() => {
    const productServiceSpy = jasmine.createSpyObj('ProductService', ['getProductById']);
    const wishlistServiceSpy = jasmine.createSpyObj('WishlistService', ['addItemToWishlist']);
    const cartServiceSpy = jasmine.createSpyObj('CartService', ['addItemToCart', 'removeItemFromCart']);
    const reviewServiceSpy = jasmine.createSpyObj('ReviewService', ['getReviewsByProductId', 'addReview']);
    const dataServiceSpy = jasmine.createSpyObj('ComponentDataService', ['getUserId']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProductDetailsComponent],
      providers: [
        { provide: ProductService, useValue: productServiceSpy },
        { provide: WishlistService, useValue: wishlistServiceSpy },
        { provide: CartService, useValue: cartServiceSpy },
        { provide: ReviewService, useValue: reviewServiceSpy },
        { provide: ComponentDataService, useValue: dataServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } }
      ]
    });

    fixture = TestBed.createComponent(ProductDetailsComponent);
    component = fixture.componentInstance;
    mockProductService = TestBed.inject(ProductService) as jasmine.SpyObj<ProductService>;
    mockWishlistService = TestBed.inject(WishlistService) as jasmine.SpyObj<WishlistService>;
    mockCartService = TestBed.inject(CartService) as jasmine.SpyObj<CartService>;
    mockReviewService = TestBed.inject(ReviewService) as jasmine.SpyObj<ReviewService>;
    mockDataService = TestBed.inject(ComponentDataService) as jasmine.SpyObj<ComponentDataService>;
    mockRouter = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to product list when goBack is called', () => {
    component.goBack();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/products']);
  });

  it('should add item to wishlist and update product wishlisted count', () => {
    const mockProduct = {
      id: 1,
      productName: 'Product 1',
      description: 'Description 1',
      stock: 10,
      price: 100,
      categoryId: 1,
      categoryName: 'Category 1',
      supplier: 'Supplier 1',
      wishlisted: 0
    };
    component.product = mockProduct;
    mockDataService.getUserId.and.returnValue(1);
    mockWishlistService.addItemToWishlist.and.returnValue(of({})); // Mocking the response as empty object for success

    component.addToWishList(mockProduct.id);

    expect(mockWishlistService.addItemToWishlist).toHaveBeenCalledWith(mockProduct.id);
    expect(component.product?.wishlisted).toBe(1);
  });

  it('should add item to cart and update product stock', () => {
    const mockProduct = {
      id: 1,
      productName: 'Product 1',
      description: 'Description 1',
      stock: 10,
      price: 100,
      categoryId: 1,
      categoryName: 'Category 1',
      supplier: 'Supplier 1',
      wishlisted: 0
    };
    const mockCartResponse = {
      id: 1,
      cartItems: [
        { id: 1, cartId: 1, productId: mockProduct.id, amount: 1 }
      ]
    };

    component.product = mockProduct;
    mockDataService.getUserId.and.returnValue(1);
    mockCartService.addItemToCart.and.returnValue(of(mockCartResponse)); // Mocking response with valid Cart

    component.addToCart(mockProduct.id);

    expect(mockCartService.addItemToCart).toHaveBeenCalledWith(1, mockProduct.id);
    expect(component.product?.stock).toBe(9);
  });

  it('should handle errors when adding to wishlist', () => {
    const mockProduct = {
      id: 1,
      productName: 'Product 1',
      description: 'Description 1',
      stock: 10,
      price: 100,
      categoryId: 1,
      categoryName: 'Category 1',
      supplier: 'Supplier 1',
      wishlisted: 0
    };
    component.product = mockProduct;
    mockDataService.getUserId.and.returnValue(1);
    mockWishlistService.addItemToWishlist.and.returnValue(throwError(() => new Error('Error')));

    component.addToWishList(mockProduct.id);

    expect(mockWishlistService.addItemToWishlist).toHaveBeenCalledWith(mockProduct.id);
    expect(component.product?.wishlisted).toBe(0); // Ensure no increment on error
  });

  it('should reset newReview after submitting a review', () => {
    const productId = 1;
    const mockProduct = { id: 1, productName: 'Product 1', price: 100, description: 'Description 1', stock: 10, categoryId: 1, categoryName: 'Category 1', supplier: 'Supplier 1', wishlisted: 0 };

    component.product = mockProduct;
    component.newReview = { title: 'New Review', description: 'Nice product', stars: 4, supplier: 'Supplier 1' };

    mockReviewService.addReview.and.returnValue(of({}));
    spyOn(component, 'loadReviews');

    component.submitReview();

    expect(component.newReview).toEqual({ title: '', description: '', stars: 1, supplier: 'Supplier 1' });
    expect(component.loadReviews).toHaveBeenCalledWith(productId);
  });
});

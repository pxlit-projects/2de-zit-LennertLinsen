import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { WishlistComponent } from './wishlist.component';
import { WishlistService, Wishlist } from '../services/wishlist.service';
import { ProductService, ProductResponse } from '../services/product.service';
import { ComponentDataService } from '../services/component-data.service';
import {Cart, CartService} from '../services/cart.service';
import { Router } from '@angular/router';

describe('WishlistComponent', () => {
  let component: WishlistComponent;
  let fixture: ComponentFixture<WishlistComponent>;
  let mockWishlistService: jasmine.SpyObj<WishlistService>;
  let mockProductService: jasmine.SpyObj<ProductService>;
  let mockCartService: jasmine.SpyObj<CartService>;
  let mockDataService: jasmine.SpyObj<ComponentDataService>;
  let mockRouter: jasmine.SpyObj<Router>;

  beforeEach(() => {
    const wishlistServiceSpy = jasmine.createSpyObj('WishlistService', ['getWishlistByUserId', 'removeItemFromWishlist']);
    const productServiceSpy = jasmine.createSpyObj('ProductService', ['getProductById']);
    const cartServiceSpy = jasmine.createSpyObj('CartService', ['addItemToCart']);
    const dataServiceSpy = jasmine.createSpyObj('ComponentDataService', ['getUserId']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      declarations: [WishlistComponent],
      providers: [
        { provide: WishlistService, useValue: wishlistServiceSpy },
        { provide: ProductService, useValue: productServiceSpy },
        { provide: CartService, useValue: cartServiceSpy },
        { provide: ComponentDataService, useValue: dataServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });

    fixture = TestBed.createComponent(WishlistComponent);
    component = fixture.componentInstance;
    mockWishlistService = TestBed.inject(WishlistService) as jasmine.SpyObj<WishlistService>;
    mockProductService = TestBed.inject(ProductService) as jasmine.SpyObj<ProductService>;
    mockCartService = TestBed.inject(CartService) as jasmine.SpyObj<CartService>;
    mockDataService = TestBed.inject(ComponentDataService) as jasmine.SpyObj<ComponentDataService>;
    mockRouter = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load wishlist items on initialization', () => {
    const userId = 1;
    const wishlist: Wishlist = { wishlistId: 1, productIds: [1, 2], userId };
    const product1: ProductResponse = { id: 1, price: 100, supplier: 'Supplier 1', description: 'Description 1', wishlisted: 0, stock: 10, categoryName: 'Category 1', productName: 'Product 1', categoryId: 1 };
    const product2: ProductResponse = { id: 2, price: 200, supplier: 'Supplier 2', description: 'Description 2', wishlisted: 1, stock: 5, categoryName: 'Category 2', productName: 'Product 2', categoryId: 2 };

    mockDataService.getUserId.and.returnValue(userId);
    mockWishlistService.getWishlistByUserId.and.returnValue(of(wishlist));
    mockProductService.getProductById.and.callFake(id => {
      if (id === 1) return of(product1);
      if (id === 2) return of(product2);
      return throwError(() => new Error('Product not found'));
    });

    fixture.detectChanges(); // Trigger ngOnInit

    expect(mockWishlistService.getWishlistByUserId).toHaveBeenCalledWith(userId);
    expect(mockProductService.getProductById).toHaveBeenCalledWith(1);
    expect(mockProductService.getProductById).toHaveBeenCalledWith(2);
    expect(component.wishlistItems).toEqual([product1, product2]);
  });

  it('should handle error when loading wishlist', () => {
    const userId = 1;
    mockDataService.getUserId.and.returnValue(userId);
    mockWishlistService.getWishlistByUserId.and.returnValue(throwError(() => new Error('Failed to fetch wishlist')));

    fixture.detectChanges(); // Trigger ngOnInit

    // Check console error messages or other error handling mechanisms as per your component
    // Expect component.wishlistItems to remain empty
    expect(component.wishlistItems).toEqual([]);
  });

  it('should add product to cart and update stock', () => {
    const userId = 1;
    const product: ProductResponse = {
      id: 1,
      price: 100,
      supplier: 'Supplier 1',
      description: 'Description 1',
      wishlisted: 0,
      stock: 10,
      categoryName: 'Category 1',
      productName: 'Product 1',
      categoryId: 1
    };

    const mockCart: Cart = {
      id: userId,
      cartItems: [
        {
          id: 1,
          cartId: userId,
          productId: product.id,
          amount: 1
        }
      ]
    };

    mockDataService.getUserId.and.returnValue(userId);
    mockCartService.addItemToCart.and.returnValue(of(mockCart)); // Provide a valid Cart object

    component.addToCart(product);

    expect(mockCartService.addItemToCart).toHaveBeenCalledWith(userId, product.id);
    expect(product.stock).toBe(9); // Ensure stock is decremented by 1
  });


  it('should remove product from wishlist', () => {
    const userId = 1;
    const product: ProductResponse = { id: 1, price: 100, supplier: 'Supplier 1', description: 'Description 1', wishlisted: 0, stock: 10, categoryName: 'Category 1', productName: 'Product 1', categoryId: 1 };
    const wishlist: Wishlist = { wishlistId: 1, productIds: [], userId };

    mockDataService.getUserId.and.returnValue(userId);
    mockWishlistService.removeItemFromWishlist.and.returnValue(of(wishlist));

    component.wishlistItems = [product];
    component.removeFromWishlist(product);

    expect(mockWishlistService.removeItemFromWishlist).toHaveBeenCalledWith(userId, product.id);
    expect(component.wishlistItems).toEqual([]); // Ensure item is removed from wishlist
  });

  it('should navigate back to catalog', () => {
    component.goBackToCatalog();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/products']);
  });
});

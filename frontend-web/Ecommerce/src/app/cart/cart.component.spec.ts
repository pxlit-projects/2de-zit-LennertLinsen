import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { CartComponent } from './cart.component';
import { CartService } from '../services/cart.service';
import { ProductService } from '../services/product.service';
import { ComponentDataService } from '../services/component-data.service';
import { ProductResponse } from '../services/product.service';
import { Cart, CartItem } from '../services/cart.service';
import { AppComponent } from '../app.component';

describe('CartComponent', () => {
  let component: CartComponent;
  let fixture: ComponentFixture<CartComponent>;
  let mockCartService: jasmine.SpyObj<CartService>;
  let mockProductService: jasmine.SpyObj<ProductService>;
  let mockDataService: jasmine.SpyObj<ComponentDataService>;
  let mockRouter: jasmine.SpyObj<Router>;
  let mockAppComponent: jasmine.SpyObj<AppComponent>;

  const cartId = 1;
  const productId = 1;
  const newProductId = 2;

  const mockCart: Cart = {
    id: cartId,
    cartItems: [
      { id: 1, cartId: cartId, productId: productId, amount: 1 }
    ]
  };

  const mockProduct: ProductResponse = {
    id: productId,
    price: 100,
    supplier: 'Supplier 1',
    description: 'Description 1',
    wishlisted: 0,
    stock: 10,
    categoryName: 'Category 1',
    productName: 'Product 1',
    categoryId: 1
  };

  const updatedCart: Cart = {
    id: cartId,
    cartItems: [
      { id: 1, cartId: cartId, productId: productId, amount: 1 },
      { id: 2, cartId: cartId, productId: newProductId, amount: 1 }
    ]
  };

  const newProduct: ProductResponse = {
    id: newProductId,
    price: 200,
    supplier: 'Supplier 2',
    description: 'Description 2',
    wishlisted: 0,
    stock: 5,
    categoryName: 'Category 2',
    productName: 'Product 2',
    categoryId: 2
  };

  beforeEach(async () => {
    const cartServiceSpy = jasmine.createSpyObj('CartService', ['getCartById', 'addItemToCart', 'removeItemFromCart', 'createOrder']);
    const productServiceSpy = jasmine.createSpyObj('ProductService', ['getProductById']);
    const dataServiceSpy = jasmine.createSpyObj('ComponentDataService', ['getUserId']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    const appComponentSpy = jasmine.createSpyObj('AppComponent', ['showCustomNotification']);

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CartComponent],
      providers: [
        { provide: CartService, useValue: cartServiceSpy },
        { provide: ProductService, useValue: productServiceSpy },
        { provide: ComponentDataService, useValue: dataServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: AppComponent, useValue: appComponentSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CartComponent);
    component = fixture.componentInstance;
    mockCartService = TestBed.inject(CartService) as jasmine.SpyObj<CartService>;
    mockProductService = TestBed.inject(ProductService) as jasmine.SpyObj<ProductService>;
    mockDataService = TestBed.inject(ComponentDataService) as jasmine.SpyObj<ComponentDataService>;
    mockRouter = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    mockAppComponent = TestBed.inject(AppComponent) as jasmine.SpyObj<AppComponent>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load cart and products on init', () => {
    mockDataService.getUserId.and.returnValue(cartId);
    mockCartService.getCartById.and.returnValue(of(mockCart));
    mockProductService.getProductById.and.returnValue(of(mockProduct));

    component.ngOnInit();

    expect(mockCartService.getCartById).toHaveBeenCalledWith(cartId);
    expect(mockProductService.getProductById).toHaveBeenCalledWith(productId);
    expect(component.productsMap.size).toBe(1);
    expect(component.totalPrice).toBe(100); // Assuming only one item in the cart
  });

  it('should remove item from cart and update products', () => {
    const updatedCart: Cart = { id: cartId, cartItems: [] };

    mockCartService.removeItemFromCart.and.returnValue(of(updatedCart));
    component.cart = mockCart; // Set initial cart state

    component.removeItem(productId);

    expect(mockCartService.removeItemFromCart).toHaveBeenCalledWith(cartId, productId);
    expect(component.cart).toEqual(updatedCart);
    expect(component.productsMap.size).toBe(0); // Cart is empty now
  });

  it('should handle errors', () => {
    mockCartService.getCartById.and.returnValue(throwError(() => new Error('Error')));
    component.ngOnInit();

    expect(component.cart).toBeNull();
    // Ensure console.error is called or you can use jasmine spies to verify console error handling
  });
});

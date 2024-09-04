import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CartService, Cart, CartItem, Order } from './cart.service';

describe('CartService', () => {
  let service: CartService;
  let httpMock: HttpTestingController;
  const baseUrl = 'http://localhost:8089/shoppingcart/api/shoppingcart';
  const orderUrl = 'http://localhost:8089/shoppingcart/api/orders';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CartService]
    });

    service = TestBed.inject(CartService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Ensure no outstanding requests
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('addItemToCart', () => {
    it('should add an item to the cart and return updated cart', () => {
      const cartId = 1;
      const productId = 101;
      const mockCart: Cart = {
        id: cartId,
        cartItems: [{ id: 1, cartId, productId, amount: 2 }]
      };

      service.addItemToCart(cartId, productId).subscribe(cart => {
        expect(cart).toEqual(mockCart);
      });

      const req = httpMock.expectOne(`${baseUrl}/addItemToCart/${cartId}/${productId}`);
      expect(req.request.method).toBe('PUT');
      req.flush(mockCart);
    });
  });

  describe('getCartById', () => {
    it('should retrieve the cart by ID', () => {
      const cartId = 1;
      const mockCart: Cart = {
        id: cartId,
        cartItems: [{ id: 1, cartId, productId: 101, amount: 2 }]
      };

      service.getCartById(cartId).subscribe(cart => {
        expect(cart).toEqual(mockCart);
      });

      const req = httpMock.expectOne(`${baseUrl}/${cartId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockCart);
    });
  });

  describe('removeItemFromCart', () => {
    it('should remove an item from the cart and return updated cart', () => {
      const cartId = 1;
      const productId = 101;
      const mockCart: Cart = {
        id: cartId,
        cartItems: [{ id: 2, cartId, productId: 202, amount: 3 }]
      };

      service.removeItemFromCart(cartId, productId).subscribe(cart => {
        expect(cart).toEqual(mockCart);
      });

      const req = httpMock.expectOne(`${baseUrl}/removeItemFromCart/${cartId}/${productId}`);
      expect(req.request.method).toBe('PUT');
      req.flush(mockCart);
    });
  });

  describe('createOrder', () => {
    it('should create an order and return the order details', () => {
      const userId = 1;
      const totalPrice = 99.99;
      const mockOrder: Order = {
        orderId: 123,
        userId,
        orderItems: [{ id: 1, cartId: 1, productId: 101, amount: 2, orderId: 123 }],
        totalPrice
      };

      service.createOrder(userId, totalPrice).subscribe(order => {
        expect(order).toEqual(mockOrder);
      });

      const req = httpMock.expectOne(`${orderUrl}/createOrder/${userId}`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toBe(totalPrice);
      req.flush(mockOrder);
    });
  });
});

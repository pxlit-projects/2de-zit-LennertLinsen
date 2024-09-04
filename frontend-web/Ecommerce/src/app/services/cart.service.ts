import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


export interface Cart {
  id: number; // ID of the cart, same as user ID
  cartItems: CartItem[]; // List of items in the cart
}

export interface CartItem {
  id: number; // ID of the CartItem
  cartId: number; // ID of the cart this item belongs to
  productId: number; // ID of the product
  amount: number; // Quantity of the product in the cart
}

export interface OrderItem {
  id: number;
  cartId: number;
  productId: number;
  amount: number;
  orderId: number;
}

export interface Order {
  orderId: number;
  userId: number;
  orderItems: OrderItem[];
  totalPrice: number;
}


@Injectable({
  providedIn: 'root'
})
export class CartService {
  private baseUrl = 'http://localhost:8089/shoppingcart/api/shoppingcart';
  private orderUrl = 'http://localhost:8089/shoppingcart/api/orders';

  constructor(private http: HttpClient) { }

  addItemToCart(cartId: number, productId: number): Observable<Cart> {
    const url = `${this.baseUrl}/addItemToCart/${cartId}/${productId}`;
    return this.http.put<Cart>(url, null); // Using PUT as per your endpoint definition
  }

  getCartById(cartId: number | null): Observable<Cart> {
    const url = `${this.baseUrl}/${cartId}`;
    return this.http.get<Cart>(url);
  }
  removeItemFromCart(cartId: number, productId: number): Observable<Cart>{
    const url = `${this.baseUrl}/removeItemFromCart/${cartId}/${productId}`;
    return this.http.put<Cart>(url, null);
  }


  createOrder(userId: number, totalPrice: number): Observable<Order> {
    const url = `${this.orderUrl}/createOrder/${userId}`;
    return this.http.post<Order>(url, totalPrice);
  }

}

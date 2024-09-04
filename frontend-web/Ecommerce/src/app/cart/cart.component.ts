import { Component, OnInit } from '@angular/core';
import { CartService } from '../services/cart.service';
import { ProductService } from '../services/product.service';
import { Cart } from '../services/cart.service';
import { CartItem } from '../services/cart.service';
import { ProductResponse } from '../services/product.service';
import { ComponentDataService } from '../services/component-data.service';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import {MatSnackBar} from "@angular/material/snack-bar";
import {AppComponent} from "../app.component";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cart: Cart | null = null;
  productsMap: Map<number, ProductResponse> = new Map();
  totalPrice: number = 0;

  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private dataService: ComponentDataService,
    private router: Router,
    private appComponent: AppComponent
  ) {}

  ngOnInit(): void {
    const cartId = this.dataService.getUserId(); // Replace with dynamic user/cart ID as needed
    this.cartService.getCartById(cartId).subscribe({
      next: (data: Cart) => {
        this.cart = data;
        this.loadProducts();
      },
      error: (error) => {
        console.error('Error loading cart:', error);
      }
    });
  }

  loadProducts(): void {
    if (this.cart) {
      const productRequests = this.cart.cartItems.map((item: CartItem) =>
        this.productService.getProductById(item.productId)
      );

      forkJoin(productRequests).subscribe({
        next: (products: ProductResponse[]) => {
          products.forEach((product: ProductResponse) => {
            this.productsMap.set(product.id, product);
          });
          this.calculateTotalPrice();
        },
        error: (error) => {
          console.error('Error loading products:', error);
        }
      });
    }
  }

  getProductName(productId: number): string {
    return this.productsMap.get(productId)?.productName || 'Unknown Product';
  }

  getProductPrice(productId: number): string {
    const price = this.productsMap.get(productId)?.price || 0;
    return price.toFixed(2); // Format price to 2 decimal places
  }

  addItem(productId: number): void {
    if (this.cart) {
      this.cartService.addItemToCart(this.cart.id, productId).subscribe({
        next: (updatedCart: Cart) => {
          this.cart = updatedCart;
          this.loadProducts(); // Reload products to update prices and recalculate total
        },
        error: (error) => {
          console.error('Error adding item to cart:', error);
        }
      });
    }
  }

  removeItem(productId: number): void {
    if (this.cart) {
      this.cartService.removeItemFromCart(this.cart.id, productId).subscribe({
        next: (updatedCart: Cart) => {
          this.cart = updatedCart;
          this.loadProducts(); // Reload products to update prices and recalculate total
        },
        error: (error) => {
          console.error('Error removing item from cart:', error);
        }
      });
    }
  }

  calculateTotalPrice(): void {
    this.totalPrice = 0;
    if (this.cart && this.cart.cartItems) {
      this.cart.cartItems.forEach((item: CartItem) => {
        const product = this.productsMap.get(item.productId);
        if (product) {
          this.totalPrice += product.price * item.amount;
        }
      });
    }
  }

  getFormattedTotalPrice(): string {
    return this.totalPrice.toFixed(2); // Format total price to 2 decimal places
  }

  goBack(): void {
    this.router.navigate(['/products']);
  }


  createOrder() {
    const userId = this.dataService.getUserId();
    if (this.cart && this.totalPrice > 0 && userId !== null) {
      this.cartService.createOrder(userId, this.totalPrice).subscribe({
        next: (response) => {
          console.log('Order created successfully', response);
          this.appComponent.showCustomNotification('Order created successfully!');
          this.router.navigate(['/products']);
        },
        error: (error) => {
          console.error('Error creating order:', error);
          this.appComponent.showCustomNotification('Failed to create order. Please try again.');
        },
      });
    }
  }

}

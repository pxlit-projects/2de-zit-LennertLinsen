import { Component, OnInit } from '@angular/core';
import {ProductResponse, ProductService} from "../services/product.service";
import {Wishlist, WishlistService} from "../services/wishlist.service";
import {ComponentDataService} from "../services/component-data.service";
import {Router} from "@angular/router";
import {CartService} from "../services/cart.service";


@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent implements OnInit {
  wishlistItems: ProductResponse[] = [];

  constructor(
    private wishlistService: WishlistService,
    private productService: ProductService,
    private dataService: ComponentDataService,
    private router: Router,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    this.loadWishlist();
  }

  loadWishlist(): void {
    const userId = this.dataService.getUserId();
    if (userId) {
      this.wishlistService.getWishlistByUserId(userId).subscribe({
        next: (wishlist: Wishlist) => {
          this.loadProducts(wishlist.productIds);
        },
        error: (err) => {
          console.error('Error fetching wishlist:', err);
        }
      });
    }
  }

  loadProducts(productIds: number[]): void {
    productIds.forEach(id => {
      this.productService.getProductById(id).subscribe({
        next: (product) => {
          this.wishlistItems.push(product);
        },
        error: (err) => {
          console.error('Error fetching product:', err);
        }
      });
    });
  }

  addToCart(product: any): void {
    const cartId = this.dataService.getUserId(); // Use userId as cartId as instructed
    if (cartId) {
      this.cartService.addItemToCart(cartId, product.id).subscribe({
        next: (response) => {
          console.log('Item added to cart:', response);
          this.updateStock(product); // Update stock locally
          // Optionally, show a success message here
        },
        error: (error) => {
          console.error('Error adding item to cart:', error);
          // Handle error (e.g., show an error message)
        }
      });
    } else {
      console.error('User ID not found');
      // Handle case where user ID is not available
    }
  }

  removeFromWishlist(item: any) {
    const userId = this.dataService.getUserId();
    if (userId !== null) {  // Ensure userId is not null
      this.wishlistService.removeItemFromWishlist(userId, item.id).subscribe({
        next: (updatedWishlist) => {
          this.wishlistItems = this.wishlistItems.filter(i => i.id !== item.id); // Update the UI
          console.log(`Removed ${item.productName} from wishlist`);
        },
        error: (err) => {
          console.error('Error removing item from wishlist', err);
        }
      });
    } else {
      console.error('User ID is null, cannot remove item from wishlist');
    }

  }

  goBackToCatalog(): void {
    this.router.navigate(['/products']);
  }

  updateStock(product: ProductResponse): void {
    // Find the product in the products array and update its stock
    const productToUpdate = product;
    if (productToUpdate && productToUpdate.stock > 0) {
      productToUpdate.stock -= 1; // Decrease the stock by 1
    }
  }
}

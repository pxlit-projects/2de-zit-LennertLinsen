import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductResponse, ProductService } from "../services/product.service";
import { ComponentDataService } from "../services/component-data.service";
import { WishlistService } from "../services/wishlist.service";
import { CartService } from "../services/cart.service";
import { ReviewService } from "../services/review.service";

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {
  product: ProductResponse | null = null;
  reviews: any[] = [];
  newReview = {
    title: '',
    description: '',
    stars: 1,
    supplier: this.product?.supplier
  };

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private router: Router,
    private dataService: ComponentDataService,
    private wishlistService: WishlistService,
    private cartService: CartService,
    private reviewService: ReviewService
  ) { }

  ngOnInit(): void {
    const productId = +this.route.snapshot.paramMap.get('id')!;

    this.productService.getProductById(productId).subscribe((data: ProductResponse) => {
      this.product = data;
      this.newReview.supplier = this.product.supplier; // Set supplier here
      this.loadReviews(productId);
    });
  }


  loadReviews(productId: number): void {
    this.reviewService.getReviewsByProductId(productId).subscribe((data: any[]) => {
      this.reviews = data.reverse(); // Reverse the array here
    });
  }

  goBack(): void {
    this.router.navigate(['/products']);
  }

  addToWishList(productId: number): void {
    const userId = this.dataService.getUserId();
    if (userId && this.product) {
      this.wishlistService.addItemToWishlist(productId).subscribe({
        next: (response) => {
          console.log('Item added to wishlist:', response);
          this.product!.wishlisted += 1;
        },
        error: (error) => {
          console.error('Error adding item to wishlist:', error);
        }
      });
    } else {
      console.error('User ID not found');
    }
  }

  addToCart(productId: number): void {
    const cartId = this.dataService.getUserId();
    if (cartId && this.product) {
      this.cartService.addItemToCart(cartId, productId).subscribe({
        next: (response) => {
          console.log('Item added to cart:', response);
          if (this.product!.stock > 0) {
            this.product!.stock -= 1;
          }
        },
        error: (error) => {
          console.error('Error adding item to cart:', error);
        }
      });
    } else {
      console.error('User ID not found');
    }
  }

  submitReview(): void {
    const productId = this.product?.id;
    if (productId) {
      this.newReview.supplier = this.product?.supplier; // Ensure supplier is set here
      this.reviewService.addReview(productId, this.newReview).subscribe({
        next: (response) => {
          console.log('Review added:', response);
          this.loadReviews(productId); // Reload reviews after adding a new one
          this.newReview = { title: '', description: '', stars: 1, supplier: this.product?.supplier };
        },
        error: (error) => {
          console.error('Error adding review:', error);
        }
      });
    } else {
      console.error('Product ID not found');
    }
  }

  viewSupplierReviews(supplier: string): void {
    this.router.navigate(['/supplier-reviews'], { queryParams: { supplier: supplier } });
  }
}

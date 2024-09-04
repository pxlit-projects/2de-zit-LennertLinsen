import { Component, OnInit } from '@angular/core';
import { ProductService } from '../services/product.service'; // Adjust the path as needed
import { ProductResponse} from "../services/product.service";

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {
  products: ProductResponse[] = [];
  refillQuantity: { [productId: number]: number } = {}; // Store refill quantities by product ID

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getAllProducts().subscribe((data: ProductResponse[]) => {
      this.products = data;
    });
  }

  deleteProduct(productId: number): void {
    this.productService.deleteProduct(productId).subscribe({
      next: () => {
        console.log('Product deleted successfully');
        this.loadProducts(); // Refresh the product list
      },
      error: (error) => {
        console.error('Error deleting product:', error);
      }
    });
  }

  refillStock(productId: number): void {
    const quantity = this.refillQuantity[productId]; // Get the quantity from the input field
    if (quantity > 0) {
      this.productService.refillStock(productId, quantity).subscribe({
        next: (updatedProduct) => {
          console.log('Stock refilled successfully:', updatedProduct);
          this.loadProducts(); // Refresh the product list
        },
        error: (error) => {
          console.error('Error refilling stock:', error);
        }
      });
    } else {
      console.error('Please enter a valid quantity for refilling.');
    }
  }
}

import { Component, OnInit } from '@angular/core';
import { ProductResponse, ProductService } from "../services/product.service";
import { ComponentDataService } from "../services/component-data.service";
import { CartService } from "../services/cart.service";
import { CategoryResponse, CategoryService } from "../services/category.service";

@Component({
  selector: 'app-product-catalog',
  templateUrl: './product-catalog.component.html',
  styleUrls: ['./product-catalog.component.css']
})
export class ProductCatalogComponent implements OnInit {

  products: ProductResponse[] = [];
  filteredProducts: ProductResponse[] = [];
  categories: CategoryResponse[] = [];
  selectedCategoryId: number | null = null;
  searchTerm: string = '';
  sortDirection: 'asc' | 'desc' | '' = '';

  constructor(
    private productService: ProductService,
    private dataService: ComponentDataService,
    private cartService: CartService,
    private categoryService: CategoryService
  ) { }

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
  }

  loadProducts(): void {
    this.productService.getAllProducts().subscribe((data: ProductResponse[]) => {
      this.products = data;
      this.applyFilters();
    });
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe((data: CategoryResponse[]) => {
      this.categories = data;
    });
  }

  applyFilters(): void {
    let filtered = this.products;

    // Filter by category
    if (this.selectedCategoryId) {
      filtered = filtered.filter(product => product.categoryId === this.selectedCategoryId);
    }

    // Filter by search term
    if (this.searchTerm) {
      filtered = filtered.filter(product =>
        product.productName.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }

    // Sort by price
    if (this.sortDirection) {
      filtered = filtered.sort((a, b) => {
        return this.sortDirection === 'asc' ? a.price - b.price : b.price - a.price;
      });
    }

    this.filteredProducts = filtered;
  }

  onPriceSortChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.sortDirection = target.value as 'asc' | 'desc' | '';
    this.applyFilters();
  }

  onCategoryFilterChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.selectedCategoryId = target.value ? +target.value : null;
    this.applyFilters();
  }

  onSearchChange(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.searchTerm = target.value;
    this.applyFilters();
  }

  addToCart(product: ProductResponse): void {
    const cartId = this.dataService.getUserId(); // Use userId as cartId as instructed
    if (cartId) {
      this.cartService.addItemToCart(cartId, product.id).subscribe({
        next: (response) => {
          console.log('Item added to cart:', response);
          this.updateStock(product); // Update stock locally
        },
        error: (error) => {
          console.error('Error adding item to cart:', error);
        }
      });
    } else {
      console.error('User ID not found');
    }
  }

  updateStock(product: ProductResponse): void {
    const productToUpdate = this.products.find(p => p.id === product.id);
    if (productToUpdate && productToUpdate.stock > 0) {
      productToUpdate.stock -= 1;
    }
  }
}

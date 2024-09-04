import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../services/product.service';
import { ProductDTO } from '../services/product.service';
import { CategoryResponse, CategoryService } from '../services/category.service';

@Component({
  selector: 'app-edit-product',
  templateUrl: './edit-product.component.html',
  styleUrls: ['../add-product/add-product.component.css']
})
export class EditProductComponent implements OnInit {
  productForm: FormGroup;
  productId: number = 0;
  product: ProductDTO = {
    productName: '',
    description: '',
    categoryId: 0,
    stock: 0,
    price: 0,
    supplier: ''
  };
  categories: CategoryResponse[] = [];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private productService: ProductService,
    private router: Router,
    private categoryService: CategoryService
  ) {
    this.productForm = this.fb.group({
      productName: ['', Validators.required],
      description: ['', Validators.required],
      stock: [0, [Validators.required, Validators.min(0)]],
      price: [0, [Validators.required, Validators.min(0)]],
      categoryId: [null, Validators.required],
      supplier: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.productId = +this.route.snapshot.params['id'];  // Use '+' to convert to number
    this.loadCategories(); // Load categories
    this.loadProduct();    // Load the product details
  }

  loadProduct(): void {
    this.productService.getProductById(this.productId).subscribe((product: ProductDTO) => {
      this.product = product;
      this.productForm.patchValue(product); // Fill the form with the existing product data
    });
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe((data: CategoryResponse[]) => {
      this.categories = data;
    });
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      const updatedProduct = this.productForm.value as ProductDTO;
      this.productService.updateProduct(this.productId, updatedProduct).subscribe(() => {
        this.router.navigate(['/admin']); // Redirect back to the product list
      });
    }
  }
}


import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { ProductCatalogComponent } from './product-catalog.component';
import { ProductService, ProductResponse } from '../services/product.service';
import { CategoryService, CategoryResponse } from '../services/category.service';
import { ComponentDataService } from '../services/component-data.service';
import { CartService, Cart } from '../services/cart.service';

describe('ProductCatalogComponent', () => {
  let component: ProductCatalogComponent;
  let fixture: ComponentFixture<ProductCatalogComponent>;
  let mockProductService: jasmine.SpyObj<ProductService>;
  let mockCategoryService: jasmine.SpyObj<CategoryService>;
  let mockDataService: jasmine.SpyObj<ComponentDataService>;
  let mockCartService: jasmine.SpyObj<CartService>;

  const mockProducts: ProductResponse[] = [
    {
      id: 1,
      productName: 'Product 1',
      description: 'Description 1',
      stock: 10,
      price: 100,
      categoryId: 1,
      categoryName: 'Category 1',
      supplier: 'Supplier 1',
      wishlisted: 0
    },
    {
      id: 2,
      productName: 'Product 2',
      description: 'Description 2',
      stock: 20,
      price: 200,
      categoryId: 2,
      categoryName: 'Category 2',
      supplier: 'Supplier 2',
      wishlisted: 0
    }
  ];

  const mockCategories: CategoryResponse[] = [
    { categoryId: 1, categoryName: 'Category 1', categoryIds: [1] },
    { categoryId: 2, categoryName: 'Category 2', categoryIds: [2] }
  ];

  const mockCart: Cart = {
    id: 1,
    cartItems: []
  };

  beforeEach(async () => {
    mockProductService = jasmine.createSpyObj<ProductService>('ProductService', ['getAllProducts']);
    mockCategoryService = jasmine.createSpyObj<CategoryService>('CategoryService', ['getCategories']);
    mockDataService = jasmine.createSpyObj<ComponentDataService>('ComponentDataService', ['getUserId']);
    mockCartService = jasmine.createSpyObj<CartService>('CartService', ['addItemToCart']);

    await TestBed.configureTestingModule({
      declarations: [ProductCatalogComponent],
      imports: [
        ReactiveFormsModule,
        RouterTestingModule
      ],
      providers: [
        { provide: ProductService, useValue: mockProductService },
        { provide: CategoryService, useValue: mockCategoryService },
        { provide: ComponentDataService, useValue: mockDataService },
        { provide: CartService, useValue: mockCartService }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductCatalogComponent);
    component = fixture.componentInstance;

    mockProductService.getAllProducts.and.returnValue(of(mockProducts));
    mockCategoryService.getCategories.and.returnValue(of(mockCategories));
    mockDataService.getUserId.and.returnValue(1);
    mockCartService.addItemToCart.and.returnValue(of(mockCart)); // Correct type

    fixture.detectChanges(); // Initial binding
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load products and categories on initialization', () => {
    expect(mockProductService.getAllProducts).toHaveBeenCalled();
    expect(mockCategoryService.getCategories).toHaveBeenCalled();
    expect(component.products).toEqual(mockProducts);
    expect(component.categories).toEqual(mockCategories);
  });

  it('should filter products by selected category', () => {
    component.selectedCategoryId = 1;
    component.applyFilters();
    expect(component.filteredProducts).toEqual([mockProducts[0]]);
  });

  it('should filter products by search term', () => {
    component.searchTerm = 'Product 1';
    component.applyFilters();
    expect(component.filteredProducts).toEqual([mockProducts[0]]);
  });

  it('should sort products by price in ascending order', () => {
    component.sortDirection = 'asc';
    component.applyFilters();
    expect(component.filteredProducts).toEqual([mockProducts[0], mockProducts[1]]);
  });

  it('should add product to cart and update stock', () => {
    const product = mockProducts[0];
    component.addToCart(product);
    expect(mockCartService.addItemToCart).toHaveBeenCalledWith(1, product.id);
    expect(product.stock).toBe(9); // Assuming stock is decremented by 1
  });
});

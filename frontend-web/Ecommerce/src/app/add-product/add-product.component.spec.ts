import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { AddProductComponent } from './add-product.component';
import { ProductService } from '../services/product.service';
import { CategoryService } from '../services/category.service';
import { CategoryResponse } from '../services/category.service';

describe('AddProductComponent', () => {
  let component: AddProductComponent;
  let fixture: ComponentFixture<AddProductComponent>;
  let mockProductService: jasmine.SpyObj<ProductService>;
  let mockCategoryService: jasmine.SpyObj<CategoryService>;

  const mockCategories: CategoryResponse[] = [
    { categoryId: 1, categoryName: 'Category 1', categoryIds: [1] },
    { categoryId: 2, categoryName: 'Category 2', categoryIds: [2] }
  ];

  beforeEach(async () => {
    mockProductService = jasmine.createSpyObj<ProductService>('ProductService', ['addProduct']);
    mockCategoryService = jasmine.createSpyObj<CategoryService>('CategoryService', ['getCategories']);

    await TestBed.configureTestingModule({
      declarations: [AddProductComponent],
      imports: [
        ReactiveFormsModule,
        RouterTestingModule
      ],
      providers: [
        FormBuilder,
        { provide: ProductService, useValue: mockProductService },
        { provide: CategoryService, useValue: mockCategoryService }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddProductComponent);
    component = fixture.componentInstance;

    // Set up the mocks
    mockCategoryService.getCategories.and.returnValue(of(mockCategories));
    mockProductService.addProduct.and.returnValue(of({
      productName: 'New Product',
      description: 'Product Description',
      categoryId: 1,
      stock: 10,
      price: 100,
      supplier: 'Supplier'
    }));

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load categories on initialization', () => {
    expect(component.categories).toEqual(mockCategories);
    expect(mockCategoryService.getCategories).toHaveBeenCalled();
  });

  it('should submit form successfully', () => {
    const navigateSpy = spyOn(component['router'], 'navigate');
    component.productForm.setValue({
      productName: 'New Product',
      description: 'Product Description',
      stock: 10,
      price: 100,
      categoryId: 1,
      supplier: 'Supplier'
    });

    component.onSubmit();

    expect(mockProductService.addProduct).toHaveBeenCalledWith(component.productForm.value);
  });

  it('should not submit form if invalid', () => {
    component.productForm.setValue({
      productName: '',
      description: '',
      stock: -1,
      price: -1,
      categoryId: null,
      supplier: ''
    });

    component.onSubmit();

    expect(mockProductService.addProduct).not.toHaveBeenCalled();
  });

  it('should handle error on product addition', () => {
    mockProductService.addProduct.and.returnValue(throwError(() => new Error('Error adding product')));

    component.productForm.setValue({
      productName: 'New Product',
      description: 'Product Description',
      stock: 10,
      price: 100,
      categoryId: 1,
      supplier: 'Supplier'
    });

    component.onSubmit();

    // Expect an error to be logged to the console (you might need to spy on console.error in a real test)
    expect(mockProductService.addProduct).toHaveBeenCalled();
  });
});

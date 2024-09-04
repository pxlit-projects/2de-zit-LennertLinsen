import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';
import { EditProductComponent } from './edit-product.component';
import { ProductService, ProductResponse, ProductDTO } from '../services/product.service';
import { CategoryService, CategoryResponse } from '../services/category.service';

describe('EditProductComponent', () => {
  let component: EditProductComponent;
  let fixture: ComponentFixture<EditProductComponent>;
  let mockProductService: jasmine.SpyObj<ProductService>;
  let mockCategoryService: jasmine.SpyObj<CategoryService>;

  const mockCategories: CategoryResponse[] = [
    { categoryId: 1, categoryName: 'Category 1', categoryIds: [1] },
    { categoryId: 2, categoryName: 'Category 2', categoryIds: [2] }
  ];

  const mockProduct: ProductResponse = {
    id: 1,
    productName: 'Existing Product',
    description: 'Existing Description',
    stock: 10,
    price: 100,
    categoryId: 1,
    categoryName: 'Category 1',
    supplier: 'Existing Supplier',
    wishlisted: 0
  };

  beforeEach(async () => {
    mockProductService = jasmine.createSpyObj<ProductService>('ProductService', ['getProductById', 'updateProduct']);
    mockCategoryService = jasmine.createSpyObj<CategoryService>('CategoryService', ['getCategories']);

    await TestBed.configureTestingModule({
      declarations: [EditProductComponent],
      imports: [
        ReactiveFormsModule,
        RouterTestingModule
      ],
      providers: [
        FormBuilder,
        { provide: ProductService, useValue: mockProductService },
        { provide: CategoryService, useValue: mockCategoryService },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              params: { id: '1' }
            },
            paramMap: of(convertToParamMap({ id: '1' }))
          }
        }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditProductComponent);
    component = fixture.componentInstance;

    // Set up the mocks
    mockCategoryService.getCategories.and.returnValue(of(mockCategories));
    mockProductService.getProductById.and.returnValue(of(mockProduct));
    mockProductService.updateProduct.and.returnValue(of(mockProduct)); // Provide a ProductResponse as a successful update response

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load product and categories on initialization', () => {
    expect(mockProductService.getProductById).toHaveBeenCalledWith(1);
    expect(mockCategoryService.getCategories).toHaveBeenCalled();
    expect(component.productForm.value).toEqual({
      productName: 'Existing Product',
      description: 'Existing Description',
      stock: 10,
      price: 100,
      categoryId: 1,
      supplier: 'Existing Supplier'
    });
    expect(component.categories).toEqual(mockCategories);
  });

  it('should submit form on successful update', () => {
    const navigateSpy = spyOn(component['router'], 'navigate');
    component.productForm.setValue({
      productName: 'Updated Product',
      description: 'Updated Description',
      stock: 20,
      price: 200,
      categoryId: 2,
      supplier: 'Updated Supplier'
    });

    component.onSubmit();

    expect(mockProductService.updateProduct).toHaveBeenCalledWith(1, component.productForm.value);
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

    expect(mockProductService.updateProduct).not.toHaveBeenCalled();
  });

  it('should handle error on product update', () => {
    mockProductService.updateProduct.and.returnValue(throwError(() => new Error('Error updating product')));

    component.productForm.setValue({
      productName: 'Updated Product',
      description: 'Updated Description',
      stock: 20,
      price: 200,
      categoryId: 2,
      supplier: 'Updated Supplier'
    });

    component.onSubmit();

    // Expect an error to be logged to the console (you might need to spy on console.error in a real test)
    expect(mockProductService.updateProduct).toHaveBeenCalled();
  });
});

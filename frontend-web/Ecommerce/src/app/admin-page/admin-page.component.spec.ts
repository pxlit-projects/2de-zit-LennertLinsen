import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdminPageComponent } from './admin-page.component';
import { ProductService } from '../services/product.service';
import { of, throwError } from 'rxjs';
import { ProductResponse } from "../services/product.service";
import { FormsModule } from '@angular/forms';
import { RouterTestingModule} from "@angular/router/testing";

describe('AdminPageComponent', () => {
  let component: AdminPageComponent;
  let fixture: ComponentFixture<AdminPageComponent>;
  let mockProductService: jasmine.SpyObj<ProductService>;

  const mockProducts: ProductResponse[] = [
    { id: 1, productName: 'Product 1', description: 'Description 1', stock: 10, price: 100, categoryId: 1, categoryName: 'Category 1', supplier: 'Supplier 1', wishlisted: 0 },
    { id: 2, productName: 'Product 2', description: 'Description 2', stock: 20, price: 200, categoryId: 2, categoryName: 'Category 2', supplier: 'Supplier 2', wishlisted: 1 }
  ];

  beforeEach(async () => {
    mockProductService = jasmine.createSpyObj<ProductService>('ProductService', [
      'getAllProducts',
      'deleteProduct',
      'refillStock'
    ]);

    await TestBed.configureTestingModule({
      declarations: [AdminPageComponent],
      imports: [FormsModule, RouterTestingModule],
      providers: [
        { provide: ProductService, useValue: mockProductService }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminPageComponent);
    component = fixture.componentInstance;

    // Ensure the method returns an Observable
    mockProductService.getAllProducts.and.returnValue(of(mockProducts));
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load products on initialization', () => {
    component.ngOnInit();

    // Use tick to simulate asynchronous passage of time if needed
    fixture.detectChanges();

    expect(mockProductService.getAllProducts).toHaveBeenCalled();
    expect(component.products).toEqual(mockProducts);
  });

  it('should delete a product and reload the product list', () => {
    mockProductService.deleteProduct.and.returnValue(of(undefined));
    mockProductService.getAllProducts.and.returnValue(of(mockProducts));

    component.deleteProduct(1);

    expect(mockProductService.deleteProduct).toHaveBeenCalledWith(1);
    expect(mockProductService.getAllProducts).toHaveBeenCalled();
  });

  it('should handle error when deleting a product', () => {
    const errorResponse = new Error('Error deleting product');
    mockProductService.deleteProduct.and.returnValue(throwError(() => errorResponse));

    spyOn(console, 'error');

    component.deleteProduct(1);

    expect(console.error).toHaveBeenCalledWith('Error deleting product:', errorResponse);
  });

  it('should refill stock for a valid quantity and reload the product list', () => {
    const updatedProduct: ProductResponse = { ...mockProducts[0], stock: 15 };
    mockProductService.refillStock.and.returnValue(of(updatedProduct));
    mockProductService.getAllProducts.and.returnValue(of(mockProducts));

    component.refillQuantity[1] = 5;
    component.refillStock(1);

    expect(mockProductService.refillStock).toHaveBeenCalledWith(1, 5);
    expect(mockProductService.getAllProducts).toHaveBeenCalled();
  });

  it('should handle error when refilling stock', () => {
    const errorResponse = new Error('Error refilling stock');
    mockProductService.refillStock.and.returnValue(throwError(() => errorResponse));

    spyOn(console, 'error');

    component.refillQuantity[1] = 5;
    component.refillStock(1);

    expect(console.error).toHaveBeenCalledWith('Error refilling stock:', errorResponse);
  });

  it('should handle invalid refill quantity', () => {
    spyOn(console, 'error');

    component.refillQuantity[1] = 0;
    component.refillStock(1);

    expect(console.error).toHaveBeenCalledWith('Please enter a valid quantity for refilling.');
  });
});

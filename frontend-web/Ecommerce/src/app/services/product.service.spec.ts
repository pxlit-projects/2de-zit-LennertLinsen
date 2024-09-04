import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ProductService, ProductResponse, ProductDTO } from './product.service'; // Adjust the path as needed

describe('ProductService', () => {
  let service: ProductService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8089/catalogue/api/products/';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProductService]
    });

    service = TestBed.inject(ProductService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Ensure there are no outstanding requests
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getAllProducts', () => {
    it('should return an array of products', () => {
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
          wishlisted: 5
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
          wishlisted: 10
        }
      ];

      service.getAllProducts().subscribe(products => {
        expect(products.length).toBe(2);
        expect(products).toEqual(mockProducts);
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('GET');
      req.flush(mockProducts);
    });
  });

  describe('getProductById', () => {
    it('should return a single product by ID', () => {
      const mockProduct: ProductResponse = {
        id: 1,
        productName: 'Product 1',
        description: 'Description 1',
        stock: 10,
        price: 100,
        categoryId: 1,
        categoryName: 'Category 1',
        supplier: 'Supplier 1',
        wishlisted: 5
      };

      service.getProductById(1).subscribe(product => {
        expect(product).toEqual(mockProduct);
      });

      const req = httpMock.expectOne(`${apiUrl}1`);
      expect(req.request.method).toBe('GET');
      req.flush(mockProduct);
    });
  });

  describe('addProduct', () => {
    it('should add a new product and return the product details', () => {
      const formData = new FormData();
      formData.append('productName', 'New Product');
      formData.append('description', 'New Description');
      formData.append('categoryId', '1');
      formData.append('stock', '15');
      formData.append('price', '150');
      formData.append('supplier', 'New Supplier');

      const mockProductDTO: ProductDTO = {
        productName: 'New Product',
        description: 'New Description',
        categoryId: 1,
        stock: 15,
        price: 150,
        supplier: 'New Supplier'
      };

      service.addProduct(formData).subscribe(product => {
        expect(product).toEqual(mockProductDTO);
      });

      const req = httpMock.expectOne(`${apiUrl}/add`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toBe(formData); // Note: formData might need specific handling in real-world tests
      req.flush(mockProductDTO);
    });
  });


  describe('refillStock', () => {
    it('should refill the stock and return the updated product', () => {
      const mockProduct: ProductResponse = {
        id: 1,
        productName: 'Product 1',
        description: 'Description 1',
        stock: 20,
        price: 100,
        categoryId: 1,
        categoryName: 'Category 1',
        supplier: 'Supplier 1',
        wishlisted: 5
      };

      service.refillStock(1, 10).subscribe(product => {
        expect(product).toEqual(mockProduct);
      });

      const req = httpMock.expectOne(`${apiUrl}refill/1`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toBe(10);
      req.flush(mockProduct);
    });
  });

  describe('updateProduct', () => {
    it('should update a product and return the updated product details', () => {
      const mockProductDTO: ProductDTO = {
        productName: 'Updated Product',
        description: 'Updated Description',
        categoryId: 1,
        stock: 30,
        price: 200,
        supplier: 'Updated Supplier'
      };

      service.updateProduct(1, mockProductDTO).subscribe(product => {
        expect(product).toEqual(mockProductDTO);
      });

      const req = httpMock.expectOne(`${apiUrl}/update/1`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(mockProductDTO);
      req.flush(mockProductDTO);
    });
  });
});

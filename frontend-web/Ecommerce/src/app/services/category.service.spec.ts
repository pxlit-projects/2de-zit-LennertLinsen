import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CategoryService, CategoryResponse } from './category.service'; // Adjust the path as needed

describe('CategoryService', () => {
  let service: CategoryService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8089/catalogue/api/categories/';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CategoryService]
    });

    service = TestBed.inject(CategoryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Ensure no outstanding requests
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getCategories', () => {
    it('should retrieve categories from the API', () => {
      const mockCategories: CategoryResponse[] = [
        { categoryId: 1, categoryName: 'Electronics', categoryIds: [2, 3] },
        { categoryId: 2, categoryName: 'Books', categoryIds: [1] }
      ];

      service.getCategories().subscribe(categories => {
        expect(categories.length).toBe(2);
        expect(categories).toEqual(mockCategories);
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('GET');
      req.flush(mockCategories);
    });

    it('should handle error responses', () => {
      const errorMessage = 'Unable to fetch categories';

      service.getCategories().subscribe(
        () => fail('should have failed with the 500 error'),
        error => {
          expect(error.error).toContain(errorMessage);
        }
      );

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('GET');
      req.flush(errorMessage, { status: 500, statusText: 'Server Error' });
    });
  });
});

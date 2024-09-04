import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ReviewService, ProductReview } from './review.service';

describe('ReviewService', () => {
  let service: ReviewService;
  let httpMock: HttpTestingController;

  const apiUrl = 'http://localhost:8089/review/api/productreviews';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ReviewService]
    });

    service = TestBed.inject(ReviewService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('getReviewsByProductId', () => {
    it('should return reviews for a given product ID', () => {
      const productId = 1;
      const mockReviews = [
        { id: 1, title: 'Great product', description: 'Loved it', stars: 5, reviewTime: '2024-09-01T00:00:00Z', productId: 1, supplier: 'Supplier A' },
        { id: 2, title: 'Not bad', description: 'It was okay', stars: 3, reviewTime: '2024-09-02T00:00:00Z', productId: 1, supplier: 'Supplier B' }
      ];

      service.getReviewsByProductId(productId).subscribe(reviews => {
        expect(reviews.length).toBe(2);
        expect(reviews).toEqual(mockReviews);
      });

      const req = httpMock.expectOne(`${apiUrl}/${productId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockReviews);
    });
  });

  describe('addReview', () => {
    it('should add a review and return it', () => {
      const productId = 1;
      const newReview = { title: 'Awesome', description: 'Really good', stars: 4, reviewTime: '2024-09-03T00:00:00Z', productId: 1, supplier: 'Supplier C' };
      const mockResponse = { id: 3, ...newReview };

      service.addReview(productId, newReview).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${apiUrl}/add/${productId}`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(newReview);
      req.flush(mockResponse);
    });
  });

  describe('getReviewsBySupplier', () => {
    it('should return reviews for a given supplier', () => {
      const supplier = 'Supplier A';
      const mockReviews: ProductReview[] = [
        { id: 1, title: 'Great product', description: 'Loved it', stars: 5, reviewTime: '2024-09-01T00:00:00Z', productId: 1, supplier },
        { id: 2, title: 'Nice', description: 'Good quality', stars: 4, reviewTime: '2024-09-02T00:00:00Z', productId: 2, supplier }
      ];

      service.getReviewsBySupplier(supplier).subscribe(reviews => {
        expect(reviews.length).toBe(2);
        expect(reviews).toEqual(mockReviews);
      });

      const req = httpMock.expectOne(`${apiUrl}/bySupplier?supplier=${encodeURIComponent(supplier)}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockReviews);
    });
  });
});

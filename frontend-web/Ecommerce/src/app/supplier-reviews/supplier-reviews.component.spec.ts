import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';
import { SupplierReviewsComponent } from './supplier-reviews.component';
import { ReviewService, ProductReview } from '../services/review.service';

describe('SupplierReviewsComponent', () => {
  let component: SupplierReviewsComponent;
  let fixture: ComponentFixture<SupplierReviewsComponent>;
  let mockReviewService: jasmine.SpyObj<ReviewService>;
  let activatedRouteStub: { queryParams: { subscribe: jasmine.Spy } };

  beforeEach(() => {
    // Create a mock for ReviewService
    const reviewServiceSpy = jasmine.createSpyObj('ReviewService', ['getReviewsBySupplier']);

    // Create a stub for ActivatedRoute
    activatedRouteStub = {
      queryParams: {
        subscribe: jasmine.createSpy().and.callFake((callback: (params: any) => void) => {
          // Simulate query parameters
          callback({ supplier: 'Supplier 1' });
        })
      }
    };

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SupplierReviewsComponent],
      providers: [
        { provide: ReviewService, useValue: reviewServiceSpy },
        { provide: ActivatedRoute, useValue: activatedRouteStub }
      ]
    });

    fixture = TestBed.createComponent(SupplierReviewsComponent);
    component = fixture.componentInstance;
    mockReviewService = TestBed.inject(ReviewService) as jasmine.SpyObj<ReviewService>;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch reviews on initialization with supplier from route', () => {
    const mockReviews: ProductReview[] = [
      { id: 1, title: 'Great Product', description: 'I love it!', stars: 5, reviewTime: '2024-01-01', productId: 1, supplier: 'Supplier 1' }
    ];

    // Set up the mock response for the review service
    mockReviewService.getReviewsBySupplier.and.returnValue(of(mockReviews));

    fixture.detectChanges(); // Trigger ngOnInit

    expect(mockReviewService.getReviewsBySupplier).toHaveBeenCalledWith('Supplier 1');
    expect(component.reviews).toEqual(mockReviews);
    expect(component.loading).toBeFalse();
  });

  it('should handle error when fetching reviews', () => {
    // Set up the mock error response
    mockReviewService.getReviewsBySupplier.and.returnValue(throwError(() => new Error('Failed to fetch reviews')));

    fixture.detectChanges(); // Trigger ngOnInit

    expect(component.error).toBe('Failed to load reviews');
    expect(component.loading).toBeFalse();
  });

  it('should set loading to true while fetching reviews', () => {
    const mockReviews: ProductReview[] = [
      { id: 1, title: 'Great Product', description: 'I love it!', stars: 5, reviewTime: '2024-01-01', productId: 1, supplier: 'Supplier 1' }
    ];

    // Set up the mock response for the review service
    mockReviewService.getReviewsBySupplier.and.returnValue(of(mockReviews));

    // Initially, loading should be true
    expect(component.loading).toBeTrue();

    fixture.detectChanges(); // Trigger ngOnInit

    // After fetching reviews, loading should be false
    expect(component.loading).toBeFalse();
  });
});

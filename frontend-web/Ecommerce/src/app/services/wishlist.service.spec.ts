import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { WishlistService, Wishlist } from './wishlist.service';
import { ComponentDataService } from './component-data.service';
import { of, throwError } from 'rxjs';

describe('WishlistService', () => {
  let service: WishlistService;
  let httpMock: HttpTestingController;
  let componentDataServiceSpy: jasmine.SpyObj<ComponentDataService>;

  const baseUrl = 'http://localhost:8089/wishlist/api/wishlist';

  beforeEach(() => {
    const spy = jasmine.createSpyObj('ComponentDataService', ['getUserId']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        WishlistService,
        { provide: ComponentDataService, useValue: spy }
      ]
    });

    service = TestBed.inject(WishlistService);
    httpMock = TestBed.inject(HttpTestingController);
    componentDataServiceSpy = TestBed.inject(ComponentDataService) as jasmine.SpyObj<ComponentDataService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('addItemToWishlist', () => {
    it('should add an item to the wishlist and return success', () => {
      const userId = 1;
      const productId = 101;

      componentDataServiceSpy.getUserId.and.returnValue(userId);

      service.addItemToWishlist(productId).subscribe(response => {
        expect(response).toBeTruthy(); // or any other specific assertion based on the response
      });

      const req = httpMock.expectOne(`${baseUrl}/addItemToWishlist/${userId}/${productId}`);
      expect(req.request.method).toBe('PUT');
      req.flush({}); // Simulate empty response or a relevant mock response
    });

    it('should throw an error if user ID is not available', () => {
      componentDataServiceSpy.getUserId.and.returnValue(null);

      expect(() => service.addItemToWishlist(101).subscribe()).toThrowError('User ID is not available');
    });
  });

  describe('getWishlistByUserId', () => {
    it('should return the wishlist for a given user ID', () => {
      const userId = 1;
      const mockWishlist: Wishlist = {
        wishlistId: 1,
        productIds: [101, 102],
        userId
      };

      service.getWishlistByUserId(userId).subscribe(wishlist => {
        expect(wishlist).toEqual(mockWishlist);
      });

      const req = httpMock.expectOne(`${baseUrl}/${userId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockWishlist);
    });
  });

  describe('removeItemFromWishlist', () => {
    it('should remove an item from the wishlist and return the updated wishlist', () => {
      const userId = 1;
      const productId = 101;
      const updatedWishlist: Wishlist = {
        wishlistId: 1,
        productIds: [102], // assuming item with ID 101 was removed
        userId
      };

      service.removeItemFromWishlist(userId, productId).subscribe(wishlist => {
        expect(wishlist).toEqual(updatedWishlist);
      });

      const req = httpMock.expectOne(`${baseUrl}/removeItemFromWishlist/${userId}/${productId}`);
      expect(req.request.method).toBe('PUT');
      req.flush(updatedWishlist);
    });
  });
});

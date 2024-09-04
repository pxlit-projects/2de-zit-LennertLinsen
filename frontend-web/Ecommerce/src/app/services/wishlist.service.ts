import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ComponentDataService} from "./component-data.service";
import {Observable} from "rxjs";

export interface Wishlist {
  wishlistId: number;
  productIds: number[];
  userId: number;
}

@Injectable({
  providedIn: 'root'
})

export class WishlistService {
  private baseUrl = 'http://localhost:8089/wishlist/api/wishlist';

  constructor(private http: HttpClient, private dataService: ComponentDataService) {}

  addItemToWishlist(productId: number): Observable<any> {
    const userId = this.dataService.getUserId();
    if (!userId) {
      throw new Error('User ID is not available');
    }
    return this.http.put<any>(`${this.baseUrl}/addItemToWishlist/${userId}/${productId}`, {});
  }

  getWishlistByUserId(userId: number): Observable<Wishlist> {
    return this.http.get<Wishlist>(`${this.baseUrl}/${userId}`);
  }

  removeItemFromWishlist(userId: number, productId: number): Observable<Wishlist> {
    return this.http.put<Wishlist>(`${this.baseUrl}/removeItemFromWishlist/${userId}/${productId}`, {});
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


export interface ProductReview {
  id: number;
  title: string;
  description: string;
  stars: number;
  reviewTime: string; // Consider using a Date type if handling dates in Angular
  productId: number;
  supplier: string;
}


@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private apiUrl = 'http://localhost:8089/review/api/productreviews';

  constructor(private http: HttpClient) { }

  getReviewsByProductId(productId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${productId}`);
  }

  addReview(productId: number, review: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/add/${productId}`, review);
  }

  getReviewsBySupplier(supplier: string): Observable<ProductReview[]> {
    return this.http.get<ProductReview[]>(`${this.apiUrl}/bySupplier`, { params: { supplier } });
  }

}

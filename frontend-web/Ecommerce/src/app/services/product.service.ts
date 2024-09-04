import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProductResponse {
  id: number;
  productName: string;
  description: string;
  stock: number;
  price: number;
  categoryId: number;
  categoryName: string;
  supplier: string;
  wishlisted: number;
}

export interface ProductDTO{
  productName: string;
  description: string;
  categoryId: number;
  stock: number;
  price: number;
  supplier: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiUrl = 'http://localhost:8089/catalogue/api/products/';



  constructor(private http: HttpClient) { }

  getAllProducts(): Observable<ProductResponse[]> {
    return this.http.get<ProductResponse[]>(this.apiUrl);
  }

  getProductById(productId: number): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${this.apiUrl}${productId}`);
  }

  addProduct(product: FormData): Observable<ProductDTO> {
    return this.http.post<ProductDTO>(`${this.apiUrl}/add`, product);
  }

  deleteProduct(productId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}delete/${productId}`);
  }

  refillStock(productId: number, quantity: number): Observable<ProductResponse> {
    return this.http.post<ProductResponse>(`${this.apiUrl}refill/${productId}`, quantity);
  }

  updateProduct(productId: number, product: ProductDTO): Observable<ProductDTO> {
    return this.http.put<ProductDTO>(`${this.apiUrl}/update/${productId}`, product);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs'; // Adjust the path as needed

export interface CategoryResponse{
  categoryId: number;
  categoryName: string;
  categoryIds: number[];
}


@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiUrl = 'http://localhost:8089/catalogue/api/categories/'; // Adjust if needed

  constructor(private http: HttpClient) { }

  getCategories(): Observable<CategoryResponse[]> {
    return this.http.get<CategoryResponse[]>(this.apiUrl);
  }
}

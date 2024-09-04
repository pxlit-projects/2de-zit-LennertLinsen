import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ComponentDataService {
  private userId: number | null = null;

  setUserId(id: number): void {
    this.userId = id;
  }

  getUserId(): number | null {
    return this.userId;
  }
}

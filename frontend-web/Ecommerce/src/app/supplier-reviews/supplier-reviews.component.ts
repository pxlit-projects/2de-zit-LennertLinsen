import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ReviewService, ProductReview } from '../services/review.service';

@Component({
  selector: 'app-supplier-reviews',
  templateUrl: './supplier-reviews.component.html',
  styleUrls: ['./supplier-reviews.component.css']
})
export class SupplierReviewsComponent implements OnInit {
  supplier: string = '';
  reviews: ProductReview[] = [];
  loading: boolean = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private productReviewService: ReviewService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.supplier = params['supplier'];
      if (this.supplier) {
        this.fetchReviews(this.supplier);
      }
    });
  }

  fetchReviews(supplier: string): void {
    this.loading = true;
    this.productReviewService.getReviewsBySupplier(supplier).subscribe({
      next: (reviews) => {
        this.reviews = reviews;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load reviews';
        this.loading = false;
      }
    });
  }
}

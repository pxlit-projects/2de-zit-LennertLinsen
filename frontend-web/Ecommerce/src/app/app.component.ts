import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {filter} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'Ecommerce';
  isLoginPage: boolean = false;
  isRegisterPage: boolean = false;
  isAdminPage: boolean = false;
  isAddProductPage: boolean = false;
  isEditProductPage: boolean = false;

  constructor(private router: Router, private route: ActivatedRoute) {
    // Listen for navigation events
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      // Get the current route
      const currentRoute = this.router.url.split('?')[0]; // Remove query parameters if any

      // Set flags based on the route
      this.isLoginPage = currentRoute === '/';
      this.isRegisterPage = currentRoute === '/register';
      this.isAdminPage = currentRoute === '/admin';
      this.isAddProductPage = currentRoute === '/add-product';
      this.isEditProductPage = currentRoute.startsWith('/edit-product');
    });
  }

  ngOnInit(): void {}

  goToWishlist(): void {
    // Logic to navigate to the Wishlist page
    this.router.navigate(['/wishlist']);
  }

  goToCart(): void {
    this.router.navigate(['/cart']);
  }

  notificationMessage: string = '';
  showNotification: boolean = false;

  showCustomNotification(message: string) {
    this.notificationMessage = message;
    this.showNotification = true;
    setTimeout(() => this.showNotification = false, 3000); // Hide after 3 seconds
  }

  onNotificationClosed() {
    this.showNotification = false;
  }
}

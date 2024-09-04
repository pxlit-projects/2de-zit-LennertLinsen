import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { ProductCatalogComponent } from './product-catalog/product-catalog.component'; // Import the component
import {RouterModule} from "@angular/router";
import {routes} from "./app.routes";
import {ProductDetailsComponent} from "./product-details/product-details.component";
import {HttpClientModule} from "@angular/common/http";
import {WishlistComponent} from "./wishlist/wishlist.component";
import { LoginComponent } from './login/login.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RegisterComponent} from "./register/register.component";
import {CartComponent} from "./cart/cart.component";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {CustomNotificationComponent} from "./custom-notification/custom-notification.component";
import {AdminPageComponent} from "./admin-page/admin-page.component";
import {AddProductComponent} from "./add-product/add-product.component";
import {SupplierReviewsComponent} from "./supplier-reviews/supplier-reviews.component";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {EditProductComponent} from "./edit-product/edit-product.component";

@NgModule({
  declarations: [
    AppComponent,
    ProductCatalogComponent, // Declare the component here
    ProductDetailsComponent,
    WishlistComponent,
    LoginComponent,
    RegisterComponent,
    CartComponent,
    CustomNotificationComponent,
    AdminPageComponent,
    AddProductComponent,
    SupplierReviewsComponent,
    EditProductComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes),
    HttpClientModule,
    FormsModule,
    MatSnackBarModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

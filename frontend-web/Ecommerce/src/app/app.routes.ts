import { Routes } from '@angular/router';
import {ProductCatalogComponent} from "./product-catalog/product-catalog.component";
import {ProductDetailsComponent} from "./product-details/product-details.component";
import {WishlistComponent} from "./wishlist/wishlist.component";
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {CartComponent} from "./cart/cart.component";
import {AdminPageComponent} from "./admin-page/admin-page.component";
import {AddProductComponent} from "./add-product/add-product.component";
import {SupplierReviewsComponent} from "./supplier-reviews/supplier-reviews.component";
import {EditProductComponent} from "./edit-product/edit-product.component";

export const routes: Routes = [
  {path:'products', component: ProductCatalogComponent },
  {path: 'products/:id', component: ProductDetailsComponent },
  { path: 'wishlist', component: WishlistComponent },
  { path: '', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'cart', component: CartComponent },
  { path: 'admin', component: AdminPageComponent },
  { path: 'add-product', component: AddProductComponent },
  { path: 'supplier-reviews', component: SupplierReviewsComponent },
  { path: 'edit-product/:id', component: EditProductComponent }
];

import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../services/login.service';
import {ComponentDataService} from "../services/component-data.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private loginService: LoginService, private router: Router, private dataService: ComponentDataService) {}

  onSubmit() {
    this.loginService.login(this.email, this.password).subscribe(
      (user) => {
        if (user.type === 'user') {
          this.dataService.setUserId(user.id);
          this.router.navigate(['/products']);
        } else if (user.type === 'admin') {
          // Admin page component to be created later
          this.router.navigate(['/admin']);
        }
      },
      (error) => {
        this.errorMessage = 'Invalid email or password.';
      }
    );
  }

  goToRegister(): void {
    this.router.navigate(['/register']); // Navigate to the Register component
  }
}

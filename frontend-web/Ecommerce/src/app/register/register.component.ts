import { Component } from '@angular/core';
import { LoginService } from '../services/login.service'; // Import LoginService
import { Router } from '@angular/router'; // Import Router

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  email: string = '';
  password: string = '';
  message: string = '';

  constructor(private loginService: LoginService, private router: Router) {} // Inject LoginService and Router

  // Method to create a new user
  onSubmit(): void {
    this.loginService.createUser(this.email, this.password).subscribe({
      next: () => {
        this.message = 'User created successfully!';
        this.clearForm();
      },
      error: (err) => {
        this.message = 'Failed to create user. Please try again.';
        console.error('Error creating user:', err);
      }
    });
  }

  // Method to navigate back to the login page
  goBackToLogin(): void {
    this.router.navigate(['']);
  }

  // Method to clear the form after submission
  clearForm(): void {
    this.email = '';
    this.password = '';
  }
}

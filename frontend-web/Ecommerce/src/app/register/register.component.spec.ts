import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { LoginService } from '../services/login.service'; // Import LoginService
import { Router } from '@angular/router'; // Import Router
import { of, throwError } from 'rxjs'; // Import RxJS for mock Observables
import { FormsModule } from '@angular/forms'; // Import FormsModule to use ngModel

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let mockLoginService: jasmine.SpyObj<LoginService>;
  let mockRouter: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    // Create spies for LoginService and Router
    mockLoginService = jasmine.createSpyObj<LoginService>('LoginService', ['createUser']);
    mockRouter = jasmine.createSpyObj<Router>('Router', ['navigate']);

    // Configure testing module
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [FormsModule], // Import FormsModule to use ngModel
      providers: [
        { provide: LoginService, useValue: mockLoginService }, // Provide the mock LoginService
        { provide: Router, useValue: mockRouter } // Provide the mock Router
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent); // Create component fixture
    component = fixture.componentInstance; // Get component instance
    fixture.detectChanges(); // Detect changes to apply template bindings
  });

  it('should create the component', () => {
    expect(component).toBeTruthy(); // Test if the component is created successfully
  });

  it('should call createUser and display success message on successful user creation', () => {
    mockLoginService.createUser.and.returnValue(of(undefined)); // Correctly return Observable<void>

    component.email = 'test@example.com'; // Set email input
    component.password = 'password123'; // Set password input
    component.onSubmit(); // Call onSubmit to trigger user creation

    expect(mockLoginService.createUser).toHaveBeenCalledWith('test@example.com', 'password123'); // Verify createUser is called with correct parameters
    expect(component.message).toBe('User created successfully!'); // Verify success message is set
    expect(component.email).toBe(''); // Verify email is cleared
    expect(component.password).toBe(''); // Verify password is cleared
  });

  it('should call createUser and display error message on failed user creation', () => {
    mockLoginService.createUser.and.returnValue(throwError(() => new Error('Error creating user'))); // Correctly return an error

    component.email = 'test@example.com'; // Set email input
    component.password = 'password123'; // Set password input
    component.onSubmit(); // Call onSubmit to trigger user creation

    expect(mockLoginService.createUser).toHaveBeenCalledWith('test@example.com', 'password123'); // Verify createUser is called with correct parameters
    expect(component.message).toBe('Failed to create user. Please try again.'); // Verify error message is set
  });

  it('should navigate to login page when goBackToLogin is called', () => {
    component.goBackToLogin(); // Call goBackToLogin

    expect(mockRouter.navigate).toHaveBeenCalledWith(['']); // Verify router navigation to login page
  });

  it('should clear form fields when clearForm is called', () => {
    component.email = 'test@example.com'; // Set email input
    component.password = 'password123'; // Set password input
    component.clearForm(); // Call clearForm

    expect(component.email).toBe(''); // Verify email is cleared
    expect(component.password).toBe(''); // Verify password is cleared
  });
});

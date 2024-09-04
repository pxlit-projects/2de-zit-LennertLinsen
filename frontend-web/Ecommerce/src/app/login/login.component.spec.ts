import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError, Observable } from 'rxjs';
import { LoginComponent } from './login.component';
import { LoginService, User } from '../services/login.service';  // Import User from LoginService
import { ComponentDataService } from '../services/component-data.service';
import { FormsModule } from '@angular/forms'; // Import FormsModule for ngModel

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockLoginService: jasmine.SpyObj<LoginService>;
  let mockRouter: jasmine.SpyObj<Router>;
  let mockDataService: jasmine.SpyObj<ComponentDataService>;

  beforeEach(async () => {
    mockLoginService = jasmine.createSpyObj<LoginService>('LoginService', ['login']);
    mockRouter = jasmine.createSpyObj<Router>('Router', ['navigate']);
    mockDataService = jasmine.createSpyObj<ComponentDataService>('ComponentDataService', ['setUserId']);

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [FormsModule], // Include FormsModule to support ngModel in the template
      providers: [
        { provide: LoginService, useValue: mockLoginService },
        { provide: Router, useValue: mockRouter },
        { provide: ComponentDataService, useValue: mockDataService }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to /products when login is successful for a user type', () => {
    const mockUser: User = { id: 123, email: 'user@example.com', password: 'password', type: 'user' };  // Adjust mockUser type
    mockLoginService.login.and.returnValue(of(mockUser));  // Correctly return Observable<User>

    component.onSubmit();

    expect(mockDataService.setUserId).toHaveBeenCalledWith(123);  // ID should match type number
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/products']);
  });

  it('should navigate to /admin when login is successful for an admin type', () => {
    const mockUser: User = { id: 456, email: 'admin@example.com', password: 'password', type: 'admin' };  // Adjust mockUser type
    mockLoginService.login.and.returnValue(of(mockUser));  // Correctly return Observable<User>

    component.onSubmit();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/admin']);
  });

  it('should display an error message when login fails', () => {
    mockLoginService.login.and.returnValue(throwError(() => new Error('Login failed')));  // Correctly return an error

    component.onSubmit();

    expect(component.errorMessage).toBe('Invalid email or password.');
  });

  it('should navigate to /register when goToRegister is called', () => {
    component.goToRegister();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/register']);
  });
});

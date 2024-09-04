import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoginService, User } from './login.service'; // Adjust the path as needed

describe('LoginService', () => {
  let service: LoginService;
  let httpMock: HttpTestingController;
  const baseUrl = 'http://localhost:8089/users/api/users';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [LoginService]
    });

    service = TestBed.inject(LoginService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Ensure there are no outstanding requests
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('login', () => {
    it('should successfully login a user and return user data', () => {
      const mockUser: User = {
        id: 1,
        email: 'test@example.com',
        password: 'password123',
        type: 'user'
      };

      service.login('test@example.com', 'password123').subscribe(user => {
        expect(user).toEqual(mockUser);
      });

      const req = httpMock.expectOne(`${baseUrl}/byCredentials`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({ email: 'test@example.com', password: 'password123' });
      req.flush(mockUser);
    });

    it('should handle login errors', () => {
      const errorMessage = 'Login failed';

      service.login('test@example.com', 'wrongpassword').subscribe(
        () => fail('should have failed with a 401 error'),
        error => {
          expect(error.error).toContain(errorMessage);
        }
      );

      const req = httpMock.expectOne(`${baseUrl}/byCredentials`);
      expect(req.request.method).toBe('POST');
      req.flush(errorMessage, { status: 401, statusText: 'Unauthorized' });
    });
  });

  describe('createUser', () => {

    it('should handle user creation errors', () => {
      const errorMessage = 'User creation failed';

      service.createUser('newuser@example.com', 'password123').subscribe(
        () => fail('should have failed with a 500 error'),
        error => {
          expect(error.error).toContain(errorMessage);
        }
      );

      const req = httpMock.expectOne(`${baseUrl}/createUser`);
      expect(req.request.method).toBe('POST');
      req.flush(errorMessage, { status: 500, statusText: 'Server Error' });
    });
  });
});

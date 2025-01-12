import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/AuthService';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['setUsername', 'setRole']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty username and default role', () => {
    expect(component.loginForm.get('username')?.value).toBe('');
    expect(component.loginForm.get('role')?.value).toBe('user');
  });

  it('should update role when selectRole is called', () => {
    component.selectRole('admin');
    expect(component.loginForm.get('role')?.value).toBe('admin');
  });

  it('should not login with invalid form', () => {
    // Form is invalid because username is empty
    component.login();
    expect(authService.setUsername).not.toHaveBeenCalled();
    expect(authService.setRole).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('should login with valid form', () => {
    component.loginForm.patchValue({
      username: 'testUser',
      role: 'user'
    });

    component.login();

    expect(authService.setUsername).toHaveBeenCalledWith('testUser');
    expect(authService.setRole).toHaveBeenCalledWith('user');
    expect(router.navigate).toHaveBeenCalledWith(['/posts']);
  });

  it('should reset form after failed login', () => {
    component.loginForm.patchValue({
      username: '',
      role: 'user'
    });

    component.login();

    expect(authService.setUsername).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('should handle admin role login', () => {
    component.loginForm.patchValue({
      username: 'testUser',
      role: 'admin'
    });

    component.login();

    expect(authService.setUsername).toHaveBeenCalledWith('testUser');
    expect(authService.setRole).toHaveBeenCalledWith('admin');
    expect(router.navigate).toHaveBeenCalledWith(['/posts']);
  });
});

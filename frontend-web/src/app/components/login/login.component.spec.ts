import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/AuthService';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockAuthService: jasmine.SpyObj<AuthService>;
  let mockRouter: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    mockAuthService = jasmine.createSpyObj('AuthService', ['setUsername', 'setRole']);
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [LoginComponent, FormsModule],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.username).toBe('');
    expect(component.role).toBe('user');
  });

  it('should update role when selectRole is called', () => {
    component.selectRole('admin');
    expect(component.role).toBe('admin');
  });

  describe('login', () => {
    it('should login successfully with valid username and role', () => {
      component.username = 'testUser';
      component.role = 'user';
      
      component.login();

      expect(mockAuthService.setUsername).toHaveBeenCalledWith('testUser');
      expect(mockAuthService.setRole).toHaveBeenCalledWith('user');
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/posts']);
    });

    it('should not login with empty username', () => {
      spyOn(window, 'alert');
      component.username = '';
      component.role = 'user';
      
      component.login();

      expect(mockAuthService.setUsername).not.toHaveBeenCalled();
      expect(mockAuthService.setRole).not.toHaveBeenCalled();
      expect(mockRouter.navigate).not.toHaveBeenCalled();
      expect(window.alert).toHaveBeenCalledWith('Please enter a username and select a role!');
    });

    it('should not login with empty role', () => {
      spyOn(window, 'alert');
      component.username = 'testUser';
      component.role = '';
      
      component.login();

      expect(mockAuthService.setUsername).not.toHaveBeenCalled();
      expect(mockAuthService.setRole).not.toHaveBeenCalled();
      expect(mockRouter.navigate).not.toHaveBeenCalled();
      expect(window.alert).toHaveBeenCalledWith('Please enter a username and select a role!');
    });
  });
});

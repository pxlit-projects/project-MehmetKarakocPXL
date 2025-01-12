import { Component } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/AuthService';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule],
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(private authService: AuthService, private router: Router) {
    // Initialize the reactive form
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      role: new FormControl('user', [Validators.required]), // Default role is 'user'
    });
  }

  get username() {
    return this.loginForm.get('username');
  }

  get role() {
    return this.loginForm.get('role');
  }

  selectRole(role: string): void {
    this.role?.setValue(role); // Update the role control value
  }

  // Method to handle login
  login(): void {
    if (this.loginForm.valid) {
      const { username, role } = this.loginForm.value;
      this.authService.setUsername(username);
      this.authService.setRole(role);

      this.router.navigate(['/posts']);
    } else {
      alert('Please enter a username and select a role!');
    }
  }
}

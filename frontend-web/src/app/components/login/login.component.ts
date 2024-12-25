import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/AuthService';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [FormsModule],
})

export class LoginComponent {
    username = '';
    role = 'user'; // Default role
  
    constructor(private authService: AuthService, private router: Router) {}

    selectRole(role: string): void {
        this.role = role;
      }
  
      login(): void {
        if (this.username && this.role) {
          this.authService.setUsername(this.username);
          this.authService.setRole(this.role);
    
          this.router.navigate(['/posts']);
        } else {
          alert('Please enter a username and select a role!');
        }
      }
  }
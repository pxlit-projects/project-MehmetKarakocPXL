import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from './AuthService'; // Je authenticatieservice

@Injectable({
  providedIn: 'root',
})

export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    // Check if the user is logged in
    if (!this.authService.isLoggedIn()) {
      alert('You need to login first');
      this.router.navigate(['']);
      return false;
    }

    // Additional check for the "comments" route
    if (route.routeConfig?.path === 'comments/:postId' || route.routeConfig?.path === 'comments') {
      const userRole = this.authService.getRole();
      if (userRole !== 'user') {
        alert('Only users can access this page');
        this.router.navigate(['/posts']); // Redirect unauthorized users
        return false;
      }
    }

    return true; // Grant access
  }
}

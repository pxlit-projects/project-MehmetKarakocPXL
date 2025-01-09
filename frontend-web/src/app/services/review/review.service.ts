import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Review } from '../../models/review.model';
import { AuthService } from '../../services/AuthService';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  private baseUrl = 'http://127.0.0.1:8084/review/api/review'; 

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const role = this.authService.getRole() || '';
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Role': role,
    });
  }

  addReview(review: Partial<Review>): Observable<void> {
    const headers = this.getHeaders();
    return this.http.post<void>(this.baseUrl, review, {headers});
  }

  getReviews(): Observable<Review[]> {
    const headers = this.getHeaders();
    return this.http.get<Review[]>(`${this.baseUrl}`, {headers});
  }
}
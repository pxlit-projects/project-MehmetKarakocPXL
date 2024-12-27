import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Review } from '../../models/review.model';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  private baseUrl = 'http://127.0.0.1:8083/api/review';

  constructor(private http: HttpClient) {}

  addReview(review: Partial<Review>): Observable<void> {
    return this.http.post<void>(this.baseUrl, review);
  }

  getReviews(): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.baseUrl}`);
  }
}
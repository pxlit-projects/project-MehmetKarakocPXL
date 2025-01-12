import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../../models/comment.model';
import { AuthService } from '../../services/AuthService';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})

export class CommentService {
  private baseUrl = environment.apiUrlComment;

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const role = this.authService.getRole() || '';
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Role': role,
    });
  }
  

  getComments(postId: number): Observable<Comment[]> {
    const headers = this.getHeaders();
    return this.http.get<Comment[]>(`${this.baseUrl}/${postId}`, {headers});
  }

  addComment(postId: number, comment: Partial<Comment>): Observable<void> {
    const headers = this.getHeaders();
    return this.http.post<void>(`${this.baseUrl}/${postId}`, comment, {headers});
  }

  updateComment(commentId: number, comment: Partial<Comment>): Observable<void> {
    const headers = this.getHeaders();
    return this.http.put<void>(`${this.baseUrl}/${commentId}`, comment, {headers});
  }

  deleteComment(commentId: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.delete<void>(`${this.baseUrl}/${commentId}`, {headers});
  }
}
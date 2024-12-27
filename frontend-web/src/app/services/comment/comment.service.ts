import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../../models/comment.model';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  private baseUrl = 'http://127.0.0.1:8081/api/comment';

  constructor(private http: HttpClient) {}

  getComments(postId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.baseUrl}/${postId}`);
  }

  addComment(postId: number, comment: Partial<Comment>): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${postId}`, comment);
  }

  updateComment(commentId: number, comment: Partial<Comment>): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${commentId}`, comment);
  }

  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${commentId}`);
  }
}
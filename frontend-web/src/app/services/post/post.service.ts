import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../../models/post.model';
import { Notification } from '../../models/notification.model';
import { AuthService } from '../../services/AuthService';

@Injectable({
  providedIn: 'root',
})

export class PostService {
  private baseUrl = 'http://127.0.0.1:8084/post/api/post'; 

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    const role = this.authService.getRole() || '';
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Role': role,
    });
  }
  getAllPostsByAuthor(author:string): Observable<Post[]> {
    const headers = this.getHeaders();
    return this.http.get<Post[]>(`${this.baseUrl}/author/${author}`, {headers});
  }

  addPost(post: Partial<Post>): Observable<void> {
    const headers = this.getHeaders();
    return this.http.post<void>(this.baseUrl, post, {headers});
  }
  
  getPostById(postId: number): Observable<Post> {
    const headers = this.getHeaders();
    return this.http.get<Post>(`${this.baseUrl}/${postId}`, {headers});
  }

  updatePost(postId: number, post: Partial<Post>): Observable<void> {
    const headers = this.getHeaders();
    return this.http.patch<void>(`${this.baseUrl}/${postId}`, post, {headers});
  }

  getPostsSorted(sortBy: string): Observable<Post[]> {
    const headers = this.getHeaders();
    return this.http.get<Post[]>(`${this.baseUrl}/sort?sortBy=${sortBy}`, {headers});
  }
  
  getFilteredPosts(filters: any): Observable<any[]> {
    const headers = this.getHeaders();
    console.log(headers);
    return this.http.get<Post[]>(`${this.baseUrl}/filtered`, { params: filters, headers });
  }

  getNotificationsByReceiver(receiver: string): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<Notification[]>(`${this.baseUrl}/notification/${receiver}`, {headers});
  }
}
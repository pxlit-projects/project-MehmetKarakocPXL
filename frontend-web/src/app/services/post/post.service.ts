import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../../models/post.model';
import { Notification } from '../../models/notification.model';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private baseUrl = 'http://127.0.0.1:8082/api/post'; 

  constructor(private http: HttpClient) {}

  getAllPostsByAuthor(author:string): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.baseUrl}/author/${author}`);
  }

  addPost(post: Partial<Post>): Observable<void> {
    return this.http.post<void>(this.baseUrl, post);
  }
  
  getPostById(postId: number): Observable<Post> {
    return this.http.get<Post>(`${this.baseUrl}/${postId}`);
  }

  updatePost(postId: number, post: Partial<Post>): Observable<void> {
    return this.http.patch<void>(`${this.baseUrl}/${postId}`, post);
  }

  getPostsSorted(sortBy: string): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.baseUrl}/sort?sortBy=${sortBy}`);
  }
  
  getFilteredPosts(filters: any): Observable<any[]> {
    return this.http.get<Post[]>(`${this.baseUrl}/filtered`, { params: filters });
  }

  getNotificationsByAuthor(author: string): Observable<any[]> {
    return this.http.get<Notification[]>(`${this.baseUrl}/notifications/${author}`);
  }
}
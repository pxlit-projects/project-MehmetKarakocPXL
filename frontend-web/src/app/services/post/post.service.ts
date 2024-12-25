import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../../models/post.model';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private baseUrl = 'http://localhost:8082/api/post'; // Replace with your backend URL

  constructor(private http: HttpClient) {}

  getPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(this.baseUrl);
  }

  getAllPublishedPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(this.baseUrl);
  }
  
  getAllToBeReviewedPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(this.baseUrl);
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
  
  // Fetch posts with filters
  getFilteredPosts(filters: { author: string; content: string; date: string | null}): Observable<any[]> {
    let params = new HttpParams();
    if (filters.author) params = params.set('author', filters.author);
    if (filters.content) params = params.set('content', filters.content);
    if (filters.date) params = params.set('date', filters.date);

    return this.http.get<any[]>(`${this.baseUrl}/filter`, { params });
  }
}
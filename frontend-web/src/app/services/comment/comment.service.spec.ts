import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CommentService } from './comment.service';
import { Comment } from '../../models/comment.model';
import { AuthService } from '../../services/AuthService';

describe('CommentService', () => {
  let service: CommentService;
  let httpMock: HttpTestingController;
  let authService: jasmine.SpyObj<AuthService>;
  const mockBaseUrl = 'http://127.0.0.1:8084/comment/api/comment';

  beforeEach(() => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['getRole']);
    authServiceSpy.getRole.and.returnValue('user');

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        CommentService,
        { provide: AuthService, useValue: authServiceSpy }
      ]
    });

    service = TestBed.inject(CommentService);
    httpMock = TestBed.inject(HttpTestingController);
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get comments for a post with correct headers', (done: DoneFn) => {
    const mockPostId = 1;
    const mockComments: Comment[] = [
      { id: 1, postId: mockPostId, content: 'First comment', author: 'John Doe', createdDate: '2024-01-09T10:30:00.000Z' },
      { id: 2, postId: mockPostId, content: 'Second comment', author: 'Jane Doe', createdDate: '2024-01-09T11:30:00.000Z' }
    ];

    service.getComments(mockPostId).subscribe({
      next: (comments) => {
        expect(comments).toEqual(mockComments);
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(`${mockBaseUrl}/${mockPostId}`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Role')).toBe('user');
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    req.flush(mockComments);
  });

  it('should add comment with correct headers and body', (done: DoneFn) => {
    const mockPostId = 1;
    const mockComment: Partial<Comment> = {
      content: 'New comment',
      author: 'John Doe'
    };

    service.addComment(mockPostId, mockComment).subscribe({
      next: () => {
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(`${mockBaseUrl}/${mockPostId}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Role')).toBe('user');
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    expect(req.request.body).toEqual(mockComment);
    req.flush(null);
  });

  it('should update comment with correct headers and body', (done: DoneFn) => {
    const mockCommentId = 1;
    const mockComment: Partial<Comment> = {
      content: 'Updated comment',
      author: 'John Doe'
    };

    service.updateComment(mockCommentId, mockComment).subscribe({
      next: () => {
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(`${mockBaseUrl}/${mockCommentId}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.headers.get('Role')).toBe('user');
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    expect(req.request.body).toEqual(mockComment);
    req.flush(null);
  });

  it('should delete comment with correct headers', (done: DoneFn) => {
    const mockCommentId = 1;

    service.deleteComment(mockCommentId).subscribe({
      next: () => {
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(`${mockBaseUrl}/${mockCommentId}`);
    expect(req.request.method).toBe('DELETE');
    expect(req.request.headers.get('Role')).toBe('user');
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    req.flush(null);
  });

  it('should handle empty role from AuthService', (done: DoneFn) => {
    authService.getRole.and.returnValue('');
    const mockPostId = 1;
    
    service.getComments(mockPostId).subscribe({
      next: () => {
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(`${mockBaseUrl}/${mockPostId}`);
    expect(req.request.headers.get('Role')).toBe('');
    req.flush([]);
  });
});

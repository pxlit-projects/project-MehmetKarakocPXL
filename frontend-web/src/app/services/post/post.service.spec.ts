import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PostService } from './post.service';
import { Post, PostStatus } from '../../models/post.model';
import { Notification } from '../../models/notification.model';
import { AuthService } from '../../services/AuthService';

describe('PostService', () => {
  let service: PostService;
  let httpMock: HttpTestingController;
  let authService: jasmine.SpyObj<AuthService>;
  const mockBaseUrl = 'http://127.0.0.1:8084/post/api/post';

  beforeEach(() => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['getRole']);
    authServiceSpy.getRole.and.returnValue('user');

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        PostService,
        { provide: AuthService, useValue: authServiceSpy }
      ]
    });

    service = TestBed.inject(PostService);
    httpMock = TestBed.inject(HttpTestingController);
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all posts by author with correct headers', (done: DoneFn) => {
    const mockAuthor = 'John Doe';
    const mockPosts: Post[] = [
      { id: 1, title: 'Post 1', content: 'Content 1', author: mockAuthor, createdDate: '2024-01-09T10:30:00.000Z', isConcept: false, status: PostStatus.PENDING },
      { id: 2, title: 'Post 2', content: 'Content 2', author: mockAuthor, createdDate: '2024-01-09T11:30:00.000Z', isConcept: false, status: PostStatus.PENDING }
    ];

    service.getAllPostsByAuthor(mockAuthor).subscribe({
      next: (posts) => {
        expect(posts).toEqual(mockPosts);
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(`${mockBaseUrl}/author/${mockAuthor}`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Role')).toBe('user');
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    req.flush(mockPosts);
  });

  it('should add post with correct headers and body', (done: DoneFn) => {
    const mockPost: Partial<Post> = {
      title: 'New Post',
      content: 'New Content',
      author: 'John Doe',
      isConcept: false,
      status: PostStatus.PENDING
    };

    service.addPost(mockPost).subscribe({
      next: () => {
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(mockBaseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Role')).toBe('user');
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    expect(req.request.body).toEqual(mockPost);
    req.flush(null);
  });

  it('should get post by id with correct headers', (done: DoneFn) => {
    const mockPost: Post = {
      id: 1,
      title: 'Post 1',
      content: 'Content 1',
      author: 'John Doe',
      createdDate: '2024-01-09T10:30:00.000Z',
      isConcept: false,
      status: PostStatus.PENDING
    };

    service.getPostById(1).subscribe({
      next: (post) => {
        expect(post).toEqual(mockPost);
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(`${mockBaseUrl}/1`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Role')).toBe('user');
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    req.flush(mockPost);
  });

  it('should update post with correct headers and body', (done: DoneFn) => {
    const mockPost: Partial<Post> = {
      title: 'Updated Post',
      content: 'Updated Content',
      isConcept: false,
      status: PostStatus.PENDING
    };

    service.updatePost(1, mockPost).subscribe({
      next: () => {
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(`${mockBaseUrl}/1`);
    expect(req.request.method).toBe('PATCH');
    expect(req.request.headers.get('Role')).toBe('user');
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    expect(req.request.body).toEqual(mockPost);
    req.flush(null);
  });

  it('should get posts sorted with correct headers', (done: DoneFn) => {
    const mockPosts: Post[] = [
      { id: 1, title: 'Post 1', content: 'Content 1', author: 'John Doe', createdDate: '2024-01-09T10:30:00.000Z', isConcept: false, status: PostStatus.PENDING },
      { id: 2, title: 'Post 2', content: 'Content 2', author: 'Jane Doe', createdDate: '2024-01-09T11:30:00.000Z', isConcept: false, status: PostStatus.PENDING }
    ];

    service.getPostsSorted('createdDate').subscribe({
      next: (posts) => {
        expect(posts).toEqual(mockPosts);
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(`${mockBaseUrl}/sort?sortBy=createdDate`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Role')).toBe('user');
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    req.flush(mockPosts);
  });

  it('should get filtered posts with correct headers and parameters', (done: DoneFn) => {
    const mockPosts: Post[] = [
      { id: 1, title: 'Post 1', content: 'Content 1', author: 'John Doe', createdDate: '2024-01-09T10:30:00.000Z', isConcept: false, status: PostStatus.PENDING }
    ];
    const filters = { author: 'John Doe', title: 'Post 1' };

    service.getFilteredPosts(filters).subscribe({
      next: (posts) => {
        expect(posts).toEqual(mockPosts);
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne((req) => req.url === `${mockBaseUrl}/filtered`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Role')).toBe('user');
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    expect(req.request.params.get('author')).toBe('John Doe');
    expect(req.request.params.get('title')).toBe('Post 1');
    req.flush(mockPosts);
  });

  it('should get notifications by receiver with correct headers', (done: DoneFn) => {
    const mockReceiver = 'John Doe';
    const mockNotifications: Notification[] = [
      { id: 1, message: 'New notification', receiver: mockReceiver, author: 'Admin' }
    ];

    service.getNotificationsByReceiver(mockReceiver).subscribe({
      next: (notifications) => {
        expect(notifications).toEqual(mockNotifications);
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(`${mockBaseUrl}/notification/${mockReceiver}`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Role')).toBe('user');
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    req.flush(mockNotifications);
  });

  it('should handle empty role from AuthService', (done: DoneFn) => {
    authService.getRole.and.returnValue('');
    
    service.getAllPostsByAuthor('John Doe').subscribe({
      next: () => {
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(`${mockBaseUrl}/author/John Doe`);
    expect(req.request.headers.get('Role')).toBe('');
    req.flush([]);
  });
});

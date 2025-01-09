import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ReviewService } from './review.service';
import { Review } from '../../models/review.model';
import { AuthService } from '../../services/AuthService';

describe('ReviewService', () => {
  let service: ReviewService;
  let httpMock: HttpTestingController;
  let authService: jasmine.SpyObj<AuthService>;
  const mockBaseUrl = 'http://127.0.0.1:8084/review/api/review';

  beforeEach(() => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['getRole']);
    authServiceSpy.getRole.and.returnValue('user');

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ReviewService,
        { provide: AuthService, useValue: authServiceSpy }
      ]
    });

    service = TestBed.inject(ReviewService);
    httpMock = TestBed.inject(HttpTestingController);
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get reviews with correct headers', (done: DoneFn) => {
    const mockReviews: Review[] = [
      { id: 1, postId: 1, content: 'Excellent post!', author: 'Jane Doe', isApproved: true, createdDate: '2025-01-09T10:30:00.000Z' },
      { id: 2, postId: 2, content: 'Good post!', author: 'John Smith', isApproved: false, createdDate: '2025-01-09T10:30:00.000Z' }
    ];

    service.getReviews().subscribe({
      next: (reviews) => {
        expect(reviews).toEqual(mockReviews);
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(mockBaseUrl);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Role')).toBe('user');
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    req.flush(mockReviews);
  });

  it('should add review with correct headers and body', (done: DoneFn) => {
    const mockReview: Partial<Review> = {
      postId: 1,
      content: 'Great post!',
      author: 'Jane Doe'
    };

    service.addReview(mockReview).subscribe({
      next: () => {
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(mockBaseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Role')).toBe('user');
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    expect(req.request.body).toEqual(mockReview);
    req.flush(null);
  });

  it('should handle empty role from AuthService', (done: DoneFn) => {
    authService.getRole.and.returnValue('');

    service.getReviews().subscribe({
      next: () => {
        expect(authService.getRole).toHaveBeenCalled();
        done();
      }
    });

    const req = httpMock.expectOne(mockBaseUrl);
    expect(req.request.headers.get('Role')).toBe('');
    req.flush([]);
  });
});

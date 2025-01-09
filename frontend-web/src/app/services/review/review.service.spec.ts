// import { TestBed, inject } from '@angular/core/testing';
// import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
// import { ReviewService } from '../review/review.service';
// import { Review } from '../../models/review.model';

// describe('ReviewService', () => {
//   let service: ReviewService;
//   let httpMock: HttpTestingController;
//   const mockBaseUrl = 'http://127.0.0.1:8084/review/api/review';

//   beforeEach(() => {
//     TestBed.configureTestingModule({
//       imports: [provideHttpClientTesting],
//       providers: [ReviewService],
//     });

//     service = TestBed.inject(ReviewService);
//     httpMock = TestBed.inject(HttpTestingController);
//   });

//   afterEach(() => {
//     httpMock.verify();
//   });

//   it('should be created', () => {
//     expect(service).toBeTruthy();
//   });

//   it('should get products', (done: DoneFn) => {
//     // Arrange
//     const mockReviews: Review[] = [
//         { id: 1, postId: 1, content: 'Excellent post!', author: 'Jane Doe', isApproved: true, createdDate: '2025-01-09T10:30:00.000Z' },
//         { id: 2, postId: 2, content: 'Good post!', author: 'John Smith', isApproved: false, createdDate: '2025-01-09T10:30:00.000Z' },
//       ];

//     // Act
//     service.getReviews().subscribe((reviews) => {
//       // Assert
//       expect(reviews).toEqual(mockReviews);
//       done();
//     });

//     // Assert
//     const req = httpMock.expectOne(mockBaseUrl);
//     expect(req.request.method).toBe('GET');
//     expect(req.request.headers.get('Role')).toBe('user');
//     req.flush(mockReviews);
//   });

// });

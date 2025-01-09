// import { ComponentFixture, TestBed } from '@angular/core/testing';
// import { NotificationsComponent } from './notifications.component';
// import { PostService } from '../../services/post/post.service';
// import { AuthService } from '../../services/AuthService';
// import { of } from 'rxjs';
// import { Notification } from '../../models/notification.model';
// import { CommonModule } from '@angular/common';
// import { BrowserModule } from '@angular/platform-browser';
// import { provideAnimations } from '@angular/platform-browser/animations';

// describe('NotificationsComponent', () => {
//   let component: NotificationsComponent;
//   let fixture: ComponentFixture<NotificationsComponent>;
//   let mockPostService: jasmine.SpyObj<PostService>;
//   let mockAuthService: jasmine.SpyObj<AuthService>;

//   const mockNotifications: Notification[] = [
//     {
//       id: 1,
//       message: 'Test notification 1',
//       author: 'sender1',
//       receiver: 'testUser'
//     },
//     {
//       id: 2,
//       message: 'Test notification 2',
//       author: 'sender2',
//       receiver: 'testUser'
//     }
//   ];

//   beforeEach(async () => {
//     mockPostService = jasmine.createSpyObj('PostService', ['getNotificationsByReceiver']);
//     mockAuthService = jasmine.createSpyObj('AuthService', ['getUsername']);

//     await TestBed.configureTestingModule({
//       declarations: [],
//       imports: [
//         BrowserModule,
//         CommonModule,
//         NotificationsComponent
//       ],
//       providers: [
//         provideAnimations(),
//         { provide: PostService, useValue: mockPostService },
//         { provide: AuthService, useValue: mockAuthService }
//       ]
//     }).compileComponents();

//     mockAuthService.getUsername.and.returnValue(null);
//     mockPostService.getNotificationsByReceiver.and.returnValue(of([]));

//     fixture = TestBed.createComponent(NotificationsComponent);
//     component = fixture.componentInstance;
//   });

//   it('should create', () => {
//     fixture.detectChanges();
//     expect(component).toBeTruthy();
//   });

//   it('should initialize with default values', () => {
//     expect(component.isNotificationsModalOpen).toBeFalse();
//     expect(component.notifications).toEqual([]);
//     expect(component.loggedInAuthor).toBe('');
//   });

//   describe('after initialization', () => {
//     beforeEach(() => {
//       mockAuthService.getUsername.and.returnValue('testUser');
//       mockPostService.getNotificationsByReceiver.and.returnValue(of(mockNotifications));
//       component.ngOnInit();
//       fixture.detectChanges();
//     });

//     it('should load user and notifications on init', () => {
//       expect(mockAuthService.getUsername).toHaveBeenCalled();
//       expect(component.loggedInAuthor).toBe('testUser');
//       expect(mockPostService.getNotificationsByReceiver).toHaveBeenCalledWith('testUser');
//       expect(component.notifications).toEqual(mockNotifications);
//     });

//     it('should load notifications for logged in user', () => {
//       component.loggedInAuthor = 'testUser';
//       component.loadNotifications();

//       expect(mockPostService.getNotificationsByReceiver).toHaveBeenCalledWith('testUser');
//       expect(component.notifications).toEqual(mockNotifications);
//     });

//     it('should toggle notifications modal', () => {
//       expect(component.isNotificationsModalOpen).toBeFalse();
      
//       component.toggleNotificationsModal();
//       expect(component.isNotificationsModalOpen).toBeTrue();
      
//       component.toggleNotificationsModal();
//       expect(component.isNotificationsModalOpen).toBeFalse();
//     });
//   });
// });

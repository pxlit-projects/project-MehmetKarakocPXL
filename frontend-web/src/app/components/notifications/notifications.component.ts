import { Component } from '@angular/core';
import { PostService } from '../../services/post/post.service';
import { AuthService } from '../../services/AuthService';
import { Notification } from '../../models/notification.model';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})
export class NotificationsComponent {
  isNotificationsModalOpen = false;
  notifications: Notification[] = [];
  loggedInAuthor: string = '';

  constructor(private postService: PostService, private authService: AuthService) {}

  ngOnInit(): void {
    this.loggedInAuthor = this.authService.getUsername() ?? '';
    this.loadNotifications();

  }

  loadNotifications(): void {
    this.postService.getNotificationsByReceiver(this.loggedInAuthor).subscribe((data) => {
      this.notifications = data;
    });
  }

  toggleNotificationsModal(): void {
    this.isNotificationsModalOpen = !this.isNotificationsModalOpen;
  }
}

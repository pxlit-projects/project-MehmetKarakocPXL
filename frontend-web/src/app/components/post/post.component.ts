import { Component, OnInit } from '@angular/core';
import { NgIf, NgClass } from '@angular/common'; // Add NgClass here
import { FormsModule } from '@angular/forms';
import { PostService } from '../../services/post/post.service';
import { ReviewService } from '../../services/review/review.service';
import { Post, PostStatus } from '../../models/post.model';
import { Review } from '../../models/review.model';
import { DatePipe } from '@angular/common'; // Import DatePipe
import { AuthService } from '../../services/AuthService';
import { Router } from '@angular/router';
import { NotificationsComponent } from '../notifications/notifications.component';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
  standalone: true,
  imports: [NgIf, NgClass, FormsModule, DatePipe, NotificationsComponent], 
  providers: [DatePipe],
})

export class PostComponent implements OnInit {
  PostStatus = PostStatus; // Expose the enum to the template
  posts: Post[] = [];
  reviews: Review[] = [];
  isAddPostModalOpen = false;
  isEditPostModalOpen = false;
  currentView: string = 'publishedPosts'; // Default view
  filteredPosts: Post[] = [];
  loggedInAuthor: string = '';
  editablePost: Partial<Post> = {};
  
  newPost: Partial<Post> = {
    title: '',
    content: '',
    isConcept: true,
    status: PostStatus.PENDING,
  };

  filters = {
    author: '',
    content: '',
    date: '',
  };

  isRejectModalOpen: boolean = false;
  rejectComment: string = '';
  rejectPost: Post | null = null; // The post being rejected

  constructor(private datePipe: DatePipe, private postService: PostService, private reviewService: ReviewService, private authService: AuthService, private router: Router
    ) {}

  ngOnInit(): void {
    this.loadPosts();
    this.loadReviews(); 
    this.loggedInAuthor = this.authService.getUsername() ?? '';
  }

  loadReviews(): void {
    this.reviewService.getReviews().subscribe((data) => {
      this.reviews = data;
    });
  }

  // Filter reviews for a specific post
  getRejectionReason(postId: number): string | null {
    const review = this.reviews.find(
      (r) => r.postId === postId
    );
    return review ? review.content : null;
  }

  get isAdmin(): boolean {
    return this.authService.isAdmin();
  }


  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }
  openAddPostModal(): void {
    this.isAddPostModalOpen = true;
  }

  closeAddPostModal(): void {
    this.isAddPostModalOpen = false;
    this.resetNewPost();
  }

  openEditPostModal(post: Post, event: Event): void {
    event.stopPropagation();
    this.editablePost = { ...post };
    this.isEditPostModalOpen = true;
  }

  closeEditPostModal(): void {
    this.isEditPostModalOpen = false;
    this.editablePost = {};
  }

  private resetNewPost(): void {
    this.newPost = {
      author: '',
      title: '',
      content: '',
      isConcept: false,
      status: PostStatus.PENDING
    };
  }

  loadPosts(): void {
    const filters: any = {};
  
    if (this.currentView === 'publishedPosts') {
      filters.status = 'APPROVED';
    } else if (this.currentView === 'pendingPosts') {
      filters.status = 'PENDING';
    } else if (this.currentView === 'rejectedPosts') {
      filters.status = 'REJECTED';
    } else if (this.currentView === 'myPosts') {
      filters.author = this.loggedInAuthor;
    }
  
    // Call the filter endpoint with the constructed filters
    this.postService.getFilteredPosts(filters).subscribe((data) => {
      this.posts = data;
    });
  }

  setView(view: string): void {
    this.currentView = view;
    this.loadPosts(); // Reload posts based on the selected view
  }

  addPost(): void {
    if (this.newPost.title && this.newPost.content) {
      this.newPost.author = this.authService.getUsername() || '';
      this.newPost.status = PostStatus.PENDING; // Explicitly set status
      this.postService.addPost(this.newPost).subscribe(() => {
        this.loadPosts();
        this.closeAddPostModal();
      });
    } else {
      alert('All fields are required!');
    }
  }
  
  editPost(): void {
    if (this.editablePost.id) {
      this.postService
        .updatePost(this.editablePost.id, this.editablePost)
        .subscribe(() => {
          this.loadPosts();
          this.closeEditPostModal();
        });
    }
  }

  navigateToComments(postId: number): void {
    this.router.navigate(['/comments', postId]);
  }
  

  addReview(post: Post | null, status: PostStatus, comment: string = '', event: Event): void {
    event.stopPropagation();
      if (!post) {
        console.error('Cannot add review: post is null');
        return;
      }

    const review: Partial<Review> = {
      postId: post.id,
      isApproved: status === PostStatus.APPROVED,
      content: comment, // Add comment only for rejection
      author: this.authService.getUsername() || 'Unknown',
    };

    this.reviewService.addReview(review).subscribe({
      next: () => {
        this.loadPosts(); // Reload posts after update
        this.loadReviews(); // Reload reviews after update
        if (status === PostStatus.REJECTED) {
          this.closeRejectModal(); // Close reject modal on success
        }
      },
    });
  }
  

  applyFilters(): void {
    // Format the date for backend compatibility
    const formattedDate = this.filters.date
      ? this.datePipe.transform(this.filters.date, 'yyyy-MM-dd')
      : null;
  
    // Prepare the filters object, including the current view
    const filterParams: any = {
      author: this.filters.author || null,
      content: this.filters.content || null,
      date: formattedDate,
      status: this.currentView === 'publishedPosts' ? 'APPROVED' :
              this.currentView === 'pendingPosts' ? 'PENDING' :
              this.currentView === 'rejectedPosts' ? 'REJECTED' :
              null
    };

    // Remove null values from the query parameters
    const params = Object.fromEntries(Object.entries(filterParams).filter(([_, v]) => v != null));

    this.postService.getFilteredPosts(params).subscribe((data) => {
      this.posts = data; // Update filteredPosts with filtered data
    });
  }

  resetFilters(): void {
    this.filters = { author: '', content: '', date: '' };
    this.loadPosts();
  }

  openRejectModal(post: Post, event: Event ): void {
    event.stopPropagation();
    this.isRejectModalOpen = true;
    this.rejectPost = post;
    this.rejectComment = '';
  }
  
  closeRejectModal(): void {
    this.isRejectModalOpen = false;
    this.rejectPost = null;
    this.rejectComment = '';
  }
}

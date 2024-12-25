import { Component, OnInit } from '@angular/core';
import { NgIf, NgFor, NgClass } from '@angular/common'; // Add NgClass here
import { FormsModule } from '@angular/forms';
import { PostService } from '../../services/post/post.service';
import { ReviewService } from '../../services/review/review.service';
import { Post, PostStatus } from '../../models/post.model';
import { Review } from '../../models/review.model';
import { DatePipe } from '@angular/common'; // Import DatePipe
import { AuthService } from '../../services/AuthService';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
  standalone: true,
  imports: [NgIf, NgFor, NgClass, FormsModule, DatePipe], // Add NgClass to imports
  providers: [DatePipe],
  
})
export class PostComponent implements OnInit {
  PostStatus = PostStatus; // Expose the enum to the template
  posts: Post[] = [];
  review: Review[] = [];
  isAddPostModalOpen = false;
  isEditPostModalOpen = false;
  currentView: string = 'allPosts'; 
  filteredPosts: Post[] = [];
  loggedInAuthor: string = '';
  editablePost: Partial<Post> = {};
  newPost: Partial<Post> = {
    title: '',
    content: '',
    isConcept: true,
    status: PostStatus.PENDING,
  };

  isRejectModalOpen: boolean = false;
  rejectComment: string = '';
  rejectPost: Post | null = null; // The post being rejected

  constructor(private datePipe: DatePipe, private postService: PostService, private reviewService: ReviewService, private authService: AuthService, private router: Router
    ) {}

  ngOnInit(): void {
    this.loadPosts();
    this.loggedInAuthor = this.authService.getUsername() ?? '';
  }

  get isAdmin(): boolean {
    return this.authService.isAdmin();
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
    this.postService.getPosts().subscribe((data) => {
      this.posts = data;
      this.filterPosts();
    });
  }

  setView(view: string): void {
    this.currentView = view;
    this.filterPosts();
  }

  private filterPosts(): void {
    if (this.currentView === 'allPosts') {
      this.filteredPosts = this.posts; // Show all posts
    } else if (this.currentView === 'myPosts') {
      this.filteredPosts = this.posts.filter(
        (post) => post.author === this.loggedInAuthor
      ); // Show only the user's posts
    } 
    // else if (this.currentView === 'myConceptPosts') {
    //   this.filteredPosts = this.posts.filter(
    //     (post) => post.author === this.loggedInAuthor && post.isConcept
    //   ); // Show only the user's concept posts
    // }
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
        if (status === PostStatus.REJECTED) {
          this.closeRejectModal(); // Close reject modal on success
        }
      },
      error: (err) => {
        console.error(`Error updating status for post `, err);
      },
    });
  }
  
  filters = {
    author: '',
    content: '',
    date: '',
  };

  applyFilters(): void {
    // Format date using DatePipe if a date is selected
    const formattedDate = this.filters.date
      ? this.datePipe.transform(this.filters.date, 'yyyy-MM-dd') // Match backend format
      : null;
      
    const filters = {
      ...this.filters,
      date: formattedDate, // Replace raw date with formatted date
    };

    this.postService.getFilteredPosts(filters).subscribe((data) => {
      this.filteredPosts = data;
    });
  }

  // Reset filters
  resetFilters() {
    this.filters = { author: '', content: '', date: '' };
    this.filteredPosts = this.posts; // Reset to original posts
  }

  openRejectModal(post: Post, event: Event ): void {
    event.stopPropagation();
    this.isRejectModalOpen = true;
    this.rejectPost = post;
    this.rejectComment = ''; // Reset comment field
  }
  
  closeRejectModal(): void {
    this.isRejectModalOpen = false;
    this.rejectPost = null;
    this.rejectComment = '';
  }
}

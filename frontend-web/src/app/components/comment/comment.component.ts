import { Component, OnInit } from '@angular/core';
import { NgClass } from '@angular/common'; // Add NgClass here
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CommentService } from '../../services/comment/comment.service';
import { PostService } from '../../services/post/post.service';
import { Post } from '../../models/post.model';
import { Comment } from '../../models/comment.model';
import { DatePipe } from '@angular/common'; // Import DatePipe
import { AuthService } from '../../services/AuthService';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css'],
  imports: [FormsModule, DatePipe],
  standalone: true,
})
export class CommentComponent implements OnInit {
  post: Post | null = null;
  comments: Comment[] = [];
  newComment: Partial<Comment> = { content: '' };
  editableCommentContent: string = '';
  editableCommentAuthor: string = '';
  editingCommentId: number | null = null;
  postId: number = 0;
  loggedInAuthor: string = '';


  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private commentService: CommentService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loggedInAuthor = this.authService.getUsername() || '';
    this.route.params.subscribe((params) => {
      this.postId = +params['postId'];
      this.loadPost();
      this.loadComments();
    });
  }

  loadPost(): void {
    this.postService.getPostById(this.postId).subscribe((post) => {
      this.post = post;
    });
  }

  loadComments(): void {
    this.commentService.getComments(this.postId).subscribe((comments) => {
      this.comments = comments;
    });
  }

  addComment(): void {
    if (this.newComment.content?.trim()) {
      this.newComment.author = this.authService.getUsername() || '';
      this.commentService.addComment(this.postId, this.newComment).subscribe(() => {
        this.loadComments();
        this.newComment.content = '';
      });
    }
  }

  startEditingComment(comment: Comment): void {
    this.editingCommentId = comment.id;
    this.editableCommentContent = comment.content;
    this.editableCommentAuthor = this.authService.getUsername() || '';
  }

  isEditingComment(commentId: number): boolean {
    return this.editingCommentId === commentId;
  }

  updateComment(commentId: number): void {
    if (this.editableCommentContent.trim()) {
      const updatedComment = { content: this.editableCommentContent , author: this.editableCommentAuthor};
      this.commentService.updateComment(commentId, updatedComment).subscribe(() => {
        this.editingCommentId = null;
        this.editableCommentContent = '';
        this.loadComments();
      });
    }
  }


  canDeleteOrEditComment(comment: Comment): boolean {
    return comment.author === this.loggedInAuthor;
  }

  deleteComment(commentId: number): void {
    this.commentService.deleteComment(commentId).subscribe(() => {
      this.loadComments();
    });
  }
}
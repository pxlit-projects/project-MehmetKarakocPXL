import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { CommentComponent } from './comment.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { CommentService } from '../../services/comment/comment.service';
import { PostService } from '../../services/post/post.service';
import { AuthService } from '../../services/AuthService';
import { Post, PostStatus } from '../../models/post.model';
import { Comment } from '../../models/comment.model';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';

describe('CommentComponent', () => {
  let component: CommentComponent;
  let fixture: ComponentFixture<CommentComponent>;
  let mockCommentService: jasmine.SpyObj<CommentService>;
  let mockPostService: jasmine.SpyObj<PostService>;
  let mockAuthService: jasmine.SpyObj<AuthService>;
  let mockActivatedRoute: any;

  const mockPost: Post = {
    id: 1,
    title: 'Test Post',
    content: 'Test Content',
    author: 'Test Author',
    isConcept: false,
    status: PostStatus.APPROVED,
    createdDate: new Date().toISOString()
  };

  const mockComments: Comment[] = [
    {
      id: 1,
      content: 'Test Comment 1',
      author: 'Test Author',
      postId: 1,
      createdDate: new Date().toISOString()
    },
    {
      id: 2,
      content: 'Test Comment 2',
      author: 'Different Author',
      postId: 1,
      createdDate: new Date().toISOString()
    }
  ];

  beforeEach(async () => {
    mockCommentService = jasmine.createSpyObj('CommentService', ['getComments', 'addComment', 'updateComment', 'deleteComment']);
    mockPostService = jasmine.createSpyObj('PostService', ['getPostById']);
    mockAuthService = jasmine.createSpyObj('AuthService', ['getUsername']);
    mockActivatedRoute = {
      params: of({ postId: '1' })
    };

    await TestBed.configureTestingModule({
      imports: [CommentComponent, FormsModule, DatePipe],
      providers: [
        { provide: CommentService, useValue: mockCommentService },
        { provide: PostService, useValue: mockPostService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ]
    }).compileComponents();

    mockPostService.getPostById.and.returnValue(of(mockPost));
    mockCommentService.getComments.and.returnValue(of(mockComments));
    mockAuthService.getUsername.and.returnValue('Test Author');

    fixture = TestBed.createComponent(CommentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with post and comments', fakeAsync(() => {
    component.ngOnInit();
    tick();

    expect(mockPostService.getPostById).toHaveBeenCalledWith(1);
    expect(mockCommentService.getComments).toHaveBeenCalledWith(1);
    expect(component.post).toEqual(mockPost);
    expect(component.comments).toEqual(mockComments);
    expect(component.loggedInAuthor).toBe('Test Author');
  }));

  it('should not add empty comment', fakeAsync(() => {
    component.newComment.content = '   ';
    component.addComment();
    tick();

    expect(mockCommentService.addComment).not.toHaveBeenCalled();
  }));

  it('should start editing comment', () => {
    const comment = mockComments[0];
    component.startEditingComment(comment);

    expect(component.editingCommentId).toBe(comment.id);
    expect(component.editableCommentContent).toBe(comment.content);
    expect(component.editableCommentAuthor).toBe('Test Author');
  });

  it('should update comment', fakeAsync(() => {
    const updatedComment = { content: 'Updated Content', author: 'Test Author' };
    mockCommentService.updateComment.and.returnValue(of(void 0));

    component.editingCommentId = 1;
    component.editableCommentContent = 'Updated Content';
    component.editableCommentAuthor = 'Test Author';
    component.updateComment(1);
    tick();

    expect(mockCommentService.updateComment).toHaveBeenCalledWith(1, updatedComment);
    expect(component.editingCommentId).toBeNull();
    expect(mockCommentService.getComments).toHaveBeenCalled();
  }));

  it('should delete comment', fakeAsync(() => {
    mockCommentService.deleteComment.and.returnValue(of(void 0));

    component.deleteComment(1);
    tick();

    expect(mockCommentService.deleteComment).toHaveBeenCalledWith(1);
    expect(mockCommentService.getComments).toHaveBeenCalled();
  }));

  it('should check if user can edit/delete their own comment', () => {
    const ownComment = mockComments[0];  // Test Author's comment
    const otherComment = mockComments[1]; // Different Author's comment

    expect(component.canDeleteOrEditComment(ownComment)).toBeTrue();
    expect(component.canDeleteOrEditComment(otherComment)).toBeFalse();
  });

  it('should check if comment is being edited', () => {
    component.editingCommentId = 1;

    expect(component.isEditingComment(1)).toBeTrue();
    expect(component.isEditingComment(2)).toBeFalse();
  });
});

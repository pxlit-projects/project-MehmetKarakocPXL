import { Routes } from '@angular/router';
import { LoginComponent } from '../app/components/login/login.component';
import { PostComponent } from '../app/components/post/post.component';
import { CommentComponent } from '../app/components/comment/comment.component';

export const routes: Routes = [
    { path: '', component: LoginComponent }, // Login as default
    { path: 'posts', component: PostComponent },
    { path: 'comments/:postId', component: CommentComponent },
];

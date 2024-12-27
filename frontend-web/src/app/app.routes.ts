import { Routes } from '@angular/router';
import { LoginComponent } from '../app/components/login/login.component';
import { PostComponent } from '../app/components/post/post.component';
import { CommentComponent } from '../app/components/comment/comment.component';
import { AuthGuard } from './services/auth.guard';

export const routes: Routes = [
    { path: '', component: LoginComponent }, // Login as default
    { path: 'posts', component: PostComponent, canActivate: [AuthGuard] },
    { path: 'comments/:postId', component: CommentComponent, canActivate: [AuthGuard] },
];

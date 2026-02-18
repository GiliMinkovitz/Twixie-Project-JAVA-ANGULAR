import { Routes } from '@angular/router';
import { LandingPageComponent } from './Components/landing-page/landing-page.component';
import { AddPostComponent } from './Components/add-post/add-post.component';
import { LogInComponent } from './Components/log-in/log-in.component';
import { SignUpComponent } from './Components/sign-up/sign-up.component';
import { FeedComponent } from './Components/feed/feed.component';
import { PostDetailsComponent } from './Components/post-details/post-details.component';
import { ProfileComponent } from './Components/profile/profile.component';
import { FollowersComponent } from './Components/follow/follow.component';
import { MyPostsComponent } from './Components/my-posts/my-posts.component';
import { EditPostComponent } from './Components/edit-post/edit-post.component';
import { AddResponseComponent } from './Components/add-response/add-response.component';
import { ResponseListComponent } from './Components/response-list/response-list.component';
import { SettingsComponent } from './Components/settings/settings.component';
import { UserDetailsComponent } from './Components/user-details/user-details.component';

export const routes: Routes = [
  { path: '', redirectTo: 'landing-page', pathMatch: 'full' },
  { path: 'landing-page', component: LandingPageComponent },
  { path: 'add-post', component: AddPostComponent },
  { path: 'log-in', component: LogInComponent },
  { path: 'sign-up', component: SignUpComponent },
  { path: 'feed', component: FeedComponent },
  { path: 'post-details/:id', component: PostDetailsComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'follow/:id', component: FollowersComponent },
  { path: 'my-posts', component: MyPostsComponent },
  { path: 'edit-post/:id', component: EditPostComponent },
  {
    path: 'add-response/:postId/:parentResponseId',
    component: AddResponseComponent,
  },
  { path: 'response-list/:postId', component: ResponseListComponent },
  { path: 'settings', component: SettingsComponent },
  { path: 'user-details', component: UserDetailsComponent}
];

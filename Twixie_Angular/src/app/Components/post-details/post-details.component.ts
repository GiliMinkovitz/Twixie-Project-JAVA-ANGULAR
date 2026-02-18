import { Component, EventEmitter, OnInit, Output, HostBinding } from '@angular/core';
import Post from '../../Model/post.model';
import { PostService } from '../../Service/post.service';
import { ActivatedRoute, Router, RouterLinkActive } from '@angular/router';
import { LikesService } from '../../Service/likes.service';
import { ResponseListComponent } from '../response-list/response-list.component';
import { FollowService } from '../../Service/follow.service';
import { UserSettingsService } from '../../Service/user-settings.service';
import { UserDetailsComponent } from '../user-details/user-details.component';
import { MenuBar } from "../menu-bar/menu-bar";
import { TimeAgoPipe } from '../../time-ago.pipe';
import { AddResponseComponent } from '../add-response/add-response.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-post-details',
  imports: [ResponseListComponent, UserDetailsComponent, MenuBar, TimeAgoPipe, AddResponseComponent],
  styleUrl: './post-details.component.css',
  templateUrl: './post-details.component.html',
})
export class PostDetailsComponent implements OnInit {
  post!: Post;
  likes!: number;
  dislikes!: number;
  isFollowing!: boolean;
  @HostBinding('class.dark-mode') darkMode: boolean = false;


  constructor(
    private _postService: PostService,
    private route: ActivatedRoute,
    private _likesService: LikesService,
    private _settingService: UserSettingsService,
    private _followService: FollowService,
    private router: Router,
    private dialog: MatDialog,

  ) { }

  ngOnInit(): void {
    this._settingService.loadUserSettings().subscribe();
    this._settingService.settings$.subscribe((settings) => {
      if (settings) {
        this.darkMode = settings.darkModeEnabled;
      }
    });
    this.route.params.subscribe((params) => {
      const postId = +params['id']; // Convert route parameter string to number
      this.getPostFromServer(postId);
    });
  }

  getPostFromServer(postId: number) {
    // Retrieve the specific post data from the server by ID
    this._postService.getPostById(postId).subscribe({
      next: (res) => {
        this.post = res;
        this.getAmountOfLikesAndDislikes();
        this._followService.isFollowing(this.post.poster.userId).subscribe({
          next: (res) => {
            this.isFollowing = res;
          },
          error: (err) => {
            console.log(err);
          },
        });
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  getAmountOfLikesAndDislikes() {
    // Retrieve the count of likes for this post from the server
    this._likesService
      .getAmountOfLikesForPost(this.post.postId, true)
      .subscribe({
        next: (res) => {
          this.likes = res;
        },
        error: (err) => {
          console.log(err);
        },
      });
    // Retrieve the count of dislikes for this post from the server
    this._likesService
      .getAmountOfLikesForPost(this.post.postId, false)
      .subscribe({
        next: (res) => {
          this.dislikes = res;
        },
        error: (err) => {
          console.log(err);
        },
      });
  }

  likeOrDislikePost(likeType: boolean) {
    this._likesService.addLike(this.post.postId, likeType).subscribe({
      next: (res) => {
        this.getAmountOfLikesAndDislikes();
      },
      error: (err) => {
        console.log(err);
      },
    });
  }


}

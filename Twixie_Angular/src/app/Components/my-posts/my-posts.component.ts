import { Component, HostBinding, OnInit } from '@angular/core';
import Post from '../../Model/post.model';
import { PostService } from '../../Service/post.service';
import { Router, RouterModule } from '@angular/router';
import { UserService } from '../../Service/user.service';
import { UserSettingsService } from '../../Service/user-settings.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { MessageService } from '../../Service/message-service';
import { MenuBar } from '../menu-bar/menu-bar';
import { CommonModule, DatePipe } from '@angular/common';
import { TimeAgoPipe } from '../../time-ago.pipe';

@Component({
  selector: 'app-my-posts',
  imports: [RouterModule, MenuBar, TimeAgoPipe],
  templateUrl: './my-posts.component.html',
  styleUrl: './my-posts.component.css',
})
export class MyPostsComponent implements OnInit {
  public postList!: Post[];
  private allPosts!: Post[];
  public isLoading = true; // לעקוב אחרי מצב הטעינה
  @HostBinding('class.dark-mode') darkMode: boolean = false;

  constructor(
    private _postService: PostService,
    private router: Router,
    private dialog: MatDialog,
    private _messageService: MessageService,
    private _settingService: UserSettingsService
  ) {}

  ngOnInit(): void {
    this._settingService.loadUserSettings().subscribe();
    this._settingService.settings$.subscribe((settings) => {
      if (settings) {
        this.darkMode = settings.darkModeEnabled;
      }
    });
    this.getPostsFromServer();
  }

  getPostsFromServer() {
    this._postService.getMyPosts().subscribe({
      next: (res) => {
        this.postList = res;
        this.allPosts = res;
        this.isLoading = false; // הטעינה הסתיימה
        console.log(this.postList);
      },
      error: (err) => {
        this.isLoading = false; // הטעינה הסתיימה גם בשגיאה
        if (err.status === 404) {
          this._messageService.showErrorMessage("You've not written posts yet");
        }
        console.log(err);
      },
    });
  }

  toShowDetails(post: Post) {
    this.router.navigate(['/post-details', post.postId]);
  }

  toEditPost(post: Post) {
    this.router.navigate(['/edit-post', post.postId]);
  }

  deletePost(post: Post) {
    // פותחים דיאלוג לאישור
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: `Are you sure you want to delete this post?` },
    });

    // מאזינים לסגירת הדיאלוג
    dialogRef.afterClosed().subscribe((result) => {
      if (result === true) {
        // המשתמש אישר – מבצעים מחיקה
        this._postService.deletePost(post.postId).subscribe({
          next: () => {
            this._messageService.showSuccessMessage(
              `"${post.title}" deleted successfully`
            );
            this.postList = this.postList.filter(
              (p) => p.postId !== post.postId
            );
            this.allPosts = this.postList.filter(
              (p) => p.postId !== post.postId
            );
          },
          error: (err) => {
            console.log(err);
          },
        });
      }
    });
  }

  getImageUrl(base64?: string) {
    if (base64 && base64.trim()) {
      return `data:image/jpeg;base64,${base64}`;
    }
    return `assets/images/default-feed.jpg`;
  }


  search(keyword: string) {
    const term = keyword.toLowerCase().trim();

    this.postList = this.allPosts.filter((post) => {
      const content = post.content.toLowerCase();
      const title = post.title.toLowerCase();

      return content.includes(term) || title.includes(term);
    });
  }
}

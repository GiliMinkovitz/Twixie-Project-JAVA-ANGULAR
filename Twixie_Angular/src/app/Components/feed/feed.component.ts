import { Component, HostListener, OnInit, HostBinding } from '@angular/core';
import Post from '../../Model/post.model';
import { PostService } from '../../Service/post.service';
import { Router, RouterModule } from '@angular/router';
import { UserService } from '../../Service/user.service';
import { TopicService } from '../../Service/topic.service';
import Topic from '../../Model/topic.model';
import { MessageService } from '../../Service/message-service';
import { MenuBar } from "../menu-bar/menu-bar";
import { UserSettingsService } from '../../Service/user-settings.service';
import { TimeAgoPipe } from '../../time-ago.pipe';

interface PageResponse {
  content: Post[];
  last: boolean; // האם זה העמוד האחרון
  number: number;
}

@Component({
  selector: 'app-feed',
  imports: [RouterModule, MenuBar, TimeAgoPipe],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.css',
})
export class FeedComponent implements OnInit {
  private allPosts: Post[] = [];
  public postList: Post[] = []; // ← מתחיל ריק, נוסיף אליו בהדרגה
  public categories: Topic[] = [];
  public userNames: string[] = [];
  private page = 0;
  private loading = false;
  private noMorePosts = false;
  public isLoading = true; // לעקוב אחרי מצב הטעינה הראשוני

  @HostBinding('class.dark-mode') darkMode: boolean = false;

  constructor(
    private _postService: PostService,
    private router: Router,
    private _userService: UserService,
    private _topicService: TopicService,
    private _messageService: MessageService,
    private _settingService: UserSettingsService
  ) {}

  ngOnInit(): void {
    this.loadMorePosts();
    this.getAllTopics();
    this.getAllUserNames();
    this.applySettings()
  }

  private loadMorePosts() {
    if (this.loading || this.noMorePosts) return;

    this.loading = true;

    this._postService.getFeedPage(this.page).subscribe({
      next: (response: PageResponse) => {
        this.allPosts = [...this.allPosts, ...response.content];
        this.postList = [...this.postList, ...response.content]; // מוסיף לסוף
        this.noMorePosts = response.last; // אם זה העמוד האחרון
        this.page++;
        this.loading = false;
        this.isLoading = false; // הטעינה הסתיימה
      },
      error: (err) => {
        if (err.status === 404) {
          this._messageService.showErrorMessage('No posts to present');
        } else {
          console.log(err);
        }
        this.loading = false;
        this.isLoading = false; // הטעינה הסתיימה גם בשגיאה
      },
    });
  }

  // גלילה אוטומטית – מופעלת כשמגיעים קרוב לסוף הדף
  @HostListener('window:scroll')
  onScroll() {
    if (
      window.innerHeight + window.scrollY >= document.body.offsetHeight - 500 &&
      !this.loading
    ) {
      this.loadMorePosts();
    }
  }

  toShowDetails(post: Post) {
    this.router.navigate(['/post-details', post.postId]);
  }

  getImageUrl(base64?: string) {
    if (base64 && base64.trim()) {
      return `data:image/jpeg;base64,${base64}`;
    }
    return `assets/images/default-feed.jpg`;
  }


  getAllUserNames() {
    this._userService.getAllUserNames().subscribe({
      next: (res) => {
        this.userNames = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  getAllTopics() {
    this._topicService.getTopicsList().subscribe({
      next: (res) => {
        this.categories = res;
        console.log(res);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }


  search(keyword: string) {
    const term = keyword.toLowerCase().trim();

    this.postList = this.allPosts.filter((post) => {
      const content = post.content.toLowerCase();
      const title = post.title.toLowerCase();

      return content.includes(term) || title.includes(term);
    });
  }

  sort(event: Event) {
    const sortOption = (event.target as HTMLSelectElement).value;
    switch (sortOption) {
      case '1':
        this.sortByDateAsc();
        break;
      case '2':
        this.sortByDateDesc();
        break;
      case '3':
        this.sortByTitle();
        break;
      default:
        this.postList = [...this.allPosts];
    }
  }

  sortByTitle() {
    this.postList.sort((a, b) => a.title.localeCompare(b.title));
  }

  sortByDateAsc() {
    this.postList.sort((a, b) => {
      const dateA = new Date(a.lastUpdated);
      const dateB = new Date(b.lastUpdated);
      return dateB.getTime() - dateA.getTime();
    });
  }

  sortByDateDesc() {
    this.postList.sort((a, b) => {
      const dateA = new Date(a.lastUpdated);
      const dateB = new Date(b.lastUpdated);
      return dateA.getTime() - dateB.getTime();
    });
  }

  filterByTopic(event: Event) {
    const topic = (event.target as HTMLSelectElement).value;
    if (topic === '0') {
      this.postList = [...this.allPosts];
      return;
    }
    this.postList = this.allPosts.filter((post) =>
      post.topicName.includes(topic)
    );
  }

  filterByPoster(event: Event) {
    const poster = (event.target as HTMLSelectElement).value;
    if (poster === '0') {
      this.postList = [...this.allPosts];
      return;
    }
    this.postList = this.allPosts.filter(
      (post) => post.poster.userName === poster
    );
  }

  applySettings(){
    // Load initial settings and subscribe to changes
    this._settingService.loadUserSettings().subscribe();
    this._settingService.settings$.subscribe((settings) => {
      if (settings) {
        this.darkMode = settings.darkModeEnabled;
      }
    });
  }
}

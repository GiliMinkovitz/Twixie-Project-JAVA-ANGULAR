import { Component, OnInit, OnDestroy, HostBinding } from '@angular/core';
import { PostService } from '../../Service/post.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import Topic from '../../Model/topic.model';
import { TopicService } from '../../Service/topic.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import {
  Subject,
  debounceTime,
  distinctUntilChanged,
  filter,
  switchMap,
  takeUntil,
} from 'rxjs';
import UserSettings from '../../Model/userSettings.model';
import { UserSettingsService } from '../../Service/user-settings.service';
import { MessageService } from '../../Service/message-service';
import { MenuBar } from "../menu-bar/menu-bar";

@Component({
  selector: 'app-add-post',
  imports: [ReactiveFormsModule, CommonModule, MenuBar],
  templateUrl: './add-post.component.html',
  styleUrl: './add-post.component.css',
})
export class AddPostComponent implements OnInit, OnDestroy {
  addForm!: FormGroup;
  topicsList: Topic[] = [];
  selectedFile: File | null = null;
  @HostBinding('class.dark-mode') darkMode: boolean = false;
  imagePreviewUrl: string | ArrayBuffer | null = null;

  aiSuggestion: string = '';
  isLoadingSuggestion: boolean = false;
  private contentChanges$ = new Subject<string>();
  private destroy$ = new Subject<void>();
  private lastUserInput: string = '';
  private lastKnownValue: string = ''; //  ערך אמיתי שנשמור
  private userSettings!: UserSettings;

  constructor(
    private _postService: PostService,
    private _topicService: TopicService,
    private _router: Router,
    private _userSettingService: UserSettingsService,
    private _massageService: MessageService
  ) {}

  ngOnInit(): void {
    this.addForm = new FormGroup({
      topicId: new FormControl(),
      topicName: new FormControl(),
      content: new FormControl(''),
      title: new FormControl(''),
      imagePath: new FormControl(),
    });
    this.getTopicList();
    this.getUserSettings();
    this.setupAutocompletion();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  getTopicList() {
    this._topicService.getTopicsList().subscribe({
      next: (res) => {
        this.topicsList = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  getUserSettings() {
    this._userSettingService.loadUserSettings().subscribe({
      next: (res) => {
        this.userSettings = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
    // Subscribe to settings changes for dark mode
    this._userSettingService.settings$.subscribe((settings) => {
      if (settings) {
        this.darkMode = settings.darkModeEnabled;
      }
    });
  }

  setupAutocompletion() {
    this.contentChanges$
      .pipe(
        debounceTime(2000),
        distinctUntilChanged(),
        filter((text) => text.trim().length > 10),
        switchMap((text) => {
          this.isLoadingSuggestion = true;
          this.lastUserInput = text;
          var title = this.addForm.value.title;
          return this._postService.getAiCompletion(
            `The post title is:${title}, the content is: ${text}`
          );
        }),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: (suggestion) => {
          this.isLoadingSuggestion = false;
          const currentContent = this.addForm.get('content')?.value || '';

          // וודא שהמשתמש לא המשיך לכתוב
          if (currentContent === this.lastUserInput && suggestion) {
            this.aiSuggestion = ' ' + suggestion.trim();
            this.lastKnownValue = currentContent; //  עדכן את הערך השמור
          }
        },
        error: (err) => {
          this.isLoadingSuggestion = false;
          this.aiSuggestion = '';
          if (err.status === 429) {
            this._massageService.showErrorMessage('Quota Exceeded');
          }
        },
      });
  }

  onContentChange(event: Event) {
    if (!this.userSettings.autocompleteEnabled) {
      return;
    }
    const textarea = event.target as HTMLTextAreaElement;
    const currentValue = textarea.value;
    // אם יש הצעה קיימת
    if (this.aiSuggestion) {
      // הטקסט המלא המצופה = הערך השמור + ההצעה
      const expectedFullText = this.lastKnownValue + this.aiSuggestion;
      // בדוק אם המשתמש הקליד יותר
      if (currentValue.length > this.lastKnownValue.length) {
        // בדוק אם מה שהוא כתב תואם להצעה
        if (expectedFullText.startsWith(currentValue)) {
          // תואם! עדכן את ההצעה
          this.aiSuggestion = expectedFullText.substring(currentValue.length);
          this.lastKnownValue = currentValue;
        } else {
          // לא תואם - בטל
          this.aiSuggestion = '';
          this.lastKnownValue = currentValue;
        }
      } else if (currentValue.length < this.lastKnownValue.length) {
        // מחק - בטל
        this.aiSuggestion = '';
        this.lastKnownValue = currentValue;
      } else {
        // אותו אורך אבל שונה
        this.lastKnownValue = currentValue;
      }
    } else {
      // אין הצעה - פשוט עדכן
      this.lastKnownValue = currentValue;
    }

    // עדכן את הטופס
    this.addForm.patchValue({ content: currentValue }, { emitEvent: false });

    // שלח לזרם
    this.contentChanges$.next(currentValue);
  }

  onContentKeyDown(event: KeyboardEvent) {
    // Tab - קבל את ההצעה
    if (event.key === 'Tab' && this.aiSuggestion) {
      event.preventDefault();
      const currentContent = this.addForm.get('content')?.value || '';
      const newContent = currentContent + this.aiSuggestion;
      this.addForm.patchValue({ content: newContent }, { emitEvent: false });
      this.aiSuggestion = '';
      this.lastUserInput = newContent;
      this.lastKnownValue = newContent;
      this.contentChanges$.next(newContent);
    }

    // Backspace או Delete - בטל הצעה
    if (
      (event.key === 'Backspace' || event.key === 'Delete') &&
      this.aiSuggestion
    ) {
      this.aiSuggestion = '';
    }
  }

  onTopicSelected(selectedId: string) {
    const selectedTopic = this.topicsList.find(
      (t) => t.topicId === Number(selectedId)
    );
    if (selectedTopic) {
      this.addForm.patchValue({
        topicName: selectedTopic.name,
      });
    }
  }

  onFileSelected(event: any) {
    const file = event.target.files[0] as File;
    this.selectedFile = file;

    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreviewUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    } else {
      this.imagePreviewUrl = null;
    }
  }

  savePost() {
    if (this.selectedFile && this.addForm.valid) {
      const postPayload = {
        poster: { userId: -1 },
        topic: { topicId: this.addForm.value.topicId },
        content: this.addForm.value.content,
        title: this.addForm.value.title,
        datePosted: new Date(Date.now()),
        rank: 0,
        lastUpdated: new Date(Date.now()),
      };

      const formData = new FormData();
      formData.append(
        'post',
        new Blob([JSON.stringify(postPayload)], { type: 'application/json' }),
        'post.json'
      );
      formData.append('image', this.selectedFile, this.selectedFile.name);

      this._postService.addPostWithImage(formData).subscribe({
        next: (res) => {
          this.imagePreviewUrl = null;
          this._router.navigate(['/my-posts']);
          this._massageService.showSuccessMessage('Post saved successfully');
        },
        error: (err) => {
          const errors: string[] = err.error || [];

          if (errors.length > 0) {
            const message = errors.map((e) => `• ${e}`).join('\n');
            this._massageService.showErrorMessage(message);
          }
        },
      });
    }
  }
}

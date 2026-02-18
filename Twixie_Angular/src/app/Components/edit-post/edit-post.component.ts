import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Post from '../../Model/post.model';
import { PostService } from '../../Service/post.service';
import { MessageService } from '../../Service/message-service';
import { MenuBar } from "../menu-bar/menu-bar";

@Component({
  selector: 'app-edit-post',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MenuBar],
  templateUrl: './edit-post.component.html',
  styleUrl: './edit-post.component.css',
})
export class EditPostComponent implements OnInit {
  constructor(
    public route: ActivatedRoute,
    public router: Router,
    private postService: PostService,
    private messageService: MessageService
  ) {}

  post!: Post;
  selectedFile: File | null = null;
  previewUrl: string | null = null;

  form = new FormGroup({
    content: new FormControl('', { nonNullable: true }),
  });

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.postService.getPostById(id).subscribe((post) => {
      this.post = post;
      this.form.patchValue({ content: post.content || '' });
    });
  }

  onFileSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    this.selectedFile = file || null;

    if (file) {
      const reader = new FileReader();
      reader.onload = () => (this.previewUrl = reader.result as string);
      reader.readAsDataURL(file);
    } else {
      this.previewUrl = null;
    }
  }

  get canSave(): boolean {
    const contentChanged =
      this.form.dirty &&
      this.form.controls.content.value.trim() !==
        (this.post.content || '').trim();
    return contentChanged || this.selectedFile !== null;
  }

  save() {
    // אין שום שינוי
    if (!this.canSave) {
      this.messageService.showErrorMessage('No updates to save');
      return;
    }

    const formData = new FormData();

    // תוכן
    if (this.form.dirty) {
      const newContent = this.form.controls.content.value;
      const oldContent = (this.post.content || '').trim();
      if (newContent.trim() !== oldContent) {
        formData.append('content', newContent);
      }
    }

    // תמונה
    if (this.selectedFile) {
      formData.append('image', this.selectedFile, this.selectedFile.name);
    }

    this.postService.updatePost(this.post.postId, formData).subscribe({
      next: (updatedPost) => {
        this.messageService.showSuccessMessage('Updates saved');
        // עדכון מקומי
        if (updatedPost) {
          this.post = { ...this.post, ...updatedPost };
        }
        this.post.content = this.form.controls.content.value; // תמיד נעדכן את התוכן

        this.form.markAsPristine();
        this.selectedFile = null;
        this.previewUrl = null;
        this.router.navigate(['/post-details', this.post.postId]);
      },
      error: (err) => {
        if (err.status === 400 && Array.isArray(err.error)) {
          const message = err.errors.map((e: string) => `• ${e}`).join('\n');
          this.messageService.showErrorMessage(message);
        }
        else{
          console.log(err)
        }
      },
    });
  }
}

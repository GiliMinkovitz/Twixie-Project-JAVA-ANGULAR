import { Component, Inject, Input, Output, HostBinding, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ResponseService } from '../../Service/response.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { MessageService } from '../../Service/message-service';
import { UserSettingsService } from '../../Service/user-settings.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-add-response',
  imports: [CommonModule, HttpClientModule, RouterModule, ReactiveFormsModule],
  templateUrl: './add-response.component.html',
  styleUrl: './add-response.component.css',
})
export class AddResponseComponent implements OnInit {
  addForm!: FormGroup;
  savedResponse!: Response
  @HostBinding('class.dark-mode') darkMode: boolean = false;

  constructor(
    private _responseService: ResponseService,
    private _router: Router,
    private route: ActivatedRoute,
    private _messageService: MessageService,
    private _settingService: UserSettingsService,
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { postId: number; parentResponseId: number }
  ) { }

  ngOnInit(): void {
    // Load initial settings and subscribe to changes
    this._settingService.loadUserSettings().subscribe();
    this._settingService.settings$.subscribe((settings) => {
      if (settings) {
        this.darkMode = settings.darkModeEnabled;
      }
    });
    this.addForm = new FormGroup({
      content: new FormControl(),
    });
  }

  saveResponse() {
    const responseToAdd = {
      parentPostResponse: {
        postId: this.data.postId,
      },
      parentResponseID: this.data.parentResponseId || 0,
      content: this.addForm.value.content,
      dateCreated: new Date(Date.now()),
    };

    this._responseService.addResponse(responseToAdd).subscribe({
      next: (res) => {
        this._messageService.showSuccessMessage("Response sent successfully")
        this.dialogRef.close(res);
      },
      error: (err) => {
        console.log(err)
        this._messageService.showErrorMessage("Error occured sending the response")
        this.dialogRef.close();
      },
    });
  }

}

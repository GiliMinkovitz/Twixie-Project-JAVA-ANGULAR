import { Component, Input, Output, EventEmitter, HostBinding, OnInit } from '@angular/core';
import Response from '../../Model/response.model';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { ResponseService } from '../../Service/response.service';
import { UserSettingsService } from '../../Service/user-settings.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MessageService } from '../../Service/message-service';
import { TimeAgoPipe } from '../../time-ago.pipe';
import { AddResponseComponent } from '../add-response/add-response.component';

@Component({
  selector: 'app-response-item',
  imports: [TimeAgoPipe],
  templateUrl: './response-item.component.html',
  styleUrl: './response-item.component.css',
})
export class ResponseItemComponent implements OnInit {
  @Input() response!: Response;
  @Input() depth: number = 0;
  @Output() responseDeleted = new EventEmitter<number>();
  @Output() responseAdded = new EventEmitter<number>();
  @HostBinding('class.dark-mode') darkMode: boolean = false;

  constructor(
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private _responseService: ResponseService,
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
  }

  deleteResponse() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: `Are you sure you want to delete this response?` },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === true) {
        this._responseService
          .deleteResponse(this.response.responseId)
          .subscribe({
            next: (res) => {
              this._messageService.showSuccessMessage(
                'Response deleted successfully'
              );
              this.notifyDelete();
            },
            error: (err) => {
              let errorMessage =
                'An error occurred. Unable to delete the response.';
              if (err.status === 403) {
                errorMessage =
                  'Oops! You do not have permission to delete this response.';
              }
              this._messageService.showErrorMessage(errorMessage);
              console.log(err);
            },
          });
      }
    });
  }

  notifyDelete() {
    this.responseDeleted.emit(this.response.responseId);
  }

  replyTo() {
    const dialogRef = this.dialog.open(AddResponseComponent, {
      data: {
        postId: this.response.parentPostResponseId,
        parentResponseId: this.response.responseId,
      },
    });

    dialogRef.afterClosed().subscribe((newResponse: Response | undefined) => {
      if (newResponse) {
        this.notifyAdd();
      }
    });
  }

  notifyAdd() {
    this.responseAdded.emit(this.response.responseId);
  }
}

import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../Service/user.service';
import { Router, RouterModule } from '@angular/router';
import { UsersLogIn } from '../../Model/users.model';
import { UserSettingsService } from '../../Service/user-settings.service';
import { MessageService } from '../../Service/message-service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-log-in',
  imports: [ReactiveFormsModule, RouterModule],
  templateUrl: './log-in.component.html',
  styleUrl: './log-in.component.css',
})
export class LogInComponent {
  logInForm!: FormGroup;

  constructor(
    private _userService: UserService,
    private _router: Router,
    private _userSettingService: UserSettingsService,
    private _messageService: MessageService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    // Initialize the login form with required controls
    this.logInForm = new FormGroup({
      userName: new FormControl(),
      password: new FormControl(),
    });
  }

  logIn() {
    if (this.logInForm.valid) {
      const newUser: UsersLogIn = {
        userName: this.logInForm.value.userName,
        email: '',
        password: this.logInForm.value.password,
        biography: '',
      };

      //send to the server
      this._userService.logIn(newUser).subscribe({
        next: (res) => {
          // Wait for user settings to load before navigation to ensure immediate application
          this._userSettingService.loadUserSettings().subscribe({
            next: () => {
              this._router.navigate(['/feed']);
            },
            error: () => {
              // Navigate to feed even if settings fail to load to prevent blocking access
              this._router.navigate(['/feed']);
            }
          });
        },
        error: (err) => {
          if (err.status === 400 && Array.isArray(err.error)) {
            const message = err.error.map((e: string) => `• ${e}`).join('\n');
            this._messageService.showErrorMessage(message);
          } else if (err.status === 401) {
            this._messageService.showErrorMessage('Wrong Password');
          } else if (err.status === 404) {
            this._messageService.showErrorMessage('Username not found');

            const dialogRef = this.dialog.open(ConfirmDialogComponent, {
              data: { message: `Do you want to sign up?` },
            });

            dialogRef.afterClosed().subscribe((result) => {
              if (result === true) {
                this._router.navigate(['/sign-up'])
              }
            });
          }
          console.log(err);
        },
      });
    }
  }
}

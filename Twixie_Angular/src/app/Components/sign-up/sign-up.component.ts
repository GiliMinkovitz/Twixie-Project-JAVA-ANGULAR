import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../Service/user.service';
import { Router, RouterModule } from '@angular/router';
import Users, { UsersLogIn } from '../../Model/users.model';
import { UserSettingsService } from '../../Service/user-settings.service';
import { MessageService } from '../../Service/message-service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
@Component({
  selector: 'app-sign-up',
  imports: [ReactiveFormsModule, RouterModule],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.css',
})
export class SignUpComponent implements OnInit {
  signUpForm!: FormGroup;
  selectedFile: File | null = null;
  // Variable to store the image Data URL for preview display
  imagePreviewUrl: string | ArrayBuffer | null = null;

  constructor(
    private _userService: UserService,
    private _router: Router,
    private _userSettingService: UserSettingsService,
    private _massageService: MessageService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    // Initialize the sign-up form with required controls
    this.signUpForm = new FormGroup({
      userName: new FormControl(),
      email: new FormControl(),
      password: new FormControl(),
      imagePath: new FormControl(),
      biography: new FormControl(),
      verifyCode: new FormControl(),
    });
  }

  onFileSelected(event: any) {
    const file = event.target.files[0] as File;
    this.selectedFile = file;

    // Logic to generate an image preview for user feedback
    if (file) {
      const reader = new FileReader();

      // Callback function invoked when the file is successfully loaded
      reader.onload = (e: any) => {
        // e.target.result contains the Data URL representation of the image
        this.imagePreviewUrl = e.target.result;
      };

      // Read the file as a Data URL for preview purposes
      reader.readAsDataURL(file);
    } else {
      this.imagePreviewUrl = null;
    }
  }

  sendVerificationCode() {
    this._userService
      .sendVerificationEmail(this.signUpForm.value.email)
      .subscribe({
        next: (res) => {
          console.log('email sent successfully');
          this._massageService.showSuccessMessage("Code Sent")
        },
        error: (err) => {
          console.log(err);
        },
      });
  }

  signUp() {
    if (this.selectedFile && this.signUpForm.valid) {
      const newUser: UsersLogIn = {
        userName: this.signUpForm.value.userName,
        email: this.signUpForm.value.email,
        password: this.signUpForm.value.password,
        biography: this.signUpForm.value.biography,
      };

      //make the form data, in the good format for the server
      const formData = new FormData();
      formData.append(
        'newUser',
        new Blob([JSON.stringify(newUser)], { type: 'application/json' }),
        'newUser.json'
      );
      formData.append('image', this.selectedFile, this.selectedFile.name);
      formData.append('verifyCode', this.signUpForm.value.verifyCode);

      // Submit the registration data to the server
      this._userService.signUp(formData).subscribe({
        next: (res) => {
          this.imagePreviewUrl = null;
          this._userService.logIn(newUser).subscribe({
            next: (res) => {
              console.log(res);
              this._router.navigate(['/feed']);
            },
            error: (err) => {
              console.log(err);
            },
          });
          this._userSettingService.loadUserSettings();
        },
        error: (err) => {
          console.log(err);
          if (err.status === 400 && Array.isArray(err.error)) {
            const message = err.error.map((e: string) => `• ${e}`).join('\n');
            this._massageService.showErrorMessage(message);
          } else if (err.status === 409) {
            this._massageService.showErrorMessage('Username already exists');

            const dialogRef = this.dialog.open(ConfirmDialogComponent, {
              data: { message: `Do you want to log in?` },
            });

            dialogRef.afterClosed().subscribe((result) => {
              if (result === true) {
                this._router.navigate(['/log-in']);
              }
            });
          }
        },
      });
    }
  }
}

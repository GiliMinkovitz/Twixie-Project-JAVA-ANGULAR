import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  constructor(private snackBar: MatSnackBar) {}
  showErrorMessage(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 2000,
      panelClass: ['error-snackbar', 'multiline-snackbar'],
    });
  }

  showSuccessMessage(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 300,
      panelClass: ['success-snackbar', 'multiline-snackbar'],
    });
  }
}

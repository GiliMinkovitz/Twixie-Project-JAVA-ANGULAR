import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserSettingsService } from '../../Service/user-settings.service';
import UserSettings from '../../Model/userSettings.model';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { MenuBar } from "../menu-bar/menu-bar";

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule, FormsModule, MenuBar],
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css'],
})
export class SettingsComponent implements OnInit {
  settings: UserSettings | null = null;
  originalSettings: UserSettings | null = null;
  loading = true;
  saving = false;
  saveSuccess = false;
  saveError = false;
  hasChanges = false;
  private returnUrl: string | null = null;

  constructor(
    private settingsService: UserSettingsService,
    private router: Router,
    private location: Location,
    private dialog: MatDialog
  ) {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras?.state) {
      this.returnUrl = navigation.extras.state['returnUrl'];
    } else {
      this.returnUrl = '/feed';
    }
  }

  ngOnInit(): void {
    this.loadSettings();
  }

  ngOnDestroy(): void {
    // If there are unsaved changes, revert to original settings to preserve user data
    if (this.hasChanges && this.originalSettings) {
      this.settingsService.applySettings(this.originalSettings);
    }
  }

  loadSettings(): void {
    this.loading = true;
    this.settingsService.loadUserSettings().subscribe({
      next: (settings) => {
        this.settings = { ...settings };
        this.originalSettings = { ...settings };
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading settings:', error);
        // Try to load from localStorage as fallback
        this.settings =
          this.settingsService.loadSettingsFromsessionStorage() ||
          this.settingsService.getDefaultSettings();
        this.originalSettings = { ...this.settings };
        this.loading = false;
      },
    });
  }

  onSettingChange(): void {
    // Check if there are changes
    this.hasChanges =
      JSON.stringify(this.settings) !== JSON.stringify(this.originalSettings);

    // Apply changes immediately (preview)
    if (this.settings) {
      // Access private method using bracket notation
      (this.settingsService as any).applySettings(this.settings);
    }

    // Hide previous messages
    this.saveSuccess = false;
    this.saveError = false;
  }

  saveSettings(): void {
    if (!this.settings || !this.hasChanges) return;

    this.saving = true;
    this.saveSuccess = false;
    this.saveError = false;

    this.settingsService.updateUserSettings(this.settings).subscribe({
      next: (updatedSettings) => {
        this.settings = { ...updatedSettings };
        this.originalSettings = { ...updatedSettings };
        this.hasChanges = false;
        this.saving = false;
        this.saveSuccess = true;

        // Hide success message after 3 seconds
        setTimeout(() => {
          this.saveSuccess = false;
          this.navigateBack();
        }, 2000);
      },
      error: (error) => {
        console.error('Error saving settings:', error);
        this.saving = false;
      },
    });
  }

  resetToDefaults(): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: `Are you sure you want to apply the default settings?` },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === true) {
        this.settings = this.settingsService.getDefaultSettings();
        this.hasChanges = true;
        this.onSettingChange();
      }
    });
  }

  private navigateBack(): void {
    if (this.returnUrl) {
      // If a return URL was stored, navigate to that location
      this.router.navigateByUrl(this.returnUrl);
    } else {
      // Otherwise, navigate back to the previous page in browser history
      this.location.back();
    }
  }
}

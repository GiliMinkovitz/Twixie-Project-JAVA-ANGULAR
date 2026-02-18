import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { UserService } from '../../Service/user.service';
import { MessageService } from '../../Service/message-service';
import { userBasicInfo } from '../../Model/users.model';
import { UserSettingsService } from '../../Service/user-settings.service';
import UserSettings from '../../Model/userSettings.model';

@Component({
  selector: 'app-menu-bar',
  imports: [RouterModule],
  templateUrl: './menu-bar.html',
  styleUrl: './menu-bar.css',
})
export class MenuBar implements OnInit {
  public darkModeEnabled = false;

  constructor(
    private _userSercive: UserService,
    private _messageService: MessageService,
    private router: Router,
    private _userService: UserService,
    private _settingsService: UserSettingsService
  ) {}

  public currentUser!: userBasicInfo;

  ngOnInit(): void {
    this.getCurrentUserInfo();
    this._settingsService.settings$.subscribe((s) => {
      if (s) this.darkModeEnabled = !!s.darkModeEnabled;
    });
  }

  signOut() {
    this._userSercive.signOut().subscribe({
      next: (res) => {
        this._messageService.showSuccessMessage('Signed out successfully');
      },
      error: (err) => {
        this._messageService.showErrorMessage('Signing out failed');
      },
    });
    localStorage.removeItem('userSettings');
    this.router.navigate(['/landing-page']);
  }

  toggleDarkMode(): void {
    const current = this._settingsService.getCurrentSettings() ||
      this._settingsService.getDefaultSettings();
    const updated: UserSettings = { ...current, darkModeEnabled: !current.darkModeEnabled };
    // apply locally (this also persists to localStorage inside the service)
    this._settingsService.applySettings(updated);
    // update subject so subscribers see change
    (this._settingsService as any).settingsSubject?.next?.(updated);
  }

  goToSettings() {
    this.router.navigate(['/settings'], {
      state: { returnUrl: this.router.url },
    });
  }

  getCurrentUserInfo() {
    this._userSercive.getBasicInfoCurrentUser().subscribe({
      next: (res) => {
        this.currentUser = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}

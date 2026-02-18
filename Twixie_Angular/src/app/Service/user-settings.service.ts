import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import UserSettings from '../Model/userSettings.model';

@Injectable({
  providedIn: 'root',
})
export class UserSettingsService {
  // BehaviorSubject לשמירת ההגדרות הנוכחיות
  private settingsSubject = new BehaviorSubject<UserSettings | null>(null);
  public settings$ = this.settingsSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadUserSettings(): Observable<UserSettings> {
    return this.http
      .get<UserSettings>(`http://localhost:8080/api/settings/getUserSetting`)
      .pipe(
        tap((settings) => {
          this.settingsSubject.next(settings);
          this.applySettings(settings);
        })
      );
  }

  updateUserSettings(settings: UserSettings): Observable<UserSettings> {
    return this.http
      .put<UserSettings>(
        `http://localhost:8080/api/settings/updateSettings`,
        settings
      )
      .pipe(
        tap((updatedSettings) => {
          this.settingsSubject.next(updatedSettings);
          this.applySettings(updatedSettings);
        })
      );
  }

  getCurrentSettings(): UserSettings | null {
    return this.settingsSubject.value;
  }

  applySettings(settings: UserSettings): void {
    const root = document.documentElement;

    if (settings.darkModeEnabled) {
      root.classList.add('dark-mode');
    } else {
      root.classList.remove('dark-mode');
    }

    root.style.setProperty('--app-font-size', settings.fontSize);
    // Persist settings so the theme can be applied immediately on next page load
    try {
      localStorage.setItem('userSettings', JSON.stringify(settings));
    } catch (e) {
      console.warn('Could not persist user settings to localStorage.', e);
    }
  }

  loadSettingsFromsessionStorage(): UserSettings | null {
    // Backwards-compatible name: read from localStorage so setting persists across sessions
    const stored = localStorage.getItem('userSettings');
    if (stored) {
      try {
        const settings = JSON.parse(stored) as UserSettings;
        this.settingsSubject.next(settings);
        this.applySettings(settings);
        return settings;
      } catch (error) {
        console.error('Error parsing settings from localStorage:', error);
        return null;
      }
    }
    return null;
  }

  /**
   *Aplly default settings
   */
  getDefaultSettings(): UserSettings {
    return {
      darkModeEnabled: false,
      fontSize: 'medium',
      autocompleteEnabled: true,
    };
  }
}

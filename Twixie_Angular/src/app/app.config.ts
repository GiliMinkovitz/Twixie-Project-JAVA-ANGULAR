import {
  ApplicationConfig,
  provideZoneChangeDetection,
  APP_INITIALIZER,
} from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { credentialsInterceptor } from './Service/credentials.interceptor';
import { UserSettingsService } from './Service/user-settings.service';
import { firstValueFrom, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

export function initializeApp(settingsService: UserSettingsService) {
  return () => {
    return (async () => {
      // 1) Try to load from localStorage (fast)
      const local = settingsService.loadSettingsFromsessionStorage();
      if (local) return;

      // 2) Otherwise try to load from backend (DB)
      try {
        await firstValueFrom(
          settingsService.loadUserSettings().pipe(catchError(() => of(null)))
        );
      } catch (e) {
        // Ignore and apply defaults below
      }

      // 3) If still no settings, apply defaults
      if (!settingsService.getCurrentSettings()) {
        const defaultSettings = settingsService.getDefaultSettings();
        (settingsService as any).applySettings(defaultSettings);
      }
    })();
  };
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([credentialsInterceptor])),

    // ← הוספה: טען הגדרות לפני אתחול האפליקציה
    {
      provide: APP_INITIALIZER,
      useFactory: initializeApp,
      deps: [UserSettingsService],
      multi: true,
    },
  ],
};

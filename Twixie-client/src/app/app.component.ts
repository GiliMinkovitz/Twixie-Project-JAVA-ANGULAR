import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FeedComponent } from './Components/feed/feed.component';
import { MenuBar } from "./Components/menu-bar/menu-bar";
import { UserSettingsService } from './Service/user-settings.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MenuBar],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'Twixie_Angular';

  constructor(private settingsService: UserSettingsService) {}

  ngOnInit(): void {
    // Apply persisted settings immediately if available; otherwise try to load from server
    const settings = this.settingsService.loadSettingsFromsessionStorage();
    if (!settings) {
      this.settingsService.loadUserSettings().subscribe({
        next: () => {},
        error: () => {},
      });
    }
  }
}

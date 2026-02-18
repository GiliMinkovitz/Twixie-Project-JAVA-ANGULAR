import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UserSettingsService } from '../../Service/user-settings.service';
@Component({
  selector: 'app-landing-page',
  imports: [RouterModule],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css'
})
export class LandingPageComponent {

  constructor(private _userSettingsService: UserSettingsService){

  }
  ngOnInit(): void {
    const defaultSettings = this._userSettingsService.getDefaultSettings()
    this._userSettingsService.applySettings(defaultSettings)
  }

}

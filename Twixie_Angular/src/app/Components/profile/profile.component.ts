import { Component, OnInit, HostBinding } from '@angular/core';
import { UserService } from '../../Service/user.service';
import Users from '../../Model/users.model';
import { FollowService } from '../../Service/follow.service';
import { UserSettingsService } from '../../Service/user-settings.service';
import { Router, RouterModule } from '@angular/router';
import { MenuBar } from "../menu-bar/menu-bar";


@Component({
  selector: 'app-profile',
  imports: [RouterModule, MenuBar],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {

  public user!: Users
  @HostBinding('class.dark-mode') darkMode: boolean = false;

  constructor(
    private _userService: UserService,
    private _settingService: UserSettingsService,
    private router:Router
  ) { }

  ngOnInit(): void {
    this._settingService.loadUserSettings().subscribe();
    this._settingService.settings$.subscribe((settings) => {
      if (settings) {
        this.darkMode = settings.darkModeEnabled;
      }
    });
    this._userService.getUserDetails().subscribe({
      next: (res) => {

        this.user=res
        console.log(res);

      }, error: (err) => {
        console.log(err);
      }
    })
  }

  viewFollows(id: number){
    this.router.navigate(['follow',id])
  }

}


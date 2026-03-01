import { Component, OnInit, HostBinding } from '@angular/core';
import { FollowService } from '../../Service/follow.service';
import Users from '../../Model/users.model';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { MessageService } from '../../Service/message-service';
import { UserSettingsService } from '../../Service/user-settings.service';
import { UserDetailsComponent } from '../user-details/user-details.component';
import { MenuBar } from "../menu-bar/menu-bar";

@Component({
  selector: 'app-followers',
  imports: [CommonModule, HttpClientModule, RouterModule, UserDetailsComponent, MenuBar],
  templateUrl: './follow.component.html',
  styleUrl: './follow.component.css',
})
export class FollowersComponent implements OnInit {
  public follow!: Users[];
  public id!: number;
  @HostBinding('class.dark-mode') darkMode: boolean = false;

  constructor(
    private _followService: FollowService,
    private route: ActivatedRoute,
    private _messageService: MessageService,
    private _settingService: UserSettingsService
  ) {}

  ngOnInit(): void {
    this._settingService.loadUserSettings().subscribe();
    this._settingService.settings$.subscribe((settings) => {
      if (settings) {
        this.darkMode = settings.darkModeEnabled;
      }
    });
    this.route.params.subscribe((params) => {
      this.id = +params['id'];
      if (this.id == 1) {
        this.getFollowers();
      } else if (this.id == 2) {
        this.getFollowees();
      }
    });
  }

  getFollowers() {
    this._followService.getFollowers().subscribe({
      next: (res) => {
        this.follow = res;
        console.log(res);
      },
      error: (err) => {
        if (err.status === 404) {
          this._messageService.showErrorMessage('Ooops... no followers');
        }
        console.log(err);
      },
    });
  }

  getFollowees() {
    this._followService.getFollowees().subscribe({
      next: (res) => {
        this.follow = res;
        console.log(res);
      },
      error: (err) => {
        if (err.status === 404) {
          this._messageService.showErrorMessage('No followees');
        }
        console.log(err);
      },
    });
  }

  removeFollow(followId: number) {
    this._followService.removeFollow(followId).subscribe({
      next: (res) => {
        this._messageService.showSuccessMessage('Follow removed successfully')
        this.getFollowees();
        console.log(res);
      },
      error: (err) => {
        this._messageService.showErrorMessage("Fails removing follow")
        console.log(err);
      },
    });
  }
}

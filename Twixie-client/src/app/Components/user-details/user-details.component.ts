import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import Users from '../../Model/users.model';
import { FollowService } from '../../Service/follow.service';
import { MatDialog } from '@angular/material/dialog';
import { MessageService } from '../../Service/message-service';

@Component({
  selector: 'app-user-details',
  imports: [RouterModule],
  templateUrl: './user-details.component.html',
  styleUrl: './user-details.component.css',
})
export class UserDetailsComponent implements OnInit {
  @Input()
  user!: Users;
  isFollowing!: boolean;

  constructor(
    private _followService: FollowService,
    private _messageService: MessageService
  ) {}

  ngOnInit(): void {
    this._followService.isFollowing(this.user.userId).subscribe({
      next: (res) => {
        this.isFollowing = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  removeFollow() {
    return this._followService.removeFollow(this.user.userId).subscribe({
      next: (res) => {
        this.isFollowing = false;
        this._messageService.showSuccessMessage('Follow removed successfully');
      },
      error: (err) => {
        console.log(err);
        this._messageService.showErrorMessage('Fails removing follow');
      },
    });
  }

  addFollow() {
    return this._followService.addFollow(this.user.userId).subscribe({
      next: (res) => {
        this.isFollowing = true;
        this._messageService.showSuccessMessage("You're now following!")
      },
      error: (err) => {
        if(err.status === 409){
          this._messageService.showErrorMessage("You're already following")
        }
        console.log(err);
      },
    });
  }
}

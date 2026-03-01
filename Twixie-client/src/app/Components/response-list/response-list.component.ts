import { Component, HostListener, Input, OnInit, HostBinding } from '@angular/core';
import { ResponseService } from '../../Service/response.service';
import { ActivatedRoute, Router, RouterLinkActive } from '@angular/router';
import { UserSettingsService } from '../../Service/user-settings.service';
import { ResponseItemComponent } from '../response-item/response-item.component';
import Response from '../../Model/response.model';
import { MatDialog } from '@angular/material/dialog';
import { AddResponseComponent } from '../add-response/add-response.component';

@Component({
  selector: 'app-response-list',
  imports: [ResponseItemComponent],
  templateUrl: './response-list.component.html',
  styleUrl: './response-list.component.css',
})
export class ResponseListComponent implements OnInit {
  public responseList!: Response[]
  @Input() postId!: number;
  @HostBinding('class.dark-mode') darkMode: boolean = false;

  constructor(
    private _responseService: ResponseService,
    private router: Router,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private _settingService: UserSettingsService
  ) { }

  ngOnInit(): void {
    this._settingService.loadUserSettings().subscribe();
    this._settingService.settings$.subscribe((settings) => {
      if (settings) {
        this.darkMode = settings.darkModeEnabled;
      }
    });
    this.getResponseList()
  }

  getResponseList() {
    this._responseService.getResponseList(this.postId).subscribe({
      next: (res) => {
        this.responseList = res;
        console.log(res);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  handleDeletedResponse(responseId: number) {
    this.getResponseList()
  }

  deletedSomeResponse() {
    this.getResponseList()
  }

  addResponse() {
    const dialogRef = this.dialog.open(AddResponseComponent, {
      data: { postId: this.postId, parentResponseId: 0 },
    });

    dialogRef.afterClosed().subscribe((newResponse) => {
      if (newResponse) {
        this.responseList.push(newResponse);
      }
    });
  }

}

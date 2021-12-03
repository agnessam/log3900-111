import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { MatSlideToggleChange } from "@angular/material/slide-toggle";
import { EditableUserParameters } from "src/app/modules/users/models/editable-user-parameters";
import { User } from "src/app/modules/users/models/user";
import { UsersService } from "src/app/modules/users/services/users.service";

@Component({
  selector: "app-user-privacy",
  templateUrl: "./user-privacy.component.html",
  styleUrls: ["./user-privacy.component.scss"],
})
export class UserPrivacyComponent implements OnInit {
  user: User;

  userLoaded: Promise<boolean>;

  constructor(
    private usersService: UsersService,
    private changeDetectorRef: ChangeDetectorRef
  ) {
    this.usersService
      .getUser(localStorage.getItem("userId")!)
      .subscribe((user) => {
        this.user = user;
        this.userLoaded = Promise.resolve(true);
      });
  }

  ngOnInit(): void {}

  toggleSearchableByEmail(event: MatSlideToggleChange) {
    const editableUserParameters = new EditableUserParameters({
      privacySetting: {
        searchableByEmail: event.checked,
        searchableByFirstName: this.user.privacySetting.searchableByFirstName,
        searchableByLastName: this.user.privacySetting.searchableByLastName,
      },
    });
    this.usersService
      .updateUser(this.user._id!, editableUserParameters)
      .subscribe((user) => {
        this.user = user;
        this.changeDetectorRef.detectChanges();
      });
  }

  toggleSearchableByFirstName(event: MatSlideToggleChange) {
    const editableUserParameters = new EditableUserParameters({
      privacySetting: {
        searchableByEmail: this.user.privacySetting.searchableByEmail,
        searchableByFirstName: event.checked,
        searchableByLastName: this.user.privacySetting.searchableByLastName,
      },
    });
    this.usersService
      .updateUser(this.user._id!, editableUserParameters)
      .subscribe((user) => {
        this.user = user;
        this.changeDetectorRef.detectChanges();
      });
  }

  toggleSearchableByLastName(event: MatSlideToggleChange) {
    const editableUserParameters = new EditableUserParameters({
      privacySetting: {
        searchableByEmail: this.user.privacySetting.searchableByEmail,
        searchableByFirstName: this.user.privacySetting.searchableByFirstName,
        searchableByLastName: event.checked,
      },
    });
    this.usersService
      .updateUser(this.user._id!, editableUserParameters)
      .subscribe((user) => {
        this.user = user;
        this.changeDetectorRef.detectChanges();
      });
  }
}

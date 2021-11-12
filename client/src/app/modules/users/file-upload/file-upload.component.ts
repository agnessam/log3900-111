import { Component, OnInit } from "@angular/core";
import { UsersService } from "../services/users.service";

@Component({
  selector: "app-file-upload",
  templateUrl: "./file-upload.component.html",
  styleUrls: ["./file-upload.component.scss"],
})
export class FileUploadComponent implements OnInit {
  fileName = "";
  formData: FormData | null;

  constructor(private usersService: UsersService) {}

  ngOnInit(): void {}

  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    if (file) {
      this.fileName = file.name;
      this.formData = new FormData();
      this.formData.append("avatar", file);
    }
  }

  upload() {
    if (this.formData) {
      this.usersService
        .uploadAvatar(localStorage.getItem("userId")!, this.formData)
        .subscribe((response) => {
          console.log(response);
          this.formData = null;
          this.fileName = "";
          console.log("LOL");
        });
    }
  }
}

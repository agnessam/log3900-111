import { Component, OnInit } from "@angular/core";
import { AvatarClientService } from "../../backend-communication/avatar-client/avatar-client.service";

@Component({
  selector: "app-file-upload",
  templateUrl: "./file-upload.component.html",
  styleUrls: ["./file-upload.component.scss"],
})
export class FileUploadComponent implements OnInit {
  fileName = "";
  formData: FormData | null;

  constructor(private avatarClient: AvatarClientService) {}

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
      this.avatarClient.uploadAvatar(this.formData).subscribe((response) => {
        console.log(response);
        this.formData = null;
        this.fileName = "";
      });
    }
  }
}

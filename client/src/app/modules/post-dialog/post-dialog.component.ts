import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PostInterface } from '../museum/models/post.model';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-post-dialog',
  templateUrl: './post-dialog.component.html',
  styleUrls: ['./post-dialog.component.scss']
})
export class PostDialogComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<PostDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public post: PostInterface,
    private sanitizer: DomSanitizer,
    ) { }

  ngOnInit(): void {
    this.dialogRef.backdropClick().subscribe(_ => {
      this.dialogRef.close(this.post);
    })
  }

  transformUri(){
    let safeDataUri = this.sanitizer.bypassSecurityTrustUrl(this.post.dataUri);
    return safeDataUri;
  }

  addComment():void{

  }

}

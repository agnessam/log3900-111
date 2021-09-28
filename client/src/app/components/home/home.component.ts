import { HttpClient } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"],
})
export class HomeComponent implements OnInit {
  constructor(private httpClient: HttpClient) {}

  ngOnInit(): void {
    this.HelloWorld();
  }

  public HelloWorld() {
    this.httpClient
      .get("http://localhost:3000/api/v1/hello")
      .subscribe((data) => console.log(data));
  }
}

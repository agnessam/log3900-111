import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MaterialModules } from "../../app-material.module";
import { ParameterMenuComponent } from "./parameter-menu.component";

@NgModule({
  declarations: [ParameterMenuComponent],
  imports: [CommonModule, MaterialModules],
})
export class ParameterMenuModule {}

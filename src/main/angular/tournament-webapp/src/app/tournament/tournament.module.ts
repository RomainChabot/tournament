import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TournamentComponent} from "./tournament.component";
import {TableModule} from "primeng/table";
import {RippleModule} from "primeng/ripple";
import {ButtonModule} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {FormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {ToolbarModule} from "primeng/toolbar";
import {ProgressBarModule} from "primeng/progressbar";


@NgModule({
  declarations: [
    TournamentComponent
  ],
  imports: [
    CommonModule,
    TableModule,
    RippleModule,
    ButtonModule,
    DialogModule,
    FormsModule,
    InputTextModule,
    ToolbarModule,
    ProgressBarModule
  ]
})
export class TournamentModule {
}

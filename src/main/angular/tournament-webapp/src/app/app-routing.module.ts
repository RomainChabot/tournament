import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TournamentComponent} from "./tournament/tournament.component";
import {HomeComponent} from "./home/home.component";

const routes: Routes = [
  {
    path: "",
    redirectTo: "home",
    pathMatch: "full"
  },
  {
    path: "home",
    component: HomeComponent
  },
  {
    path: "tournament/:id",
    component: TournamentComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

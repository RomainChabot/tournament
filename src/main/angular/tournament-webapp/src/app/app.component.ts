import {Component, OnInit} from '@angular/core';
import {TournamentService} from "./shared/service/tournament.service";
import {MenuItem, MessageService} from "primeng/api";
import {NavigationEnd, Router} from "@angular/router";
import {filter} from "rxjs/operators";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'tournament-webapp';
  items: MenuItem[] = [];
  home: MenuItem = {icon: 'pi pi-home', routerLink: '/'};

  constructor(private tournamentService: TournamentService,
              private messageService: MessageService,
              private router: Router) {
  }

  ngOnInit() {
    this.items = [];

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => this.items = this.createBreadcrumbs());
  }

  private createBreadcrumbs(): MenuItem[] {
    if (this.router.url.includes("/tournament")) {
      return [
        {label: 'Tournament'},
      ]
    } else {
      return []
    }
  }

}

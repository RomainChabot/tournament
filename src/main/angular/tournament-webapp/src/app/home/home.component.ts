import {Component, OnInit} from '@angular/core';
import {TournamentService} from "../shared/service/tournament.service";
import {Tournament} from "../shared/model/tournament.model";
import {ConfirmationService, LazyLoadEvent, MessageService} from "primeng/api";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {

  tournaments: Array<Tournament> = [];
  tournament: Tournament = new Tournament();
  tournamentDialog: boolean = false;

  loading: boolean = false
  totalRecords: number = 0;
  visible: boolean = true

  constructor(private tournamentService: TournamentService,
              private messageService: MessageService,
              private router: Router,
              private confirmationService: ConfirmationService) {
  }

  ngOnInit(): void {
    this.loading = true;
  }

  openNew() {
    this.tournament = new Tournament();
    this.tournamentDialog = true;
  }

  createTournament(): void {
    this.tournamentService.create(this.tournament.name)
      .subscribe(
        data => this.handleSuccesOnTournamentCreate(data),
        error => this.handleErrorOnTournamentCreate(error))
  }

  hideDialog() {
    this.tournamentDialog = false;
  }

  selectTournament(): void {
    this.router.navigate([`/tournament/${this.tournament._id}`])
  }

  deleteTournament(tournament: Tournament) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete the selected tournament ?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.tournamentService.delete(tournament._id).subscribe(() => this.handleSuccesOnTournamentDelete(tournament))
      }
    });
  }

  onLazyLoad(event: LazyLoadEvent) {
    this.loading = true
    const first = event.first ? event.first : 0
    const rows = event.rows ? event.rows : 0
    const nextPageNumber = first / rows
    setTimeout(() => {
      this.tournamentService.findAll(nextPageNumber, rows)
        .subscribe(data => {
          this.tournaments = data.content
          this.totalRecords = data.totalRecords
          this.loading = false
        })
    }, 500)

  }

  private handleSuccesOnTournamentDelete(tournament: Tournament) {
    this.messageService.add({
      severity: 'success',
      summary: 'Tournament successfully deleted',
      detail: `Tournament ${tournament.name} deleted`
    })
    this.tournamentDialog = false
    this.refreshTable();

  }

  private handleSuccesOnTournamentCreate(tournament: Tournament) {

    this.messageService.add({
      severity: 'success',
      summary: 'Tournament successfully created',
      detail: `Tournament ${tournament.name} created`
    })
    this.tournamentDialog = false
    this.refreshTable();
  }

  private handleErrorOnTournamentCreate(error: any) {
    this.messageService.add({
      severity: 'error',
      summary: 'Tournament creation failed',
      detail: error.error
    })
    this.tournamentDialog = false
  }

  private refreshTable() {
    this.visible = false
    setTimeout(() => (this.visible = true), 0)
  }
}

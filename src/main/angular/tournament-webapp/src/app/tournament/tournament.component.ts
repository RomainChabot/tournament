import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {TournamentService} from "../shared/service/tournament.service";
import {Tournament} from "../shared/model/tournament.model";
import {Player} from "../shared/model/player.model";
import {ConfirmationService, MessageService} from "primeng/api";

@Component({
  selector: 'app-tournament',
  templateUrl: './tournament.component.html',
  styleUrls: ['./tournament.component.scss']
})
export class TournamentComponent implements OnInit {

  tournamentId: string = ""
  tournament: Tournament = new Tournament()
  updatePlayerScoreDialog: boolean = false
  registerPlayerDialog: boolean = false

  player: Player = new Player()

  constructor(private tournamentService: TournamentService,
              private route: ActivatedRoute,
              private messageService: MessageService,
              private confirmationService: ConfirmationService) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(params => {
      this.tournamentId = params['id'];

      this.tournamentService.find(this.tournamentId).subscribe(
        data => this.tournament = data
      )
    });
  }

  hideUpdatePlayerScoreDialog() {
    this.updatePlayerScoreDialog = false;
  }

  editPlayer(player: Player) {
    this.player = {...player};
    this.updatePlayerScoreDialog = true;
  }

  updatePlayerScore() {
    this.tournamentService.updatePlayerScore(this.tournamentId, this.player).subscribe(
      tournament => this.handleSuccesUpdatePlayerScore(tournament),
      error => this.handleErrorUpdatePlayerScore(error)
    )
  }

  openRegisterPlayerDialog() {
    this.registerPlayerDialog = true;
    this.player = new Player()
  }

  registerPlayer() {
    this.tournamentService.registerPlayer(this.tournamentId, this.player).subscribe(
      player => this.handleSuccesRegisterPlayer(player),
      error => this.handleErrorRegisterPlayer(error)
    )
  }

  hideRegisterPlayerDialog() {
    this.registerPlayerDialog = false;
  }

  deletePlayers() {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete all players in ' + this.tournament.name + ' ?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.tournamentService.deletePlayers(this.tournament._id).subscribe((data) => {
          this.tournament = data
        })
      }
    });
  }

  private handleSuccesUpdatePlayerScore(tournament: Tournament) {
    this.tournament = tournament
    this.updatePlayerScoreDialog = false
    this.messageService.add({
      severity: 'success',
      summary: 'Player score updated',
      detail: `Player score is now ${this.player.score}`
    })
  }

  private handleErrorUpdatePlayerScore(error: any) {
    this.messageService.add({
      severity: 'error',
      summary: 'Player score update failed',
      detail: error.error
    })
    this.updatePlayerScoreDialog = false
  }

  private handleSuccesRegisterPlayer(tournament: Tournament) {
    this.tournament = tournament
    this.registerPlayerDialog = false
    this.messageService.add({
      severity: 'success',
      summary: 'Player registration succes',
      detail: `Player ${this.player.playerName} was successfully registered`
    })
  }

  private handleErrorRegisterPlayer(error: any) {
    this.messageService.add({
      severity: 'error',
      summary: 'Player registration failed',
      detail: error.error
    })
    this.registerPlayerDialog = false
  }
}

import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Tournament} from "../model/tournament.model";
import {Player} from "../model/player.model";
import {Page} from "../model/page.model";

@Injectable({
  providedIn: 'root'
})
export class TournamentService {

  baseUrl = "/api"

  constructor(private http: HttpClient) {
  }

  findAll(page: number, size: number): Observable<Page<Tournament>> {
    const params = new HttpParams()
      .set("page", page)
      .set("size", size)
    return this.http.get<Page<Tournament>>(`${this.baseUrl}/tournament`, {params: params})
  }

  find(id: string): Observable<Tournament> {
    return this.http.get<Tournament>(`${this.baseUrl}/tournament/${id}`)
  }

  getLeaderboard(id: string): Observable<Array<Player>> {
    return this.http.get<Array<Player>>(`${this.baseUrl}/tournament/${id}/leaderboard`)
  }

  create(name: string): Observable<Tournament> {
    const params = new HttpParams()
      .set("name", name)
    return this.http.post<Tournament>(
      `${this.baseUrl}/tournament`,
      {},
      {params: params}
    )
  }

  delete(tournamentId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/tournament/${tournamentId}`)
  }

  registerPlayer(tournamentId: string, player: Player): Observable<Array<Player>> {
    const params = new HttpParams()
      .set("playerName", player.playerName)
    return this.http.post<Array<Player>>(
      `${this.baseUrl}/tournament/${tournamentId}/players`,
      {},
      {params: params}
    )
  }

  updatePlayerScore(tournamentId: string, player: Player): Observable<Array<Player>> {
    const params = new HttpParams()
      .set("score", player.score)
    return this.http.put<Array<Player>>(
      `${this.baseUrl}/tournament/${tournamentId}/players/${player.playerName}/scores`,
      {},
      {params: params}
    )
  }

  deletePlayers(tournamentId: string): Observable<Tournament> {
    return this.http.delete<Tournament>(`${this.baseUrl}/tournament/${tournamentId}/players`)
  }
}

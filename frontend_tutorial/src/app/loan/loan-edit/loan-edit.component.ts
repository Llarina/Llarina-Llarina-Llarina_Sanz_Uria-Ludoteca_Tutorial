import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { LoanService } from '../loan.service';
import { Loan } from '../model/Loan';
import { Client } from 'src/app/client/model/Client';
import { Game } from 'src/app/game/model/Game';
import { GameService } from 'src/app/game/game.service';
import { ClientService } from 'src/app/client/client.service';

@Component({
  selector: 'app-loan-edit',
  templateUrl: './loan-edit.component.html',
  styleUrls: ['./loan-edit.component.scss']
})
export class LoanEditComponent implements OnInit {
  filterLoan: { gameName: string; clientName: string } = { gameName: '', clientName: '' };;
  loans: any;
  loan: Loan;
  clients: Client[] = [];
  games: Game[] = [];

  constructor(
    public dialogRef: MatDialogRef<LoanEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private loanService: LoanService,
    private gameService: GameService,
    private clientService: ClientService,
  ) { }

  ngOnInit(): void {
    if (this.data.loan != null) {
      this.loan = Object.assign({}, this.data.loan);
    }
    else {
      this.loan = new Loan();
    }

    this.gameService.getGames().subscribe(
      games => { this.games = games }
    );

    this.clientService.getClients().subscribe(
      clients => { this.clients = clients }

    );

  }



  onSave() {
    console.log(this.loan);
    this.loanService.saveLoan(this.loan).subscribe(result => {
      this.dialogRef.close();
    });    
  }  

  onClose() {
    this.dialogRef.close();
  }

}
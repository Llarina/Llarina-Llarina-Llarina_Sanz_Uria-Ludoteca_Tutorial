import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Loan } from '../model/Loan';
import { LoanService } from '../loan.service';
import { MatDialog } from '@angular/material/dialog';
import { LoanEditComponent } from '../loan-edit/loan-edit.component';
import { DialogConfirmationComponent } from 'src/app/core/dialog-confirmation/dialog-confirmation.component';
import { PageEvent } from '@angular/material/paginator';
import { Client } from 'src/app/client/model/Client';
import { ClientService } from 'src/app/client/client.service';
import { Game } from 'src/app/game/model/Game';
import { GameService } from 'src/app/game/game.service';

@Component({
  selector: 'app-loan-list',
  templateUrl: './loan-list.component.html',
  styleUrls: ['./loan-list.component.scss']
})
export class LoanListComponent implements OnInit {

  dataSource = new MatTableDataSource<Loan>();
  displayedColumns: string[] = ['id', 'gameName', 'clientName', 'loanDate', 'returnDate', 'action'];
  loans: any;
  filterGameName: string = null;
  filterClientName: string  = null;
  filterLoanDate: Date = null;
  pageNumber: number = 0;
  pageSize: number = 5;
  totalElements: number = 0;
  filterLoan: Loan;
  clients: Client[]=[];
  games: Game[]=[];


  constructor(
    private loanService: LoanService,
    public dialog: MatDialog,
    private clientService : ClientService,
    private gameService : GameService,
  ) { }

  ngOnInit(): void {
    this.loadPage();

    console.log("INICIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");

    this.clientService.getClients().subscribe(
      clients => { this.clients = clients }
    );

    this.gameService.getGames().subscribe(
      games => { this.games = games }
    );
  }

  onCleanFilter(): void {
    this.filterGameName = null;
    this.filterClientName = null;
    this.filterLoanDate = null;


    this.onSearch();
  }

  onSearch(): void {
    this.loadPage();

    console.log("On Search");

    this.clientService.getClients().subscribe(
      clients => { this.clients = clients }
    );

    this.gameService.getGames().subscribe(
      games => { this.games = games }
    );
  
  }

  createLoan() {
    const dialogRef = this.dialog.open(LoanEditComponent, {
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.ngOnInit();
    });
  }
  deleteLoan(loan: Loan) {
    const dialogRef = this.dialog.open(DialogConfirmationComponent, {
      data: { title: "Eliminar préstamo", description: "Atención si borra el préstamo se perderán sus datos.<br> ¿Desea eliminar el préstamo?" }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loanService.deleteLoan(loan.id).subscribe(result => {
          this.ngOnInit();
        });
      } else {
        console.log(loan.id);
      }
    });
  }

  loadPage(event?: PageEvent) {
    if (event) {
      this.pageSize = event.pageSize;
      this.pageNumber = event.pageIndex;
    }

    
    
    const params: { [key: string]: any } = {};

    if (this.filterGameName) {
     params.gameName = this.filterGameName;
    }
    if (this.filterClientName) {
    params.clientName = this.filterClientName;
    }
    if (this.filterLoanDate) {
     params.loanDate = this.filterLoanDate instanceof Date ? this.filterLoanDate : new Date(this.filterLoanDate);
    }
    params.page = this.pageNumber;
    params.size = this.pageSize;
    params.sort = 'id,asc';




    this.loanService.getLoans(params)
    .subscribe(data => {

      console.log(data+", "+data.content+"              ----------------       --------")

      if (data && data.content) {
        this.dataSource.data = data.content; // Asignar los datos a la fuente de la tabla
        this.pageNumber = data.pageable.pageNumber;
        this.pageSize = data.pageable.pageSize;
        this.totalElements = data.totalElements;
      } else {
        console.log('No se han encontrado datos');
      }
    },
    error => {
      console.error('Error al cargar los datos', error);
    });

  }
}
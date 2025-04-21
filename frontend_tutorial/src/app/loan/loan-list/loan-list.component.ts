import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Loan } from '../model/Loan';
import { LoanService } from '../loan.service';
import { MatDialog } from '@angular/material/dialog';
import { LoanEditComponent } from '../loan-edit/loan-edit.component';
import { DialogConfirmationComponent } from 'src/app/core/dialog-confirmation/dialog-confirmation.component';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-loan-list',
  templateUrl: './loan-list.component.html',
  styleUrls: ['./loan-list.component.scss']
})
export class LoanListComponent implements OnInit {

  dataSource = new MatTableDataSource<Loan>();
  displayedColumns: string[] = ['id', 'gameName', 'clientName', 'loanDate', 'returnDate', 'action'];
  loans: any;
  filterGameName: string;
  filterClientName: string;
  filterLoanDate: Date;
  pageNumber: number = 0;
  pageSize: number = 5;
  totalElements: number = 0;

  constructor(
    private loanService: LoanService,
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.loadPage();
  }

  onCleanFilter(): void {
    this.filterGameName = null;
    this.filterClientName = null;
    this.filterLoanDate = null;


    this.onSearch();
  }

  onSearch(): void {

    let gameName = this.filterGameName;
    let clientName = this.filterClientName;
    let loanDate = this.filterLoanDate;


    this.loanService.getLoans({
      gameName: this.filterGameName,
      clientName: this.filterClientName,
      loanDate: this.filterLoanDate,
      pageNumber: this.pageNumber,
      pageSize: this.pageSize,
      sort: 'id,asc' // o el criterio de orden que prefieras
    }).subscribe(
      loans => this.loans = loans
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

    this.loanService.getLoans({
      gameName: this.filterGameName,
      clientName: this.filterClientName,
      loanDate: this.filterLoanDate,
      pageNumber: this.pageNumber,
      pageSize: this.pageSize,
      sort: 'id,asc'
    }).subscribe(data => {
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
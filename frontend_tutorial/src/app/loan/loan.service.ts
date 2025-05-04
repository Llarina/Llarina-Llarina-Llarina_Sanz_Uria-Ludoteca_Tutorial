import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { Loan } from './model/Loan';
import { LoanPage } from './model/LoanPage';



@Injectable({
  providedIn: 'root'
})
export class LoanService {

  constructor(private http: HttpClient) { }

  getLoans(params): Observable<LoanPage> {
    return this.http.get<LoanPage>('http://localhost:8080/loan', {
      params
    });
  }

  saveLoan(loan: Loan): Observable<Loan> {
    let url = 'http://localhost:8080/loan';
    if (loan.id != null) url += '/' + loan.id;

    return this.http.put<Loan>(url, loan);
  }


  deleteLoan(idLoan: number): Observable<any> {
    return this.http.delete('http://localhost:8080/loan/' + idLoan);
  }
  private composeFindUrl(gameName?: String, clientName?: String, loanDate?: Date): string {
    let params = '';

    if (gameName != null) {
      params += 'gameName=' + gameName;
    }

    if (clientName != null) {
      params += 'clientName=' + clientName;
    }

    if (loanDate != null) {
      params += 'loanDate=' + loanDate;
    }

    let url = 'http://localhost:8080/loan'

    if (params == '') return url;
    else return url + '?' + params;
  }


  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      console.error('Error del lado del cliente:', error.error);
    } else {
      console.error(`Error del servidor (estado ${error.status}):`, error.error);
    }
    return throwError('Algo salió mal; por favor, intenta nuevamente más tarde.');
  }


}
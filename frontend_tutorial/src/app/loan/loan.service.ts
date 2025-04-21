import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Loan } from './model/Loan';
import { Pageable } from '../core/model/page/Pageable';
import { LoanPage } from './model/LoanPage';
import {LoanSearchParams} from './LoanSearchParams';

@Injectable({
  providedIn: 'root'
})
export class LoanService {

  constructor(private http: HttpClient) { }

  getLoans(params: LoanSearchParams): Observable<LoanPage> {
    const httpParams = {
      gameName: params.gameName,
      clientName: params.clientName,
      loanDate: params.loanDate ? params.loanDate.toISOString().split('T')[0] : '2020-01-01',
      page: params.pageNumber?.toString(),
      size: params.pageSize?.toString(),
      sort: params.sort
    };
    console.log("http://localhost:8080/loan"+httpParams);
    return this.http.get<LoanPage>('http://localhost:8080/loan', {
      params: httpParams
    });
  }

  saveLoan(loan: Loan): Observable<Loan> {
    let url = 'http://localhost:8080/loan';
        if (loan.id != null) url += '/'+loan.id;

        return this.http.put<Loan>(url, loan);
  }


  deleteLoan(idLoan : number): Observable<any> {
    return this.http.delete('http://localhost:8080/loan/'+idLoan);
  }  
  private composeFindUrl(gameName?: String, clientName?: String, loanDate?: Date) : string {
    let params = '';

    if (gameName != null) {
        params += 'gameName='+gameName;
    }

    if (clientName != null) {
      params += 'clientName='+clientName;
    }

    if (loanDate != null) {
      params += 'loanDate='+loanDate;
  }

    let url = 'http://localhost:8080/loan'

    if (params == '') return url;
    else return url + '?'+params;
}

}
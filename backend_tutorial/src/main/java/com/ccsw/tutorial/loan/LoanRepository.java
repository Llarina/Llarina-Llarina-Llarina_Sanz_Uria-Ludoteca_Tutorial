package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan>, JpaRepository<Loan, Long> {

    List<Loan> findAllByGameName(String gameName);

    List<Loan> findAllByClientName(String clientName);

    List<Loan> findAllByLoanDate(Date loanDate);

    List<Loan> findAllByGameNameAndClientNameAndLoanDate(String gameName, String clientName, Date loanDate);

    List<Loan> findAllByGameNameAndClientName(String gameName, String clientName);

    List<Loan> findAllByGameNameAndLoanDate(String gameName, Date loanDate);

    List<Loan> findAllByClientNameAndLoanDate(String clientName, Date loanDate);

}

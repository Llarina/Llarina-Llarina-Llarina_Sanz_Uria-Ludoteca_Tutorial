package com.ccsw.tutorial.loan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "gameName", nullable = false)
    private String gameName;

    @NotNull
    @Column(name = "clientName", nullable = false)
    private String clientName;

    @NotNull
    @Column(name = "loanDate", nullable = false)
    private Date loanDate;

    @NotNull
    @Column(name = "returnDate", nullable = false)
    private Date returnDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "Loan{" + "id=" + id + ", clientName='" + clientName + '\'' + ", gameName='" + gameName + '\'' + ", loanDate=" + loanDate + ", returnDate=" + returnDate + '}';
    }
}
package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Loan get(Long id) {

        return this.loanRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Loan> findLoans(String gameName, String clientName, Date loanDate, Pageable pageable, String sort) {
        System.out.println("Game name: " + gameName + " Client name: " + clientName + " Date: " + loanDate + " Pageable: " + pageable + " Sort: " + sort);
        List<Loan> loans = new ArrayList<>();

        if (gameName != null && clientName == null && loanDate == null) {
            loans.addAll(loanRepository.findAllByGameName(gameName));
        } else if (clientName != null && gameName == null && loanDate == null) {
            loans.addAll(loanRepository.findAllByClientName(clientName));
        } else if (loanDate != null && gameName == null && clientName == null) {
            loans.addAll(loanRepository.findAllByLoanDate(loanDate));
        } else if (gameName != null && clientName != null && loanDate == null) {
            loans.addAll(loanRepository.findAllByGameNameAndClientName(gameName, clientName));
        } else if (gameName != null && clientName == null && loanDate != null) {
            loans.addAll(loanRepository.findAllByGameNameAndLoanDate(gameName, loanDate));
        } else if (clientName != null && gameName == null && loanDate != null) {
            loans.addAll(loanRepository.findAllByClientNameAndLoanDate(clientName, loanDate));
        } else if (gameName != null && clientName != null && loanDate != null) {
            loans.addAll(loanRepository.findAllByGameNameAndClientNameAndLoanDate(gameName, clientName, loanDate));
        }

        if (gameName == null && clientName == null && loanDate == null) {
            return loanRepository.findAll(pageable);
        } else {
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), loans.size());
            return new PageImpl<>(loans.subList(start, end), pageable, loans.size());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Loan> findAll() {

        return (List<Loan>) this.loanRepository.findAll();
    }

    public List<Loan> find(String gameName, String clientName, Date loanDate) {

        LoanSpecification gameNameSpec = new LoanSpecification(new SearchCriteria("gameName", ":", gameName));
        LoanSpecification clientNameSpec = new LoanSpecification(new SearchCriteria("clientName", ":", clientName));
        LoanSpecification loanDateSpec = new LoanSpecification(new SearchCriteria("loanDate", ":", loanDate));

        Specification<Loan> spec = Specification.where(gameNameSpec).and(clientNameSpec).and(loanDateSpec);
        return null;
        //return this.loanRepository.findAllBySpec(spec);
    }

    @Override
    public void save(Long id, LoanDto dto) {

        Loan loan;

        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.get(id);
        }

        // Verificación 1: La fecha de devolución no puede ser anterior a la fecha de inicio
        if (dto.getReturnDate().before(dto.getLoanDate())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        // Verificación 2: El período de préstamo no puede ser mayor a 14 días
        Calendar loanCalendar = Calendar.getInstance();
        loanCalendar.setTime(dto.getLoanDate());
        loanCalendar.add(Calendar.DAY_OF_YEAR, 14);
        Date loanPlus14 = loanCalendar.getTime();

        if (dto.getReturnDate().after(loanPlus14)) {
            throw new IllegalArgumentException("El período de préstamo no puede ser mayor a 14 días.");
        }

        // Verificación 3: El juego no puede estar prestado en el rango de fechas solicitado
        List<Loan> activeGames = loanRepository.findAllByGameName(dto.getGameName());
        for (Loan activeGame : activeGames) {
            if ((dto.getLoanDate().before(activeGame.getReturnDate()) && dto.getReturnDate().after(activeGame.getLoanDate()))) {
                throw new IllegalArgumentException("El juego ya está prestado en ese rango de fechas.");
            }
        }

        // Verificación 4: El cliente no puede tener más de 2 juegos prestados en el mismo día
        String date;
        List<Loan> clientLoans = loanRepository.findAllByClientName(dto.getClientName());
        if (dto.getLoanDate().getDate() < 10) {
            date = "0" + dto.getLoanDate().getDate();
        } else {
            date = "" + dto.getLoanDate().getDate();
        }
        List<Loan> count = clientLoans.stream().filter(lo -> lo.getLoanDate().toString().split(" ", 0)[0].equals((dto.getLoanDate().getYear() + 1900) + "-" + dto.getLoanDate().getMonth() + 1 + "-" + date)).toList();
        if (count.size() >= 2) {

            throw new IllegalArgumentException("El cliente ya tiene más de 2 juegos prestados en este día.");
        }

        loan.setClientName(dto.getClientName());
        loan.setGameName(dto.getGameName());
        loan.setLoanDate(dto.getLoanDate());
        loan.setReturnDate(dto.getReturnDate());

        this.loanRepository.save(loan);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        this.loanRepository.deleteById(id);
    }

}
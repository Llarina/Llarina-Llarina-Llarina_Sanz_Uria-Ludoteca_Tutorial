package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ccsw
 *
 */
@Tag(name = "Loan", description = "API of Loan")
@RequestMapping(value = "/loan")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    ModelMapper mapper;

    /**
     * Método para recuperar todas las {@link Loan}
     *
     * @return {@link List} de {@link LoanDto}
     */
    @Operation(summary = "Find", description = "Method that return a list of Loans")
    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public List<LoanDto> findAll() {

        List<Loan> loans = this.loanService.findAll();

        return loans.stream().map(e -> mapper.map(e, LoanDto.class)).collect(Collectors.toList());
    }

    /**
     * Método para crear o actualizar una {@link Loan}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Loan")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody LoanDto dto) {

        this.loanService.save(id, dto);
    }

    @Operation(summary = "Find with filters and pagination", description = "Method that returns a page of Loans with filters and pagination")
    @GetMapping
    public Page<Loan> findLoans(@RequestParam(required = false) String gameName, @RequestParam(required = false) String clientName, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date loanDate,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "id,asc") String sort) {

        // Si loanDate es nulo, se maneja como null, para no causar un error de conversión
        if (loanDate == null) {
            loanDate = null;  // Si prefieres otro valor predeterminado, puedes configurarlo aquí.
        }
        System.out.println("aaaaaab");
        // Parsear el parámetro sort para manejar el orden ascendente/descendente
        String[] sortParams = sort.split(",");
        Sort.Order sortOrder = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? Sort.Order.desc(sortParams[0]) : Sort.Order.asc(sortParams[0]);
        System.out.println("aaaaaac");
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortOrder));

        // Llamada al servicio para obtener los préstamos con filtros aplicados
        var page2 = loanService.findLoans(gameName, clientName, loanDate, pageable, sort);
        System.out.println(page2.toString());
        return page2;

        // Convertir la página de Loan a LoanDto
        // List<LoanDto> loanDtos = loansPage.getContent().stream().map(e -> mapper.map(e, LoanDto.class)).collect(Collectors.toList());

        // Retornar la página con los préstamos convertidos a DTO
        // return new LoanPage(loanDtos, loansPage.getTotalElements(), loansPage.getPageable().getPageNumber(), loansPage.getPageable().getPageSize());
    }

    /**
     * Método para borrar una {@link Loan}
     *
     * @param id PK de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a Loan")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {

        this.loanService.delete(id);
    }

}
package com.NikolaySHA.expenses_api.web;


import com.NikolaySHA.expenses_api.model.dto.InExpenseDTO;
import com.NikolaySHA.expenses_api.model.dto.OutExpenseDTO;
import com.NikolaySHA.expenses_api.service.ExpenseService;
import com.NikolaySHA.expenses_api.service.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }
    
    @PostMapping
    public ResponseEntity<OutExpenseDTO> createExpense(@RequestBody InExpenseDTO data){
        OutExpenseDTO createdExpense = expenseService.createExpense(data);
        return ResponseEntity
                .created(URI.create("/expenses/" + createdExpense.getId()))
                .body(createdExpense);
    }
    
    @GetMapping()
    public ResponseEntity<List<OutExpenseDTO>> getAllProtocols(){
        return ResponseEntity
                .ok(expenseService.getAllExpenses());
    }
    @GetMapping("/appointment/{id}")
    public ResponseEntity<List<OutExpenseDTO>> getAllProtocolsByAppointmentId(@PathVariable("id") Long id){
        return ResponseEntity
                .ok(expenseService.getAllExpensesByAppointmentId(id));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OutExpenseDTO> findById(@PathVariable("id") Long id) {
        OutExpenseDTO expenseDTO = expenseService.findExpenseById(id);
        return ResponseEntity.ok(expenseDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
    
    @ControllerAdvice
    public static class GlobalExceptionHandler {
        @ExceptionHandler(ObjectNotFoundException.class)
        public ResponseEntity<String> handleEntityNotFound(ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

package com.NikolaySHA.expenses_api.service;


import com.NikolaySHA.expenses_api.model.dto.InExpenseDTO;
import com.NikolaySHA.expenses_api.model.dto.OutExpenseDTO;

import java.util.List;

public interface ExpenseService {
    
    OutExpenseDTO createExpense(InExpenseDTO data);
    
    List<OutExpenseDTO> getAllExpenses();
    
    void deleteExpense(Long id);
    
    OutExpenseDTO findExpenseById(Long id);
    
    List<OutExpenseDTO> getAllExpensesByAppointmentId(Long id);
}

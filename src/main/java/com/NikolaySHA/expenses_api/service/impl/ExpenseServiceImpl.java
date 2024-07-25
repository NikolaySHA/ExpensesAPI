package com.NikolaySHA.expenses_api.service.impl;

import com.NikolaySHA.expenses_api.model.dto.InExpenseDTO;
import com.NikolaySHA.expenses_api.model.dto.OutExpenseDTO;
import com.NikolaySHA.expenses_api.model.entity.Expense;
import com.NikolaySHA.expenses_api.repository.ExpenseRepository;
import com.NikolaySHA.expenses_api.service.ExpenseService;
import com.NikolaySHA.expenses_api.service.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    
    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    
    @Override
    public OutExpenseDTO createExpense(InExpenseDTO data) {
        Expense expense = new Expense();
        expense.setName(data.getName());
        expense.setPrice(data.getPrice());
        expense.setAppointmentId(data.getAppointmentId());
        expenseRepository.save(expense);
        return this.map(expense);
    }
    
    @Override
    public List<OutExpenseDTO> getAllExpenses() {
        return expenseRepository.findAll().stream().map(this::map).toList();
    }
    @Override
    public List<OutExpenseDTO> getAllExpensesByAppointmentId(Long id) {
        return expenseRepository.findAll().stream()
                .filter(expense -> expense.getAppointmentId() ==  id )
                .map(this::map)
                .toList();
    }
    @Override
    public OutExpenseDTO findExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(ObjectNotFoundException::new);
        return map(expense);
    }
    @Override
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ObjectNotFoundException();
        }
        expenseRepository.deleteById(id);
    }
    private OutExpenseDTO map(Expense expense){
        OutExpenseDTO dto = new OutExpenseDTO();
        dto.setName(expense.getName());
        dto.setPrice(expense.getPrice());
        dto.setId(expense.getId());
        return dto;
    }
}

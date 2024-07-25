package com.NikolaySHA.expenses_api.web;

import com.NikolaySHA.expenses_api.model.entity.Expense;
import com.NikolaySHA.expenses_api.repository.ExpenseRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ExpensesControllerIT {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    public void setUp() {
        expenseRepository.deleteAll();
    }
    
    @Test
    public void testCreateExpense() throws Exception {
        // Arrange
        String newExpenseJson = "{\"name\":\"New Expense\",\"price\":1500.00}";
        
        // Act
        MvcResult result = mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newExpenseJson))
                .andExpect(status().isCreated()) // Expecting 201 Created
                .andExpect(header().exists("Location")) // Check if Location header is present
                .andReturn();
        
        // Assert
        String locationHeader = result.getResponse().getHeader("Location");
        String locationPath = locationHeader.replace("/expenses/", ""); // Extract the ID from the Location header
        int id = Integer.parseInt(locationPath);
        
        Optional<Expense> expenseOptional = expenseRepository.findById((long) id);
        Assertions.assertTrue(expenseOptional.isPresent());
        Expense expense = expenseOptional.get();
        Assertions.assertEquals("New Expense", expense.getName());
        Assertions.assertEquals(1500.00, expense.getPrice());
    }
    
    @Test
    public void testGetAllExpenses() throws Exception {
        // Arrange
        createTestExpense();
        
        // Act & Assert
        mockMvc.perform(get("/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Expense")))
                .andExpect(jsonPath("$[0].price", is(100.50)));
    }
    
    @Test
    public void testFindExpenseById() throws Exception {
        // Arrange
        Expense actualExpense = createTestExpense();
        
        // Act & Assert
        mockMvc.perform(get("/expenses/{id}", actualExpense.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) actualExpense.getId())))
                .andExpect(jsonPath("$.price", is(actualExpense.getPrice())))
                .andExpect(jsonPath("$.name", is(actualExpense.getName())));
    }
    
    @Test
    public void testFindExpenseByIdNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/expenses/{id}", 999L) // Assuming 999L does not exist
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testDeleteExpense() throws Exception {
        // Arrange
        Expense actualExpense = createTestExpense();
        
        // Act & Assert
        mockMvc.perform(delete("/expenses/{id}", actualExpense.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        Assertions.assertTrue(
                expenseRepository.findById(actualExpense.getId()).isEmpty()
        );
    }
    
    @Test
    public void testDeleteExpenseNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/expenses/{id}", 999L) // Assuming 999L does not exist
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    private Expense createTestExpense() {
        Expense expense = new Expense();
        expense.setName("Test Expense");
        expense.setPrice(100.50);
        return expenseRepository.save(expense);
    }
}


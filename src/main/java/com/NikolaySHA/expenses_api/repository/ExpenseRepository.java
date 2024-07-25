package com.NikolaySHA.expenses_api.repository;

import com.NikolaySHA.expenses_api.model.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}

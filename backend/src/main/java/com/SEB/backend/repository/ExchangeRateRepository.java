package com.SEB.backend.repository;

import com.SEB.backend.entity.ExchangeRate;
import com.SEB.backend.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    List<ExchangeRate> findByCurrencyAndDateBetweenOrderByDateDesc(Currency currency, LocalDate startDate, LocalDate endDate);

    List<ExchangeRate> findByDate(LocalDate localDate);

    @Query("SELECT MAX(e.date) FROM ExchangeRate e")
    Optional<LocalDate> findMaxDate();
}

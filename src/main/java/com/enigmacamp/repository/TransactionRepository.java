package com.enigmacamp.repository;

import com.enigmacamp.model.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {

    @NativeQuery("select count(*) from transactions t where t.start_date::text like :startDate% and (t.is_up = true or t.is_up = false) and t.is_down = false and t.mountain_id = :mountainId")
    Integer countTotalTransactionByStartDateAndIsUp(String startDate, String mountainId);

    @NativeQuery("select count(*) from transactions t where t.end_date::text like :endDate% and (t.is_up = true or t.is_up = false) and t.is_down = false and t.mountain_id = :mountainId")
    Integer countTotalTransactionByEndDateAndIsUp(String endDate, String mountainId);

    @NativeQuery("select * from transactions t where extract(month from t.created_at ) = :month and extract(year from t.created_at) = :year and t.mountain_id = :mountainId")
    Page<Transaction> getTransactionByMonthAndYearAndMountain(Integer month, Integer year, String mountainId, Pageable pageable);
}

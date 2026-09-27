package com.alumni.portal.repository;

import com.alumni.portal.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    List<JournalEntry> findByJournalId(Long journalId);

    List<JournalEntry> findByReferenceId(String referenceId);

    List<JournalEntry> findByCampaignId(Long campaignId);

    List<JournalEntry> findAllByOrderByEntryDateDesc();

    @Query("SELECT COALESCE(SUM(je.amount), 0) FROM JournalEntry je WHERE je.debitAccount.id = :accountId")
    BigDecimal sumDebitByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT COALESCE(SUM(je.amount), 0) FROM JournalEntry je WHERE je.creditAccount.id = :accountId")
    BigDecimal sumCreditByAccountId(@Param("accountId") Long accountId);
}

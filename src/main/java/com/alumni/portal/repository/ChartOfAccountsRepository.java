package com.alumni.portal.repository;

import com.alumni.portal.entity.ChartOfAccounts;
import com.alumni.portal.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChartOfAccountsRepository extends JpaRepository<ChartOfAccounts, Long> {
    Optional<ChartOfAccounts> findByCode(String code);
    Optional<ChartOfAccounts> findByName(String name);
    List<ChartOfAccounts> findByType(AccountType type);
    boolean existsByCode(String code);
}

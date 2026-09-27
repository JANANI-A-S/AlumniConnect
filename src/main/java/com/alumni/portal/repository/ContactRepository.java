package com.alumni.portal.repository;

import com.alumni.portal.entity.Contact;
import com.alumni.portal.enums.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByContactType(ContactType contactType);
}

package com.loulysoft.moneytransfer.accounting.services.impl;

import com.loulysoft.moneytransfer.accounting.entities.JournalEntity;
import com.loulysoft.moneytransfer.accounting.exceptions.TransactionException;
import com.loulysoft.moneytransfer.accounting.mappers.JournalMapper;
import com.loulysoft.moneytransfer.accounting.models.Journal;
import com.loulysoft.moneytransfer.accounting.repositories.JournalRepository;
import com.loulysoft.moneytransfer.accounting.services.JournalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class JournalServiceImpl implements JournalService {

    private final JournalMapper journalMapper;

    private final JournalRepository journalRepository;

    @Override
    public Journal createJournal(Journal journal) {
        try {
            JournalEntity newJournal = journalMapper.toEntity(journal);

            JournalEntity savedJournal = journalRepository.save(newJournal);

            return journalMapper.toDto(savedJournal);

        } catch (RuntimeException e) {
            throw new TransactionException("Error while creating journal : " + e.getMessage());
        }
    }
}

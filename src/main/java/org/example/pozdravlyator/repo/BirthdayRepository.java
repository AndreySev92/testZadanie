package org.example.pozdravlyator.repo;

import org.example.pozdravlyator.model.BirthdayRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BirthdayRepository {
    List<BirthdayRecord> findAll();
    Optional<BirthdayRecord> findById(UUID id);

    void add(BirthdayRecord r);
    void update(BirthdayRecord r);
    boolean delete(UUID id);
}

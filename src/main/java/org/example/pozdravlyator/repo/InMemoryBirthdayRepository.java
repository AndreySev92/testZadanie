package org.example.pozdravlyator.repo;

import org.example.pozdravlyator.model.BirthdayRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryBirthdayRepository implements BirthdayRepository {
    private final List<BirthdayRecord> data = new ArrayList<>();

    @Override
    public List<BirthdayRecord> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Optional<BirthdayRecord> findById(UUID id) {
        return data.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    @Override
    public void add(BirthdayRecord r) {
        data.add(r);

    }

    @Override
    public void update(BirthdayRecord r) {
        delete(r.getId());
        data.add(r);

    }

    @Override
    public boolean delete(UUID id) {
        return data.removeIf(x -> x.getId().equals(id));
    }
    public void replaceAll(List<BirthdayRecord> items) {
        data.clear();
        data.addAll(items);
    }

}

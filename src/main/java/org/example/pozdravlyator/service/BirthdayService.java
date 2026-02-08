package org.example.pozdravlyator.service;

import org.example.pozdravlyator.model.BirthdayRecord;
import org.example.pozdravlyator.repo.BirthdayRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class BirthdayService {
    private final BirthdayRepository repo;

    public BirthdayService(BirthdayRepository repo) {
        this.repo = repo;
    }
    public static LocalDate nextBirthday(LocalDate birthDate, LocalDate today) {
        LocalDate next = birthDate.withYear(today.getYear());
        if(next.isBefore(today)) next = next.plusYears(1);
        return next;
    }


    public List<BirthdayRecord> getUpcoming(int days) {
        LocalDate today = LocalDate.now();
        return repo.findAll().stream()
                .sorted(Comparator.comparing(r-> nextBirthday(r.getBirthDate(), today)))
                .filter(r->{
                    long diff = ChronoUnit.DAYS.between(today,nextBirthday(r.getBirthDate(), today));
                    return diff >= 0 && diff < days;
                })
                .collect(Collectors.toList());
    }

    public List<BirthdayRecord> getAllSortedByNextDate(){
        LocalDate today = LocalDate.now();
        return repo.findAll().stream()
                .sorted(Comparator.comparing(r->nextBirthday(r.getBirthDate(),today)))
                .collect(Collectors.toList());
    }

    public void add (String fullName, LocalDate birthDate, String group) {
        repo.add(new BirthdayRecord(UUID.randomUUID(), fullName, birthDate, group));
    }

    public BirthdayRecord findById(UUID id) {
        return repo.findById(id).orElse(null);
    }

    public boolean update(BirthdayRecord r) {
        if (repo.findById(r.getId()).isEmpty()) return false;
        repo.update(r);
        return true;
    }

    public boolean delete(UUID id) {
        return repo.delete(id);
    }

}

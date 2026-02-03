package org.example.pozdravlyator.model;

import java.time.LocalDate;
import java.util.UUID;

public class BirthdayRecord {
    private UUID id;
    private String fullName;
    private LocalDate birthDate;
    private String group;

    public BirthdayRecord(UUID id, String fullName, LocalDate birthDate, String group) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.group = group;
    }

    public UUID getId() {return id;}
    public String getFullName() {return fullName;}
    public LocalDate getBirthDate() {return birthDate;}
    public String getGroup() {return group;}

    public void setFullName(String fullName) {this.fullName = fullName;}
    public void setBirthDate(LocalDate birthDate) {this.birthDate = birthDate;}
    public void setGroup(String group) {this.group = group;}
}


package org.example.pozdravlyator;

import org.example.pozdravlyator.repo.InMemoryBirthdayRepository;
import org.example.pozdravlyator.service.BirthdayService;
import org.example.pozdravlyator.storage.CsvStorage;
import org.example.pozdravlyator.ui.ConsoleMenu;

import java.nio.file.Path;

public class App {
    public static void main(String[] args) {
        Path file = Path.of("data", "birthdays.csv");

        var repo = new InMemoryBirthdayRepository();
        var storage = new CsvStorage();

        repo.replaceAll(storage.load(file));

        var service = new BirthdayService(repo);

        new ConsoleMenu(service).run();

        storage.save(file, repo.findAll());

        System.out.println("Данные сохранены: " + file.toAbsolutePath());
    }
}

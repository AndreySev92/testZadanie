package org.example.pozdravlyator.ui;

import org.example.pozdravlyator.model.BirthdayRecord;
import org.example.pozdravlyator.service.BirthdayService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final BirthdayService service;
    private final Scanner sc = new Scanner(System.in);

    public ConsoleMenu(BirthdayService birthdayService) {
        this.service = birthdayService;
    }

    public void run() {
        showUpcoming(14);

        while (true) {
            System.out.println("\n=== Поздравлятор ===");
            System.out.println("1) Показать весь список");
            System.out.println("2) Сегодня и ближайшие ДР (14 дней)");
            System.out.println("0) Выход");
            System.out.print("Выберите пункт: ");

            String cmd = sc.nextLine().trim();
            switch (cmd) {
                case "1" -> showAll();
                case "2" ->showUpcoming(14);
                case "0" -> { return; }
                default -> System.out.println("Неизвестная команда: " + cmd);
            }
        }
    }

    private void showAll() {
        printList(service.getAllSortedByNextDate());
    }
    private void showUpcoming(int days) {
        System.out.println("\nСегодня и ближайшие (" + days + " дней):");
        printList(service.getUpcoming(days));
    }

    private void printList(List<BirthdayRecord> list) {
        LocalDate today = LocalDate.now();
        if (list.isEmpty()) {
            System.out.println("(пусто)");
            return;
        }
        for (BirthdayRecord r : list) {
            LocalDate next = BirthdayService.nextBirthday(r.getBirthDate(), today);
            long diff = ChronoUnit.DAYS.between(today, next);

            String status = (diff == 0) ? "СЕГОДНЯ" : ("через " + diff + " дн.");
            System.out.printf("%s | %s | др: %s | след: %s | группа: %s | %s%n",
                    r.getId(),
                    r.getFullName(),
                    r.getBirthDate(),
                    next,
                    r.getGroup(),
                    status
            );
        }
    }
}

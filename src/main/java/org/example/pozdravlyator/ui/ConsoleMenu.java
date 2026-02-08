package org.example.pozdravlyator.ui;

import org.example.pozdravlyator.model.BirthdayRecord;
import org.example.pozdravlyator.service.BirthdayService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

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
            System.out.println("1) Показать весь список ДР");
            System.out.println("2) Сегодня и ближайшие ДР (14 дней)");
            System.out.println("3) Добавить запись");
            System.out.println("4) Редактировать запись");
            System.out.println("5) Удалить запись");
            System.out.println("0) Выход");
            System.out.print("Выберите пункт: ");

            String cmd = sc.nextLine().trim();
            switch (cmd) {
                case "1" -> showAll();
                case "2" -> showUpcoming(14);
                case "3" -> addRecord();
                case "4" -> editRecord();
                case "5" -> deleteRecord();
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

    private void addRecord() {
        System.out.println("\n=== Добавление записи ===");
        String name = readNonEmpty("Имя (ФИО): ");

        LocalDate birthDate = readDate("Дата рождения (yyyy-MM-dd): ");

        System.out.print("Группа (можно пусто): ");
        String group = sc.nextLine().trim();

        service.add(name, birthDate, group);

        System.out.println("✅ Запись добавлена.");
    }

    private void editRecord() {
        System.out.println("\n=== Редактирование записи ===");
        showAll();

        UUID id = readUuid("Введите id записи для редактирования: ");

        BirthdayRecord r = service.findById(id);
        if (r == null) {
            System.out.println("❌ Запись не найдена: " + id);
            return;
        }

        System.out.println("Оставьте поле пустым, чтобы не менять значение.");

        System.out.print("Имя (текущее: " + r.getFullName() + "): ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) r.setFullName(name);

        System.out.print("Дата рождения yyyy-MM-dd (текущая: " + r.getBirthDate() + "): ");
        String dateStr = sc.nextLine().trim();
        if (!dateStr.isEmpty()) {
            try {
                r.setBirthDate(LocalDate.parse(dateStr));
            } catch (DateTimeParseException e) {
                System.out.println("❌ Неверный формат даты. Изменение даты пропущено.");
            }
        }

        System.out.print("Группа (текущая: " + r.getGroup() + "): ");
        String group = sc.nextLine().trim();
        if (!group.isEmpty()) r.setGroup(group);

        boolean ok = service.update(r);
        System.out.println(ok ? "✅ Запись обновлена." : "❌ Не удалось обновить запись.");
    }

    private void deleteRecord() {
        System.out.println("\n=== Удаление записи ===");
        showAll();

        UUID id = readUuid("Введите id записи для удаления: ");

        boolean ok = service.delete(id);
        System.out.println(ok ? "✅ Удалено." : "❌ Запись не найдена.");
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("❌ Значение не может быть пустым.");
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return LocalDate.parse(s);
            } catch (Exception e) {
                System.out.println("❌ Неверный формат даты. Нужно yyyy-MM-dd (например 1990-02-05).");
            }
        }
    }

    private UUID readUuid(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return UUID.fromString(s);
            } catch (Exception e) {
                System.out.println("❌ Неверный UUID. Скопируйте id из списка целиком.");
            }
        }
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

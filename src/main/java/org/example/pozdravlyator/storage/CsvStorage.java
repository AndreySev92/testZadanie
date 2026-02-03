package org.example.pozdravlyator.storage;

import com.sun.jdi.event.ExceptionEvent;
import org.example.pozdravlyator.model.BirthdayRecord;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ClientInfoStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CsvStorage {
    public List<BirthdayRecord> load(Path path) {
        List<BirthdayRecord> list = new ArrayList<>();
        if (!Files.exists(path)) return list;

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] p = line.split(";", -1);
                if (p.length < 4) continue;

                UUID id = UUID.fromString(p[0]);
                String fullName = p[1];
                LocalDate birthDate = LocalDate.parse(p[2]);
                String group = p[3];

                list.add(new BirthdayRecord(id, fullName, birthDate, group));
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения файла: " + path, e);
        }
        return list;
    }

    public void save(Path path, List<BirthdayRecord> data) {
        try {
            if (path.getParent() != null) Files.createDirectories(path.getParent());

            try (BufferedWriter bw = Files.newBufferedWriter(path)) {
                for (BirthdayRecord r : data) {
                    bw.write(r.getId() + ";" + r.getFullName() + r.getBirthDate() + ";" + r.getBirthDate());
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка записи файла: " + path, e);
        }
    }
}

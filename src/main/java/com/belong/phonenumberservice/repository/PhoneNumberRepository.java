package com.belong.phonenumberservice.repository;

import com.belong.phonenumberservice.model.PhoneNumber;
import com.belong.phonenumberservice.dto.PhoneNumberStatusDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class PhoneNumberRepository {
    private static final String CSV_FILE = "./data/phone_numbers.csv";
    private static final String CSV_HEADER = "row_id,phone_id,customer_id,phone_number,activation_status,creation_date,modified_date";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private final Map<UUID, PhoneNumber> phoneNumbers;
    private final Object fileLock = new Object();
    private long currentRowId = 0;

    public PhoneNumberRepository() {
        this.phoneNumbers = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void init() {
        createCsvIfNotExists();
        loadData();
    }

    private void createCsvIfNotExists() {
        Path file = Paths.get(CSV_FILE);
        if (!Files.exists(file)) {
            try {
                Files.write(file, Collections.singletonList(CSV_HEADER),
                        StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } catch (IOException e) {
                log.error("Error creating CSV file", e);
            }
        }
    }

    private void loadData() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
            if (lines.size() <= 1) return; // Only header exists

            lines.stream()
                    .skip(1) // Skip header
                    .forEach(line -> {
                        String[] parts = line.split(",");
                        if (parts.length == 7) {
                            long rowId = Long.parseLong(parts[0]);
                            currentRowId = Math.max(currentRowId, rowId);

                            PhoneNumber phoneNumber = PhoneNumber.builder()
                                    .id(UUID.fromString(parts[1]))
                                    .customerId(UUID.fromString(parts[2]))
                                    .number(parts[3])
                                    .status(PhoneNumberStatusDto.valueOf(parts[4]))
                                    .createdAt(LocalDateTime.parse(parts[5], DATE_FORMATTER))
                                    .updatedAt(LocalDateTime.parse(parts[6], DATE_FORMATTER))
                                    .build();

                            phoneNumbers.put(phoneNumber.getId(), phoneNumber);
                        }
                    });
        } catch (IOException e) {
            log.error("Error loading phone numbers from CSV", e);
        }
    }

    public void saveData(PhoneNumber phoneRecord) throws IOException {
        File file = new File(CSV_FILE);
        boolean isNewFile = !file.exists() || file.length() == 0;

        List<String> lines = new ArrayList<>();
        if (!isNewFile) {
            lines = Files.readAllLines(file.toPath());
        }

        boolean found = false;
        if (lines.size() >= 2) {
            for (int i = 1; i < lines.size(); i++) {
                String[] columns = lines.get(i).split(",");
                if (columns.length > 1 && columns[1].equals(phoneRecord.getNumber())) {
                    // Update the existing record and mark as found
                    String updatedRecord = convertToCSV(phoneRecord);
                    lines.set(i, updatedRecord);
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            // Add new record if not found
            lines.add(convertToCSV(phoneRecord));
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            if (isNewFile) {
                writer.write(CSV_HEADER);
                writer.newLine();
            }
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private String convertToCSV(PhoneNumber phoneNumber) {
        currentRowId++;
        return String.format("%d,%s,%s,%s,%s,%s,%s",
                currentRowId,
                phoneNumber.getId(),
                phoneNumber.getCustomerId(),
                phoneNumber.getNumber(),
                phoneNumber.getStatus(),
                phoneNumber.getCreatedAt().format(DATE_FORMATTER),
                phoneNumber.getUpdatedAt().format(DATE_FORMATTER)
        );
    }

    public List<PhoneNumber> findAll(PhoneNumberStatusDto status, int page, int limit) {
        loadData();
        return phoneNumbers.values().stream()
                .filter(number -> status == null || number.getStatus() == status)
                .skip((long) (page - 1) * limit)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public long countAll(PhoneNumberStatusDto status) {
        loadData();
        return phoneNumbers.values().stream()
                .filter(number -> status == null || number.getStatus() == status)
                .count();
    }

    public List<PhoneNumber> findByCustomerId(UUID customerId, PhoneNumberStatusDto status) {
        loadData();
        return phoneNumbers.values().stream()
                .filter(number -> number.getCustomerId().equals(customerId))
                .filter(number -> status == null || number.getStatus() == status)
                .collect(Collectors.toList());
    }

    public Optional<PhoneNumber> findById(UUID id) {
        loadData();
        return Optional.ofNullable(phoneNumbers.get(id));
    }

    public PhoneNumber save(PhoneNumber phoneNumber) {
        try {
            saveData(phoneNumber);
        } catch (IOException e) {
            log.error("Error saving phone number to CSV", e);
        }

        return phoneNumber;
    }
}
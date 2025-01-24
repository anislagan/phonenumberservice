package com.belong.phonenumberservice.repository;

import com.belong.phonenumberservice.model.Customer;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class CustomerRepository {
    private static final String CSV_FILE = "./data/customers.csv";
    private static final String CSV_HEADER = "customer_id,created_date,modified_date";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private final Map<UUID, Customer> customers;
    private final Object fileLock = new Object();
    private long lastModifiedTime = 0;

    public CustomerRepository() {
        this.customers = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void init() {
        createCsvIfNotExists();
        loadData();
        updateLastModifiedTime();
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

    private boolean isFileModified() {
        try {
            long currentModifiedTime = Files.getLastModifiedTime(Paths.get(CSV_FILE)).toMillis();
            return currentModifiedTime > lastModifiedTime;
        } catch (IOException e) {
            log.error("Error checking file modification time", e);
            return true;
        }
    }

    private void updateLastModifiedTime() {
        try {
            lastModifiedTime = Files.getLastModifiedTime(Paths.get(CSV_FILE)).toMillis();
        } catch (IOException e) {
            log.error("Error updating last modified time", e);
        }
    }

    private void loadDataIfModified() {
        if (isFileModified()) {
            synchronized (fileLock) {
                if (isFileModified()) {
                    loadData();
                    updateLastModifiedTime();
                }
            }
        }
    }

    private void loadData() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(CSV_FILE));
            if (lines.size() <= 1) return;

            lines.stream()
                    .skip(1)
                    .forEach(line -> {
                        String[] parts = line.split(",");
                        if (parts.length == 3) {
                            UUID customerId = UUID.fromString(parts[0]);
                            Customer customer = Customer.builder()
                                    .id(customerId)
                                    .createdAt(LocalDateTime.parse(parts[1], DATE_FORMATTER))
                                    .updatedAt(LocalDateTime.parse(parts[2], DATE_FORMATTER))
                                    .build();
                            customers.put(customerId, customer);
                        }
                    });
        } catch (IOException e) {
            log.error("Error loading customers from CSV", e);
        }
    }

    public void saveData(Customer customer) throws IOException {
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
                if (columns.length > 0 && columns[0].equals(customer.getId().toString())) {
                    lines.set(i, convertToCSV(customer));
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            lines.add(convertToCSV(customer));
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

    private String convertToCSV(Customer customer) {
        return String.format("%s,%s,%s",
                customer.getId(),
                customer.getCreatedAt().format(DATE_FORMATTER),
                customer.getUpdatedAt().format(DATE_FORMATTER)
        );
    }

    public boolean existsById(UUID customerId) {
        loadDataIfModified();
        return customers.containsKey(customerId);
    }

    public void save(Customer customer) {
        try {
            synchronized (fileLock) {
                saveData(customer);
            }
        } catch (IOException ex) {
            log.error("Error saving customer to CSV", ex);
        }
    }
}
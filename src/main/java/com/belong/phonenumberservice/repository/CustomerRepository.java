package com.belong.phonenumberservice.repository;

import com.belong.phonenumberservice.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class CustomerRepository {
    private static final Map<UUID, Customer> customers = new ConcurrentHashMap<>();

    static {
        // Initialize with sample data
        addCustomer(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                LocalDateTime.now().minusDays(30), LocalDateTime.now().minusDays(30));
        addCustomer(UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8"),
                LocalDateTime.now().minusDays(20), LocalDateTime.now().minusDays(15));
    }

    private static void addCustomer(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        customers.put(id, Customer.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build());
    }

    public boolean existsById(UUID customerId) {
        return customers.containsKey(customerId);
    }

    public void save(Customer customer) {
        customers.put(customer.getId(), customer);
    }
}
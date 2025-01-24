package com.belong.phonenumberservice.service;

import com.belong.phonenumberservice.dto.PhoneNumberDto;
import com.belong.phonenumberservice.dto.PhoneNumberResponseDto;
import com.belong.phonenumberservice.exception.CustomerNotFoundException;
import com.belong.phonenumberservice.exception.InvalidActivationCodeException;
import com.belong.phonenumberservice.exception.PhoneNumberAlreadyActivatedException;
import com.belong.phonenumberservice.exception.PhoneNumberNotFoundException;
import com.belong.phonenumberservice.mapper.PhoneNumberMapper;
import com.belong.phonenumberservice.model.ApiResponse;
import com.belong.phonenumberservice.model.PaginationInfo;
import com.belong.phonenumberservice.model.PhoneNumber;
import com.belong.phonenumberservice.model.PhoneNumberStatus;
import com.belong.phonenumberservice.repository.CustomerRepository;
import com.belong.phonenumberservice.repository.PhoneNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Add cache annotations to the service layer
@Service
@EnableCaching
@RequiredArgsConstructor
public class PhoneNumberService {

    private final PhoneNumberRepository repository;
    private final CustomerRepository customerRepository;

    @Cacheable(cacheNames = "phoneNumbers", key = "'all_' + #status + '_' + #page + '_' + #limit")
    public ApiResponse<List<PhoneNumber>> getAllPhoneNumbers(PhoneNumberStatus status, int page, int limit) {
        List<PhoneNumber> numbers = repository.findAll(status, page, limit);
        long total = repository.countAll(status);

        return ApiResponse.<List<PhoneNumber>>builder()
                .data(numbers)
                .pagination(PaginationInfo.builder()
                        .currentPage(page)
                        .itemsPerPage(limit)
                        .totalItems(total)
                        .totalPages((int) Math.ceil((double) total / limit))
                        .build())
                .build();
    }

    @Cacheable(cacheNames = "customerPhoneNumbers", key = "#customerId + '_' + #status")
    public List<PhoneNumber> getCustomerPhoneNumbers(UUID customerId, PhoneNumberStatus status) {
        boolean exists = customerRepository.existsById(customerId);
        if (!exists) {
            throw new CustomerNotFoundException("Customer not found");
        }

        return repository.findByCustomerId(customerId, status);
    }

    @CacheEvict(value = "phoneNumbers", allEntries = true)
    @CachePut(value = "phoneNumbers", key = "#result.id")
    public PhoneNumberResponseDto activatePhoneNumber(UUID phoneNumberId, String activationCode) {
        PhoneNumber phoneNumber = repository.findById(phoneNumberId)
                .orElseThrow(() -> new PhoneNumberNotFoundException("Phone number not found"));

        if (phoneNumber.getStatus() == PhoneNumberStatus.ACTIVE) {
            throw new PhoneNumberAlreadyActivatedException("Phone number is already activated");
        }

        if (!isValidActivationCode(activationCode)) {
            throw new InvalidActivationCodeException("Invalid activation code");
        }

        phoneNumber.setStatus(PhoneNumberStatus.ACTIVE);
        phoneNumber.setUpdatedAt(LocalDateTime.now());

        return new PhoneNumberMapper().toDto(repository.save(phoneNumber));
    }

    /**
     * This may require external verification in actual production environment.
     * Defaults to true
     * @param activationCode
     * @return
     */
    private boolean isValidActivationCode(String activationCode) {
        return true;
    }
}

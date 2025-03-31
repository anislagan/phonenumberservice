package com.exam.phonenumberservice.service;

import com.exam.phonenumberservice.dto.PhoneNumberDto;
import com.exam.phonenumberservice.exception.CustomerNotFoundException;
import com.exam.phonenumberservice.exception.PhoneNumberAlreadyActivatedException;
import com.exam.phonenumberservice.exception.PhoneNumberNotFoundException;
import com.exam.phonenumberservice.mapper.PhoneNumberMapper;
import com.exam.phonenumberservice.dto.ApiResponseDto;
import com.exam.phonenumberservice.dto.PaginationInfoDto;
import com.exam.phonenumberservice.model.PhoneNumber;
import com.exam.phonenumberservice.dto.PhoneNumberStatusDto;
import com.exam.phonenumberservice.repository.CustomerRepository;
import com.exam.phonenumberservice.repository.PhoneNumberRepository;
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
    public ApiResponseDto<List<PhoneNumberDto>> getAllPhoneNumbers(PhoneNumberStatusDto status, int page, int limit) {
        List<PhoneNumber> numbers = repository.findAll(status, page, limit);
        long total = repository.countAll(status);

        return ApiResponseDto.<List<PhoneNumberDto>>builder()
                .data(PhoneNumberMapper.toDtoList(numbers))
                .pagination(PaginationInfoDto.builder()
                        .currentPage(page)
                        .itemsPerPage(limit)
                        .totalItems(total)
                        .totalPages((int) Math.ceil((double) total / limit))
                        .build())
                .build();
    }

    @Cacheable(cacheNames = "customers", key = "#customerId + '_' + #status")
    public List<PhoneNumberDto> getCustomerPhoneNumbers(UUID customerId, PhoneNumberStatusDto status) {
        boolean exists = customerRepository.existsById(customerId);
        if (!exists) {
            throw new CustomerNotFoundException("Customer not found");
        }

        List<PhoneNumber> numbers = repository.findByCustomerId(customerId, status);
        return PhoneNumberMapper.toDtoList(numbers);
    }

    @CacheEvict(value = "phoneNumbers", allEntries = true)
    @CachePut(value = "phoneNumbers", key = "#result.id")
    public PhoneNumberDto activatePhoneNumber(UUID phoneNumberId) {
        PhoneNumber phoneNumber = repository.findById(phoneNumberId)
                .orElseThrow(() -> new PhoneNumberNotFoundException("Phone number not found"));

        if (phoneNumber.getStatus() == PhoneNumberStatusDto.ACTIVE) {
            throw new PhoneNumberAlreadyActivatedException("Phone number is already activated");
        }

        phoneNumber.setStatus(PhoneNumberStatusDto.ACTIVE);
        phoneNumber.setUpdatedAt(LocalDateTime.now());

        return PhoneNumberMapper.toDto(repository.save(phoneNumber));
    }
}

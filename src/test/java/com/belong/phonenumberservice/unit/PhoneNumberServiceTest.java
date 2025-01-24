package com.belong.phonenumberservice.unit;

import com.belong.phonenumberservice.dto.PhoneNumberDto;
import com.belong.phonenumberservice.exception.PhoneNumberNotFoundException;
import com.belong.phonenumberservice.model.PhoneNumber;
import com.belong.phonenumberservice.dto.PhoneNumberStatusDto;
import com.belong.phonenumberservice.repository.CustomerRepository;
import com.belong.phonenumberservice.repository.PhoneNumberRepository;
import com.belong.phonenumberservice.service.PhoneNumberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PhoneNumberServiceTest {

    @Mock
    private PhoneNumberRepository phoneNumberRepository;

    @Mock
    CustomerRepository  customerRepository;

    @InjectMocks
    private PhoneNumberService phoneNumberService;

    private PhoneNumber testPhoneNumber;
    private UUID testId;
    private UUID testCustomerId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testCustomerId = UUID.randomUUID();
        testPhoneNumber = PhoneNumber.builder()
                .id(testId)
                .number("+1234567890")
                .customerId(testCustomerId)
                .status(PhoneNumberStatusDto.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllPhoneNumbers_ShouldReturnPaginatedResults() {
        // Arrange
        List<PhoneNumber> phoneNumbers = Arrays.asList(testPhoneNumber);
        when(phoneNumberRepository.findAll(any(), anyInt(), anyInt())).thenReturn(phoneNumbers);
        when(phoneNumberRepository.countAll(any())).thenReturn(1L);

        // Act
        var response = phoneNumberService.getAllPhoneNumbers(null, 1, 20);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getData().size());
        assertEquals(1, response.getPagination().getTotalItems());
        assertEquals(1, response.getPagination().getCurrentPage());
    }

    @Test
    void getCustomerPhoneNumbers_ShouldReturnCustomerNumbers() {
        // Arrange
        List<PhoneNumber> phoneNumbers = Arrays.asList(testPhoneNumber);
        when(phoneNumberRepository.findByCustomerId(testCustomerId, null)).thenReturn(phoneNumbers);
        when(customerRepository.existsById(testCustomerId)).thenReturn(true);

        // Act
        List<PhoneNumberDto> result = phoneNumberService.getCustomerPhoneNumbers(testCustomerId, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCustomerId.toString(), result.getFirst().getCustomerId());
    }

    @Test
    void activatePhoneNumber_ShouldActivateValidNumber() {
        // Arrange
        when(phoneNumberRepository.findById(testId)).thenReturn(Optional.of(testPhoneNumber));
        when(phoneNumberRepository.save(any())).thenReturn(testPhoneNumber);

        // Act
        PhoneNumberDto result = phoneNumberService.activatePhoneNumber(testId, "123456");

        // Assert
        assertEquals(PhoneNumberStatusDto.ACTIVE.name(), result.getStatus());
        verify(phoneNumberRepository).save(any());
    }

    @Test
    void activatePhoneNumber_ShouldThrowException_WhenNumberNotFound() {
        // Arrange
        when(phoneNumberRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PhoneNumberNotFoundException.class, () ->
                phoneNumberService.activatePhoneNumber(testId, "123456")
        );
    }
}

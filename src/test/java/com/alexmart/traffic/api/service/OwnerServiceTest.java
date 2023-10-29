package com.alexmart.traffic.api.service;

import com.alexmart.traffic.api.domain.model.Owner;
import com.alexmart.traffic.api.domain.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OwnerServiceTest {

    @InjectMocks
    private OwnerService ownerService;

    @Mock
    private OwnerRepository ownerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Owner owner1 = new Owner(1L, "John", "john@example.com", "123-456-7890");
        Owner owner2 = new Owner(2L, "Alice", "alice@example.com", "987-654-3210");
        List<Owner> owners = Arrays.asList(owner1, owner2);

        Mockito.when(ownerRepository.findAll()).thenReturn(owners);

        List<Owner> result = ownerService.findAll();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getName());
        assertEquals("Alice", result.get(1).getName());
    }
}

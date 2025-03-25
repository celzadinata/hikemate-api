package com.enigmacamp.service.impl;

import com.enigmacamp.model.dto.request.HikerRequest;
import com.enigmacamp.model.dto.request.SearchRequest;
import com.enigmacamp.model.dto.response.HikerResponse;
import com.enigmacamp.model.entity.Hiker;
import com.enigmacamp.repository.HikerRepository;
import com.enigmacamp.utils.exception.ResourceNotFoundException;
import com.enigmacamp.utils.mapper.HikerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HikerServiceImplTest {

    @Mock
    private HikerRepository hikerRepository;

    @Mock
    private HikerMapper hikerMapper;

    @InjectMocks
    private HikerServiceImpl hikerService;

    private HikerRequest hikerRequest;
    private Hiker hiker;
    private HikerResponse hikerResponse;

    @BeforeEach
    void setUp() {
        hikerRequest = new HikerRequest();
        hikerRequest.setId("1");
        hikerRequest.setName("John Doe");
        hikerRequest.setPhoneNumber("123456789");
        hikerRequest.setPassword("newpassword");

        hiker = new Hiker();
        hiker.setId("1");
        hiker.setName("John Doe");
        hiker.setPhoneNumber("123456789");

        hikerResponse = new HikerResponse();
        hikerResponse.setId("1");
        hikerResponse.setName("John Doe");
        hikerResponse.setPhoneNumber("123456789");
    }

    @Test
    void testCreateHiker() {
        when(hikerMapper.requestToEntity(hikerRequest)).thenReturn(hiker);
        when(hikerRepository.save(hiker)).thenReturn(hiker);
        when(hikerMapper.entityToResponse(hiker)).thenReturn(hikerResponse);

        HikerResponse result = hikerService.create(hikerRequest);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("123456789", result.getPhoneNumber());

        verify(hikerRepository, times(1)).save(hiker);
    }

    @Test
    void testGetAllHikers() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setPage(1);
        searchRequest.setSize(10);
        searchRequest.setSortBy("name");
        searchRequest.setDirection("ASC");

        Page<Hiker> hikerPage = new PageImpl<>(List.of(hiker));
        when(hikerRepository.findAll((Example<Hiker>) any(), any(Pageable.class))).thenReturn(hikerPage);
        when(hikerMapper.entityToResponse(hiker)).thenReturn(hikerResponse);

        Page<HikerResponse> result = hikerService.getAllHikers(searchRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John Doe", result.getContent().get(0).getName());
        verify(hikerRepository, times(1)).findAll((Example<Hiker>) any(), any(Pageable.class));
    }

    @Test
    void testGetHikerById() {
        when(hikerRepository.findById("1")).thenReturn(Optional.of(hiker));
        when(hikerMapper.entityToResponse(hiker)).thenReturn(hikerResponse);

        HikerResponse result = hikerService.getById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("John Doe", result.getName());
        verify(hikerRepository, times(1)).findById("1");
    }

    @Test
    void testGetHikerByIdNotFound() {
        when(hikerRepository.findById("1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            hikerService.getById("1");
        });

        assertEquals("Hiker not found", exception.getMessage());
        verify(hikerRepository, times(1)).findById("1");
    }

    @Test
    void testUpdateHiker() {
        when(hikerRepository.findById("1")).thenReturn(Optional.of(hiker));
        when(hikerMapper.requestToEntity(hikerRequest)).thenReturn(hiker);
        when(hikerRepository.saveAndFlush(hiker)).thenReturn(hiker);
        when(hikerMapper.entityToResponse(hiker)).thenReturn(hikerResponse);

        HikerResponse result = hikerService.updateHiker(hikerRequest);

        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(hikerRepository, times(1)).saveAndFlush(hiker);
    }

    @Test
    void testDeleteHiker() {
        when(hikerRepository.findById("1")).thenReturn(Optional.of(hiker));
        when(hikerRepository.saveAndFlush(hiker)).thenReturn(hiker);
        when(hikerMapper.entityToResponse(hiker)).thenReturn(hikerResponse);

        HikerResponse result = hikerService.delete("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertNotNull(hiker.getDeletedAt()); // Check if deletedAt is set
        verify(hikerRepository, times(1)).saveAndFlush(hiker);
    }
}

package com.example.socks.service;

import com.example.socks.model.Sock;
import com.example.socks.repository.SockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SockServiceTest {

    @Mock
    private SockRepository sockRepository;

    @InjectMocks
    private SockService sockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSocks() {
        Sock sock = new Sock();
        sock.setColor("red");
        sock.setCottonPercentage(80);
        sock.setQuantity(100);

        when(sockRepository.findByColorAndCottonPercentage("red", 80)).thenReturn(null);
        when(sockRepository.save(any(Sock.class))).thenReturn(sock);

        sockService.addSocks("red", 80, 100);

        verify(sockRepository, times(1)).save(any(Sock.class));
    }

    @Test
    void testRemoveSocks() {
        Sock sock = new Sock();
        sock.setColor("red");
        sock.setCottonPercentage(80);
        sock.setQuantity(100);

        when(sockRepository.findByColorAndCottonPercentage("red", 80)).thenReturn(sock);
        when(sockRepository.save(any(Sock.class))).thenReturn(sock);

        sockService.removeSocks("red", 80, 50);

        assertEquals(50, sock.getQuantity());
        verify(sockRepository, times(1)).save(any(Sock.class));
    }

    @Test
    void testUpdateSock() {
        Sock sock = new Sock();
        sock.setId(1L);
        sock.setColor("red");
        sock.setCottonPercentage(80);
        sock.setQuantity(100);

        when(sockRepository.findById(1L)).thenReturn(Optional.of(sock));
        when(sockRepository.save(any(Sock.class))).thenReturn(sock);

        sockService.updateSock(1L, "blue", 90, 200);

        assertEquals("blue", sock.getColor());
        assertEquals(90, sock.getCottonPercentage());
        assertEquals(200, sock.getQuantity());
        verify(sockRepository, times(1)).save(any(Sock.class));
    }

    @Test
    void testGetTotalSocks() {
        Sock sock1 = new Sock();
        sock1.setColor("red");
        sock1.setCottonPercentage(80);
        sock1.setQuantity(100);

        Sock sock2 = new Sock();
        sock2.setColor("blue");
        sock2.setCottonPercentage(90);
        sock2.setQuantity(200);

        when(sockRepository.findAll()).thenReturn(List.of(sock1, sock2));

        List<Sock> result = sockService.getTotalSocks(null, null, null, null, null, null);

        assertEquals(2, result.size());
        assertTrue(result.contains(sock1));
        assertTrue(result.contains(sock2));
    }
}

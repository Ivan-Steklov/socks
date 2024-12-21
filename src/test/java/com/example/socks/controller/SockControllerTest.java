package com.example.socks.controller;

import com.example.socks.model.Sock;
import com.example.socks.service.SockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SockControllerTest {

    @Mock
    private SockService sockService;

    @InjectMocks
    private SockController sockController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sockController).build();
    }

    @Test
    void testAddSocks() throws Exception {
        mockMvc.perform(post("/api/socks/income")
                        .param("color", "red")
                        .param("cottonPercentage", "80")
                        .param("quantity", "100"))
                .andExpect(status().isOk());

        verify(sockService, times(1)).addSocks("red", 80, 100);
    }

    @Test
    void testRemoveSocks() throws Exception {
        mockMvc.perform(post("/api/socks/outcome")
                        .param("color", "red")
                        .param("cottonPercentage", "80")
                        .param("quantity", "50"))
                .andExpect(status().isOk());

        verify(sockService, times(1)).removeSocks("red", 80, 50);
    }

    @Test
    void testGetTotalSocks() throws Exception {
        Sock sock1 = new Sock();
        sock1.setColor("red");
        sock1.setCottonPercentage(80);
        sock1.setQuantity(100);

        Sock sock2 = new Sock();
        sock2.setColor("blue");
        sock2.setCottonPercentage(90);
        sock2.setQuantity(200);

        when(sockService.getTotalSocks(null, null, null, null, null, null)).thenReturn(List.of(sock1, sock2));

        mockMvc.perform(get("/api/socks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testUpdateSock() throws Exception {
        mockMvc.perform(put("/api/socks/1")
                        .param("color", "blue")
                        .param("cottonPercentage", "90")
                        .param("quantity", "200"))
                .andExpect(status().isOk());

        verify(sockService, times(1)).updateSock(1L, "blue", 90, 200);
    }
}

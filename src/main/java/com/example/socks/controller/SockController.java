package com.example.socks.controller;

import com.example.socks.model.Sock;
import com.example.socks.service.SockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/socks")
@Tag(name = "Sock Management System", description = "Operations pertaining to socks in Sock Management System")
public class SockController {
    @Autowired
    private SockService sockService;

    @PostMapping("/income")
    @Operation(summary = "Add socks to the inventory", description = "Adds a specified quantity of socks to the inventory")
    public void addSocks(
            @Parameter(description = "Color of the socks", required = true) @RequestParam String color,
            @Parameter(description = "Cotton percentage of the socks", required = true) @RequestParam int cottonPercentage,
            @Parameter(description = "Quantity of the socks", required = true) @RequestParam int quantity) {
        sockService.addSocks(color, cottonPercentage, quantity);
    }

    @PostMapping("/outcome")
    @Operation(summary = "Remove socks from the inventory", description = "Removes a specified quantity of socks from the inventory")
    public void removeSocks(
            @Parameter(description = "Color of the socks", required = true) @RequestParam String color,
            @Parameter(description = "Cotton percentage of the socks", required = true) @RequestParam int cottonPercentage,
            @Parameter(description = "Quantity of the socks", required = true) @RequestParam int quantity) {
        sockService.removeSocks(color, cottonPercentage, quantity);
    }

    @GetMapping
    @Operation(summary = "Get total socks with filters", description = "Gets the total number of socks with optional filters")
    public List<Sock> getTotalSocks(
            @Parameter(description = "Color of the socks") @RequestParam(required = false) String color,
            @Parameter(description = "Operator for cotton percentage filter") @RequestParam(required = false) String operator,
            @Parameter(description = "Cotton percentage of the socks") @RequestParam(required = false) Integer cottonPercentage,
            @Parameter(description = "Minimum cotton percentage") @RequestParam(required = false) Integer minCottonPercentage,
            @Parameter(description = "Maximum cotton percentage") @RequestParam(required = false) Integer maxCottonPercentage,
            @Parameter(description = "Sort by field") @RequestParam(required = false) String sortBy) {
        return sockService.getTotalSocks(color, operator, cottonPercentage, minCottonPercentage, maxCottonPercentage, sortBy);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a sock by ID", description = "Updates the details of a sock by its ID")
    public void updateSock(
            @Parameter(description = "ID of the sock", required = true) @PathVariable Long id,
            @Parameter(description = "Color of the socks", required = true) @RequestParam String color,
            @Parameter(description = "Cotton percentage of the socks", required = true) @RequestParam int cottonPercentage,
            @Parameter(description = "Quantity of the socks", required = true) @RequestParam int quantity) {
        sockService.updateSock(id, color, cottonPercentage, quantity);
    }

    @PostMapping("/batch")
    @Operation(summary = "Batch upload socks from a file", description = "Uploads a batch of socks from a file")
    public void batchUpload(
            @Parameter(description = "File containing socks data", required = true) @RequestParam MultipartFile file) throws IOException {
        List<Sock> socks = parseFile(file);
        sockService.batchUpload(socks);
    }

    private List<Sock> parseFile(MultipartFile file) throws IOException {
        List<Sock> socks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] values = line.split(",");
                Sock sock = new Sock();
                sock.setColor(values[0]);
                sock.setCottonPercentage(Integer.parseInt(values[1]));
                sock.setQuantity(Integer.parseInt(values[2]));
                socks.add(sock);
            }
        }
        return socks;
    }
}

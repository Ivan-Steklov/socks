package com.example.socks.service;

import com.example.socks.exception.StockException;
import com.example.socks.model.Sock;
import com.example.socks.repository.SockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SockService {
    private static final Logger logger = LoggerFactory.getLogger(SockService.class);

    @Autowired
    private SockRepository sockRepository;

    public void addSocks(String color, int cottonPercentage, int quantity) {
        logger.info("Adding socks: color={}, cottonPercentage={}, quantity={}", color, cottonPercentage, quantity);
        Sock sock = sockRepository.findByColorAndCottonPercentage(color, cottonPercentage);
        if (sock != null) {
            sock.setQuantity(sock.getQuantity() + quantity);
        } else {
            sock = new Sock();
            sock.setColor(color);
            sock.setCottonPercentage(cottonPercentage);
            sock.setQuantity(quantity);
        }
        sockRepository.save(sock);
    }

    public void removeSocks(String color, int cottonPercentage, int quantity) {
        logger.info("Removing socks: color={}, cottonPercentage={}, quantity={}", color, cottonPercentage, quantity);
        Sock sock = sockRepository.findByColorAndCottonPercentage(color, cottonPercentage);
        if (sock != null && sock.getQuantity() >= quantity) {
            sock.setQuantity(sock.getQuantity() - quantity);
            sockRepository.save(sock);
        } else {
            throw new StockException("Not enough socks in stock");
        }
    }

    public List<Sock> getTotalSocks(String color, String operator, Integer cottonPercentage, Integer minCottonPercentage, Integer maxCottonPercentage, String sortBy) {
        logger.info("Getting total socks: color={}, operator={}, cottonPercentage={}, minCottonPercentage={}, maxCottonPercentage={}, sortBy={}",
                color, operator, cottonPercentage, minCottonPercentage, maxCottonPercentage, sortBy);

        List<Sock> socks = sockRepository.findAll();

        if (color != null) {
            socks = socks.stream().filter(sock -> sock.getColor().equalsIgnoreCase(color)).collect(Collectors.toList());
        }

        if (cottonPercentage != null) {
            switch (operator) {
                case "moreThan":
                    socks = socks.stream().filter(sock -> sock.getCottonPercentage() > cottonPercentage).collect(Collectors.toList());
                    break;
                case "lessThan":
                    socks = socks.stream().filter(sock -> sock.getCottonPercentage() < cottonPercentage).collect(Collectors.toList());
                    break;
                case "equal":
                    socks = socks.stream().filter(sock -> sock.getCottonPercentage() == cottonPercentage).collect(Collectors.toList());
                    break;
            }
        }

        if (minCottonPercentage != null && maxCottonPercentage != null) {
            socks = socks.stream().filter(sock -> sock.getCottonPercentage() >= minCottonPercentage && sock.getCottonPercentage() <= maxCottonPercentage).collect(Collectors.toList());
        }

        if (sortBy != null) {
            switch (sortBy) {
                case "color":
                    socks = socks.stream().sorted(Comparator.comparing(Sock::getColor)).collect(Collectors.toList());
                    break;
                case "cottonPercentage":
                    socks = socks.stream().sorted(Comparator.comparingInt(Sock::getCottonPercentage)).collect(Collectors.toList());
                    break;
            }
        }

        return socks;
    }

    public void updateSock(Long id, String color, int cottonPercentage, int quantity) {
        logger.info("Updating sock: id={}, color={}, cottonPercentage={}, quantity={}", id, color, cottonPercentage, quantity);
        Sock sock = sockRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Sock not found"));
        sock.setColor(color);
        sock.setCottonPercentage(cottonPercentage);
        sock.setQuantity(quantity);
        sockRepository.save(sock);
    }

    public void batchUpload(List<Sock> socks) {
        logger.info("Batch uploading socks: {}", socks);
        sockRepository.saveAll(socks);
    }
}

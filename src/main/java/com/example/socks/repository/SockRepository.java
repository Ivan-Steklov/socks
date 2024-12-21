package com.example.socks.repository;

import com.example.socks.model.Sock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SockRepository extends JpaRepository<Sock, Long> {
    Sock findByColorAndCottonPercentage(String color, int cottonPercentage);
}

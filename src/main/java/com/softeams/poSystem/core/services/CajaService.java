package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.entities.Caja;
import com.softeams.poSystem.core.repositories.CajaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CajaService {
    private final CajaRepository cajaRepository;

    //CRUD

    //CREATE
    public Caja createCaja(Caja caja) {
        log.info("Creating caja: {}", caja.getDate());
        return cajaRepository.save(caja);
    }
    //READ
    public Caja getCajaById(Long id) {
        log.info("Fetching caja by id: {}", id);
        return cajaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Caja not found with id: " + id));
    }
    public Caja getCajaByDate(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching caja by date: {}", startDate);
        return cajaRepository.findByDateBetween(startDate,endDate)
                .orElseThrow(() -> new RuntimeException("Caja not found with date: " + startDate));
    }
    public List<Caja> getAllCajas() {
        log.info("Fetching all cajas");
        return cajaRepository.findAll()
                .stream()
                .sorted((c1, c2) -> c2.getDate().compareTo(c1.getDate())) // Sort by date descending
                .toList();
    }

    //UPDATE
    public Caja updateCaja(Long id, Caja caja) {
        log.info("Updating caja with id: {}", id);
        Caja existingCaja = getCajaById(id);
        existingCaja.setDate(caja.getDate());
        existingCaja.setMontoInicial(caja.getMontoInicial());
        existingCaja.setMontoFinal(caja.getMontoFinal());
        return cajaRepository.save(existingCaja);
    }

    //DELETE
    public void deleteCaja(Long id) {
        log.info("Deleting caja with id: {}", id);
        Caja existingCaja = getCajaById(id);
        cajaRepository.delete(existingCaja);
    }

}

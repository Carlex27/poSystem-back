package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.entities.Salidas;
import com.softeams.poSystem.core.repositories.SalidasRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SalidasService {
    private final SalidasRepository salidasRepository;

    //CRUD operations for Salidas can be implemented here
    //CREATE
    public Salidas createSalida(Salidas salida) {
        log.info("Creating salida: {}", salida);
        return salidasRepository.save(salida);
    }

    public List<Salidas> createSalida(List<Salidas> salidas) {
        log.info("Creating multiple salidas");
        return salidasRepository.saveAll(salidas);
    }
    //READ
    public Salidas getSalidaById(Long id) {
        log.info("Fetching salida by id: {}", id);
        return salidasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salida not found with id: " + id));
    }
    //UPDATE
    @Transactional
    public Salidas updateSalida(Long id, Salidas salida) {
        log.info("Updating salida with id: {}", id);
        Salidas existingSalida = getSalidaById(id);
        existingSalida.setDescription(salida.getDescription());
        existingSalida.setAmount(salida.getAmount());
        existingSalida.setDate(salida.getDate());
        return salidasRepository.save(existingSalida);
    }
    //DELETE
    @Transactional
    public void deleteSalida(Long id) {
        log.info("Deleting salida with id: {}", id);
        Salidas existingSalida = getSalidaById(id);
        salidasRepository.delete(existingSalida);
    }
}

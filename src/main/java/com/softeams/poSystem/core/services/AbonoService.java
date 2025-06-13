package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.entities.Abono;
import com.softeams.poSystem.core.entities.Client;
import com.softeams.poSystem.core.repositories.AbonoRepository;
import com.softeams.poSystem.core.services.interfaces.IAbonoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AbonoService implements IAbonoService {
    private final AbonoRepository abonoRepository;
    //CRUD
    //CREATE
    public Abono createAbono(Abono abono) {
        log.info("Creating abono: {}", abono);
        return abonoRepository.save(abono);
    }

    public List<Abono> createAbono(List<Abono> abonos) {
        log.info("Creating abonos: {}", abonos);
        return abonoRepository.saveAll(abonos);
    }

    //READ
    public List<Abono> getAllAbonosByClient(Long clientId) {
        log.info("Fetching all abonos for client with id: {}", clientId);
        return abonoRepository.findAllByIsActiveTrueAndClientId(clientId);
    }

    public BigDecimal getTotalAbonosInRange(LocalDateTime start, LocalDateTime finish){
        return abonoRepository.getTotalAbonosInRangeAndIsActive(start,finish);
    }

    //UPDATE NOT REQUIRED
    //DELETE
    public void deleteAbono(Long id) {
        log.info("Deleting abono with id: {}", id);
        Abono abono = abonoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Abono not found with id: " + id));
        abono.setActive(false);
        abonoRepository.save(abono);
    }

    //LOGIC
    @Transactional
    public Abono createAbonoForClient(Client client, BigDecimal abonoAmount) {
        log.info("Creating abono for client with id: {}", client.getName());
        Abono abono = Abono.builder()
                .client(client)
                .fechaAbono(LocalDateTime.now())
                .montoAbono(abonoAmount)
                .isActive(true)
                .build();
        return createAbono(abono);
    }

}

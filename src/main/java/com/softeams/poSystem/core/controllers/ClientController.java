package com.softeams.poSystem.core.controllers;

import com.softeams.poSystem.core.dtos.client.ClientDto;
import com.softeams.poSystem.core.mappers.ClientMapper;
import com.softeams.poSystem.core.services.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/client")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final ClientMapper clientMapper;
    //CRUD
    //CREATE

    @PostMapping("/create")
    public ResponseEntity<?> createClient(
            @Valid
            @RequestBody ClientDto clientDto
            ){
        log.info("[ClientController | CreateClient] Creating client: {}", clientDto.name());
        return ResponseEntity.ok(clientService.createClient(clientMapper.toEntity(clientDto)));
    }

    @PostMapping("/createMany")
    public ResponseEntity<?> createClients(
            @Valid
            @RequestBody List<ClientDto> clientDtos
            ){
        log.info("[ClientController | CreateClients] Creating clients: {}", clientDtos);
        return ResponseEntity.ok(clientService.createClient(clientMapper.toEntity(clientDtos)));
    }

    //READ

    @GetMapping("/findAll")
    public ResponseEntity<?> getAllClients() {
        log.info("[ClientController | GetAllClients] Fetching all clients");
        return ResponseEntity.ok(clientMapper.toDto(clientService.getAllClients()));
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> getClientById(
            @PathVariable Long id
    ) {
        log.info("[ClientController | GetClientById] Fetching client by id: {}", id);
        return ResponseEntity.ok(clientMapper.toDto(clientService.getClientById(id)));
    }
    @GetMapping("/search")
    public ResponseEntity<?> getClientsByName(
            @RequestParam("query") String query
    ) {
        log.info("[ClientController | GetClientsByNameOrEmail] Fetching clients by name or email: {}", query);
        return ResponseEntity.ok(clientMapper.toDto(clientService.getClientsByName(query)));
    }
    //UPDATE

    @PutMapping("/update")
    public ResponseEntity<?> updateClient(
            @Valid
            @RequestBody ClientDto clientDto,
            @RequestParam Long id
    ) {
        log.info("[ClientController | UpdateClient] Updating client by id: {}", id);
        return ResponseEntity.ok(clientMapper.toDto(clientService.updateClient(id,clientMapper.toEntity(clientDto))));
    }


    //DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteClient(
            @PathVariable Long id
    ) {
        log.info("[ClientController | DeleteClient] Deleting client by id: {}", id);
        clientService.deleteClient(id);
        return ResponseEntity.ok("Client deleted successfully");
    }

    //LOGIC
    @PostMapping("/abonar")
    public ResponseEntity<?> abonar(
            @RequestParam Long id,
            @RequestParam BigDecimal abono
            ) {
        log.info("[ClientController | Abonar] Abonando client by id: {} with amount: {}", id, abono);
        return ResponseEntity.ok(clientService.abonar(id, abono));
    }
}

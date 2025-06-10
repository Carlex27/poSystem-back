package com.softeams.poSystem.core.services;

import com.softeams.poSystem.core.entities.Client;
import com.softeams.poSystem.core.repositories.ClientRepository;
import com.softeams.poSystem.core.services.interfaces.IClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService implements IClientService {
    private final ClientRepository clientRepository;

    //CRUD
    //CREATE
    public Client createClient(Client client) {
        log.info("Creating client: {}", client.getName());
        return clientRepository.save(client);
    }

    public List<Client> createClient(List<Client> clients) {
        log.info("Creating clients: {}", clients);
        return clientRepository.saveAll(clients);
    }

    //READ

    public List<Client> getAllClients() {
        log.info("Fetching all clients");
        return clientRepository.findAllByIsActiveTrue();
    }

    public Client getClientById(Long id) {
        log.info("Fetching client by id: {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
    }
    public List<Client> getClientsByName(String nameTerm) {
        log.info("Fetching clients by name: {}", nameTerm);
        return clientRepository.findByNameContainingIgnoreCase(nameTerm);
    }


    //UPDATE
    @Transactional
    public Client updateClient(Long id, Client client) {
        log.info("Updating client with id: {}", id);
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        existingClient.setName(client.getName());
        existingClient.setPhone(client.getPhone());
        existingClient.setDirection(client.getDirection());
        existingClient.setCreditLimit(client.getCreditLimit());

        return clientRepository.save(existingClient);
    }

    //DELETE
    @Transactional
    public void deleteClient(Long id) {
        log.info("Deleting client with id: {}", id);
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        existingClient.setActive(false);
        clientRepository.save(existingClient);
    }

    //LOGIC
    @Transactional
    public String abonar(Long id, BigDecimal abono){
        log.info("Abonando {} al cliente con id: {}", abono, id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));

        BigDecimal nuevoSaldo = client.getBalance().subtract(abono);
        client.setBalance(nuevoSaldo);
        clientRepository.save(client);

        return "Abono realizado correctamente. Nuevo saldo: " + nuevoSaldo;

    }

}

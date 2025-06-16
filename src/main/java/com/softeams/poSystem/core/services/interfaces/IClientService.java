package com.softeams.poSystem.core.services.interfaces;

import com.softeams.poSystem.core.entities.Client;

import java.util.List;

public interface IClientService {
    Client getClientById(Long id);
    Client updateClient(Long id, Client client);
    List<Client> getAllClients();
}

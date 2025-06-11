package com.softeams.poSystem.core.mappers;

import com.softeams.poSystem.core.dtos.client.ClientDto;
import com.softeams.poSystem.core.entities.Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClientMapper {

    public Client toEntity(ClientDto dto) {
        if (dto == null) {
            return null;
        }
        BigDecimal creditLimit = (dto.creditLimit() == null || BigDecimal.ZERO.compareTo(dto.creditLimit()) == 0)
                ? null
                : dto.creditLimit();

        return Client.builder()
                .name(dto.name())
                .phone(dto.phoneNumber())
                .direction(dto.direction())
                .balance(BigDecimal.ZERO)
                .creditLimit(creditLimit)
                .isActive(true)
                .build();
    }

    public List<Client> toEntity(List<ClientDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public ClientDto toDto(Client client) {
        if (client == null) {
            return null;
        }
        return new ClientDto(
                client.getId(),
                client.getName(),
                client.getDirection(),
                client.getPhone(),
                client.getBalance(),
                client.getCreditLimit(),
                client.getLastAbonoDate()
        );
    }
    public List<ClientDto> toDto(List<Client> clients) {
        if (clients == null) {
            return Collections.emptyList();
        }
        return clients.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}

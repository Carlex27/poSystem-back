package com.softeams.poSystem.core.mappers;

import com.softeams.poSystem.core.dtos.salidas.SalidasDto;
import com.softeams.poSystem.core.entities.Salidas;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SalidasMapper {
    public Salidas toEntity(SalidasDto salidasRequest) {
        log.info("Converting SalidasDto to Salidas entity");
        return Salidas.builder()
                .description(salidasRequest.description())
                .amount(salidasRequest.amount())
                .date(salidasRequest.date())
                .build();
    }
    public List<Salidas> toEntity(List<SalidasDto> salidasRequest) {
        log.info("Converting SalidasDto list to Salidas entities list");
        return salidasRequest.stream()
                .map(this::toEntity)
                .toList();
    }
    public SalidasDto toDto(Salidas salidas) {
        log.info("Converting Salidas entity to SalidasDto");
        return new SalidasDto(
                salidas.getId(),
                salidas.getDescription(),
                salidas.getAmount(),
                salidas.getDate()
        );
    }
    public List<SalidasDto> toDto(List<Salidas> salidasList) {
        log.info("Converting list of Salidas entities to list of SalidasDto");
        return salidasList.stream()
                .map(this::toDto)
                .toList();
    }
}

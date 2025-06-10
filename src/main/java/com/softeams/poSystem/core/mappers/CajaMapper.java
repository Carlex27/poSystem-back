package com.softeams.poSystem.core.mappers;

import com.softeams.poSystem.core.dtos.caja.CajaDto;
import com.softeams.poSystem.core.entities.Caja;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CajaMapper {
    public Caja toEntity(CajaDto caja) {
        log.info("[CajaMapper | toEntity] Converting AltaCaja to Caja: {}", caja);
        return Caja.builder()
                .montoFinal(caja.montoInicial())
                .date(caja.date().atStartOfDay())
                .build();
    }

    public List<Caja> toEntity(List<CajaDto> cajas) {
        log.info("[CajaMapper | toEntity] Converting List<AltaCaja> to List<Caja>: {}", cajas);
        return cajas.stream()
                .map(this::toEntity)
                .toList();
    }

    public CajaDto toDto(Caja caja) {
        log.info("[CajaMapper | toDto] Converting Caja to AltaCaja: {}", caja);
        return new CajaDto(
                caja.getId(),
                caja.getMontoInicial(),
                caja.getMontoFinal(),
                caja.getDate().toLocalDate()
        );
    }
    public List<CajaDto> toDto(List<Caja> cajas) {
        log.info("[CajaMapper | toDto] Converting List<Caja> to List<AltaCaja>: {}", cajas);
        return cajas.stream()
                .map(this::toDto)
                .toList();
    }
}

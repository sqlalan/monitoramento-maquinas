package br.com.cloudtech.monitoramento.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Dados operacionais enviados para registrar a producao de uma maquina (RF02).
 * O horario de acionamento e aceito no formato "HH:mm" (ex.: "08:30").
 */
public record ProducaoRequest(
        @NotNull(message = "o id da máquina é obrigatório")
        Long maquinaId,

        @NotNull(message = "o consumo em kW é obrigatório")
        @PositiveOrZero(message = "o consumo em kW não pode ser negativo")
        Double consumoKw,

        @NotNull(message = "o horário de acionamento é obrigatório")
        LocalTime horarioAcionamento,

        @NotNull(message = "a quantidade de peças produzidas é obrigatória")
        @PositiveOrZero(message = "a quantidade de peças produzidas não pode ser negativa")
        Integer pecasProduzidas) {
}

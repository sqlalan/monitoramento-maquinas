package br.com.cloudtech.monitoramento.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Dados enviados para atualizar o status de funcionamento de uma maquina.
 */
public record StatusRequest(
        @NotBlank(message = "o status de funcionamento é obrigatório") String status) {
}

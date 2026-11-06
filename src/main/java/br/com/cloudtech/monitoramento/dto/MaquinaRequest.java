package br.com.cloudtech.monitoramento.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Dados enviados para cadastrar uma maquina (RF01).
 */
public record MaquinaRequest(
        @NotBlank(message = "o nome (identificação) da máquina é obrigatório") String nome,
        @NotBlank(message = "o tipo da máquina é obrigatório") String tipo,
        @NotBlank(message = "o setor é obrigatório") String setor,
        @NotBlank(message = "o status de funcionamento é obrigatório") String status) {
}

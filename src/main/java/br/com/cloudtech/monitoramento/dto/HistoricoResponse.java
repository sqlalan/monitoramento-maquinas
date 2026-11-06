package br.com.cloudtech.monitoramento.dto;

import java.util.List;

import br.com.cloudtech.monitoramento.model.Maquina;
import br.com.cloudtech.monitoramento.model.RegistroProducao;

/**
 * Historico basico de producao e consumo de uma maquina.
 */
public record HistoricoResponse(
        Maquina maquina,
        int totalRegistros,
        int totalPecasProduzidas,
        double consumoTotalKw,
        double consumoMedioKw,
        List<RegistroProducao> registros) {
}

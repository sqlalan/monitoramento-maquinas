package br.com.cloudtech.monitoramento.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.cloudtech.monitoramento.dto.HistoricoResponse;
import br.com.cloudtech.monitoramento.service.ProducaoService;

/**
 * Historico basico de producao e consumo por maquina.
 */
@RestController
public class HistoricoController {

    private final ProducaoService service;

    public HistoricoController(ProducaoService service) {
        this.service = service;
    }

    @GetMapping("/maquinas/{id}/historico")
    public HistoricoResponse historico(@PathVariable Long id) {
        return service.historico(id);
    }
}

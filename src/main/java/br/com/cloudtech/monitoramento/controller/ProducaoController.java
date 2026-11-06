package br.com.cloudtech.monitoramento.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cloudtech.monitoramento.dto.ProducaoRequest;
import br.com.cloudtech.monitoramento.model.RegistroProducao;
import br.com.cloudtech.monitoramento.service.ProducaoService;
import jakarta.validation.Valid;

/**
 * Endpoints de registro (RF02) e consulta (RF04) de dados de producao.
 */
@RestController
@RequestMapping("/producao")
public class ProducaoController {

    private final ProducaoService service;

    public ProducaoController(ProducaoService service) {
        this.service = service;
    }

    /** RF04 - Consulta a producao. Filtro opcional: /producao?maquinaId=1 */
    @GetMapping
    public List<RegistroProducao> listar(@RequestParam(required = false) Long maquinaId) {
        return service.listar(maquinaId);
    }

    /** RF02 - Registra os dados operacionais de uma maquina. */
    @PostMapping
    public ResponseEntity<RegistroProducao> registrar(@Valid @RequestBody ProducaoRequest request) {
        RegistroProducao registro = service.registrar(request);
        return ResponseEntity
                .created(URI.create("/producao?maquinaId=" + registro.getMaquinaId()))
                .body(registro);
    }
}

package br.com.cloudtech.monitoramento.controller;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cloudtech.monitoramento.dto.MaquinaRequest;
import br.com.cloudtech.monitoramento.dto.StatusRequest;
import br.com.cloudtech.monitoramento.model.Maquina;
import br.com.cloudtech.monitoramento.service.MaquinaService;
import jakarta.validation.Valid;

/**
 * Endpoints de cadastro (RF01) e consulta (RF03) de maquinas.
 */
@RestController
@RequestMapping("/maquinas")
public class MaquinaController {

    private final MaquinaService service;

    public MaquinaController(MaquinaService service) {
        this.service = service;
    }

    /** RF03 - Lista as maquinas. Filtro opcional: /maquinas?status=parada */
    @GetMapping
    public List<Maquina> listar(@RequestParam(required = false) String status) {
        return service.listar(status);
    }

    @GetMapping("/{id}")
    public Maquina buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    /** Orientacao de uso do cadastro: mostra o JSON esperado no POST. */
    @GetMapping("/cadastro")
    public Map<String, Object> instrucoesCadastro() {
        Map<String, Object> exemplo = new LinkedHashMap<>();
        exemplo.put("nome", "Máquina CNC 01");
        exemplo.put("tipo", "CNC");
        exemplo.put("setor", "Usinagem");
        exemplo.put("status", "em operação");

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("mensagem", "Envie um POST para /maquinas/cadastro com o JSON abaixo");
        resposta.put("exemplo", exemplo);
        return resposta;
    }

    /** RF01 - Cadastra uma nova maquina. */
    @PostMapping("/cadastro")
    public ResponseEntity<Maquina> cadastrar(@Valid @RequestBody MaquinaRequest request) {
        Maquina maquina = service.cadastrar(request);
        return ResponseEntity.created(URI.create("/maquinas/" + maquina.getId())).body(maquina);
    }

    /** Acompanhamento: atualiza o status de funcionamento da maquina. */
    @PatchMapping("/{id}/status")
    public Maquina atualizarStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        return service.atualizarStatus(id, request.status());
    }
}

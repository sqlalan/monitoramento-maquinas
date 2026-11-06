package br.com.cloudtech.monitoramento.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cloudtech.monitoramento.dto.StatusResponse;
import br.com.cloudtech.monitoramento.dto.VersaoResponse;

/**
 * Pagina inicial, status (RF05) e versao da aplicacao.
 */
@RestController
public class HomeController {

    private final String nomeAplicacao;
    private final String versao;

    public HomeController(@Value("${app.nome}") String nomeAplicacao,
                          @Value("${app.versao}") String versao) {
        this.nomeAplicacao = nomeAplicacao;
        this.versao = versao;
    }

    @GetMapping("/")
    public Map<String, Object> paginaInicial() {
        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("aplicacao", nomeAplicacao);
        resposta.put("mensagem", "Bem-vindo à aplicação de monitoramento de máquinas da CloudTech Solutions");
        resposta.put("endpoints", List.of(
                "GET /",
                "GET /status",
                "GET /versao",
                "GET /maquinas",
                "GET /maquinas/{id}",
                "GET /maquinas/cadastro",
                "POST /maquinas/cadastro",
                "PATCH /maquinas/{id}/status",
                "GET /maquinas/{id}/historico",
                "GET /producao",
                "POST /producao"));
        return resposta;
    }

    /** RF05 - Verifica se a aplicacao esta online. */
    @GetMapping("/status")
    public StatusResponse status() {
        return new StatusResponse("online", "Aplicação de monitoramento em execução");
    }

    @GetMapping("/versao")
    public VersaoResponse versao() {
        return new VersaoResponse(nomeAplicacao, versao, System.getProperty("java.version"));
    }
}

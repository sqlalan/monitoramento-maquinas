package br.com.cloudtech.monitoramento.config;

import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.cloudtech.monitoramento.dto.MaquinaRequest;
import br.com.cloudtech.monitoramento.dto.ProducaoRequest;
import br.com.cloudtech.monitoramento.service.MaquinaService;
import br.com.cloudtech.monitoramento.service.ProducaoService;

/**
 * Carrega alguns dados de exemplo ao iniciar a aplicacao,
 * para que os endpoints de consulta ja retornem informacoes.
 */
@Component
public class DadosIniciais implements CommandLineRunner {

    private final MaquinaService maquinaService;
    private final ProducaoService producaoService;

    public DadosIniciais(MaquinaService maquinaService, ProducaoService producaoService) {
        this.maquinaService = maquinaService;
        this.producaoService = producaoService;
    }

    @Override
    public void run(String... args) {
        maquinaService.cadastrar(new MaquinaRequest("Máquina CNC 01", "CNC", "Usinagem", "em operação"));
        maquinaService.cadastrar(new MaquinaRequest("Prensa Hidráulica 02", "Prensa", "Estamparia", "em manutenção"));
        maquinaService.cadastrar(new MaquinaRequest("Injetora 03", "Injetora", "Plásticos", "parada"));

        producaoService.registrar(new ProducaoRequest(1L, 12.5, LocalTime.of(8, 30), 150));
        producaoService.registrar(new ProducaoRequest(1L, 13.1, LocalTime.of(13, 30), 162));
        producaoService.registrar(new ProducaoRequest(2L, 20.0, LocalTime.of(7, 0), 80));
    }
}

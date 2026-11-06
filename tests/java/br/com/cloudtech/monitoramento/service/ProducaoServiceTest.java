package br.com.cloudtech.monitoramento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.cloudtech.monitoramento.dto.HistoricoResponse;
import br.com.cloudtech.monitoramento.dto.MaquinaRequest;
import br.com.cloudtech.monitoramento.dto.ProducaoRequest;
import br.com.cloudtech.monitoramento.exception.RecursoNaoEncontradoException;
import br.com.cloudtech.monitoramento.model.RegistroProducao;
import br.com.cloudtech.monitoramento.repository.MaquinaRepository;
import br.com.cloudtech.monitoramento.repository.ProducaoRepository;

/**
 * Testes unitarios do registro de producao e do historico.
 */
class ProducaoServiceTest {

    private MaquinaService maquinaService;
    private ProducaoService producaoService;

    @BeforeEach
    void configurar() {
        maquinaService = new MaquinaService(new MaquinaRepository());
        producaoService = new ProducaoService(new ProducaoRepository(), maquinaService);
        maquinaService.cadastrar(new MaquinaRequest("Máquina CNC 01", "CNC", "Usinagem", "em operação"));
        maquinaService.cadastrar(new MaquinaRequest("Prensa 02", "Prensa", "Estamparia", "em operação"));
    }

    @Test
    @DisplayName("Deve registrar dados de produção para uma máquina existente")
    void deveRegistrarProducao() {
        RegistroProducao registro = producaoService.registrar(
                new ProducaoRequest(1L, 12.5, LocalTime.of(8, 30), 150));

        assertEquals(1L, registro.getId());
        assertEquals(1L, registro.getMaquinaId());
        assertEquals(12.5, registro.getConsumoKw());
        assertEquals(LocalTime.of(8, 30), registro.getHorarioAcionamento());
        assertEquals(150, registro.getPecasProduzidas());
        assertNotNull(registro.getDataRegistro());
    }

    @Test
    @DisplayName("Não deve registrar produção para máquina inexistente")
    void naoDeveRegistrarParaMaquinaInexistente() {
        assertThrows(RecursoNaoEncontradoException.class,
                () -> producaoService.registrar(new ProducaoRequest(99L, 10.0, LocalTime.NOON, 10)));
    }

    @Test
    @DisplayName("Deve listar a produção de todas as máquinas ou de uma máquina específica")
    void deveListarProducao() {
        producaoService.registrar(new ProducaoRequest(1L, 12.5, LocalTime.of(8, 30), 150));
        producaoService.registrar(new ProducaoRequest(2L, 20.0, LocalTime.of(7, 0), 80));
        producaoService.registrar(new ProducaoRequest(1L, 13.1, LocalTime.of(13, 30), 162));

        assertEquals(3, producaoService.listar(null).size());
        assertEquals(2, producaoService.listar(1L).size());
        assertEquals(1, producaoService.listar(2L).size());
    }

    @Test
    @DisplayName("Deve calcular o histórico de produção e consumo da máquina")
    void deveCalcularHistorico() {
        producaoService.registrar(new ProducaoRequest(1L, 12.5, LocalTime.of(8, 30), 150));
        producaoService.registrar(new ProducaoRequest(1L, 13.1, LocalTime.of(13, 30), 162));
        producaoService.registrar(new ProducaoRequest(2L, 20.0, LocalTime.of(7, 0), 80));

        HistoricoResponse historico = producaoService.historico(1L);

        assertEquals("Máquina CNC 01", historico.maquina().getNome());
        assertEquals(2, historico.totalRegistros());
        assertEquals(312, historico.totalPecasProduzidas());
        assertEquals(25.6, historico.consumoTotalKw());
        assertEquals(12.8, historico.consumoMedioKw());
    }

    @Test
    @DisplayName("Histórico de máquina sem produção deve vir zerado")
    void historicoSemProducao() {
        HistoricoResponse historico = producaoService.historico(2L);

        assertEquals(0, historico.totalRegistros());
        assertEquals(0, historico.totalPecasProduzidas());
        assertEquals(0.0, historico.consumoMedioKw());
    }
}

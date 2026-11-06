package br.com.cloudtech.monitoramento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.cloudtech.monitoramento.dto.MaquinaRequest;
import br.com.cloudtech.monitoramento.exception.RecursoNaoEncontradoException;
import br.com.cloudtech.monitoramento.model.Maquina;
import br.com.cloudtech.monitoramento.repository.MaquinaRepository;

/**
 * Testes unitarios das regras de cadastro e consulta de maquinas.
 */
class MaquinaServiceTest {

    private MaquinaService service;

    @BeforeEach
    void configurar() {
        service = new MaquinaService(new MaquinaRepository());
    }

    @Test
    @DisplayName("Deve cadastrar máquinas gerando ids sequenciais")
    void deveCadastrarComIdSequencial() {
        Maquina cnc = service.cadastrar(new MaquinaRequest("Máquina CNC 01", "CNC", "Usinagem", "em operação"));
        Maquina prensa = service.cadastrar(new MaquinaRequest("Prensa 02", "Prensa", "Estamparia", "parada"));

        assertEquals(1L, cnc.getId());
        assertEquals(2L, prensa.getId());
        assertEquals("CNC", cnc.getTipo());
        assertEquals("Usinagem", cnc.getSetor());
    }

    @Test
    @DisplayName("Deve remover espaços extras dos campos no cadastro")
    void deveRemoverEspacos() {
        Maquina maquina = service.cadastrar(new MaquinaRequest("  Torno 05  ", " Torno ", " Usinagem ", " parada "));

        assertEquals("Torno 05", maquina.getNome());
        assertEquals("parada", maquina.getStatus());
    }

    @Test
    @DisplayName("Deve listar todas as máquinas e filtrar por status")
    void deveListarEFiltrarPorStatus() {
        service.cadastrar(new MaquinaRequest("CNC 01", "CNC", "Usinagem", "em operação"));
        service.cadastrar(new MaquinaRequest("Prensa 02", "Prensa", "Estamparia", "parada"));
        service.cadastrar(new MaquinaRequest("Injetora 03", "Injetora", "Plásticos", "Parada"));

        assertEquals(3, service.listar(null).size());

        List<Maquina> paradas = service.listar("parada");
        assertEquals(2, paradas.size());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar máquina inexistente")
    void deveLancarExcecaoParaMaquinaInexistente() {
        RecursoNaoEncontradoException ex = assertThrows(RecursoNaoEncontradoException.class,
                () -> service.buscarPorId(99L));

        assertEquals("Máquina com id 99 não encontrada", ex.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar o status de funcionamento da máquina")
    void deveAtualizarStatus() {
        Maquina maquina = service.cadastrar(new MaquinaRequest("CNC 01", "CNC", "Usinagem", "em operação"));

        service.atualizarStatus(maquina.getId(), "em manutenção");

        assertEquals("em manutenção", service.buscarPorId(maquina.getId()).getStatus());
    }
}

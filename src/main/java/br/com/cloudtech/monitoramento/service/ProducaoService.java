package br.com.cloudtech.monitoramento.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.cloudtech.monitoramento.dto.HistoricoResponse;
import br.com.cloudtech.monitoramento.dto.ProducaoRequest;
import br.com.cloudtech.monitoramento.model.Maquina;
import br.com.cloudtech.monitoramento.model.RegistroProducao;
import br.com.cloudtech.monitoramento.repository.ProducaoRepository;

/**
 * Regras de negocio do registro (RF02) e da consulta (RF04) de producao e consumo.
 */
@Service
public class ProducaoService {

    private final ProducaoRepository repository;
    private final MaquinaService maquinaService;

    public ProducaoService(ProducaoRepository repository, MaquinaService maquinaService) {
        this.repository = repository;
        this.maquinaService = maquinaService;
    }

    public RegistroProducao registrar(ProducaoRequest request) {
        // Garante que a maquina existe (lanca 404 caso contrario)
        maquinaService.buscarPorId(request.maquinaId());

        RegistroProducao registro = new RegistroProducao(null,
                request.maquinaId(),
                request.consumoKw(),
                request.horarioAcionamento(),
                request.pecasProduzidas(),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return repository.salvar(registro);
    }

    /**
     * Lista os registros de producao. Se o id da maquina for informado, filtra por ela.
     */
    public List<RegistroProducao> listar(Long maquinaId) {
        if (maquinaId == null) {
            return repository.listarTodos();
        }
        maquinaService.buscarPorId(maquinaId);
        return repository.listarPorMaquina(maquinaId);
    }

    /**
     * Monta o historico basico de producao e consumo de uma maquina.
     */
    public HistoricoResponse historico(Long maquinaId) {
        Maquina maquina = maquinaService.buscarPorId(maquinaId);
        List<RegistroProducao> registros = repository.listarPorMaquina(maquinaId);

        int totalPecas = registros.stream().mapToInt(RegistroProducao::getPecasProduzidas).sum();
        double consumoTotal = registros.stream().mapToDouble(RegistroProducao::getConsumoKw).sum();
        double consumoMedio = registros.isEmpty() ? 0.0 : consumoTotal / registros.size();

        return new HistoricoResponse(maquina, registros.size(), totalPecas,
                arredondar(consumoTotal), arredondar(consumoMedio), registros);
    }

    private static double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}

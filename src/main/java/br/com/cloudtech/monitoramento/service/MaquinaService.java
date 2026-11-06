package br.com.cloudtech.monitoramento.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.cloudtech.monitoramento.dto.MaquinaRequest;
import br.com.cloudtech.monitoramento.exception.RecursoNaoEncontradoException;
import br.com.cloudtech.monitoramento.model.Maquina;
import br.com.cloudtech.monitoramento.repository.MaquinaRepository;

/**
 * Regras de negocio do cadastro e consulta de maquinas (RF01 e RF03).
 */
@Service
public class MaquinaService {

    private final MaquinaRepository repository;

    public MaquinaService(MaquinaRepository repository) {
        this.repository = repository;
    }

    public Maquina cadastrar(MaquinaRequest request) {
        Maquina maquina = new Maquina(null,
                request.nome().trim(),
                request.tipo().trim(),
                request.setor().trim(),
                request.status().trim());
        return repository.salvar(maquina);
    }

    /**
     * Lista as maquinas cadastradas. Se um status for informado, filtra por ele.
     */
    public List<Maquina> listar(String status) {
        List<Maquina> todas = repository.listarTodas();
        if (status == null || status.isBlank()) {
            return todas;
        }
        return todas.stream()
                .filter(m -> m.getStatus().equalsIgnoreCase(status.trim()))
                .toList();
    }

    public Maquina buscarPorId(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Máquina com id " + id + " não encontrada"));
    }

    public Maquina atualizarStatus(Long id, String novoStatus) {
        Maquina maquina = buscarPorId(id);
        maquina.setStatus(novoStatus.trim());
        return repository.salvar(maquina);
    }
}

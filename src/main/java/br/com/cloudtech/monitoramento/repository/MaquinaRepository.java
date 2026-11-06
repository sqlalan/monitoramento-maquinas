package br.com.cloudtech.monitoramento.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import br.com.cloudtech.monitoramento.model.Maquina;

/**
 * Armazenamento em memoria das maquinas cadastradas.
 */
@Repository
public class MaquinaRepository {

    private final Map<Long, Maquina> maquinas = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    public Maquina salvar(Maquina maquina) {
        if (maquina.getId() == null) {
            maquina.setId(sequencia.incrementAndGet());
        }
        maquinas.put(maquina.getId(), maquina);
        return maquina;
    }

    public List<Maquina> listarTodas() {
        return maquinas.values().stream()
                .sorted(Comparator.comparing(Maquina::getId))
                .toList();
    }

    public Optional<Maquina> buscarPorId(Long id) {
        return Optional.ofNullable(maquinas.get(id));
    }
}

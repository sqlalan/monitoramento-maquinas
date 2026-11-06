package br.com.cloudtech.monitoramento.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import br.com.cloudtech.monitoramento.model.RegistroProducao;

/**
 * Armazenamento em memoria dos registros de producao.
 */
@Repository
public class ProducaoRepository {

    private final Map<Long, RegistroProducao> registros = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    public RegistroProducao salvar(RegistroProducao registro) {
        if (registro.getId() == null) {
            registro.setId(sequencia.incrementAndGet());
        }
        registros.put(registro.getId(), registro);
        return registro;
    }

    public List<RegistroProducao> listarTodos() {
        return registros.values().stream()
                .sorted(Comparator.comparing(RegistroProducao::getId))
                .toList();
    }

    public List<RegistroProducao> listarPorMaquina(Long maquinaId) {
        return listarTodos().stream()
                .filter(r -> r.getMaquinaId().equals(maquinaId))
                .toList();
    }
}

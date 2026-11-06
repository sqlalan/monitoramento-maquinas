package br.com.cloudtech.monitoramento.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Registro operacional de uma maquina: consumo, horario de acionamento e producao.
 */
public class RegistroProducao {

    private Long id;
    private Long maquinaId;
    private Double consumoKw;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioAcionamento;

    private Integer pecasProduzidas;
    private LocalDateTime dataRegistro;

    public RegistroProducao() {
    }

    public RegistroProducao(Long id, Long maquinaId, Double consumoKw, LocalTime horarioAcionamento,
                            Integer pecasProduzidas, LocalDateTime dataRegistro) {
        this.id = id;
        this.maquinaId = maquinaId;
        this.consumoKw = consumoKw;
        this.horarioAcionamento = horarioAcionamento;
        this.pecasProduzidas = pecasProduzidas;
        this.dataRegistro = dataRegistro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMaquinaId() {
        return maquinaId;
    }

    public void setMaquinaId(Long maquinaId) {
        this.maquinaId = maquinaId;
    }

    public Double getConsumoKw() {
        return consumoKw;
    }

    public void setConsumoKw(Double consumoKw) {
        this.consumoKw = consumoKw;
    }

    public LocalTime getHorarioAcionamento() {
        return horarioAcionamento;
    }

    public void setHorarioAcionamento(LocalTime horarioAcionamento) {
        this.horarioAcionamento = horarioAcionamento;
    }

    public Integer getPecasProduzidas() {
        return pecasProduzidas;
    }

    public void setPecasProduzidas(Integer pecasProduzidas) {
        this.pecasProduzidas = pecasProduzidas;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}

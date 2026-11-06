package br.com.cloudtech.monitoramento.model;

/**
 * Maquina industrial monitorada pela aplicacao.
 */
public class Maquina {

    private Long id;
    private String nome;
    private String tipo;
    private String setor;
    private String status;

    public Maquina() {
    }

    public Maquina(Long id, String nome, String tipo, String setor, String status) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.setor = setor;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

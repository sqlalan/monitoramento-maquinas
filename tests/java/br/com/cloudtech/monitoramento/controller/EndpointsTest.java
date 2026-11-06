package br.com.cloudtech.monitoramento.controller;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Testes de integracao dos endpoints da API (sobe o contexto do Spring).
 */
@SpringBootTest
@AutoConfigureMockMvc
class EndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET / deve exibir a página inicial")
    void paginaInicial() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aplicacao").value("Monitoramento de Máquinas - CloudTech Solutions"))
                .andExpect(jsonPath("$.endpoints").isArray());
    }

    @Test
    @DisplayName("GET /status deve informar que a aplicação está online (RF05)")
    void statusOnline() throws Exception {
        mockMvc.perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("online"))
                .andExpect(jsonPath("$.mensagem").value("Aplicação de monitoramento em execução"));
    }

    @Test
    @DisplayName("GET /versao deve retornar a versão da aplicação")
    void versao() throws Exception {
        mockMvc.perform(get("/versao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.versao").value("1.0.0"));
    }

    @Test
    @DisplayName("POST /maquinas/cadastro deve cadastrar uma máquina (RF01)")
    void cadastrarMaquina() throws Exception {
        String json = """
                {"nome": "Torno CNC 10", "tipo": "Torno", "setor": "Usinagem", "status": "em operação"}
                """;

        mockMvc.perform(post("/maquinas/cadastro").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Torno CNC 10"))
                .andExpect(jsonPath("$.status").value("em operação"));
    }

    @Test
    @DisplayName("POST /maquinas/cadastro sem campos obrigatórios deve retornar 400")
    void cadastrarMaquinaInvalida() throws Exception {
        String json = """
                {"nome": "", "tipo": "CNC"}
                """;

        mockMvc.perform(post("/maquinas/cadastro").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Dados inválidos"))
                .andExpect(jsonPath("$.detalhes.length()").value(3));
    }

    @Test
    @DisplayName("GET /maquinas deve listar as máquinas cadastradas (RF03)")
    void listarMaquinas() throws Exception {
        mockMvc.perform(get("/maquinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(3)))
                .andExpect(jsonPath("$[*].nome").value(hasItem("Máquina CNC 01")));
    }

    @Test
    @DisplayName("GET /maquinas/{id} inexistente deve retornar 404")
    void maquinaInexistente() throws Exception {
        mockMvc.perform(get("/maquinas/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Máquina com id 9999 não encontrada"));
    }

    @Test
    @DisplayName("PATCH /maquinas/{id}/status deve atualizar o status")
    void atualizarStatus() throws Exception {
        long id = cadastrar("Fresadora 20");

        mockMvc.perform(patch("/maquinas/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"em manutenção\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("em manutenção"));
    }

    @Test
    @DisplayName("POST /producao deve registrar produção e o histórico deve refletir os dados (RF02/RF04)")
    void registrarProducaoEConsultarHistorico() throws Exception {
        long id = cadastrar("Robô de Solda 30");

        registrarProducao(id, 12.5, "08:30", 150);
        registrarProducao(id, 7.5, "14:00", 50);

        mockMvc.perform(get("/producao").param("maquinaId", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].horarioAcionamento").value("08:30"));

        mockMvc.perform(get("/maquinas/" + id + "/historico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRegistros").value(2))
                .andExpect(jsonPath("$.totalPecasProduzidas").value(200))
                .andExpect(jsonPath("$.consumoTotalKw").value(20.0))
                .andExpect(jsonPath("$.consumoMedioKw").value(10.0));
    }

    @Test
    @DisplayName("POST /producao para máquina inexistente deve retornar 404")
    void producaoMaquinaInexistente() throws Exception {
        String json = """
                {"maquinaId": 9999, "consumoKw": 10.0, "horarioAcionamento": "08:30", "pecasProduzidas": 10}
                """;

        mockMvc.perform(post("/producao").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /producao com horário inválido deve retornar 400")
    void producaoHorarioInvalido() throws Exception {
        String json = """
                {"maquinaId": 1, "consumoKw": 10.0, "horarioAcionamento": "25h", "pecasProduzidas": 10}
                """;

        mockMvc.perform(post("/producao").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /producao com consumo negativo deve retornar 400")
    void producaoConsumoNegativo() throws Exception {
        String json = """
                {"maquinaId": 1, "consumoKw": -5, "horarioAcionamento": "08:30", "pecasProduzidas": 10}
                """;

        mockMvc.perform(post("/producao").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes[0]").value("consumoKw: o consumo em kW não pode ser negativo"));
    }

    private long cadastrar(String nome) throws Exception {
        String json = """
                {"nome": "%s", "tipo": "Teste", "setor": "Testes", "status": "em operação"}
                """.formatted(nome);

        MvcResult resultado = mockMvc.perform(post("/maquinas/cadastro")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode corpo = objectMapper.readTree(resultado.getResponse().getContentAsString());
        return corpo.get("id").asLong();
    }

    private void registrarProducao(long maquinaId, double consumo, String horario, int pecas) throws Exception {
        String json = """
                {"maquinaId": %d, "consumoKw": %s, "horarioAcionamento": "%s", "pecasProduzidas": %d}
                """.formatted(maquinaId, consumo, horario, pecas);

        mockMvc.perform(post("/producao").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }
}

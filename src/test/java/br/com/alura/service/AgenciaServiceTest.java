package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.Endereco;
import br.com.alura.domain.enumeration.SituacaoCadastral;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.exception.AgenciaInativaOuNaoEncontradaException;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


@QuarkusTest
public class AgenciaServiceTest {

    @Inject
    private AgenciaService agenciaService;

    @InjectMock
    private AgenciaRepository agenciaRepository;

    @InjectMock
    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    private Agencia agencia;

    @Test
    void deveNaoCadastrarQuandoClientRetornarNull() {

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(null);

        Agencia agencia1 = criarAgencia();

        Assertions.assertThrows(AgenciaInativaOuNaoEncontradaException.class, () ->
                agenciaService.cadastrar(agencia1));

        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia1);

    }

    @Test
    void deveCadastrarQuandoClientRetornarAgenciaAtiva() {

        Agencia agencia1 = criarAgencia();

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(criarAgenciaHttp());

        agenciaService.cadastrar(agencia1);

        Mockito.verify(agenciaRepository).persist(agencia1);

    }

    private Agencia criarAgencia() {
        Endereco endereco = new Endereco(1, "Rua 1", "123", "Bairro 1", 123);
        Agencia agencia = new Agencia( 1l, "Agencia 1", "Razao Social 1", "123", endereco);
        return agencia;
    }

    private AgenciaHttp criarAgenciaHttp() {
        return new AgenciaHttp( "A", "Agencia 1", "12345601000145", "ATIVO");
    }

    private AgenciaHttp agenciaHttpInativa = criarAgenciaHttpInativa();

    @Test
    public void deveNaoCadastrarQuandoClientRetornarAgenciaInativa() {

        Agencia agencia1 = criarAgencia();

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(agenciaHttpInativa);

        Assertions.assertThrows(AgenciaInativaOuNaoEncontradaException.class, () -> agenciaService.cadastrar(agencia1));

        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia1);
    }

    public AgenciaHttp criarAgenciaHttpInativa() {
        return new AgenciaHttp("Agencia Teste", "Razao social da Agencia Teste", "123", "INATIVO");
    }
}

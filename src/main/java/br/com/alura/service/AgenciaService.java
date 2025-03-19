package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.enumeration.SituacaoCadastral;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.exception.AgenciaInativaOuNaoEncontradaException;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AgenciaService {

    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    private List<Agencia> agencias = new ArrayList<>();

    private AgenciaRepository  agenciaRepository;

    public AgenciaService(AgenciaRepository agenciaRepository){
        this.agenciaRepository = agenciaRepository;
    }

    public List<Agencia> listar(){
        return agenciaRepository.listAll();
    }


    public void cadastrar(Agencia agencia){

        AgenciaHttp agenciaHttp = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());

        if(agenciaHttp != null && agenciaHttp.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)){
            Log.info("A agência com o CNPJ: " + agencia.getCnpj() + " foi cadastrada!");
            agenciaRepository.persist(agencia);
        } else {
            Log.info("A agência com o CNPJ: " + agencia.getCnpj() + " NÃO foi cadastrada!");
            throw new AgenciaInativaOuNaoEncontradaException("Agência com CNPJ inativo");
        }

    }

    public Agencia buscarPorId(Long id){
        return agenciaRepository.findById(id);
    }

    public void deletar(Long id){
        agenciaRepository.deleteById(id);
    }

    public void alterar2(Agencia agencia){
        agenciaRepository.update("nome = ?1, razaoSocial = ?2, cnpj = ?3 where id = ?4",
                agencia.getNome(),
                agencia.getRazaoSocial(),
                agencia.getCnpj(),
                agencia.getId());
    }

    public void alterar(Agencia agencia){
        Agencia agencia1 = agenciaRepository.findById(agencia.getId());
        if(agencia1 != null){
            agencia1.setCnpj(agencia.getCnpj());
            agencia1.setNome(agencia.getNome());
            agencia1.setRazaoSocial(agencia.getRazaoSocial());
            agencia1.setEndereco(agencia.getEndereco());
        } else {
            throw new AgenciaInativaOuNaoEncontradaException("Agência não encontrada");
        }
    }
}

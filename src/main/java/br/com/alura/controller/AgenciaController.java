package br.com.alura.controller;

import br.com.alura.domain.Agencia;
import br.com.alura.service.AgenciaService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/agencias")
public class AgenciaController {

    private AgenciaService agenciaService;

    public AgenciaController(AgenciaService agenciaService){
        this.agenciaService = agenciaService;
    }

    @GET
    public RestResponse<List<Agencia>> listar(){
        return RestResponse.ok(agenciaService.listar());
    }

    @POST
    @Transactional
    public RestResponse<Void> criarAgencia(Agencia agencia, @Context UriInfo uriInfo){
        agenciaService.cadastrar(agencia);
        return RestResponse.created(uriInfo.getAbsolutePath());
    }

    @GET
    @Path("{id}")
    public RestResponse<Agencia> buscarPorId(Long id){
        return RestResponse.ok(agenciaService.buscarPorId(id));
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public RestResponse<Void> deletar(Long id){
        agenciaService.deletar(id);
        return RestResponse.noContent();
    }

    @PUT
    @Transactional
    public RestResponse<Void> alterar(Agencia agencia){
        agenciaService.alterar2(agencia);
        return RestResponse.ok();
    }

}

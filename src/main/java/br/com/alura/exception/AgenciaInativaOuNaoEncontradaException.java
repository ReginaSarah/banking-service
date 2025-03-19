package br.com.alura.exception;

public class AgenciaInativaOuNaoEncontradaException extends RuntimeException{

    public AgenciaInativaOuNaoEncontradaException(String message) {
        super(message);
    }
}

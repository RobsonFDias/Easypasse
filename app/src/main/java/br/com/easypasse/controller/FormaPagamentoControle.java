package br.com.easypasse.controller;

import br.com.easypasse.dao.FormaPagamentoDAO;
import br.com.easypasse.model.FormaPagamentoModelo;


public class FormaPagamentoControle {

    private FormaPagamentoDAO formaPagamentoDAO;

    public FormaPagamentoControle() {
        formaPagamentoDAO = new FormaPagamentoDAO();
    }

    public void inserirFormaPagamento(FormaPagamentoModelo formaPagamentoModelo) throws Exception {
        formaPagamentoDAO.insert(formaPagamentoModelo);
    }

    public void deletarFormaPagamento(Integer usuario) throws Exception {
        formaPagamentoDAO.deletarFormaPagamento(usuario);
    }

    public FormaPagamentoModelo buscarFormaPagamentoPeloUsuario(int usuario) throws Exception {
        FormaPagamentoModelo modelo = formaPagamentoDAO.pesquisarFormaPagamentoPeloUsuario(usuario);
        return modelo;
    }
}

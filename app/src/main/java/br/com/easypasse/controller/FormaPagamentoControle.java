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

    public void atualizarFormaPagamento(FormaPagamentoModelo formaPagamentoModelo) throws Exception {
        formaPagamentoDAO.update(formaPagamentoModelo);
    }

    public FormaPagamentoModelo buscarFormaPagamentoId(int id) throws Exception {
        FormaPagamentoModelo modelo = formaPagamentoDAO.pesquisarFormaPagamentoPeloId(id);
        return modelo;
    }
}

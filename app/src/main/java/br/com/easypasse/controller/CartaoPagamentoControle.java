package br.com.easypasse.controller;

import br.com.easypasse.dao.CartaoPagamentoDAO;
import br.com.easypasse.model.CartaoPagamentoModelo;


public class CartaoPagamentoControle {

    private CartaoPagamentoDAO cartaoPagamentoDAO;

    public CartaoPagamentoControle() {
        cartaoPagamentoDAO = new CartaoPagamentoDAO();
    }

    public void inserirCartao(CartaoPagamentoModelo cartaoPagamentoModelo) throws Exception {
        cartaoPagamentoDAO.insert(cartaoPagamentoModelo);
    }

    public CartaoPagamentoModelo buscarCartaoPeloUsuario(int usuario) throws Exception {
        CartaoPagamentoModelo modelo = cartaoPagamentoDAO.pesquisarCartaoPeloUsuario(usuario);
        return modelo;
    }

    public void deletarCartao(int usuario) throws Exception {
        cartaoPagamentoDAO.deletarCartao(usuario);
    }
}

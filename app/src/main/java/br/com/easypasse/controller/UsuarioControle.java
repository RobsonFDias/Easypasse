package br.com.easypasse.controller;

import br.com.easypasse.dao.UsuarioDAO;
import br.com.easypasse.model.UsuarioModelo;


public class UsuarioControle {

	private UsuarioDAO usuarioDAO;

	public UsuarioControle() {
		usuarioDAO = new UsuarioDAO();
	}

	public void inserirUsuario(UsuarioModelo usuario) throws Exception {
		usuarioDAO.insert(usuario);
	}
	
	public void atualizarUsuario(UsuarioModelo usuario)	throws Exception {
		usuarioDAO.update(usuario);
	}
	
	public UsuarioModelo buscarUsuarioId(int id) throws Exception {
		 UsuarioModelo modelo = usuarioDAO.pesquisarUsuarioPeloId(id);
		 return modelo;
	}

	public UsuarioModelo buscarUsuarioLogado() throws Exception {
		UsuarioModelo modelo = usuarioDAO.pesquisarUsuarioLogado();
		return modelo;
	}

	public void deletarUsuario(Integer usuario)	throws Exception {
		usuarioDAO.deletarUsuario(usuario);
	}
}

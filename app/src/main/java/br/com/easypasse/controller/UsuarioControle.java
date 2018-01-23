package com.example.gustavo.easypasse.controller;

import com.example.gustavo.easypasse.dao.UsuarioDAO;
import com.example.gustavo.easypasse.model.UsuarioModelo;

import java.util.List;


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

	public void deletarUsuario(Integer usuario)	throws Exception {
		usuarioDAO.deletarUsuario(usuario);
	}
}

package com.example.gustavo.easypasse.dao;

public class Tabelas {
    public static final String CRIAR_USUARIO = "CREATE TABLE USUARIO(ID INTEGER(99), CPF VARCHAR(50), NOME VARCHAR(50), EMAIL VARCHAR(50), SENHA VARCHAR(20), TELEFONE VARCHAR(100), CIDADE VARCHAR(50), SEXO VARCHAR(10), LOGADO VARCHAR(3))";
    public static final String DELETAR_USUARIO = "DROP TABLE USUARIO";
}

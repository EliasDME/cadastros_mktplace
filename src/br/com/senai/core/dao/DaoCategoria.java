package br.com.senai.core.dao;

import java.util.List;

import br.com.senai.core.domain.Categoria;

public interface DaoCategoria {
	
	public void inserir(Categoria categoria);
	
	public void alterar(Categoria alterar);
	
	public void excluirPor(int id);
	
	public Categoria bucarPor(int id);
	
	public List<Categoria> listarPor(String nome);

}

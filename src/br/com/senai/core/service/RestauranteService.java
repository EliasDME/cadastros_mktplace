package br.com.senai.core.service;

import java.util.List;

import br.com.senai.core.dao.DaoHorario;
import br.com.senai.core.dao.DaoRestaurante;
import br.com.senai.core.dao.FactoryDao;
import br.com.senai.core.domain.Categoria;
import br.com.senai.core.domain.Restaurante;

public class RestauranteService {
	
	private DaoRestaurante dao;
	private DaoHorario daoHorario;
	
	public RestauranteService() {
		this.dao = FactoryDao.getInstance().getDaoRestaurante();
		this.daoHorario = FactoryDao.getInstance().getDaoHorario();
	}
	
	public void salvar(Restaurante restaurante) {
		this.validar(restaurante);
		
		boolean isJaInserido = restaurante.getId() > 0;
		if(isJaInserido) {
			this.dao.alterar(restaurante);
		}else {
			this.dao.inserir(restaurante);
		}
	}
	
	public void validar(Restaurante restaurante) {
		if(restaurante != null) {

			if(restaurante.getEndereco() != null) {
				
				if(restaurante.getCategoria() != null && restaurante.getCategoria().getId() > 0) {
					
					boolean isNomeInvalido = restaurante.getNome() == null
							|| restaurante.getNome().isBlank()
							|| restaurante.getNome().length() > 250;
							
					if(isNomeInvalido) {
						throw new IllegalArgumentException("O nome é obrigatório"
								  + "e deve conter menos de 250 caracteres");
					}
							
					boolean isDescricaoInvalida = restaurante.getDescricao() == null
							|| restaurante.getDescricao().isBlank();
					
					if(isDescricaoInvalida) {
						throw new IllegalArgumentException("A descrição é obrigatória");
					}
					
					boolean isLogradouroInvalido = restaurante.getEndereco().getLogradouro() == null
							|| restaurante.getEndereco().getLogradouro().isBlank()
							|| restaurante.getEndereco().getLogradouro().length() > 200;
							
					if(isLogradouroInvalido) {
						throw new IllegalArgumentException("O logradouro do endereço é obrigatório"
								+ "e não deve possuir mais de 200 caracteres");
					}
					
					boolean isCidadeInvalide = restaurante.getEndereco().getCidade() == null
							|| restaurante.getEndereco().getCidade().isBlank()
							|| restaurante.getEndereco().getCidade().length() > 80;
							
					if(isCidadeInvalide) {
						throw new IllegalArgumentException("A cidade do endereço é obrigatória"
								+ "e não deve possuir mais de 80 caracteres");
					}
					
					boolean isBairroInvalido = restaurante.getEndereco().getBairro() == null
							|| restaurante.getEndereco().getBairro().isBlank()
							|| restaurante.getEndereco().getBairro().length() > 50;
							
					if(isBairroInvalido) {
						throw new IllegalArgumentException("O bairro do endereço é obrigatório"
								+ "e deve conter até 50 caracteres");
					}
							
				}else {
					throw new IllegalArgumentException("A categoria do restaurante é obrigatória");
				}
				
			}else {
				throw new NullPointerException("O endereço não pode ser nulo");
			}
			
		}else {
			throw new NullPointerException("O restaurante não pode ser null");
		}
	}
	
	public List<Restaurante> listarPor(String nome, Categoria categoria){
		
		boolean isCategoriaInformada = categoria != null && categoria.getId() > 0;
		
		boolean isNomeInformado = nome != null && !nome.isBlank();
		
		if(!isCategoriaInformada && !isNomeInformado) {
			throw new IllegalArgumentException("Informe o nome e a categoria"
					+ "para listagem");
		}
		
		String filtroNome = "";
		
		if(isCategoriaInformada) {
			filtroNome = nome + "%";
		}else {
			filtroNome = "%" + nome + "%";
		}
		
		return dao.listarPor(filtroNome, categoria);
	}
	
	public List<Restaurante> listarTodos(){
		return dao.listarPor("%%", null);
	}
	
	public void excluirPor(int id) {
		if(id > 0) {
			
			int qtdeDeHorario = daoHorario.contarPor(id);
			
			boolean isExisteHorarioVinculado = qtdeDeHorario > 0;
			
			if(isExisteHorarioVinculado) {
				throw new IllegalArgumentException("Não foi possível excluir o restaurante. "
						+ "Motivo: Existem " + qtdeDeHorario + " horários vinculados ao restaurante");
			}
			
			this.dao.excluirPor(id);
		}else {
			throw new IllegalArgumentException("O id para exclusão deve ser maior que 0");
		}
	}
	
	public Restaurante buscarPor(int idDoRestaurante) {
		if(idDoRestaurante > 0) {
			Restaurante restauranteEncontrado = this.dao.buscaPor(idDoRestaurante);
			
			if(restauranteEncontrado == null) {
				throw new IllegalArgumentException("Não existe restaurante vinculado ao id informado");
			}
			return restauranteEncontrado;
		}
			
		else {
			 throw new IllegalArgumentException("O id para busca não pode ser menor do que 0");
		}
		
	}

}

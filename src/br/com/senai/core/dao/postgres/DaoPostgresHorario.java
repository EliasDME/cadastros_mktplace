package br.com.senai.core.dao.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import br.com.senai.core.dao.DaoHorario;
import br.com.senai.core.dao.ManagerDb;

public class DaoPostgresHorario implements DaoHorario {
	
	private Connection conexao;
	
	private final String COUNT_BY_REST = "SELECT Count (*) qtde "
			+ "FROM horarios_atendimento h "
			+ "WHERE h.id_restaurante = ?";
	
	public DaoPostgresHorario() {
		this.conexao = ManagerDb.getInstance().getConexao();
	}
	
	public int contarPor(int idDoRestaurante) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conexao.prepareStatement(COUNT_BY_REST);
			ps.setInt(1, idDoRestaurante);
			rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getInt("qtde");
			}
			return 0;

		} catch (Exception e) {
			throw new RuntimeException("Ocorreu um erro ao contar os horários. "
					+ "Motivo: " + e.getMessage());
		}finally {
			ManagerDb.getInstance().fechar(ps);
			ManagerDb.getInstance().fechar(rs);
		}
	}

}

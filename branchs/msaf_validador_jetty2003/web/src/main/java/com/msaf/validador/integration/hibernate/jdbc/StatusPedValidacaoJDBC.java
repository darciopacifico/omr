package com.msaf.validador.integration.hibernate.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.msaf.validador.consultaonline.exceptions.ValidadorRuntimeException;


public class StatusPedValidacaoJDBC implements IStatusPedValidacaoJDBC {
	
	
	private ComboPooledDataSource dataSource;

	public StatusPedValidacaoJDBC(){}
	
	public StatusPedValidacaoJDBC(ComboPooledDataSource dataSource){
		this.dataSource = dataSource;
	}
	
	
	public Connection getCon() throws SQLException {
		return dataSource.getConnection();
	}
	
	/* (non-Javadoc)
	 * @see com.msaf.validador.integration.hibernate.jdbc.IStatusPedValidacao#getListaStatusPedidoValidacao(java.lang.String)
	 */
	public List<StatusPediValidacao> getListaStatusPedidoValidacao(String idPedValidacao) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<StatusPediValidacao> list = new ArrayList<StatusPediValidacao>();
		
		String query = "  SELECT   " + 
		"  dri.tipovalidacaofk_id idTipoValidacao, " + 
		"  tpv.descricao descricaoValidacao, " + 
		"  dri.tiporesultadofk_id idTipoResultado, " + 
		"  tpr.descricao descricaoResultado, " + 
		"  count(pdr.dadosretornofk_id) qtTotal " + 
		"  from  " + 
		"  dadosretinstvo dri " + 
		"     inner join PESSOAVO_DADOSRETINSTVO pdr on pdr.dadosretornofk_id = dri.id " + 
		"     inner join pessoavo p on p.id = pdr.pessoavo_id " + 
		"     left OUTER join tpresultvo tpr on tpr.id = dri.tiporesultadofk_id " + 
		"     left OUTER join tpvalidvo tpv on tpv.id = dri.tipovalidacaofk_id " + 
		"     where p.pedvalidfk_id = ? " + 
		"   group by  dri.tipovalidacaofk_id, " + 
		"  tpv.descricao, " + 
		"  dri.tiporesultadofk_id, " + 
		"  tpr.descricao " + 
		"  Order by dri.tipovalidacaofk_id,dri.tiporesultadofk_id ";

		try {
			con = getCon();
			stmt = con.prepareStatement(query);
			stmt.setInt(1, Integer.parseInt(idPedValidacao));
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				StatusPediValidacao statusPediValidacao = new StatusPediValidacao();

				statusPediValidacao.setIdTipoValidacao(rs.getString("idTipoValidacao"));
				statusPediValidacao.setDescricaoValidacao(rs.getString("descricaoValidacao"));
				statusPediValidacao.setIdTipoResultado(rs.getString("idTipoResultado"));
				statusPediValidacao.setDescricaoResultado(rs.getString("descricaoResultado"));
				statusPediValidacao.setQtTotal(Integer.parseInt(rs.getString("qtTotal")));
				
				list.add(statusPediValidacao );
			}
			
		} catch (Exception e) {
			throw new ValidadorRuntimeException("Erro ao tentar determinar status do pedido!",e);
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			} catch (SQLException e) {}
		}
		return list;
	}
	


}

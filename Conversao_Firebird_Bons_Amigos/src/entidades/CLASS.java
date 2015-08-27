package entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class CLASS {
	Connection antigo = Conexao.getSqlConnectionAntigo();
	Connection dmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblRegistros) throws Exception {
		String antigoGRUPO = "select * from GRUPOSPRO";
		String dCLASS = "Insert Into DBO.Class (Cod_Classi, Des_Classi) Values (?,?)";
		try (PreparedStatement pVmd = dmd.prepareStatement(dCLASS);
			 PreparedStatement cAntigo = antigo.prepareStatement(antigoGRUPO)) {
			
			ResultSet rs = cAntigo.executeQuery();
			
			// contar a qtde de registros
			int registros = a.contaRegistros("GRUPOSPRO");
			int total = registros;
			progressBar2.setMaximum(registros);
			registros = 0;
			
			while (rs.next()) {
				
				// grava no varejo
				pVmd.setInt(1, rs.getInt("GRU_CODIGO"));
				
				String descricao = rs.getString("GRU_DESCRICAO");
				
				if(descricao != null) {
					 pVmd.setString(2, descricao.length() > 30 ? descricao.substring(0, 30) : descricao);
				} else {
					pVmd.setString(2, null);
				}		
				
				pVmd.executeUpdate();

				registros++;
				lblRegistros.setText(registros+"/"+total);
				progressBar2.setValue(registros);
			}
			
			System.out.println("Funcionou CLASS");
			pVmd.close();
			cAntigo.close();
			progressBar2.setValue(0);

		}
	}

}

package entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class CLTRI {
	Connection antigo = Conexao.getSqlConnectionAntigo();
	Connection dmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblRegistros) throws Exception {
		String antigoGENEROS = "select * from GENEROS";
		String dCLTRI = "Insert Into DBO.Cltri (Cod_ClaTri, Des_ClaTri) Values (?,?)";
		try (PreparedStatement pVmd = dmd.prepareStatement(dCLTRI);
			 PreparedStatement cAntigo = antigo.prepareStatement(antigoGENEROS)) {
			
			ResultSet rs = cAntigo.executeQuery();
			
			// contar a qtde de registros
			int registros = a.contaRegistros("GENEROS");
			int total = registros;
			progressBar2.setMaximum(registros);
			registros = 0;
			
			while (rs.next()) {
				
				// grava no varejo
				pVmd.setInt(1, rs.getInt("GEN_CODIGO"));
				
				String descricao = rs.getString("GEN_DESCRICAO");
				
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
			
			System.out.println("Funcionou CLTRI");
			pVmd.close();
			cAntigo.close();
			progressBar2.setValue(0);

		}
	}

}

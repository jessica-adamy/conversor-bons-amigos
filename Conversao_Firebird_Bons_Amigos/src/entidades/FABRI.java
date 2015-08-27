package entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class FABRI {
	Connection antigo = Conexao.getSqlConnectionAntigo();
	Connection vmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblRegistros) throws Exception {
		String antigoFABRICANTE = "SELECT * FROM MARCAS";
		String dFABRI = "Insert Into FABRI (Cod_Fabric, Des_Fabric) Values (?,?)";
		try (PreparedStatement pVmd = vmd.prepareStatement(dFABRI);
			 PreparedStatement pAntigo = antigo.prepareStatement(antigoFABRICANTE)) {
			
			ResultSet rs = pAntigo.executeQuery();
			
			// contar a qtde de registros
			int registros = a.contaRegistros("MARCAS");
			int total = registros;
			progressBar2.setMaximum(registros);
			registros = 0;
			
			while (rs.next()) {
				// grava no varejo
				pVmd.setInt(1, rs.getInt("MAR_CODIGO"));
			    pVmd.setString(2, rs.getString("MAR_DESCRICAO").length() > 25 ? rs.getString("MAR_DESCRICAO").substring(0, 24) : rs.getString("MAR_DESCRICAO"));
				
				pVmd.executeUpdate();

				registros++;
				lblRegistros.setText(registros+"/"+total);
				progressBar2.setValue(registros);
			}
			
			System.out.println("Funcionou FABRI");
			pVmd.close();
			pAntigo.close();
			progressBar2.setValue(0);

		}
	}
}


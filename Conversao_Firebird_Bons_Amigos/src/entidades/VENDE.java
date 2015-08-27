package entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class VENDE {
	Connection antigo = Conexao.getSqlConnectionAntigo();
	Connection vmd = Conexao.getSqlConnection();
	Connection vmdAux = Conexao.getSqlConnectionAux();
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblRegistros) throws Exception {
		String antigoFUNCIONARIOS = "select * from FUNCIONARIOS";
		String dVENDE = "Insert Into DBO.VENDE (Cod_Vended, Nom_Vended, Num_Cpf, Cod_TabCom) Values (?,?,?,1)";
		String dTBCOM = "IF NOT EXISTS(SELECT 1 FROM DBO.TBCOM WHERE Cod_TabCom = 1) Insert Into DBO.TBCOM (Cod_TabCom, Des_TabCom) Values (1,'PADRÃO')";
		try (PreparedStatement pVmd = vmd.prepareStatement(dVENDE);
			 PreparedStatement cAntigo = antigo.prepareStatement(antigoFUNCIONARIOS);
				PreparedStatement pTBCOM = vmd.prepareStatement(dTBCOM)) {
			
			pTBCOM.executeUpdate();
			ResultSet rs = cAntigo.executeQuery();
			
			// contar a qtde de registros
			int registros = a.contaRegistros("FUNCIONARIOS");
			int total = registros;
			progressBar2.setMaximum(registros);
			registros = 0;
			
			while (rs.next()) {
				
				// grava no varejo
				pVmd.setInt(1, a.prox(vmdAux, "Cod_Vended", "VENDE"));
				
				String nom_Vended = rs.getString("FUN_NOME");
				if (nom_Vended != null) {
					pVmd.setString(2, nom_Vended.length() > 35 ? nom_Vended.substring(0, 35) : nom_Vended);		
				} else {
					pVmd.setString(2, null);
				}
				
				String num_CpfCgc = rs.getString("FUN_CPF");
				if (num_CpfCgc != null) {
					num_CpfCgc = num_CpfCgc.replaceAll("\\D", "");
					pVmd.setString(3, num_CpfCgc.length() > 11 ? num_CpfCgc.substring(0, 11) : num_CpfCgc);		
				} else {
					pVmd.setString(3, null);
				}

				
				pVmd.executeUpdate();

				registros++;
				lblRegistros.setText(registros+"/"+total);
				progressBar2.setValue(registros);
			}
			
			System.out.println("Funcionou VENDE");
			pVmd.close();
			cAntigo.close();
			progressBar2.setValue(0);

		}
	}

}

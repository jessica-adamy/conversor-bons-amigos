package entidades;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class FORNE {
	Connection antigo = Conexao.getSqlConnectionAntigo();
	Connection vmd_aux = Conexao.getSqlConnection();
	Connection vmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblRegistros) throws Exception {
		String antigoFORNECEDOR = "select * from CREDORES";
		String dFORNE = "Insert Into FORNE (Cod_Fornec, Des_RazSoc, Des_Fantas, Num_CgcCpf, Num_CgfRg, Des_Endere, Des_Bairro, Des_Cidade, Des_Estado, Cod_RegTri, Num_Cep, Num_Fone, Num_Fax, Nom_Contat, Flg_Bloque) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,0)";
		String vmdRgtri1 = "IF NOT EXISTS(SELECT 1 FROM DBO.Rgtri WHERE Cod_RegTri = 4) INSERT INTO DBO.Rgtri (Cod_RegTri, Des_RegTri) VALUES (4, 'LOCAL')";
		String vmdRgtri2 = "IF NOT EXISTS(SELECT 1 FROM DBO.Rgtri WHERE Cod_RegTri = 1) INSERT INTO DBO.Rgtri (Cod_RegTri, Des_RegTri) VALUES (1, 'CEARÁ')";
		String vmdRgtri3 = "IF NOT EXISTS(SELECT 1 FROM DBO.Rgtri WHERE Cod_RegTri = 9) INSERT INTO DBO.Rgtri (Cod_RegTri, Des_RegTri) VALUES (9, 'SUL/SUDESTE')";
		try (PreparedStatement pVmd = vmd.prepareStatement(dFORNE);
			 PreparedStatement pAntigo = antigo.prepareStatement(antigoFORNECEDOR);
			 PreparedStatement grtriVmd1 = vmd.prepareStatement(vmdRgtri1);
			 PreparedStatement grtriVmd2 = vmd.prepareStatement(vmdRgtri2);
			 PreparedStatement grtriVmd3 = vmd.prepareStatement(vmdRgtri3)
					 ) {
			
			
			grtriVmd1.executeUpdate();
			grtriVmd2.executeUpdate();
			grtriVmd3.executeUpdate();
			
			// contar a qtde de registros
			ResultSet rs = pAntigo.executeQuery();

			// contar a qtde de registros
			int registros = a.contaRegistros("CREDORES");
			int total = registros;
			progressBar2.setMaximum(registros);
			registros = 0;

			while (rs.next()) {
				// grava no varejo
				pVmd.setInt(1, a.prox(vmd_aux, "Cod_Fornec", "FORNE"));
			  
				String des_RazSoc = rs.getString("CRE_NOME");	
				if (des_RazSoc != null) {
					pVmd.setString(2, des_RazSoc.length() > 35 ? des_RazSoc.substring(0, 35) : des_RazSoc);						
				} else {
					pVmd.setString(2, null);		
				}
				
				String des_Fantas = rs.getString("CRE_FANTASIA");	
				if (des_Fantas != null) {
					pVmd.setString(3, des_Fantas.length() > 25 ? des_Fantas.substring(0, 25) : des_Fantas);						
				} else {
					pVmd.setString(3, null);		
				}
				
				String num_CgcCpf = rs.getString("CRE_CNPJ");	
				if (num_CgcCpf != null) {
					pVmd.setString(4, num_CgcCpf.length() > 14 ? num_CgcCpf.substring(0, 14) : num_CgcCpf);						
				} else {
					pVmd.setString(4, null);		
				}
				
				String num_CgfRg = rs.getString("CRE_CGF");	
				if (num_CgfRg != null) {
					pVmd.setString(5, num_CgfRg.length() > 15 ? num_CgfRg.substring(0, 15) : num_CgfRg);						
				} else {
					pVmd.setString(5, null);		
				}

				String des_Endere = rs.getString("CRE_ENDERECO");	
				if (des_Endere != null) {
					pVmd.setString(6, des_Endere.length() > 35 ? des_Endere.substring(0, 35) : des_Endere);						
				} else {
					pVmd.setString(6, null);		
				}

				String des_Bairro = rs.getString("CRE_BAIRRO");	
				if (des_Bairro != null) {
					pVmd.setString(7, des_Bairro.length() > 25 ? des_Bairro.substring(0, 25) : des_Bairro);						
				} else {
					pVmd.setString(7, null);		
				}

				String des_Cidade = rs.getString("CRE_CIDADE");	
				if (des_Cidade != null) {
					pVmd.setString(8, des_Cidade.length() > 25 ? des_Cidade.substring(0, 25) : des_Cidade);						
				} else {
					pVmd.setString(8, null);		
				}
				
				String des_Estado = rs.getString("CRE_UF");	
				if (des_Estado != null) {
					pVmd.setString(9, des_Estado.length() > 2 ? des_Estado.substring(0, 2) : des_Estado);
					
					//Cod_RegTri
					String estadoUpper = des_Estado.toUpperCase();
					 if (estadoUpper.equals("CE")) {
						 pVmd.setInt(10, 1);
						} else {
							if(estadoUpper.equals("AC") || estadoUpper.equals("AL") || estadoUpper.equals("AP") ||
							   estadoUpper.equals("AM") || estadoUpper.equals("BA") || estadoUpper.equals("DF") ||
							   estadoUpper.equals("GO") || estadoUpper.equals("MA") || estadoUpper.equals("MT") ||
							   estadoUpper.equals("MS") || estadoUpper.equals("PB") || estadoUpper.equals("PA") ||
							   estadoUpper.equals("PE") || estadoUpper.equals("PI") || estadoUpper.equals("RN") ||
							   estadoUpper.equals("RO") || estadoUpper.equals("RR") || estadoUpper.equals("SE") || estadoUpper.equals("TO")){
								pVmd.setInt(10, 4);
							} else {
								if(estadoUpper.equals("ES") || estadoUpper.equals("MG") ||
								   estadoUpper.equals("PR") || estadoUpper.equals("RJ") || 
								   estadoUpper.equals("RS") || estadoUpper.equals("SC") || 
								   estadoUpper.equals("SP")){
									pVmd.setInt(10, 9);
								}else {
									pVmd.setString(10, null);
								}
						    }
						 } 
					
					
				} else {
					pVmd.setString(9, null);
					pVmd.setString(10, null);
				}

				String Num_Cep = rs.getString("CRE_CEP");	
				if (Num_Cep != null) {
					pVmd.setString(11, Num_Cep.length() > 8 ? Num_Cep.substring(0, 8) : Num_Cep);						
				} else {
					pVmd.setString(11, null);		
				}

				String num_Fone = rs.getString("CRE_FONE");	
				if (num_Fone != null) {
					pVmd.setString(12, num_Fone.length() > 11 ? num_Fone.substring(0, 11) : num_Fone);						
				} else {
					pVmd.setString(12, null);		
				}
				
				String num_Fax = rs.getString("CRE_FAX");	
				if (num_Fax != null) {
					pVmd.setString(13, num_Fax.length() > 11 ? num_Fax.substring(0, 11) : num_Fax);						
				} else {
					pVmd.setString(13, null);		
				}

				String nom_Contat = rs.getString("CRE_CONTATO");	
				if (nom_Contat != null) {
					pVmd.setString(14, nom_Contat.length() > 25 ? nom_Contat.substring(0, 25) : nom_Contat);						
				} else {
					pVmd.setString(14, null);		
				}

			    
				// todo demais campo
				pVmd.executeUpdate();

				registros++;
				lblRegistros.setText(registros+"/"+total);
				progressBar2.setValue(registros);
			}
			System.out.println("Funcionou FORNE");
			pVmd.close();
			pAntigo.close();

			progressBar2.setValue(0);
		}
	}
}
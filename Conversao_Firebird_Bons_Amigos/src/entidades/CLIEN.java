package entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class CLIEN {
	Connection antigo = Conexao.getSqlConnectionAntigo();
	Connection vmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblRegistros) throws Exception {
		String antigoCLIENTE = "SELECT * FROM CLIENTES";
		String dmdCLIEN = "set dateformat ymd Insert Into CLIEN (Cod_Client, Nom_Client, Dat_Cadast, Sex_Client, Num_CpfCgc, Num_RGCgf, Num_FonCel, Des_Email, Des_Observ, Cod_EndRes, Nom_PaiCli, Nom_MaeCli, Cod_RegTri, Ctr_Vencim, Cod_GrpCli) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,'N', 999)";
		String vmdRgtri = "IF NOT EXISTS(SELECT 1 FROM DBO.Rgtri WHERE Cod_RegTri = 999) INSERT INTO DBO.Rgtri (Cod_RegTri, Des_RegTri) VALUES (999, 'Local')";
		String vmdGrpCli = "IF NOT EXISTS(SELECT 1 FROM DBO.GrCli WHERE Cod_GrpCli = 999) INSERT INTO DBO.GrCli (Cod_GrpCli, Des_GrpCli) VALUES (999, 'A CADASTRAR')";
		try (PreparedStatement pVmd = vmd.prepareStatement(dmdCLIEN);
			 PreparedStatement pAntigo = antigo.prepareStatement(antigoCLIENTE);
			 PreparedStatement grtriVmd = vmd.prepareStatement(vmdRgtri);
			 PreparedStatement grpcliVmd = vmd.prepareStatement(vmdGrpCli)) {
			
			//grtriVmd.executeUpdate();
			grpcliVmd.executeUpdate();
			ResultSet rs = pAntigo.executeQuery();

			// contar a qtde de registros
			int registros = a.contaRegistros("CLIENTES");
			int total = registros;
			progressBar2.setMaximum(registros);
			registros = 0;

			while (rs.next()) {
				// grava no varejo
				int cod = rs.getInt("CLI_CODIGO");
				pVmd.setInt(1,cod);
				
				String nom_Client = rs.getString("CLI_NOME");
				if (nom_Client != null) {
					pVmd.setString(2, nom_Client.length() > 35 ? nom_Client.substring(0, 35) : nom_Client);		
				} else {
					pVmd.setString(2, null);
				}
				
			    SimpleDateFormat formata = new SimpleDateFormat("yyyy-MM-dd");
			    java.util.Date minhaData = formata.parse("1899-12-30");
			    
			    if (rs.getString("CLI_DATACADASTRO") != null) {
			    	java.util.Date data_cadastro = formata.parse(rs.getString("CLI_DATACADASTRO"));
				    if (data_cadastro.before(minhaData) || data_cadastro.equals(minhaData)) {
				    	pVmd.setString(3,  null);
				    } else {
				    	pVmd.setDate(3,  rs.getDate("CLI_DATACADASTRO"));
				    }
			    }

				String sex_Client = rs.getString("CLI_SEXO");
				pVmd.setString(4,sex_Client);
				
				String num_CpfCgc = rs.getString("CLI_CPF_CNPJ");
				if (num_CpfCgc != null) {
					num_CpfCgc = num_CpfCgc.replaceAll("\\D", "");
					pVmd.setString(5, num_CpfCgc.length() > 14 ? num_CpfCgc.substring(0, 14) : num_CpfCgc);		
				} else {
					pVmd.setString(5, null);
				}

				String num_RGCgf = rs.getString("CLI_IDENTIDADE");
				if (num_RGCgf != null) {
					num_RGCgf = num_RGCgf.replaceAll("\\D", "");
					pVmd.setString(6, num_RGCgf.length() > 15 ? num_RGCgf.substring(0, 15) : num_RGCgf);		
				} else {
					pVmd.setString(6, null);
				}
				
				String num_FonCel = rs.getString("CLI_CELULAR");
				if (num_FonCel != null) {
					num_FonCel = num_FonCel.replaceAll("\\D", "");
					pVmd.setString(7, num_FonCel.length() > 50 ? num_FonCel.substring(0, 50) : num_FonCel);		
				} else {
					pVmd.setString(7, null);
				}

				String des_Email = rs.getString("CLI_EMAIL");
				if (des_Email != null) {
					pVmd.setString(8, des_Email.length() > 50 ? des_Email.substring(0, 50) : des_Email);		
				} else {
					pVmd.setString(8, null);
				}
				
				String des_Observ = rs.getString("CLI_OBS");
				if (des_Observ != null) {
					pVmd.setString(9, des_Observ.length() > 16 ? des_Observ.substring(0, 16) : des_Observ);		
				} else {
					pVmd.setString(9, null);
				}

				String cod_EndRes = rs.getString("CLI_FONE");
				if (cod_EndRes != null) {
					cod_EndRes = cod_EndRes.replaceAll("\\D", "");
					pVmd.setString(10, cod_EndRes.length() > 50 ? cod_EndRes.substring(0, 50) : cod_EndRes);		
				} else {
					pVmd.setString(10, null);
				}

				String nom_PaiCli = rs.getString("CLI_PAI");
				if (nom_PaiCli != null) {
					pVmd.setString(11, nom_PaiCli.length() > 35 ? nom_PaiCli.substring(0, 35) : nom_PaiCli);		
				} else {
					pVmd.setString(11, null);
				}
				
				String nom_MaeCli = rs.getString("CLI_MAE");
				if (nom_MaeCli != null) {
					pVmd.setString(12, nom_MaeCli.length() > 35 ? nom_MaeCli.substring(0, 35) : nom_MaeCli);		
				} else {
					pVmd.setString(12, null);
				}

//				Cod_RegTri		CLI_UF
				String des_Estado = rs.getString("CLI_UF");	
				if (des_Estado != null) {

					String estadoUpper = des_Estado.toUpperCase();
					 if (estadoUpper.equals("CE")) {
						 pVmd.setInt(13, 1);
						} else {
							if(estadoUpper.equals("AC") || estadoUpper.equals("AL") || estadoUpper.equals("AP") ||
							   estadoUpper.equals("AM") || estadoUpper.equals("BA") || estadoUpper.equals("DF") ||
							   estadoUpper.equals("GO") || estadoUpper.equals("MA") || estadoUpper.equals("MT") ||
							   estadoUpper.equals("MS") || estadoUpper.equals("PB") || estadoUpper.equals("PA") ||
							   estadoUpper.equals("PE") || estadoUpper.equals("PI") || estadoUpper.equals("RN") ||
							   estadoUpper.equals("RO") || estadoUpper.equals("RR") || estadoUpper.equals("SE") || estadoUpper.equals("TO")){
								pVmd.setInt(13, 4);
							} else {
								if(estadoUpper.equals("ES") || estadoUpper.equals("MG") ||
								   estadoUpper.equals("PR") || estadoUpper.equals("RJ") || 
								   estadoUpper.equals("RS") || estadoUpper.equals("SC") || 
								   estadoUpper.equals("SP")){
									pVmd.setInt(13, 9);
								}else {
									pVmd.setString(13, null);
								}
						    }
						 } 
					
					
				} else {
					pVmd.setString(13, null);
				}		    
	

				pVmd.executeUpdate();

				registros++;
				lblRegistros.setText(registros+"/"+total);
				progressBar2.setValue(registros);
			}
			System.out.println("Funcionou CLIEN");
			pVmd.close();
			pAntigo.close();

			progressBar2.setValue(0);
		}
	}

	}
	

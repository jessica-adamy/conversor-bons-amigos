package entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class ENDER {
	Connection antigo = Conexao.getSqlConnectionAntigo();
	Connection vmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblRegistros) throws Exception {
		String fbENDER = "SELECT C.*, M.mun_nome FROM CLIENTES C INNER JOIN MUNICIPIOS M ON (M.mun_codigo = C.mun_codigo)";
		String vENDER = "Insert Into ENDER (Cod_EndFon, Des_Endere, Des_Bairro, Num_Cep, Des_Cidade, Des_Estado, Des_PtoRef, Nom_Contat, Dat_Cadast) Values (?,?,?,?,?,?,?,?,?)";
		String vCLXED = "Insert Into CLXED (Cod_Client, Cod_EndFon) Values (?,?)";
		String vCLIEN = "Update CLIEN set Cod_EndRes = ? where Cod_Client = ?";
		try (PreparedStatement pVmd = vmd.prepareStatement(vENDER);
				 PreparedStatement pFb = antigo.prepareStatement(fbENDER);
				 PreparedStatement pVmdCLXED = vmd.prepareStatement(vCLXED);
				 PreparedStatement pVmdCLIEN = vmd.prepareStatement(vCLIEN)
						 ){
			
			ResultSet rs = pFb.executeQuery();

			// contar a qtde de registros
			int registros = a.contaRegistros("(SELECT C.*, M.mun_nome FROM CLIENTES C INNER JOIN MUNICIPIOS M ON (M.mun_codigo = C.mun_codigo)) a");
			int total = registros;
			progressBar2.setMaximum(registros);
			registros = 0;

			while (rs.next()) {
				// grava no varejo
				
				String wfone = "";
				String fone = rs.getString("CLI_FONE");
				if(fone != null){
				fone = fone.replaceAll("\\D", "");
				
				if(!fone.equals("") && !existeCod_EndFon(fone)){
					wfone = fone;
				}else{
					wfone = "000"+rs.getString("CLI_CODIGO");
				}
				}else{
					wfone = "000"+rs.getString("CLI_CODIGO");
				}
				pVmd.setString(1, wfone);
				
				String des_Endere = rs.getString("CLI_ENDERECO");
				if (des_Endere != null) {
					pVmd.setString(2, des_Endere.length() > 45 ? des_Endere.substring(0, 45) : des_Endere);					
				} else {
					pVmd.setString(2, null);
				}
				
				String des_Bairro = rs.getString("CLI_BAIRRO");
				if (des_Bairro != null) {
					pVmd.setString(3, des_Bairro.length() > 25 ? des_Bairro.substring(0, 25) : des_Bairro);					
				} else {
					pVmd.setString(3, null);
				}

				String num_Cep = rs.getString("CLI_CEP");
				if (num_Cep != null) {
					num_Cep = num_Cep.replaceAll("\\D", "");
					pVmd.setString(4, num_Cep.length() > 8 ? num_Cep.substring(0, 8) : num_Cep);					
				} else {
					pVmd.setString(4, null);
				}

				String des_Cidade = rs.getString("MUN_NOME");
				if (des_Cidade != null) {
					pVmd.setString(5, des_Cidade.length() > 25 ? des_Cidade.substring(0, 25) : des_Cidade);					
				} else {
					pVmd.setString(5, null);
				}
				
				String des_Estado = rs.getString("CLI_UF");
				pVmd.setString(6, des_Estado);
				
				String des_PtoRef = rs.getString("CLI_COMPL_ENDERECO");
				if (des_PtoRef != null) {
					pVmd.setString(7, des_PtoRef.length() > 160 ? des_PtoRef.substring(0, 160) : des_PtoRef);					
				} else {
					pVmd.setString(7, null);
				}
				
				String nom_Contat = rs.getString("CLI_NOME");
				if (nom_Contat != null) {
					pVmd.setString(8, nom_Contat.length() > 35 ? nom_Contat.substring(0, 35) : nom_Contat);					
				} else {
					pVmd.setString(8, null);
				}
								
			    SimpleDateFormat formata = new SimpleDateFormat("yyyy-MM-dd");
			    java.util.Date minhaData = formata.parse("1899-12-30");
			    
			    if (rs.getString("CLI_DATACADASTRO") != null) {
			    	java.util.Date data_cadastro = formata.parse(rs.getString("CLI_DATACADASTRO"));
				    if (data_cadastro.before(minhaData) || data_cadastro.equals(minhaData)) {
				    	pVmd.setString(9,  null);
				    } else {
				    	pVmd.setDate(9,  rs.getDate("CLI_DATACADASTRO"));
				    }
			    }
			    
				pVmd.executeUpdate();
				
				//CLXED
				pVmdCLXED.setString(1, rs.getString("CLI_CODIGO"));
				pVmdCLXED.setString(2, wfone);
				pVmdCLXED.executeUpdate();
				
				//CLIEN
				pVmdCLIEN.setString(1, wfone);
				pVmdCLIEN.setString(2, rs.getString("CLI_CODIGO"));
				pVmdCLIEN.executeUpdate();


				registros++;
				lblRegistros.setText(registros+"/"+total);
				progressBar2.setValue(registros);
			}
			System.out.println("Funcionou ENDER");
			pVmd.close();
			pFb.close();

			progressBar2.setValue(0);
		}
	}
	
	private boolean existeCod_EndFon(String Cod_EndFon) throws SQLException {
		String sql = "SELECT CAST(CASE WHEN EXISTS(SELECT * FROM ENDER where Cod_EndFon = '"+Cod_EndFon+"') THEN 1 ELSE 0 END AS BIT)";
		try (PreparedStatement ps = Conexao.getSqlConnectionAux().prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getBoolean(1);
				} else
					return false;
		}
	}


}

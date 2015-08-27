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

public class PRODU {
	Connection antigo = Conexao.getSqlConnectionAntigo();
	Connection dmd = Conexao.getSqlConnection();
	Connection dmdAux = Conexao.getSqlConnectionAux();
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblRegistros) throws Exception {
		String antigoPRODUTO = "select * from PRODUTOS";
		String dPRODU = "Insert Into PRODU (Cod_Produt, Des_Produt, Des_Resumi, Des_Comple, Dat_Implan, Cod_Classi, Cod_Seccao, Cod_ClaTri, Ctr_Preco, Ctr_Lista, Ctr_Venda, Cod_Fabric, Cod_GrpPrc, Cod_EAN, Cod_Ncm, Qtd_FraVen, Qtd_EmbVen, Des_UniCom, Des_UniVen, Ctr_Origem, Cod_AbcFar, Prc_MaxVen, Num_REGMS) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String vPRXLJ = "Update PRXLJ set Prc_CusLiqMed = ?, Prc_CusEnt = ?, Prc_CusLiq = ?, Prc_VenAtu = ?, Flg_BlqVen = ?, Dat_UltVen = ?  where Cod_Produt = ?";
		String vmdTbsec = "IF NOT EXISTS(SELECT 1 FROM DBO.Tbsec WHERE Cod_Seccao = 999) INSERT INTO DBO.Tbsec (Cod_Seccao, Des_Seccao) VALUES (999, 'A CADASTRAR')";
		//String vmdCltri = "IF NOT EXISTS(SELECT 1 FROM DBO.Cltri WHERE Cod_ClaTri = '**') INSERT INTO DBO.Cltri (Cod_ClaTri, Des_ClaTri) VALUES ('**', 'A CADASTRAR')";
		String vmdGrprc = "IF NOT EXISTS(SELECT 1 FROM DBO.Grprc WHERE Cod_GrpPrc = '*') INSERT INTO DBO.Grprc (Cod_GrpPrc, Des_GrpPrc) VALUES ('*', 'A CADASTRAR')";
		try (PreparedStatement pVmd = dmd.prepareStatement(dPRODU);
			 PreparedStatement pAntigo = antigo.prepareStatement(antigoPRODUTO);
			 PreparedStatement prxljVmd = dmd.prepareStatement(vPRXLJ);
			 PreparedStatement tbsecVmd = dmd.prepareStatement(vmdTbsec);
			// PreparedStatement cltriVmd = dmd.prepareStatement(vmdCltri);
			 PreparedStatement grprcVmd = dmd.prepareStatement(vmdGrprc)) {
			
			
			tbsecVmd.executeUpdate();
			//cltriVmd.executeUpdate();
			grprcVmd.executeUpdate();
			
			ResultSet rs = pAntigo.executeQuery();

			// contar a qtde de registros
			int registros = a.contaRegistros("PRODUTOS");
			int total = registros;
			progressBar2.setMaximum(registros);
			registros = 0;
			
			while (rs.next()) {
				// GRAVA NA PRODU
				
				int codigo = a.prox(dmdAux, "Cod_Produt", "PRODU");
				pVmd.setInt(1, codigo);
				
				String descricao = rs.getString("DESCRICAO");
				if (descricao != null) {
					pVmd.setString(2, descricao.length() > 40 ? descricao.substring(0, 39) : descricao);	
					pVmd.setString(3, descricao.length() > 24 ? descricao.substring(0, 23) : descricao);
				    pVmd.setString(4, descricao.length() > 50 ? descricao.substring(0, 49) : descricao);
				}
			    
			    
			    pVmd.setDate(5, rs.getDate("DTCAD"));
			    pVmd.setString(6, rs.getString("CODGRUPOI"));
			    pVmd.setInt(7, 999);
			    pVmd.setString(8, "**");
			    pVmd.setString(9, "C");
			    pVmd.setString(10, "N");
			    pVmd.setString(11, "L");
			    
			    pVmd.setString(12, rs.getString("CODFAB"));
			    pVmd.setString(13, "*");
			    
				String cod_ean = rs.getString("CODBARRA");
				if (cod_ean != null) {
					pVmd.setString(14, cod_ean.length() > 14 ? cod_ean.substring(0, 14) : cod_ean);
				} else {
					pVmd.setString(14, null);
				}
			    
			    cadastraNCM(rs.getString("NCM"));
		    	pVmd.setInt(15, rs.getInt("NCM") );
		    	
		    	pVmd.setInt(16, rs.getInt("QTDNACAIXA"));
		    	pVmd.setInt(17, 1);
		    	pVmd.setString(18, rs.getString("CODUNDI"));
		    	pVmd.setString(19, rs.getString("CODUNDI"));
		    	pVmd.setInt(20, 0);
		    	pVmd.setInt(21, rs.getInt("CODABCFARMA"));
		    	pVmd.setFloat(22, rs.getFloat("PMAXCONS"));
		    	pVmd.setString(23, rs.getString("RMSREMEDIO"));
				
				pVmd.executeUpdate();
				
				// GRAVA NA PRXLJ
				prxljVmd.setInt(1, rs.getInt("CUSTOMEDIO"));
				prxljVmd.setFloat(2, rs.getFloat("VALORULTCOMPRA"));
				prxljVmd.setFloat(3, rs.getFloat("VALORULTCOMPRA"));
				prxljVmd.setFloat(4, rs.getFloat("VALOR"));
				prxljVmd.setBoolean(5, rs.getString("ATIVO").equals("N") ? true : false);
				
				
			    SimpleDateFormat formata = new SimpleDateFormat("yyyy-MM-dd");
			    java.util.Date minhaData = formata.parse("1899-12-30");
			    
			    if (rs.getString("DTULTMODPRECO") != null) {
			    	java.util.Date data_ult_preco = formata.parse(rs.getString("DTULTMODPRECO"));
				    if (data_ult_preco.before(minhaData) || data_ult_preco.equals(minhaData)) {
				    	prxljVmd.setString(6,  null);
				    } else {
				    	prxljVmd.setDate(6,  rs.getDate("DTULTMODPRECO"));
				    }
			    } else {
			    	prxljVmd.setDate(6,  null);
			    }
			    
				
				prxljVmd.setInt(7, rs.getInt("CODITEM"));
				
				prxljVmd.executeUpdate();
				
				registros++;
				lblRegistros.setText(registros+"/"+total);
				progressBar2.setValue(registros);
			}
			
			System.out.println("Funcionou PRODU");
			pVmd.close();
			pAntigo.close();

			progressBar2.setValue(0);
		}
	}
	
	private void cadastraNCM(String cod_ncm) throws SQLException {
		String fbNCM = "select * from tbncm where codncm = '" +cod_ncm+"'";
		String vNCM = "IF NOT EXISTS(SELECT 1 FROM DBO.Tbncm WHERE Cod_Ncm = ?) INSERT INTO DBO.Tbncm (Cod_Ncm, Des_Ncm) VALUES (?, ?)";
		try (PreparedStatement ps = Conexao.getSqlConnectionAux().prepareStatement(vNCM);
			 PreparedStatement pf = Conexao.getSqlConnectionAuxAntigo().prepareStatement(fbNCM)) {
			
			ResultSet rs = pf.executeQuery();

			while (rs.next()) {
				// grava no varejo
				ps.setInt(1, rs.getInt("CODNCM"));
				ps.setInt(2, rs.getInt("CODNCM"));
			    ps.setString(3, rs.getString("DESCRICAO"));
				
				ps.executeUpdate();
				System.out.println("CADASTROU NCM");
			}
			pf.close();
			ps.close();
		}
	}

}

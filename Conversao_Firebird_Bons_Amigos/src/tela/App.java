package tela;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;
import conexao.Conexao;
import entidades.CLASS;
import entidades.CLIEN;
import entidades.CLTRI;
import entidades.ENDER;
import entidades.FABRI;
import entidades.FORNE;
import entidades.PRODU;
import entidades.VENDE;

public class App extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtDMDServidor;
	private JTextField txtDMDBanco;
	private JButton btn_limpa_dados;
	private JProgressBar progressBar;
	private JButton btn_processa;
	private JProgressBar progressBar2;
	private JLabel lblTabela;
	private JLabel lblRegistros;
	private JLabel lblDMDServidor;
	private JSeparator separator;
	private JSeparator separator_1;
	private JLabel lblDMDUsuario;
	private JTextField txtDMDUsuario;
	private JLabel lblDMDSenha;
	private JPasswordField pwdDMDSenha;
	private JCheckBox cboxFORNE;
	private JCheckBox cboxCLIEN;
	private JCheckBox cboxCLASS;
	private JCheckBox cboxFABRI;
	private JCheckBox cboxPRODU;
	private JTextField txtFBBanco;
	private JLabel label;
	private JCheckBox cboxENDER;
	private JCheckBox cboxCLTRI;
	private JCheckBox cboxVENDE;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App() {
		setTitle("inFarma - Conversor de dados");
		setResizable(false);
		setBounds(100, 100, 470, 337);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panelTop = new JPanel();
		getContentPane().add(panelTop, BorderLayout.NORTH);
		panelTop.setLayout(new MigLayout("", "[fill][][grow][][grow]", "[][][][][][]"));
		
		txtFBBanco = new JTextField();
		txtFBBanco.setText("C:/Temp/DADOS.FDB");
		txtFBBanco.setColumns(10);
		panelTop.add(txtFBBanco, "cell 2 0,growx");
		
		separator = new JSeparator();
		panelTop.add(separator, "cell 0 2 5 1");
				
		JLabel lblDMD = new JLabel("DMD");
		panelTop.add(lblDMD, "cell 0 3,alignx trailing");
								
		lblDMDServidor = new JLabel("Servidor");
		lblDMDServidor.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panelTop.add(lblDMDServidor, "cell 1 3,alignx trailing");

		txtDMDServidor = new JTextField();
		txtDMDServidor.setText("localhost");
		panelTop.add(txtDMDServidor, "flowy,cell 2 3,growx");
		txtDMDServidor.setColumns(10);

		JLabel lblDMDBanco = new JLabel("Banco");
		lblDMDBanco.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panelTop.add(lblDMDBanco, "cell 3 3,alignx trailing,aligny baseline");
				
		txtDMDBanco = new JTextField();
		txtDMDBanco.setText("VMD_BONSAMIGOS");
		panelTop.add(txtDMDBanco, "cell 4 3,growx");
		txtDMDBanco.setColumns(10);
				
		lblDMDUsuario = new JLabel("Usu\u00E1rio");
		lblDMDUsuario.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panelTop.add(lblDMDUsuario, "cell 1 4,alignx trailing");
		
		txtDMDUsuario = new JTextField();
		txtDMDUsuario.setText("sa");
		txtDMDUsuario.setColumns(10);
		panelTop.add(txtDMDUsuario, "cell 2 4,growx");
		
		lblDMDSenha = new JLabel("Senha");
		lblDMDSenha.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panelTop.add(lblDMDSenha, "cell 3 4,alignx trailing");
		
		pwdDMDSenha = new JPasswordField();
		pwdDMDSenha.setText("87125006");
		panelTop.add(pwdDMDSenha, "cell 4 4,growx");
		
		separator_1 = new JSeparator();
		panelTop.add(separator_1, "cell 0 5 5 1");
		
		label = new JLabel("Caminho BD Antigo");
		panelTop.add(label, "cell 0 0");

		final JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);

		class ProcessaWorker extends SwingWorker<Void, Void> {

			@Override
			protected Void doInBackground() throws Exception {
				progressBar.setValue(0);
				progressBar.setMaximum(20);
				btn_limpa_dados.setEnabled(false);
				btn_processa.setEnabled(false);
				int resp = JOptionPane.showConfirmDialog(panel, "Confirma?",
						"Processar Dados", JOptionPane.YES_NO_OPTION);
																		
				if (resp == 0) {
					FORNE forne  = new FORNE();
					CLIEN clien  = new CLIEN();
					CLASS classi = new CLASS();
					CLTRI cltri = new CLTRI();
					FABRI fabri  = new FABRI();
					PRODU produ  = new PRODU();
					ENDER ender = new ENDER();
					VENDE vende = new VENDE();
					if (cboxFORNE.isSelected() && cboxCLIEN.isSelected() && cboxCLASS.isSelected() && 
						cboxCLTRI.isSelected() && cboxFABRI.isSelected() && cboxPRODU.isSelected() && 
						cboxENDER.isSelected() && cboxVENDE.isSelected()) {

					// APAGANDO DADOS
						progressBar.setValue(progressBar.getValue() + 1);
						deleta("VENDE");
						deleta("CLXED");
						deleta("ENDER");
						deleta("FORNE"); 
						deleta("CLIEN");
						deleta("GRCLI");
						deleta("CLASS"); 
						deleta("CLTRI"); 
						deleta("FABRI");
						deleta("PRODU");
						progressBar.setValue(progressBar.getValue() + 1);
					}

					// IMPORTAÇÃO
					
					//FABRI					
					if (cboxFABRI.isSelected()) {
						System.out.println("COMEÇOU FABRI");
						progressBar.setValue(progressBar.getValue() + 1);
						deleta("FABRI");
						lblTabela.setText("Importando FABRI");
						fabri.importa(progressBar2, lblRegistros);
						progressBar.setValue(progressBar.getValue() + 1);
					}
					
					//CLASS					
					if (cboxCLTRI.isSelected()) {
						System.out.println("COMEÇOU CLTRI");
						progressBar.setValue(progressBar.getValue() + 1);
						deleta("CLTRI");
						lblTabela.setText("Importando CLTRI");
						cltri.importa(progressBar2, lblRegistros);
						progressBar.setValue(progressBar.getValue() + 1);
					}
					
					//CLASS					
					if (cboxCLASS.isSelected()) {
						System.out.println("COMEÇOU CLASS");
						progressBar.setValue(progressBar.getValue() + 1);
						deleta("CLASS");
						lblTabela.setText("Importando CLASS");
						classi.importa(progressBar2, lblRegistros);
						progressBar.setValue(progressBar.getValue() + 1);
					}
		
					
					//PRODU				
					if (cboxPRODU.isSelected()) {
						System.out.println("COMEÇOU PRODU");
						progressBar.setValue(progressBar.getValue() + 1);
						deleta("PRODU");
						lblTabela.setText("Importando PRODU");
						produ.importa(progressBar2, lblRegistros);
						progressBar.setValue(progressBar.getValue() + 1);
					}
					
					//FORNE					
					if (cboxFORNE.isSelected()) {
						System.out.println("COMEÇOU FORNE");
						progressBar.setValue(progressBar.getValue() + 1);
						deleta("FORNE");
						lblTabela.setText("Importando FORNE");
						forne.importa(progressBar2, lblRegistros);
						progressBar.setValue(progressBar.getValue() + 1);
						
					}
					
					//CLIEN					
					if (cboxCLIEN.isSelected()) {
						System.out.println("COMEÇOU CLIEN");
						progressBar.setValue(progressBar.getValue() + 1);
						deleta("CLIEN");
						lblTabela.setText("Importando CLIEN");
						clien.importa(progressBar2, lblRegistros);
						progressBar.setValue(progressBar.getValue() + 1);
					}
					
					//ENDER					
					if (cboxENDER.isSelected()) {
						System.out.println("COMEÇOU ENDER");
						progressBar.setValue(progressBar.getValue() + 1);
						deleta("CLXED");
						deleta("ENDER");
						lblTabela.setText("Importando ENDER");
						ender.importa(progressBar2, lblRegistros);
						progressBar.setValue(progressBar.getValue() + 1);
					}
					
					//VENDE					
					if (cboxVENDE.isSelected()) {
						System.out.println("COMEÇOU VENDE");
						progressBar.setValue(progressBar.getValue() + 1);
						deleta("VENDE");
						lblTabela.setText("Importando VENDE");
						vende.importa(progressBar2, lblRegistros);
						progressBar.setValue(progressBar.getValue() + 1);
					}
					
					JOptionPane.showMessageDialog(getContentPane(),
							"Processamento de dados realizado com sucesso",
							"Informação", JOptionPane.INFORMATION_MESSAGE);
					lblTabela.setText("");
					lblRegistros.setText("");

				} else {
					JOptionPane.showMessageDialog(getContentPane(),
							"Processamento de dados cancelado", "Informação",
							JOptionPane.INFORMATION_MESSAGE);
				}

				return null;
			}

			@Override
			protected void done() {
				try {
					progressBar.setValue(0);
					btn_limpa_dados.setEnabled(true);
					btn_processa.setEnabled(true);
					getContentPane().setCursor(Cursor.getDefaultCursor());
					// Descobre como está o processo. É responsável por lançar
					// as exceptions
					get();
				} catch (ExecutionException e) {
					final String msg = String.format(
							"Erro ao exportar dados: %s", e.getCause()
									.toString());
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JOptionPane.showMessageDialog(getContentPane(),
									"Erro ao exportar: " + msg, "Erro",
									JOptionPane.ERROR_MESSAGE);
						}
					});
				} catch (InterruptedException e) {
					System.out.println("Processo de exportação foi interrompido");
				}
			}
		}

		btn_processa = new JButton("Processa");
		btn_processa.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Conexao.CAMINHO_BANCO_ANTIGO = txtFBBanco.getText();
				Conexao.SQL_BANCO_DMD = txtDMDBanco.getText();
				Conexao.SQL_SERVIDOR_DMD = txtDMDServidor.getText();
				Conexao.SQL_USUARIO_DMD = txtDMDUsuario.getText();
				Conexao.SQL_SENHA_DMD = pwdDMDSenha.getText();
				Conexao.getSqlConnection();
				Conexao.getSqlConnectionAntigo();
				new ProcessaWorker().execute();
			}
		});

		class LimpaDadosWorker extends SwingWorker<Void, Void> {

			@Override
			protected Void doInBackground() throws Exception {
				progressBar.setValue(0);
				progressBar.setMaximum(8);
				btn_limpa_dados.setEnabled(false);
				btn_processa.setEnabled(false);
				int resp = JOptionPane.showConfirmDialog(panel, "Confirma?",
						"Limpeza de Dados", JOptionPane.YES_NO_OPTION);

				if(resp == 0){
				// APAGANDO DADOS 

				// FORNE
					
					if (cboxENDER.isSelected()) {
						deleta("CLXED");
						deleta("ENDER");
						progressBar.setValue(progressBar.getValue() + 1);
					}
					
					if (cboxFORNE.isSelected()) {
						deleta("FORNE");
						progressBar.setValue(progressBar.getValue() + 1);
					}
				// CLIEN
					if (cboxCLIEN.isSelected()) {
						deleta("CLIEN");
						progressBar.setValue(progressBar.getValue() + 1);
					}
				// CLASS
					if (cboxCLASS.isSelected()) {
						deleta("PRODU");
						deleta("CLASS");
						progressBar.setValue(progressBar.getValue() + 1);
					}

				// FABRI
					if (cboxFABRI.isSelected()) {
						deleta("FABRI");
						progressBar.setValue(progressBar.getValue() + 1);
					}
				// PRODU
					if (cboxPRODU.isSelected()) {
						deleta("PRODU");
						progressBar.setValue(progressBar.getValue() + 1);
					}

				JOptionPane.showMessageDialog(getContentPane(),
						"Limpeza de dados realizada com sucesso",
						"Informação", JOptionPane.INFORMATION_MESSAGE);

			} else {
				JOptionPane.showMessageDialog(getContentPane(),
						"Limpeza de dados cancelada", "Informação",
						JOptionPane.INFORMATION_MESSAGE);
			}
				return null;
			}

			@Override
			protected void done() {
				try {
					progressBar.setValue(0);
					btn_limpa_dados.setEnabled(true);
					btn_processa.setEnabled(true);
					getContentPane().setCursor(Cursor.getDefaultCursor());

					get();
					
				} catch (ExecutionException e) {
					final String msg = String.format("Erro ao limpar dados: %s", e.getCause().toString());
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JOptionPane.showMessageDialog(getContentPane(),
									"Erro ao limpar: " + msg, "Erro",
									JOptionPane.ERROR_MESSAGE);
						}
					});
				} catch (InterruptedException e) {
					System.out.println("Processo de exportação foi interrompido");
				}
			}
		}

		btn_limpa_dados = new JButton("Limpa Dados");
		btn_limpa_dados.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Conexao.CAMINHO_BANCO_ANTIGO= txtFBBanco.getText();
				Conexao.SQL_BANCO_DMD = txtDMDBanco.getText();
				Conexao.SQL_SERVIDOR_DMD = txtDMDServidor.getText();
				Conexao.SQL_USUARIO_DMD = txtDMDUsuario.getText();
				Conexao.SQL_SENHA_DMD = pwdDMDSenha.getText();
				new LimpaDadosWorker().execute();
			}
		});
		panel.add(btn_limpa_dados);
		panel.add(btn_processa);

		JPanel panel_1 = new JPanel();
		panel_1.setToolTipText("");
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new MigLayout("", "[][][][][][][][grow,fill]", "[][][][][][][][][]"));
		
		JLabel lblNewLabel_3 = new JLabel(
				"Converte uma base para o atacado");
		panel_1.add(lblNewLabel_3, "cell 0 0 5 1");
		
		cboxFABRI = new JCheckBox("1 - FABRI");
		panel_1.add(cboxFABRI, "cell 0 1");
		
		cboxCLTRI = new JCheckBox("3 - CLTRI");
		panel_1.add(cboxCLTRI, "cell 1 1");
		
		cboxFORNE = new JCheckBox("5 - FORNE");
		panel_1.add(cboxFORNE, "cell 2 1");
		
		cboxENDER = new JCheckBox("7 - ENDER");
		panel_1.add(cboxENDER, "cell 3 1");
		
		cboxCLASS = new JCheckBox("2 - CLASS");
		panel_1.add(cboxCLASS, "cell 0 2");
		
		cboxPRODU = new JCheckBox("4 - PRODU");
		panel_1.add(cboxPRODU, "cell 1 2");
		
		cboxCLIEN = new JCheckBox("6 - CLIEN");
		panel_1.add(cboxCLIEN, "cell 2 2");
		
		cboxVENDE = new JCheckBox("8- VENDE");
		panel_1.add(cboxVENDE, "cell 3 2");
		
		lblTabela = new JLabel("");
		panel_1.add(lblTabela, "cell 0 6 3 1");
		
		lblRegistros = new JLabel("");
		panel_1.add(lblRegistros, "cell 3 6 2 1");

		progressBar = new JProgressBar();
		progressBar.setMaximum(20);
		panel_1.add(progressBar, "cell 0 7 8 1,growx");

		progressBar2 = new JProgressBar();
		panel_1.add(progressBar2, "cell 0 8 8 1,growx");

	}
	
	public int prox(Connection conn, String chave, String tabela) {
		try (Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery("(Select Isnull(MAX(" + chave
						+ "), 0) + 1 From DBO."+tabela + ")")) {
			if (rs.next())
				return rs.getInt(1);
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public void deleta(String tabela) throws Exception {
		try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
			stmt.executeUpdate("DELETE FROM DBO."+tabela);
			stmt.close();
			System.out.println("Deletou " +tabela);
		}
	}
	
	public int contaRegistros(String tabela) throws SQLException {
		String sql = "SELECT count(*) qtde FROM " + tabela;
		try (PreparedStatement ps = Conexao.getSqlConnectionAuxAntigo().prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
			}
			return 0;
		}
	}
	
	public void updateNUMER(String campo) throws Exception {
		try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
			stmt.executeUpdate("Update DBO.NUMER set Valor = 0 where Campo = '"+campo+"'");
			stmt.close();
			System.out.println("Update Numer");
		}
	}
	
	public JCheckBox getCboxForne() {
		return cboxFORNE;
	}
	public JCheckBox getCboxCLIEN() {
		return cboxCLIEN;
	}
	public JCheckBox getCboxClass() {
		return cboxCLASS;
	}

	public JCheckBox getCboxFABRI() {
		return cboxFABRI;
	}
	public JCheckBox getCboxPRODU() {
		return cboxPRODU;
	}

	public JTextField getTxtFBBanco() {
		return txtFBBanco;
	}
	public JCheckBox getCboxENDER() {
		return cboxENDER;
	}
}

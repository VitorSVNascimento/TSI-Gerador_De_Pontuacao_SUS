package vsvn.geradorPontuacao;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import vsvn.geradorPontuacao.models.Comentario;
import vsvn.geradorPontuacao.models.Pergunta;

public class Pontuacao extends JFrame {

	private JPanel contentPane;
	private JTextField adjectiveTextField;
	private JTable commentsTable;
    private JFileChooser fileChooser;
    private JTextField notaTextField;
    private JTextField aceptTextField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Pontuacao frame = new Pontuacao();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Pontuacao() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 641, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new TitledBorder(null, "Gerador de pontua\u00E7\u00E3o SUS", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(53, 40, 158, 208);
		contentPane.add(panel);
		panel.setLayout(null);
		
        fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos CSV", "csv");
        fileChooser.setFileFilter(filter);
		
		adjectiveTextField = new JTextField();
		adjectiveTextField.setBorder(new TitledBorder(null, "Adijetivo", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		adjectiveTextField.setEditable(false);
		adjectiveTextField.setBounds(10, 97, 138, 44);
		panel.add(adjectiveTextField);
		adjectiveTextField.setColumns(10);
		
		notaTextField = new JTextField();
		notaTextField.setBorder(new TitledBorder(null, "Nota", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		notaTextField.setEditable(false);
		notaTextField.setHorizontalAlignment(SwingConstants.CENTER);
		notaTextField.setFont(new Font("Tahoma", Font.PLAIN, 40));
		notaTextField.setBounds(10, 11, 138, 75);
		panel.add(notaTextField);
		notaTextField.setColumns(10);
		
		aceptTextField = new JTextField();
		aceptTextField.setEditable(false);
		aceptTextField.setColumns(10);
		aceptTextField.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Aceitabilidade", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		aceptTextField.setBounds(10, 152, 138, 44);
		panel.add(aceptTextField);
		
		JButton openCSVButton = new JButton("Abrir arquivo");
		openCSVButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				File file = openFileChosser();
				if(file != null) 
					readCSV(file);
			}
		});
		openCSVButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		openCSVButton.setBounds(10, 259, 108, 31);
		contentPane.add(openCSVButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(270, 42, 345, 248);
		contentPane.add(scrollPane);
		
		commentsTable = new JTable();
		commentsTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Data/Hora", "Coment\u00E1rios Adicionais"
			}
		));
		commentsTable.getColumnModel().getColumn(0).setPreferredWidth(45);
		commentsTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		commentsTable.setFillsViewportHeight(true);
		scrollPane.setViewportView(commentsTable);
		this.setVisible(true);
	}

	protected void readCSV(File file) {
		List<Pergunta> perguntas = new ArrayList<Pergunta>();
		ArrayList<Comentario> comentarios = new ArrayList<Comentario>();
		
		for(byte i = 1 ; i < 11; i ++)
			perguntas.add(new Pergunta(i));
		
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
            	line = line.replace("\"", "");
                String[] values = line.split(",");
                for(int col = 1 ; col < 11 ; col++) {
                	perguntas.get(col - 1).addResposta(Integer.parseInt(values[col]));
                }
                
                if(values.length>=12)
                	comentarios.add(new Comentario(values[0],values[11]));
            }
            
            exibirSoma(perguntas);
            exibirComentarios(comentarios);
            
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		
	}

	private void exibirComentarios(ArrayList<Comentario> comentarios) {
		DefaultTableModel model = (DefaultTableModel) commentsTable.getModel();
		model.setRowCount(0);
		for(Comentario c : comentarios) {
			Object[] rowData = {c.getId(), c.getComentario()};
			model.addRow(rowData); 
		}
		commentsTable.repaint();
		
	}

	private void exibirSoma(List<Pergunta> perguntas) {
		float notaFinal = 0f;
		for(Pergunta pergunta : perguntas)
			notaFinal += pergunta.somaRespostasSUS();
		notaFinal*=2.5;
		notaTextField.setText(String.valueOf(Math.round(notaFinal)));
		aceptTextField.setText(generateAcceptance(notaFinal));
		adjectiveTextField.setText(generateAdjective(notaFinal));
		
	}

	public File openFileChosser() {
		int returnVal = fileChooser.showOpenDialog(this);
		return returnVal == JFileChooser.APPROVE_OPTION ? fileChooser.getSelectedFile() : null; 
	}
	
	private String generateAcceptance(float nota) {
        if(nota < 50) return "Inaceitável";
        if(nota < 70) return "Marginal";
        return "Aceitável";
    }
	
	private String generateAdjective(float nota) {
        if(nota < 25) return "Pior Imáginável";
        if(nota < 50) return "Pobre";
        if(nota < 70) return "OK";
        if(nota < 80) return "Bom";
        if(nota < 85) return  "Excelente";
        return "Melhor Imáginável";
	}
}

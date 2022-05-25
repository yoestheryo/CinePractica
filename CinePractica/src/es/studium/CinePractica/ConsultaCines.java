package es.studium.CinePractica;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ConsultaCines implements WindowListener, ActionListener
{
	Frame ventanaConsulta = new Frame("Consulta Cines");
	TextArea texto = new TextArea(15,90);
	Label lblPersonas = new Label("Listado de Cines de la cadena");
	Button btnPdf = new Button("Exportar a PDF");
	//Button btnExcel = new Button("Exportar a Excel");

	BaseDatos bd = new BaseDatos();
	int tipoUsuario;

	public ConsultaCines(int tipoUsuario)
	{
		this.tipoUsuario=tipoUsuario;
		ventanaConsulta.setLayout(new FlowLayout());
		ventanaConsulta.addWindowListener(this);
		btnPdf.addActionListener(this);
		//btnExcel.addActionListener(this);

		ventanaConsulta.add(lblPersonas);
		bd.conectar();
		texto.setEnabled(false);
		texto.setText(bd.consultarCines(tipoUsuario)); 	//Método consultar creado en BaseDatos: SELECT * FROM
		bd.desconectar();
		ventanaConsulta.add(texto);
		ventanaConsulta.add(btnPdf);
		//ventanaConsulta.add(btnExcel);

		ventanaConsulta.setLocationRelativeTo(null);
		ventanaConsulta.setBackground(Color.cyan);
		ventanaConsulta.setResizable(false);
		ventanaConsulta.setSize(940,310);
		ventanaConsulta.setVisible(true);
	}

	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		ventanaConsulta.setVisible(false);
	}
	@Override
	public void windowClosed(WindowEvent e){}
	@Override
	public void windowIconified(WindowEvent e){}
	@Override
	public void windowDeiconified(WindowEvent e){}
	@Override
	public void windowActivated(WindowEvent e){}
	@Override
	public void windowDeactivated(WindowEvent e){}
	@Override
	public void actionPerformed(ActionEvent event)
	{
		//Botón PDF y Excel
	}
}

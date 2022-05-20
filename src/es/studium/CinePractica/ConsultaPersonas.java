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

public class ConsultaPersonas implements WindowListener, ActionListener
{
	Frame ventanaConsulta = new Frame("Listado de Personas");
	TextArea texto = new TextArea(15,45);
	Label lblPersonas = new Label("Personas");
	Button btnPdf = new Button("Exportar a PDF");
	//Button btnExcel = new Button("Exportar a Excel");
	
	BaseDatos bd = new BaseDatos();	//Creación objeto de la Clase BaseDatos
	
	public ConsultaPersonas()
	{
		ventanaConsulta.setLayout(new FlowLayout());
		ventanaConsulta.addWindowListener(this);
		btnPdf.addActionListener(this);
		//btnExcel.addActionListener(this);

		ventanaConsulta.add(lblPersonas);
		bd.conectar();
		texto.setEnabled(false);
		texto.setText(bd.consultarPersonas()); 	//Método consultar creado en BaseDatos: SELECT * FROM
		bd.desconectar();
		ventanaConsulta.add(texto);
		ventanaConsulta.add(btnPdf);
		//ventanaConsulta.add(btnExcel);

		ventanaConsulta.setLocationRelativeTo(null);
		ventanaConsulta.setBackground(Color.pink);
		ventanaConsulta.setResizable(false);
		ventanaConsulta.setSize(700,290);
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

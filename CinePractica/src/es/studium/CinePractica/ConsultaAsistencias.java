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

public class ConsultaAsistencias implements WindowListener, ActionListener
{
	Frame ventanaConsulta = new Frame("Consulta Asistencias");
	TextArea texto = new TextArea(11,30);
	Label lblAsistencias = new Label("Información sobre las asistencias de las personas a los cines:");
	Button btnPdf = new Button("Exportar a PDF");
	//Button btnExcel = new Button("Exportar a Excel");

	BaseDatos bd = new BaseDatos();

	ConsultaAsistencias()
	{
		ventanaConsulta.setLayout(new FlowLayout());
		ventanaConsulta.addWindowListener(this);
		btnPdf.addActionListener(this);
		//btnExcel.addActionListener(this);

		ventanaConsulta.add(lblAsistencias);
		bd.conectar();
		texto.setText(bd.consultarAsistencias());
		bd.desconectar();
		ventanaConsulta.add(texto);
		ventanaConsulta.add(btnPdf);
		//ventanaConsulta.add(btnExcel);
		texto.setEnabled(false);

		ventanaConsulta.setLocationRelativeTo(null);
		ventanaConsulta.setBackground(Color.orange);
		ventanaConsulta.setResizable(false);
		ventanaConsulta.setSize(500,260);
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

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

public class ConsultaClientes implements WindowListener, ActionListener
{
	Frame ventanaConsulta = new Frame("Consulta Clientes");
	TextArea texto = new TextArea(11,45);
	Label lblClientes = new Label("Información sobre los Clientes:");
	Button btnPdf = new Button("Exportar a PDF");
	//Button btnExcel = new Button("Exportar a Excel");

	BaseDatos bd = new BaseDatos();
	int tipoUsuario;

	ConsultaClientes(int tipoUsuario)
	{
		this.tipoUsuario=tipoUsuario;
		ventanaConsulta.setLayout(new FlowLayout());
		ventanaConsulta.addWindowListener(this);
		btnPdf.addActionListener(this);
		//btnExcel.addActionListener(this);

		ventanaConsulta.add(lblClientes);
		bd.conectar();
		texto.setText(bd.consultarClientes(tipoUsuario));
		bd.desconectar();
		ventanaConsulta.add(texto);
		ventanaConsulta.add(btnPdf);
		//ventanaConsulta.add(btnExcel);
		texto.setEnabled(false);

		ventanaConsulta.setLocationRelativeTo(null);
		ventanaConsulta.setBackground(Color.gray);
		ventanaConsulta.setResizable(false);
		ventanaConsulta.setSize(670,260);
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


package es.studium.CinePractica;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BajaCine implements WindowListener, ActionListener
{
	Frame ventanaBaja = new Frame("Baja Cine");
	Label lblEnunciado= new Label("Elegir el Cine que desea Borrar:");
	Choice choCines = new Choice();
	Button btnBorrar = new Button("Borrar");

	Dialog dlgConfirmacion = new Dialog(ventanaBaja, "Confirmación", true);
	Label lblConfirmacion = new Label("¿Seguro de Borrar el cine ");
	Button btnSi = new Button("Sí");
	Button btnNo = new Button("No");

	Dialog dlgMensaje = new Dialog(ventanaBaja, "Mensaje", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXXXXXX");

	BaseDatos bd = new BaseDatos();
	ResultSet rs = null;

	public BajaCine()
	{
		ventanaBaja.setLayout(new FlowLayout());
		ventanaBaja.addWindowListener(this);
		dlgConfirmacion.addWindowListener(this);
		dlgMensaje.addWindowListener(this);
		btnBorrar.addActionListener(this);
		btnSi.addActionListener(this);
		btnNo.addActionListener(this);

		ventanaBaja.add(lblEnunciado);
		rellenarChoiceCines();
		bd.desconectar();
		ventanaBaja.add(choCines);
		ventanaBaja.add(btnBorrar);

		ventanaBaja.setLocationRelativeTo(null);
		ventanaBaja.setBackground(Color.cyan);
		ventanaBaja.setResizable(false);
		ventanaBaja.setSize(400,200);
		ventanaBaja.setVisible(true);	
	}

	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		if(dlgConfirmacion.isActive())
		{
			dlgConfirmacion.setVisible(false);
		}
		else if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
			rellenarChoiceCines();
			dlgConfirmacion.setVisible(false);
		}
		else
		{
			ventanaBaja.setVisible(false);
		}	
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
	public void actionPerformed(ActionEvent evento)
	{ 
		if(evento.getSource().equals(btnBorrar))
		{
			if(choCines.getSelectedIndex()==0)
			{
				lblMensaje.setText("Debe elegir una opción");
				mostrarMensaje();
			}
			else
			{
				dlgConfirmacion.setLayout(new FlowLayout());

				lblConfirmacion.setText("¿Seguro que quiere borrar " + choCines.getSelectedItem() + "?");
				dlgConfirmacion.add(lblConfirmacion);
				dlgConfirmacion.add(btnSi);
				dlgConfirmacion.add(btnNo);

				dlgConfirmacion.setLocationRelativeTo(null);
				dlgConfirmacion.setResizable(false);
				dlgConfirmacion.setSize(350,150);
				dlgConfirmacion.setVisible(true);
			}
		}
		else if(evento.getSource().equals(btnNo))
		{
			dlgConfirmacion.setVisible(false);
		}

		else if(evento.getSource().equals(btnSi))
		{
			bd.conectar();
			String[] array = choCines.getSelectedItem().split("-");
			int resultado = bd.eliminarCines(Integer.parseInt(array[0]));
			if(resultado==0)
			{
				lblMensaje.setText("Cine borrado correctamente.");
			}
			else
			{
				lblMensaje.setText("No se puede borrar un Cine con clientes ya registrados.");
			}
			mostrarMensaje();
			bd.rellenarCines();
		}bd.desconectar();
	}

	private void rellenarChoiceCines()
	{
		choCines.removeAll();
		choCines.add("Elija el Cine que desea borrar...");
		bd.conectar();
		rs = bd.rellenarCines();
		try
		{
			while(rs.next())
			{
				choCines.add(rs.getInt("idCine")+
						"-"+ rs.getString("nombreCine")+
						"-"+ rs.getString("ciudadCine")+
						"-"+ rs.getString("telefonoCine"));
			}
		} catch (SQLException e){}
		bd.desconectar();
	}

	public void mostrarMensaje()				
	{
		dlgMensaje.setLayout(new FlowLayout());
		dlgMensaje.add(lblMensaje);
		dlgMensaje.setResizable(false);
		dlgMensaje.setSize(380,100);
		dlgMensaje.setLocationRelativeTo(null);
		dlgMensaje.setVisible(true);
	}
}
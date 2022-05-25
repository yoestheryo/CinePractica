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

public class BajaAsistencia implements WindowListener, ActionListener
{
	Frame ventanaBaja = new Frame("Baja Asistencia");
	Label lblEnunciado= new Label("Elija la Asistencia que quiere borrar:");
	Choice choAsistencias= new Choice();
	Button btnBorrar = new Button("Borrar");
	//Crear diálogo para preguntar si está seguro de que desea borrar esa asistencia:
	Dialog dlgConfirmacion = new Dialog(ventanaBaja, "Confirmación", true);
	Label lblConfirmacion = new Label("¿Seguro de Borrar la Asistencia ");
	Button btnSi = new Button("Sí");
	Button btnNo = new Button("No");
	//Diálogo con el mensaje de la ejecución:	
	Dialog dlgMensaje = new Dialog(ventanaBaja, "Mensaje", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXXXXXX");

	BaseDatos bd = new BaseDatos();
	ResultSet rs = null;
	int tipoUsuario;

	public BajaAsistencia(int tipoUsuario)
	{
		this.tipoUsuario = tipoUsuario;
		ventanaBaja.setLayout(new FlowLayout());
		ventanaBaja.addWindowListener(this);
		dlgConfirmacion.addWindowListener(this);
		dlgMensaje.addWindowListener(this);
		btnBorrar.addActionListener(this);
		btnSi.addActionListener(this);
		btnNo.addActionListener(this);

		ventanaBaja.add(lblEnunciado);
		rellenarChoiceAsistencia();			//Para cargar, refrescar el choice cada vez que haya cambios.
		bd.desconectar();
		ventanaBaja.add(choAsistencias);
		ventanaBaja.add(btnBorrar);

		ventanaBaja.setLocationRelativeTo(null);
		ventanaBaja.setBackground(Color.orange);
		ventanaBaja.setResizable(false);
		ventanaBaja.setSize(250,150);
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
			rellenarChoiceAsistencia();
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
			if(choAsistencias.getSelectedIndex()==0)
			{
				lblMensaje.setText("Debe elegir una opción");
				mostrarMensaje();
			}
			else
			{
				dlgConfirmacion.setLayout(new FlowLayout());

				lblConfirmacion.setText("¿Seguro que quiere borrar " + choAsistencias.getSelectedItem() + "?");
				dlgConfirmacion.add(lblConfirmacion);
				dlgConfirmacion.add(btnSi);
				dlgConfirmacion.add(btnNo);

				dlgConfirmacion.setLocationRelativeTo(null);
				dlgConfirmacion.setResizable(false);
				dlgConfirmacion.setSize(380,150);
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
			try
			{
				String[] seleccionado = choAsistencias.getSelectedItem().split("-");
				int resultado = bd.eliminarAsistencia(tipoUsuario, Integer.parseInt(seleccionado[0]));
				if(resultado==0)
				{
					lblMensaje.setText("La asistencia se ha borrado correctamente.");
				}
				else
				{
					lblMensaje.setText("Se ha producido un error");
				}
				mostrarMensaje();
				bd.rellenarAsistencias();
			}
			catch(NumberFormatException e)
			{
				lblMensaje.setText("Debe elegir una opción");
				mostrarMensaje();
			}
			bd.desconectar();
		}
	}
	private void rellenarChoiceAsistencia()		
	{
		choAsistencias.removeAll();
		choAsistencias.add("Elegir la asistencia a Borrar...");
		bd.conectar();
		//Sacar datos de la tabla Asistir
		rs = bd.rellenarAsistencias();
		try
		{
			while(rs.next())
			{
				choAsistencias.add(rs.getInt("idAsistir")+
						"-"+ rs.getString("idPersonaFK2")+
						"-"+ rs.getString("idCineFK3"));
			}
		} catch (SQLException e){}
	}

	public void mostrarMensaje()				
	{
		dlgMensaje.setLayout(new FlowLayout());
		dlgMensaje.add(lblMensaje);

		dlgMensaje.setResizable(false);
		dlgMensaje.setSize(300,100);
		dlgMensaje.setLocationRelativeTo(null);
		dlgMensaje.setVisible(true);
	}
}

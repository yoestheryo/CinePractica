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

public class BajaCliente implements WindowListener, ActionListener
{
	Frame ventanaBaja = new Frame("Baja Cliente");
	Label lblEnunciado= new Label("Elegir Cliente:");
	Choice choClientes= new Choice();
	Button btnBorrar = new Button("Borrar");

	//Crear diálogo para preguntar si está seguro de que desea borrar ese cliente:
	Dialog dlgConfirmacion = new Dialog(ventanaBaja, "Confirmación", true);
	Label lblConfirmacion = new Label("¿Seguro de Borrar el Cliente ");
	Button btnSi = new Button("Sí");
	Button btnNo = new Button("No");

	//Diálogo con el mensaje de la ejecución:
	Dialog dlgMensaje = new Dialog(ventanaBaja, "Mensaje", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXXXXXX");

	BaseDatos bd = new BaseDatos();
	ResultSet rs = null;

	public BajaCliente()
	{
		ventanaBaja.setLayout(new FlowLayout());
		ventanaBaja.addWindowListener(this);
		dlgConfirmacion.addWindowListener(this);
		dlgMensaje.addWindowListener(this);
		btnBorrar.addActionListener(this);
		btnSi.addActionListener(this);
		btnNo.addActionListener(this);

		ventanaBaja.add(lblEnunciado);
		rellenarChoiceCliente();
		bd.desconectar();
		ventanaBaja.add(choClientes);
		ventanaBaja.add(btnBorrar);

		ventanaBaja.setLocationRelativeTo(null);
		ventanaBaja.setBackground(Color.gray);
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
			rellenarChoiceCliente();
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
			if(choClientes.getSelectedIndex()==0)
			{
				lblMensaje.setText("Debe elegir una opción");
				mostrarMensaje();
			}
			else
			{
			dlgConfirmacion.setLayout(new FlowLayout());

			lblConfirmacion.setText("¿Seguro que quiere borrar " + choClientes.getSelectedItem() + "?");
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
				String[] array = choClientes.getSelectedItem().split("-");
				int resultado = bd.eliminarCliente(Integer.parseInt(array[0]));
				if(resultado==0)
				{
					lblMensaje.setText("El cliente se ha borrado correctamente.");
				}
				else
				{
					lblMensaje.setText("Se ha producido un error");
				}
				mostrarMensaje();
				bd.rellenarClientes();
			}
			catch(NumberFormatException e)
			{
				lblMensaje.setText("Debe elegir una opción");
				mostrarMensaje();
			}
			bd.desconectar();
		}
	}

	private void rellenarChoiceCliente()		// En esta clase, a diferencia del Alta Cliente, hemos extraido método para rellenar el Choice
	{
		choClientes.removeAll();
		choClientes.add("Elija el cliente a Borrar...");
		bd.conectar();
		//Sacar datos de la tabla Clientes
		rs = bd.rellenarClientes();
		try
		{
			while(rs.next())
			{
				choClientes.add(rs.getInt("idCliente")+
						"-"+ rs.getString("facturaCliente")+
						"-"+ rs.getString("numeroVisitasCliente")+
						"-"+ rs.getString("idPersonaFK1"));
			}
		} catch (SQLException e){}
	}

	//Método para el segundo dialogo: Mensaje, para confirmar la baja o indicar en caso de error
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

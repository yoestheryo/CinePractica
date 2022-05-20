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

public class BajaPersona implements WindowListener, ActionListener
{
	Frame ventanaBaja = new Frame("Baja Persona");
	Label lblEnunciado= new Label("Elegir la persona a Borrar:");
	Choice choPersonas = new Choice();
	Button btnBorrar = new Button("Borrar");

	//Crear dialogo para preguntar si está seguro:
	Dialog dlgConfirmacion = new Dialog(ventanaBaja, "Confirmación", true);
	Label lblConfirmacion = new Label("¿Seguro de Borrar la persona ");
	Button btnSi = new Button("Sí");
	Button btnNo = new Button("No");

	//Dialogo con el mensaje de la ejecución:
	Dialog dlgMensaje = new Dialog(ventanaBaja, "Mensaje", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXXXXXX");

	BaseDatos bd = new BaseDatos();
	ResultSet rs = null;

	public BajaPersona()
	{
		ventanaBaja.setLayout(new FlowLayout());
		ventanaBaja.addWindowListener(this);
		dlgConfirmacion.addWindowListener(this);
		dlgMensaje.addWindowListener(this);
		btnBorrar.addActionListener(this);
		btnSi.addActionListener(this);
		btnNo.addActionListener(this);

		ventanaBaja.add(lblEnunciado);
		rellenarChoicePersonas();
		bd.desconectar();
		ventanaBaja.add(choPersonas);
		ventanaBaja.add(btnBorrar);

		ventanaBaja.setLocationRelativeTo(null);
		ventanaBaja.setBackground(Color.pink);
		ventanaBaja.setResizable(false);
		ventanaBaja.setSize(300,200);
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
			rellenarChoicePersonas();
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
		// Si pulsamos botón Borrar... mostramos ventana dialogo para confirmar
		if(evento.getSource().equals(btnBorrar))
		{
			if(choPersonas.getSelectedIndex()==0)
			{
				lblMensaje.setText("Debe elegir una opción");
				mostrarMensaje();
			}
			else
			{
				dlgConfirmacion.setLayout(new FlowLayout());

				//A la etiqueta Confirmación, le añadimos el texto que se obtenga del Choice (del elemento elegido):
				lblConfirmacion.setText("¿Seguro que quiere borrar " + choPersonas.getSelectedItem() + "?");
				dlgConfirmacion.add(lblConfirmacion);
				dlgConfirmacion.add(btnSi);
				dlgConfirmacion.add(btnNo);

				dlgConfirmacion.setLocationRelativeTo(null);
				dlgConfirmacion.setResizable(false);
				dlgConfirmacion.setSize(400,150);
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
			String[] array = choPersonas.getSelectedItem().split("-");
			int resultado = bd.eliminarPersona(Integer.parseInt(array[0]));
			if(resultado==0)
			{
				lblMensaje.setText("La persona se ha borrado correctamente.");
			}
			else
			{
				lblMensaje.setText("No se puede borrar esta Persona que ya es Cliente.");
			}
			mostrarMensaje();
			bd.rellenarPersonas();
		}bd.desconectar();
	}

	private void rellenarChoicePersonas()
	{
		choPersonas.removeAll();
		choPersonas.add("Elija la persona...");
		bd.conectar();
		//Sacar datos de la tabla personas
		rs = bd.rellenarPersonas();
		//Y registro a registro meterlos en el Choice
		try
		{
			while(rs.next())
			{
				choPersonas.add(rs.getInt("idPersona")+
						"-"+ rs.getString("dniPersona")+
						"-"+ rs.getString("nombrePersona")+
						"-"+ rs.getString("primerApellidoPersona")+
						"-"+ rs.getString("segundoApellidoPersona"));
			}
		} catch (SQLException e){}
		bd.desconectar();
	}

	//Método para el segundo dialogo: Mensaje, para confirmar la baja o indicar en caso de error
	public void mostrarMensaje()				
	{
		dlgMensaje.setLayout(new FlowLayout());
		dlgMensaje.add(lblMensaje);

		dlgMensaje.setResizable(false);
		dlgMensaje.setSize(400,100);
		dlgMensaje.setLocationRelativeTo(null);
		dlgMensaje.setVisible(true);
	}
}



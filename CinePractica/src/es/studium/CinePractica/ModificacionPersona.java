package es.studium.CinePractica;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModificacionPersona implements WindowListener, ActionListener
{
	Frame ventana = new Frame("Modificar Persona");
	Choice choPersonas = new Choice();
	Button btnModificar = new Button("Modificar");

	Frame ventanaEdicion = new Frame("Campos Persona");
	Label lblId = new Label("idPersona:");
	TextField txtId = new TextField(20);
	Label lblDni = new Label("DNI:");
	TextField txtDni= new TextField(20);
	Label lblNombre = new Label("Nombre:");
	TextField txtNombre = new TextField(20);
	Label lblPrimerApellido = new Label("Primer Apellido:");
	TextField txtPrimerApellido = new TextField(20);
	Label lblSegundoApellido = new Label("Segundo Apellido:");
	TextField txtSegundoApellido = new TextField(20);
	Label lblDomicilio = new Label("Domicilio:");
	TextField txtDomicilio = new TextField(20);
	Label lblTelefono = new Label("Teléfono:");
	TextField txtTelefono = new TextField(20);
	Label lblEmail = new Label("Email:");
	TextField txtEmail = new TextField(20);
	Button btnGuardar = new Button("Guardar Cambios");
	Button btnCancelar = new Button("Cancelar");

	Dialog dlgMensaje = new Dialog(ventana, "Información", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXXXXX");

	int idPersona;						//Nos interesa guardar el id de la Personas que se está modificando
	BaseDatos bd = new BaseDatos();
	ResultSet rs = null;
	
	int tipoUsuario;

	public ModificacionPersona(int tipoUsuario)
	{
		this.tipoUsuario=tipoUsuario;
		ventana.setLayout(new FlowLayout());
		ventana.addWindowListener(this);
		ventanaEdicion.addWindowListener(this);
		dlgMensaje.addWindowListener(this);
		btnModificar.addActionListener(this);
		btnGuardar.addActionListener(this);
		btnCancelar.addActionListener(this);
		txtId.setEditable(false);

		bd.conectar();
		choPersonas.removeAll();
		choPersonas.add("Elige Persona a Modificar...");		//Se limpia el Choice y se rellena actualizado
		rellenarChoicePersonas();
		bd.desconectar();

		ventana.add(choPersonas);
		ventana.add(btnModificar);

		ventana.setSize(650,150);
		ventana.setResizable(false);
		ventana.setBackground(Color.pink);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}

	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		if(ventana.isActive())
		{
			ventana.setVisible(false);
		}
		else if(ventanaEdicion.isActive())
		{
			ventanaEdicion.setVisible(false);
			ventana.setVisible(true);
		}
		else if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
			rellenarChoicePersonas();
			ventanaEdicion.setVisible(false);
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
		if(evento.getSource().equals(btnModificar))	//Que cuando pulsemo modificar, salte la ventana con los campos rellenos por defecto
		{
			if(choPersonas.getSelectedIndex()==0)
			{
				lblMensaje.setText("Debe elegir una opción");
				mostrarMensaje();
			}
			else
			{
				bd.conectar();
				ventanaEdicion.setLayout(new FlowLayout());
				String[] seleccionado = choPersonas.getSelectedItem().split("-");
				idPersona = Integer.parseInt(seleccionado[0]);
				ventanaEdicion.add(lblId);
				txtId.setText(seleccionado[0]);
				ventanaEdicion.add(txtId);
				ventanaEdicion.add(lblDni);
				txtDni.setText(seleccionado[1]);
				ventanaEdicion.add(txtDni);
				ventanaEdicion.add(lblNombre);
				txtNombre.setText(seleccionado[2]);
				ventanaEdicion.add(txtNombre);
				ventanaEdicion.add(lblPrimerApellido);
				txtPrimerApellido.setText(seleccionado[3]);
				ventanaEdicion.add(txtPrimerApellido);
				ventanaEdicion.add(lblSegundoApellido);
				txtSegundoApellido.setText(seleccionado[4]);
				ventanaEdicion.add(txtSegundoApellido);
				ventanaEdicion.add(lblDomicilio);
				txtDomicilio.setText(seleccionado[5]);
				ventanaEdicion.add(txtDomicilio);
				ventanaEdicion.add(lblTelefono);
				txtTelefono.setText(seleccionado[6]);
				ventanaEdicion.add(txtTelefono);
				ventanaEdicion.add(lblEmail);
				txtEmail.setText(seleccionado[7]);
				ventanaEdicion.add(txtEmail);
				ventanaEdicion.add(btnGuardar);
				ventanaEdicion.add(btnCancelar);

				bd.desconectar();
				ventanaEdicion.setSize(280,550);
				ventanaEdicion.setResizable(true);
				ventanaEdicion.setBackground(Color.pink);
				ventanaEdicion.setLocationRelativeTo(null);
				ventanaEdicion.setVisible(true);
			}
		}
		else if(evento.getSource().equals(btnCancelar))
		{
			limpiarTextos();
		}
		else if(evento.getSource().equals(btnGuardar))
		{
			bd.conectar();
			int resultado = bd.modificarPersonas(tipoUsuario, Integer.parseInt(txtId.getText()), txtDni.getText(), txtNombre.getText(), txtPrimerApellido.getText(), txtSegundoApellido.getText(), 
					txtDomicilio.getText(), txtTelefono.getText(), txtEmail.getText());	
			
			if(resultado == 0)
			{	
				lblMensaje.setText("Se ha modificado correctamente");
				
			}
			else
			{
				lblMensaje.setText("Error en la Modificación");
			}
			mostrarMensaje();
			bd.rellenarPersonas();
		}
		bd.desconectar();
	}

	public void rellenarChoicePersonas()
	{
		choPersonas.removeAll();
		choPersonas.add("Elige Persona a Modificar...");
		bd.conectar();
		rs = bd.rellenarPersonas();
		try
		{	
			while(rs.next())
			{
				choPersonas.add(rs.getInt("idPersona")+
						"-"+ rs.getString("dniPersona")+
						"-"+ rs.getString("nombrePersona")+
						"-"+ rs.getString("primerApellidoPersona")+
						"-"+ rs.getString("segundoApellidoPersona")+
						"-"+ rs.getString("domicilioPersona")+
						"-"+ rs.getString("telefonoPersona")+
						"-"+ rs.getString("emailPersona"));
			}	
		} 
		catch (SQLException e){}	
		bd.desconectar();
	}

	public void limpiarTextos()
	{
		txtNombre.setText("");
		txtDni.setText("");
		txtPrimerApellido.setText("");
		txtSegundoApellido.setText("");
		txtDomicilio.setText("");
		txtTelefono.setText("");
		txtEmail.setText("");
		txtDni.requestFocus();
	}
	public void mostrarMensaje()
	{
		dlgMensaje.setLayout(new FlowLayout());
		dlgMensaje.add(lblMensaje);

		dlgMensaje.setLocationRelativeTo(null);
		dlgMensaje.setResizable(false);
		dlgMensaje.setSize(250,100);
		dlgMensaje.setVisible(true);
	}
}


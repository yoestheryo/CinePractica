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

public class ModificacionCine implements WindowListener, ActionListener
{
	Frame ventana = new Frame("Modificar Cine");
	Choice choCines = new Choice();
	Button btnModificar = new Button("Modificar");

	Frame ventanaEdicion = new Frame("Campos Cine");
	Label lblId = new Label("idCine:");
	TextField txtId = new TextField(20);
	Label lblNombre = new Label("Nombre Cine:");
	TextField txtNombre = new TextField(20);
	Label lblCiudad = new Label("Ciudad:");
	TextField txtCiudad = new TextField(20);
	Label lblDireccion = new Label("Direccion");
	TextField txtDireccion = new TextField(20);
	Label lblTelefono = new Label("Teléfono:");
	TextField txtTelefono = new TextField(20);
	Label lblWeb = new Label("Página Web:");
	TextField txtWeb = new TextField(20);
	Label lblEmail = new Label("Email:");
	TextField txtEmail = new TextField(20);
	Button btnGuardar = new Button("Guardar Cambios");
	Button btnCancelar = new Button("Cancelar");

	Dialog dlgMensaje = new Dialog(ventana, "Información", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXXXXX");

	int idCine;						//Nos interesa guardar el id del Cine que se está modificando
	BaseDatos bd = new BaseDatos();
	ResultSet rs = null;
	
	int tipoUsuario;

	public ModificacionCine(int tipoUsuario)
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
		choCines.removeAll();
		choCines.add("Elige un Cine...");		//Se limpia el Choice y se rellena actualizado
		rellenarChoiceCines();
		bd.desconectar();

		ventana.add(choCines);
		ventana.add(btnModificar);

		ventana.setSize(850,150);
		ventana.setBackground(Color.cyan);
		ventana.setResizable(false);
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
			rellenarChoiceCines();
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
			if(choCines.getSelectedIndex()==0)
			{
				lblMensaje.setText("Debe elegir una opción.");
				mostrarMensaje();
			}
			else
			{
				bd.conectar();
				ventanaEdicion.setLayout(new FlowLayout());
				String[] seleccionado = choCines.getSelectedItem().split("-");
				idCine = Integer.parseInt(seleccionado[0]);
				ventanaEdicion.add(lblId);
				txtId.setText(seleccionado[0]);
				ventanaEdicion.add(txtId);
				ventanaEdicion.add(lblNombre);
				txtNombre.setText(seleccionado[1]);
				ventanaEdicion.add(txtNombre);
				ventanaEdicion.add(lblCiudad);
				txtCiudad.setText(seleccionado[2]);
				ventanaEdicion.add(txtCiudad);
				ventanaEdicion.add(lblDireccion);
				txtDireccion.setText(seleccionado[3]);
				ventanaEdicion.add(txtDireccion);
				ventanaEdicion.add(lblTelefono);
				txtTelefono.setText(seleccionado[4]);
				ventanaEdicion.add(txtTelefono);
				ventanaEdicion.add(lblWeb);
				txtWeb.setText(seleccionado[5]);
				ventanaEdicion.add(txtWeb);
				ventanaEdicion.add(lblEmail);
				txtEmail.setText(seleccionado[6]);
				ventanaEdicion.add(txtEmail);
				ventanaEdicion.add(btnGuardar);
				ventanaEdicion.add(btnCancelar);

				bd.desconectar();
				ventanaEdicion.setSize(280,550);
				ventanaEdicion.setBackground(Color.cyan);
				ventanaEdicion.setResizable(true);
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
			int resultado = bd.modificarCines(tipoUsuario, Integer.parseInt(txtId.getText()), txtNombre.getText(), txtCiudad.getText(), txtDireccion.getText(),
					txtTelefono.getText(), txtWeb.getText(), txtEmail.getText());	
			
			if(resultado == 0)
			{	
				lblMensaje.setText("Se ha modificado correctamente");
				
			}
			else
			{
				lblMensaje.setText("Error en la Modificación");
			}
			mostrarMensaje();
			bd.rellenarCines();
		}
		bd.desconectar();
	}

	public void rellenarChoiceCines()
	{
		choCines.removeAll();
		choCines.add("Elige un Cine...");
		bd.conectar();
		rs = bd.rellenarCines();
		try
		{	
			while(rs.next())
			{
				choCines.add(rs.getInt("idCine")+
						"-"+ rs.getString("nombreCine")+
						"-"+ rs.getString("ciudadCine")+
						"-"+ rs.getString("direccionCine")+
						"-"+ rs.getString("telefonoCine")+
						"-"+rs.getString("paginaWebCine")+
						"-"+ rs.getString("emailCine"));
			}	
		} 
		catch (SQLException e){}	
		bd.desconectar();
	}

	public void limpiarTextos()
	{
		txtNombre.setText("");
		txtCiudad.setText("");
		txtDireccion.setText("");
		txtTelefono.setText("");
		txtWeb.setText("");
		txtEmail.setText("");
		txtNombre.requestFocus();
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
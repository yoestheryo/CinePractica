package es.studium.CinePractica;

import java.awt.Button;
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

public class AltaCine implements WindowListener, ActionListener
{
	Frame ventanaAlta = new Frame("Alta Cine");
	Label lblNombre= new Label("Nombre Cine: ");
	Label lblCiudad = new Label("Población: ");
	Label lblDireccion= new Label("Dirección: ");
	Label lblTelefono= new Label("Teléfono");
	Label lblWeb= new Label("Página Web: ");
	Label lblEmail= new Label("Email:");

	TextField txtNombre = new TextField(40);
	TextField txtCiudad = new TextField(40);
	TextField txtDireccion = new TextField(40);
	TextField txtTelefono = new TextField(40);
	TextField txtWeb = new TextField(40);
	TextField txtEmail = new TextField(40);
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar = new Button("Cancelar");

	BaseDatos bd = new BaseDatos();

	Dialog dlgConfirmacion = new Dialog(ventanaAlta, "Información importante", true);
	Label lblConfirmacion = new Label("XXXXXXXXXXXXXXXXXX");

	public AltaCine()
	{
		ventanaAlta.setLayout(new FlowLayout());
		ventanaAlta.addWindowListener(this);
		dlgConfirmacion.addWindowListener(this);
		btnAceptar.addActionListener(this);
		btnCancelar.addActionListener(this);

		ventanaAlta.add(lblNombre);
		ventanaAlta.add(txtNombre);
		ventanaAlta.add(lblCiudad);
		ventanaAlta.add(txtCiudad);
		ventanaAlta.add(lblDireccion);
		ventanaAlta.add(txtDireccion);
		ventanaAlta.add(lblTelefono);
		ventanaAlta.add(txtTelefono);
		ventanaAlta.add(lblWeb);
		ventanaAlta.add(txtWeb);
		ventanaAlta.add(lblEmail);
		ventanaAlta.add(txtEmail);
		ventanaAlta.add(btnAceptar);
		ventanaAlta.add(btnCancelar);

		ventanaAlta.setLocationRelativeTo(null);
		ventanaAlta.setBackground(Color.cyan);
		ventanaAlta.setResizable(false);
		ventanaAlta.setSize(450,500);
		ventanaAlta.setVisible(true);
	}

	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		if(dlgConfirmacion.isActive())
		{
			dlgConfirmacion.setVisible(false);
			limpiarTextos();
		}
		else
		{
			ventanaAlta.setVisible(false);
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
		if(evento.getSource().equals(btnCancelar))
		{
			limpiarTextos();
		}
		else if(evento.getSource().equals(btnAceptar))
		{
			if((txtNombre.getText().equals(""))||(txtDireccion.getText().equals("")))
			{
				lblConfirmacion.setText("Debe rellenar todos los campos obligatorios.");
				mostrarDialogo();
			}
			else
			{
				bd.conectar();
				int resultado = bd.altaCines(txtNombre.getText(), txtCiudad.getText(), txtDireccion.getText(), 
						txtTelefono.getText(), txtWeb.getText(), txtEmail.getText());
				if(resultado == 0)
				{
					lblConfirmacion.setText("Alta correcta");
					limpiarTextos();
				}
				else
				{
					lblConfirmacion.setText("Se ha producido un error.");
				}
				mostrarDialogo();
				bd.desconectar();
			}
		}
	}

	public void mostrarDialogo()
	{
		dlgConfirmacion.setLayout(new FlowLayout());
		dlgConfirmacion.add(lblConfirmacion);
		dlgConfirmacion.setLocationRelativeTo(null);
		dlgConfirmacion.setResizable(false);
		dlgConfirmacion.setSize(300,150);
		dlgConfirmacion.setVisible(true);
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
}
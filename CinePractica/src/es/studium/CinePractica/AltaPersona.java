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

public class AltaPersona implements WindowListener, ActionListener
{
	//Componentes de la ventana alta: ventana, labels, textFields y Buttons:
	Frame ventanaAlta = new Frame("Alta Persona");
	Label lblDni= new Label("DNI");
	Label lblNombre = new Label("Nombre:");
	Label lblPrimerApellido= new Label("Primer Apellido:");
	Label lblSegundoApellido= new Label("Segundo Apellido:");
	Label lblDomicilio= new Label("Domicilio:");
	Label lblTelefono= new Label("Teléfono:");
	Label lblEmail= new Label("Email:");

	TextField txtDni = new TextField(40);
	TextField txtNombre = new TextField(40);
	TextField txtPrimerApellido = new TextField(40);
	TextField txtSegundoApellido = new TextField(40);
	TextField txtDomicilio = new TextField(40);
	TextField txtTelefono = new TextField(40);
	TextField txtEmail = new TextField(40);
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar = new Button("Cancelar");

	BaseDatos bd = new BaseDatos();

	//Diálogo para cuando los registros introducidos por el usuario, no sean correctos o no:
	Dialog dlgConfirmacion = new Dialog(ventanaAlta, "Información importante", true);
	Label lblConfirmacion = new Label("XXXXXXXXXXXXXXXXXX");

	public AltaPersona()
	{
		ventanaAlta.setLayout(new FlowLayout());
		ventanaAlta.addWindowListener(this);
		dlgConfirmacion.addWindowListener(this);
		btnAceptar.addActionListener(this);
		btnCancelar.addActionListener(this);

		ventanaAlta.add(lblDni);
		ventanaAlta.add(txtDni);
		ventanaAlta.add(lblNombre);
		ventanaAlta.add(txtNombre);
		ventanaAlta.add(lblPrimerApellido);
		ventanaAlta.add(txtPrimerApellido);
		ventanaAlta.add(lblSegundoApellido);
		ventanaAlta.add(txtSegundoApellido);
		ventanaAlta.add(lblDomicilio);
		ventanaAlta.add(txtDomicilio);
		ventanaAlta.add(lblTelefono);
		ventanaAlta.add(txtTelefono);
		ventanaAlta.add(lblEmail);
		ventanaAlta.add(txtEmail);
		ventanaAlta.add(btnAceptar);
		ventanaAlta.add(btnCancelar);

		ventanaAlta.setLocationRelativeTo(null);
		ventanaAlta.setBackground(Color.pink);
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
			if((txtDni.getText().equals(""))||(txtNombre.getText().equals("")))
			{
				lblConfirmacion.setText("El DNI y Nombre no pueden estar vacíos");
				mostrarDialogo();
			}
			else
			{
				bd.conectar();
				int resultado = bd.altaPersonas(txtDni.getText(), txtNombre.getText(), txtPrimerApellido.getText(), txtSegundoApellido.getText(), 
						txtDomicilio.getText(), txtTelefono.getText(), txtEmail.getText());
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
		dlgConfirmacion.setSize(200,150);
		dlgConfirmacion.setVisible(true);
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
}



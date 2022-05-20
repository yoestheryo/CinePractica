package es.studium.CinePractica;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Login extends Frame implements WindowListener, ActionListener
{	
	private static final long serialVersionUID = 1L;
	//Componentes LOGIN
	Label lblUsuario = new Label("Usuario:");
	TextField txtUsuario = new TextField(20);
	Label lblClave = new Label("Clave:");
	TextField txtClave = new TextField(20);
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar = new Button("Cancelar");
	//Añadir imagen de fondo a la ventana login
	Image imagen;
	Toolkit herramienta;

	BaseDatos bd = new BaseDatos();					//	Se crea OBJETO de la clase BaseDatos (para todas las conexiones)

	//Dialogo de conexión incorrecta cuando usuario y contraseña erroneos.
	Dialog dlgMensaje = new Dialog(this, "Mensaje", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXXXXXXXXX");

	public Login()
	{
		setLayout(new FlowLayout());
		addWindowListener(this);
		dlgMensaje.addWindowListener(this);

		add(lblUsuario);
		add(txtUsuario);
		add(lblClave);
		txtClave.setEchoChar('*');	// Que al escribir la clave, no se vea lo que se escribe
		add(txtClave);

		btnAceptar.addActionListener(this);
		btnCancelar.addActionListener(this);
		add(btnAceptar);
		add(btnCancelar);
		
		herramienta = getToolkit();
		imagen = herramienta.getImage("img\\fondo.png");

		setTitle("Login");
		setLocationRelativeTo(null);
		setResizable(false);
		setSize(250,200);
		setVisible(true);
	}
	
	public void paint(Graphics g)
	{
		// Dibujar la imagen
		g.drawImage(imagen,4,23,this); 
	}
	
	//MAIN
	public static void main(String[] args)
	{
		new Login();
	}
	//Métodos implementados WindowListener y ActionListener
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		if(dlgMensaje.isActive())			
		{
			dlgMensaje.setVisible(false);	//Para que se cierre el diálogo
		}
		else 
		{
			System.exit(0);
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
		if(evento.getSource().equals(btnCancelar))		//Cuando Cancelar, se borre el texto, se borran todos los campos
		{
			txtUsuario.setText("");
			txtClave.setText("");
			txtUsuario.requestFocus();	
		}
		else if(evento.getSource().equals(btnAceptar))
		{
			//Conectar BD
			bd.conectar();				// método creado en BaseDatos para hacer conexión con la Base de Datos
			//Coger los textos de la ventana: usuario y clave:
			String usuario = txtUsuario.getText();				
			String clave = txtClave.getText();
			//Hacer un SELECT
			int resultado = bd.consultar("SELECT * FROM usuarios WHERE " +
					"nombreUsuario = '" + usuario + "' AND claveUsuario = SHA2('" + clave + "', 256);");	//SHA, función para encriptar contraseña

			//Si credenciales correctas, mostrar Menú Principal
			if(resultado==-1)
			{
				dlgMensaje.setLayout(new FlowLayout());
				lblMensaje.setText("Credenciales incorrectas");
				dlgMensaje.add(lblMensaje);

				dlgMensaje.setLocationRelativeTo(null);
				dlgMensaje.setResizable(false);
				dlgMensaje.setSize(200,150);
				dlgMensaje.setVisible(true);

			}
			//Si Credenciales incorrectas, diálogo error.
			else
			{
				new MenuPrincipal(resultado);
				setVisible(false);
			}
		}
		//Desconectar BD
		bd.desconectar();
	}
}

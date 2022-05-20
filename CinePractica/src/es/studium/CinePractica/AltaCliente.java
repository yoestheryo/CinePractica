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

public class AltaCliente implements WindowListener, ActionListener
{
	//Componentes de la ventana alta: ventana, labels, textFields y Buttons:
	Frame ventanaAlta = new Frame("Alta Cliente");
	Label lblFactura= new Label("Número de Factura:");
	Label lblVisitas = new Label("Número de visitas:");
	Label lblSocio= new Label("¿Es socio o no? (1/0):");
	Label lblIdPersonaFK= new Label("Elige datos de la persona:");	//FK Persona

	TextField txtFactura = new TextField(20);
	TextField txtVisitas = new TextField(20);
	TextField txtSocio = new TextField(20);
	Choice choPersonasFK = new Choice();
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar = new Button("Cancelar");

	BaseDatos bd = new BaseDatos();
	ResultSet rs = null;
	int idPersonaFK1;

	//Dialogo cuando los registros introducidos por el usuario, no sean correctos:
	Dialog dlgConfirmacion = new Dialog(ventanaAlta, "Información importante", true);
	Label lblConfirmacion = new Label("XXXXXXXXXXXXXXXXXX");

	public AltaCliente()
	{
		ventanaAlta.setLayout(new FlowLayout());
		ventanaAlta.addWindowListener(this);
		dlgConfirmacion.addWindowListener(this);
		btnAceptar.addActionListener(this);
		btnCancelar.addActionListener(this);

		ventanaAlta.add(lblFactura);
		ventanaAlta.add(txtFactura);
		ventanaAlta.add(lblVisitas);
		ventanaAlta.add(txtVisitas);
		ventanaAlta.add(lblSocio);
		ventanaAlta.add(txtSocio);
		ventanaAlta.add(lblIdPersonaFK);
		choPersonasFK.add("Elige Persona a Modificar...");			// Esta sería la primera opción siempre
		bd.conectar();					//1.	Te conectas a la BD
		
		rs = bd.miniChoicePersonas();	//2. Haces un Select de los campos que queremos

		try
		{
			while(rs.next())					//3.	Se rellena el Choice con lo que se nos devualve
			{
				choPersonasFK.add(rs.getInt("idPersona")+
						"-"+ rs.getString("dniPersona")+
						"-"+ rs.getString("nombrePersona")+
						"-"+ rs.getString("primerApellidoPersona"));
			}
		} catch (SQLException e){}
		bd.desconectar();					//4.	Nos desconectamos
		ventanaAlta.add(choPersonasFK);		//5.	Ahora sí, añadimos el Choice a la ventana
		ventanaAlta.add(btnAceptar);
		ventanaAlta.add(btnCancelar);

		ventanaAlta.setLocationRelativeTo(null);
		ventanaAlta.setBackground(Color.gray);
		ventanaAlta.setResizable(false);
		ventanaAlta.setSize(397,250);
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
		try
		{
			if(evento.getSource().equals(btnCancelar))
			{
				limpiarTextos();
			}
			else if(evento.getSource().equals(btnAceptar))
			{
				bd.conectar();
				String[] seleccionado = choPersonasFK.getSelectedItem().split("-");
				idPersonaFK1 = Integer.parseInt(seleccionado[0]);
				int resultado = bd.altaCliente(txtFactura.getText(), txtVisitas.getText(), txtSocio.getText(), idPersonaFK1);
				if(resultado == 0)
				{
					lblConfirmacion.setText("Alta correcta");
					limpiarTextos();	
				}
				else
				{
					lblConfirmacion.setText("Se ha producido un error.");
				}
			}
		}
		catch(NumberFormatException e)
		{
			lblConfirmacion.setText("Debe elegir una opción");
		}
		mostrarDialogo();
		bd.desconectar();
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
		txtFactura.setText("");
		txtVisitas.setText("");
		txtSocio.setText("");
		txtFactura.requestFocus();
	}
}



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

public class AltaAsistencia implements WindowListener, ActionListener
{
	Frame ventanaAlta = new Frame("Alta Asistencia");
	Label lblPersonaFK= new Label("Elige la Persona: ");
	Label lblCineFK = new Label("Elige el Cine:");
	Choice choPersonasFK = new Choice();
	Choice choCinesFK = new Choice();
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar = new Button("Cancelar");

	BaseDatos bd = new BaseDatos();
	ResultSet rs = null;
	ResultSet rs2 = null;
	int idPersonaFK2;
	int idCineFK3;
	int tipoUsuario;

	Dialog dlgConfirmacion = new Dialog(ventanaAlta, "Información importante", true);
	Label lblConfirmacion = new Label("XXXXXXXXXXXXXXXXXX");

	public AltaAsistencia(int tipoUsuario)
	{
		this.tipoUsuario = tipoUsuario;
		ventanaAlta.setLayout(new FlowLayout());
		ventanaAlta.addWindowListener(this);
		dlgConfirmacion.addWindowListener(this);
		btnAceptar.addActionListener(this);
		btnCancelar.addActionListener(this);
		bd.conectar();
		ventanaAlta.add(lblPersonaFK);
		choPersonasFK.add("Elige la Persona...");
		

		rs = bd.miniChoicePersonas();		//Linea 198 BaseDatos --Para PersonasFK
		try
		{
			while(rs.next())					//Se rellena el Choice con lo que se nos devuelve
			{
				choPersonasFK.add(rs.getInt("idPersona")+
						"-"+ rs.getString("dniPersona")+
						"-"+ rs.getString("nombrePersona")+
						"-"+ rs.getString("primerApellidoPersona"));
			}
		} catch (SQLException e){}
		ventanaAlta.add(choPersonasFK);
		
		ventanaAlta.add(lblCineFK);
		choCinesFK.add("Elige el Cine...");
		rs2 = bd.miniChoiceCines();		//Linea 299  BaseDatos. Para CinesFK
		try
		{
			while(rs2.next())					//Se rellena el Choice con lo que se nos devuelve
			{
				choCinesFK.add(rs2.getInt("idCine")+
						"-"+ rs2.getString("nombreCine")+
						"-"+ rs2.getString("ciudadCine")+
						"-"+ rs2.getString("telefonoCine"));
			}
		} catch (SQLException e){}
		
		bd.desconectar();					

		ventanaAlta.add(choCinesFK);
		ventanaAlta.add(btnAceptar);
		ventanaAlta.add(btnCancelar);
		
		ventanaAlta.setBackground(Color.orange);
		ventanaAlta.setLocationRelativeTo(null);
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
				//limpiarTextos();
			}
			else if(evento.getSource().equals(btnAceptar))
			{
				bd.conectar();
				String[] seleccionado = choPersonasFK.getSelectedItem().split("-");
				idPersonaFK2 = Integer.parseInt(seleccionado[0]);
				String[] seleccionado2 = choCinesFK.getSelectedItem().split("-");
				idCineFK3 = Integer.parseInt(seleccionado2[0]);
				int resultado = bd.altaAsistencia(tipoUsuario, idPersonaFK2, idCineFK3);
				if(resultado == 0)
				{
					lblConfirmacion.setText("Alta correcta");
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
}

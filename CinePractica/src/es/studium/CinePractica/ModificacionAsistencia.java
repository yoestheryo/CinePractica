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

public class ModificacionAsistencia implements WindowListener, ActionListener
{
	// Primera ventana
	Frame ventana = new Frame("Modificar Asistencia");
	Choice choAsistencias = new Choice();
	Button btnModificar = new Button("Modificar");

	// Segunda ventana
	Frame ventanaEdicion = new Frame("Campos Asistencias");
	Label lblId = new Label("idAsistencia:");
	TextField txtId = new TextField(18);
	Label lblPersonaFK = new Label("Asigne una Persona a esta Asistencia:");
	Choice choPersonasFK = new Choice();
	Label lblCineFK = new Label("Asignele un Cine a la Persona elegida anteriormente:");
	Choice choCinesFK = new Choice();
	Button btnGuardar = new Button("Guardar Cambios");

	Dialog dlgMensaje = new Dialog(ventana, "Información Importante", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXXXXX");

	int idAsistencia;
	int idPersonaFK;
	int idCineFK;
	BaseDatos bd = new BaseDatos();
	ResultSet rs = null;
	ResultSet rs2 = null;

	public ModificacionAsistencia()
	{
		ventana.setLayout(new FlowLayout());
		ventana.addWindowListener(this);
		ventanaEdicion.addWindowListener(this);
		dlgMensaje.addWindowListener(this);
		btnModificar.addActionListener(this);
		btnGuardar.addActionListener(this);
		txtId.setEditable(false);

		choAsistencias.add("Elige la asistencia a Modificar...");
		
		bd.conectar();
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
		bd.desconectar();

		ventana.add(choAsistencias);
		ventana.add(btnModificar);

		ventana.setSize(400,150);
		ventana.setBackground(Color.orange);
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
			ventanaEdicion.setVisible(false);
			ventana.setVisible(true);
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
		if(evento.getSource().equals(btnModificar))
		{
			if(choAsistencias.getSelectedIndex()==0)
			{
				lblMensaje.setText("Debe elegir una opción");
				mostrarMensaje();
			}
			else
			{
				bd.conectar();
				ventanaEdicion.setLayout(new FlowLayout());
				String[] seleccionado = choAsistencias.getSelectedItem().split("-");
				idAsistencia = Integer.parseInt(seleccionado[0]);
				ventanaEdicion.add(lblId);
				ventanaEdicion.add(txtId);
				txtId.setText(seleccionado[0]);
				ventanaEdicion.add(lblPersonaFK);
				choPersonasFK.add("Elige la Persona asignada...");
				int idPersonaFK = Integer.parseInt(seleccionado[1]);
				int idCineFK = Integer.parseInt(seleccionado[2]);
				
				int posicion = 0;
				int posicion2 = 0;
				int i = 1;
				int i2 = 1;
				
				bd.conectar();
				rs = bd.miniChoicePersonas();	
				try
				{
					while(rs.next())
					{
						if(idPersonaFK == rs.getInt("idPersona"))
						{
							posicion = i;
						}
						choPersonasFK.add(rs.getInt("idPersona")
								+ "-"+ rs.getString("dniPersona")
								+ "-"+ rs.getString("nombrePersona")
								+ "-"+ rs.getString("primerApellidoPersona")
								+ "\n");
						i++;
					}choPersonasFK.select(posicion);
				}catch (SQLException e) {}
				ventanaEdicion.add(choPersonasFK);
				
				ventanaEdicion.add(lblCineFK);
				choCinesFK.add("Elige el Cine asignado...");
				rs2 = bd.miniChoiceCines();		
				try
				{
					while(rs2.next())
					{
						if(idCineFK == rs2.getInt("idCine"))
						{
							posicion2 = i2;
						}
						choCinesFK.add(rs2.getInt("idCine")
								+ "-"+ rs2.getString("nombreCine")
								+ "-"+ rs2.getString("ciudadCine")
								+ "-"+ rs2.getString("telefonoCine")
								+ "\n");
						i2++;
					}choCinesFK.select(posicion2);
				}catch (SQLException e) {}
				
				ventanaEdicion.add(choCinesFK);
				bd.desconectar();

				ventanaEdicion.add(btnGuardar);
				ventanaEdicion.setSize(280,400);
				ventanaEdicion.setBackground(Color.orange);
				ventanaEdicion.setResizable(true);
				ventanaEdicion.setLocationRelativeTo(null);
				ventanaEdicion.setVisible(true);
			}
		}

		else if(evento.getSource().equals(btnGuardar))
		{
			bd.conectar();
			String[] seleccionado2 = choPersonasFK.getSelectedItem().split("-");
			idPersonaFK = Integer.parseInt(seleccionado2[0]);
			String[] seleccionado3 = choCinesFK.getSelectedItem().split("-");
			idCineFK = Integer.parseInt(seleccionado3[0]);
			int resultado = bd.modificarAsistencia(Integer.parseInt(txtId.getText()), idPersonaFK, idCineFK);	

			if(resultado == 0)
			{
				lblMensaje.setText("Se ha modificado correctamente");
				choAsistencias.removeAll();
				choAsistencias.add("Elige la asistencia a Modificar...");
				rs = bd.rellenarAsistencias();
				try
				{
					while(rs.next())
					{
						choAsistencias.add(rs.getInt("idAsistir")+
								"-"+ rs.getString("idPersonaFK2")+
								"-"+ rs.getString("idCineFK3"));
					}ventana.setVisible(false);
				} catch (SQLException e){}
				catch(NumberFormatException e)
				{
					System.out.println(e.getMessage());
				}
			}
			else
			{
				lblMensaje.setText("Se ha producido un error.");
			}
			mostrarMensaje();	
		}
		bd.desconectar();	
	}
	
	public void mostrarMensaje()
	{
		dlgMensaje.setLayout(new FlowLayout());
		dlgMensaje.add(lblMensaje);
		dlgMensaje.setLocationRelativeTo(null);
		dlgMensaje.setResizable(false);
		dlgMensaje.setSize(250,150);
		dlgMensaje.setVisible(true);
	}
}

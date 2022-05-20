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

public class ModificacionCliente implements WindowListener, ActionListener
{
	Frame ventana = new Frame("Modificar Cliente");
	Choice choClientes = new Choice();
	Button btnModificar = new Button("Modificar");

	Frame ventanaEdicion = new Frame("Campos Cliente");
	Label lblId = new Label("idCliente:");
	TextField txtId = new TextField(20);
	Label lblFactura = new Label("Número de Factura:");
	TextField txtFactura= new TextField(20);
	Label lblVisitas = new Label("Número de Visitas:");
	TextField txtVisitas = new TextField(20);
	Label lblSocio = new Label("Es Socio o no (1/0):");
	TextField txtSocio = new TextField(20);
	Label lblIdPersonaFK = new Label("idPersona:");
	Choice choPersonas = new Choice();
	Button btnGuardar = new Button("Guardar Cambios");
	Button btnCancelar = new Button("Cancelar");

	Dialog dlgMensaje = new Dialog(ventana, "Información Importante", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXXXXX");

	int idCliente;
	int idPersona;
	BaseDatos bd = new BaseDatos();
	ResultSet rs = null;

	public ModificacionCliente()
	{
		ventana.setLayout(new FlowLayout());
		ventana.addWindowListener(this);
		ventanaEdicion.addWindowListener(this);
		dlgMensaje.addWindowListener(this);
		btnModificar.addActionListener(this);
		btnGuardar.addActionListener(this);
		btnCancelar.addActionListener(this);
		txtId.setEditable(false);

		choClientes.add("Elige el cliente a Modificar...");
		choPersonas.add("Elige la Persona asignada...");
		bd.conectar();
		rs = bd.rellenarClientes();
		try
		{
			while(rs.next())
			{
				choClientes.add(rs.getInt("idCliente")+
						"-"+ rs.getString("facturaCliente")+
						"-"+ rs.getString("numeroVisitasCliente")+
						"-"+ rs.getString("serSocio")+
						"-"+ rs.getString("idPersonaFK1"));
			}
		} catch (SQLException e){}
		bd.desconectar();

		ventana.add(choClientes);
		ventana.add(btnModificar);

		ventana.setSize(400,150);
		ventana.setBackground(Color.gray);
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
		if(evento.getSource().equals(btnModificar))	//Que cuando pulsemos modificar, salte la ventana con los campos rellenos
		{
			if(choClientes.getSelectedIndex()==0)
			{
				lblMensaje.setText("Debe elegir una opción");
				mostrarMensaje();
			}
			else
			{
				bd.conectar();
				ventanaEdicion.setLayout(new FlowLayout());
				String[] seleccionado = choClientes.getSelectedItem().split("-");
				idPersona = Integer.parseInt(seleccionado[0]);
				ventanaEdicion.add(lblId);
				txtId.setText(seleccionado[0]);
				ventanaEdicion.add(txtId);
				ventanaEdicion.add(lblFactura);
				txtFactura.setText(seleccionado[1]);
				ventanaEdicion.add(txtFactura);
				ventanaEdicion.add(lblVisitas);
				txtVisitas.setText(seleccionado[2]);
				ventanaEdicion.add(txtVisitas);
				ventanaEdicion.add(lblSocio);
				txtSocio.setText(seleccionado[3]);
				ventanaEdicion.add(txtSocio);
				ventanaEdicion.add(lblIdPersonaFK);
				int idPersonaFK = Integer.parseInt(seleccionado[4]);
				int posicion = 0;
				int i=1;

				bd.conectar();
				rs = bd.rellenarPersonas();
				try
				{
					while(rs.next())
					{
						if(idPersonaFK == rs.getInt("idPersona"))
						{
							posicion = i;
						}
						choPersonas.add(rs.getInt("idPersona")
								+ "-"+ rs.getString("dniPersona")
								+ "-"+ rs.getString("nombrePersona")
								+ "-"+ rs.getString("primerApellidoPersona")
								+ "-"+ rs.getString("segundoApellidoPersona")
								+ "\n");
						i++;
					}choPersonas.select(posicion);
				}catch (SQLException e) {}
				bd.desconectar();

				ventanaEdicion.add(choPersonas);
				ventanaEdicion.add(btnGuardar);
				ventanaEdicion.add(btnCancelar);

				ventanaEdicion.setSize(280,400);
				ventanaEdicion.setResizable(true);
				ventanaEdicion.setBackground(Color.gray);
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
			int resultado = bd.modificarCliente(Integer.parseInt(txtId.getText()), txtFactura.getText(), txtVisitas.getText(),
					txtSocio.getText(), idPersona);	
			
			if(resultado == 0)
			{
				lblMensaje.setText("Se ha modificado correctamente");
				choClientes.removeAll();
				choClientes.add("Elige el cliente a Modificar...");
				rs = bd.rellenarClientes();
				try
				{
					while(rs.next())
					{
						choClientes.add(rs.getInt("idCliente")+
								"-"+ rs.getString("facturaCliente")+
								"-"+ rs.getString("numeroVisitasCliente")+
								"-"+ rs.getString("serSocio")+
								"-"+ rs.getString("idPersonaFK1"));
					}ventana.setVisible(false);
				} catch (SQLException e){}
			}
			else
			{
				lblMensaje.setText("Se ha producido un error.");
			}
			mostrarMensaje();	
		}
		bd.desconectar();	
	}

	public void limpiarTextos()
	{
		txtFactura.setText("");
		txtVisitas.setText("");
		txtSocio.setText("");
		txtFactura.requestFocus();
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


package es.studium.CinePractica;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MenuPrincipal extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;

	//Componentes: ventana Menú Principal:
	MenuBar barraMenu = new MenuBar();

	// Alta, baja, modificación y consulta:
	Menu mnuAsistencias = new Menu("Asistencias");
	MenuItem itAltaAsistencia = new MenuItem("Nueva Asistencia");
	MenuItem itBajaAsistencia = new MenuItem("Eliminar Asistencia");	
	MenuItem itModificacionAsistencia = new MenuItem("Modificar Asistencia");
	MenuItem itConsultaAsistencia = new MenuItem("Listado Asistencias");

	Menu mnuPersonas = new Menu("Personas");
	MenuItem itAltaPersona = new MenuItem("Nueva Persona");
	MenuItem itBajaPersona = new MenuItem("Eliminar Persona");	
	MenuItem itModificacionPersona = new MenuItem("Modificar Persona");
	MenuItem itConsultaPersona = new MenuItem("Listado Personas");

	Menu mnuClientes = new Menu("Clientes");
	MenuItem itAltaCliente = new MenuItem("Nuevo Cliente");
	MenuItem itBajaCliente = new MenuItem("Eliminar Cliente");	
	MenuItem itModificacionCliente = new MenuItem("Modificar Cliente");
	MenuItem itConsultaCliente = new MenuItem("Listado Clientes");

	Menu mnuCines = new Menu("Cines");
	MenuItem itAltaCine = new MenuItem("Nuevo Cine");
	MenuItem itBajaCine = new MenuItem("Eliminar Cine");	
	MenuItem itModificacionCine = new MenuItem("Modificar Cine");
	MenuItem itConsultaCine = new MenuItem("Listado Cines");
	
	Image imagen;
	Toolkit herramienta;

	public MenuPrincipal(int tipo)
	{
		setLayout(new FlowLayout());
		addWindowListener(this);
		
		mnuAsistencias.addActionListener(this);
		itAltaAsistencia.addActionListener(this);
		itBajaAsistencia.addActionListener(this);
		itModificacionAsistencia.addActionListener(this);
		itConsultaAsistencia.addActionListener(this);
		
		mnuPersonas.addActionListener(this);
		itAltaPersona.addActionListener(this);
		itBajaPersona.addActionListener(this);
		itModificacionPersona.addActionListener(this);
		itConsultaPersona.addActionListener(this);
		
		mnuClientes.addActionListener(this);
		itAltaCliente.addActionListener(this);
		itBajaCliente.addActionListener(this);
		itModificacionCliente.addActionListener(this);
		itConsultaCliente.addActionListener(this);
		
		mnuCines.addActionListener(this);
		itAltaCine.addActionListener(this);
		itBajaCine.addActionListener(this);
		itModificacionCine.addActionListener(this);
		itConsultaCine.addActionListener(this);
		
		herramienta = getToolkit();
		imagen = herramienta.getImage("img\\cine.jpg");
		
		barraMenu.add(mnuAsistencias);
		mnuAsistencias.add(itAltaAsistencia);
		if(tipo==1)								//Si el tipo de Usuario es 1, añadir estás dos opciones más
		{
			mnuAsistencias.add(itBajaAsistencia);
			mnuAsistencias.add(itModificacionAsistencia);
		}
		mnuAsistencias.add(itConsultaAsistencia);

		barraMenu.add(mnuPersonas);
		mnuPersonas.add(itAltaPersona);
		if(tipo==1)
		{
			mnuPersonas.add(itBajaPersona);
			mnuPersonas.add(itModificacionPersona);
		}
		mnuPersonas.add(itConsultaPersona);

		barraMenu.add(mnuClientes);
		mnuClientes.add(itAltaCliente);
		if(tipo==1)
		{
			mnuClientes.add(itBajaCliente);
			mnuClientes.add(itModificacionCliente);
		}
		mnuClientes.add(itConsultaCliente);

		barraMenu.add(mnuCines);
		mnuCines.add(itAltaCine);
		if(tipo==1)
		{
			mnuCines.add(itBajaCine);
			mnuCines.add(itModificacionCine);
		}
		mnuCines.add(itConsultaCine);

		setMenuBar(barraMenu);	// Al menú le añadimos la barra de menú 

		setTitle("Menú Principal:");
		setLocationRelativeTo(null);
		setResizable(false);
		setSize(300,180);
		setVisible(true);		
	}
	
	public void paint(Graphics g)
	{
		g.drawImage(imagen,4,23,this); 
	}

	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		System.exit(0);
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
		if(evento.getSource().equals(itConsultaPersona))
		{
			new ConsultaPersonas();
		}
		else if(evento.getSource().equals(itAltaPersona))
		{
			new AltaPersona();
		}
		else if(evento.getSource().equals(itBajaPersona))
		{
			new BajaPersona();
		}
		else if(evento.getSource().equals(itModificacionPersona))
		{
			new ModificacionPersona();
		}
		else if(evento.getSource().equals(itConsultaCliente))
		{
			new ConsultaClientes();
		}
		else if(evento.getSource().equals(itAltaCliente))
		{
			new AltaCliente();
		}
		else if(evento.getSource().equals(itBajaCliente))
		{
			new BajaCliente();
		}
		else if(evento.getSource().equals(itModificacionCliente))
		{
			new ModificacionCliente();
		}
		else if(evento.getSource().equals(itConsultaCine))
		{
			new ConsultaCines();
		}
		else if(evento.getSource().equals(itAltaCine))
		{
			new AltaCine();
		}
		else if(evento.getSource().equals(itBajaCine))
		{
			new BajaCine();
		}
		else if(evento.getSource().equals(itModificacionCine))
		{
			new ModificacionCine();
		}
		else if(evento.getSource().equals(itConsultaAsistencia))
		{
			new ConsultaAsistencias();
		}
		else if(evento.getSource().equals(itAltaAsistencia))
		{
			new AltaAsistencia();
		}
		else if(evento.getSource().equals(itBajaAsistencia))
		{
			new BajaAsistencia();
		}
		else if(evento.getSource().equals(itModificacionAsistencia))
		{
			new ModificacionAsistencia();
		}
	}
}


package es.studium.CinePractica;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseDatos
{
	//Conexión a BD
	String driver = "com.mysql.cj.jdbc.Driver";			
	String url = "jdbc:mysql://localhost:3306/cines_pr";	
	String login = "admin_cines_pr";					
	String password = "Studium2022;";			
	String sentencia = "";
	Connection connection = null;		
	Statement statement = null;				
	ResultSet resultado = null;
	int tipoUsuario;

	public BaseDatos(){}

	public void conectar() 			// Cada vez que vayamos a hacer conexión, lo llamamos en otras clases
	{
		try
		{
			// Cargar los controladores para el acceso a la BD
			Class.forName(driver);
			// Establecer la conexión con la BD Empresa
			connection = DriverManager.getConnection(url, login, password);
		}
		catch (ClassNotFoundException cnfe){}
		catch (SQLException sqle){}
	}

	public void desconectar()						// Para DESCONECTAR
	{
		try
		{
			if(connection!=null)
			{
				connection.close();
			}
		}
		catch (SQLException e){}
	}

	public int consultar(String sentencia)			//Usado en la clase Login
	{														
		int resultado = -1;
		ResultSet rs = null;
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			rs = statement.executeQuery(sentencia);
			if(rs.next())
			{
				resultado = rs.getInt("tipoUsuario");
			}
		}
		catch (SQLException e) {}
		return(resultado);
	}

	// executeUpdate para todo menos consulta: ALTA, BAJA, MODIFICACIÓN
	// executeQuery para CONSULTA/SELECT

	public String consultarPersonas(int tipoUsuario)				// Método usado en clase ConsultaPersonas (SELECT)
	{
		String texto = String.format("%-3s %-10s %-7s %-13s %-13s %-13s\n", "Id", "Nombre", "Apellido1", "Apellido2", "Teléfono", "Email");
		String sentencia = "SELECT * FROM personas;";
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			resultado = statement.executeQuery(sentencia);
			while(resultado.next()) 
			{
				texto = texto + (String.format("%-3d %-10s %-9s %-15s %-15s %-14s\n",  //% por cada columnas, - para alineacion a la izq, número de espacios y tipo de dato: d, entero; s: string.
						resultado.getInt("idPersona"),						
						resultado.getString("nombrePersona"),
						resultado.getString("primerApellidoPersona"), 
						resultado.getString("segundoApellidoPersona"),
						resultado.getString("telefonoPersona"),
						resultado.getString("emailPersona"))); 
			}
		}
		catch (SQLException e) {}
		return(texto);
	}

	// Método para INSERT usado en la clase ALTAPERSONA
	public int altaPersonas(int tipoUsuario, String dni, String nombre, String primerApellido, String segundoApellido, String domicilio, String telefono, String email)
	{
		int resultado = 0; 	// Se inicializa a 0 y significa que todo bien y no da error. Saltará diálogo con OK
		String sentencia = "INSERT INTO personas VALUES(null, '" +dni+ "', '" +nombre+ "', '" +primerApellido+ "', '" 
				+segundoApellido+ "', '" +domicilio+ "', '" +telefono+ "', '" +email+ "');";
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			statement.executeUpdate(sentencia);
		}
		catch (SQLException e) 
		{
			resultado = 1;	// Cualquier número diferente de 0: Error al introducir los datos,no se produce el alta. Salta diálogo con ERROR
		}
		return(resultado);	
	}


	public ResultSet rellenarPersonas()			//Rellenar el choice de la clase BajaPersona
	{
		ResultSet rs = null;
		try
		{	
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT * FROM personas");
		} 
		catch (SQLException e){}
		return(rs);
	}

	public int eliminarPersona(int tipoUsuario, int idPersona)				// Borrar registro Personas de la clase Baja Persona
	{
		int resultado = 0;
		sentencia = "DELETE FROM personas WHERE idPersona = "+idPersona;
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			statement.executeUpdate(sentencia);	
		}
		catch (SQLException e)
		{
			resultado = -1;
		}
		return(resultado);
	}

	public int modificarPersonas(int tipoUsuario, int idPersona, String dni, String nombre, String apellido1, String apellido2, String dom, String tel, String email)
	{
		int resultado = 0;
		String sentencia = "UPDATE personas SET dniPersona = '"+dni+
				"', nombrePersona ='" +nombre+ 
				"', primerApellidoPersona = '" +apellido1+
				"', segundoApellidoPersona = '" +apellido2+
				"', domicilioPersona = '" +dom+
				"', telefonoPersona = '" +tel+
				"', emailPersona = '"+email+"' WHERE idPersona = " +idPersona;
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			statement.executeUpdate(sentencia);
		} 
		catch (SQLException e)
		{	
			resultado = -1;
		}
		return resultado;
	}

	public String consultarClientes(int tipoUsuario)		//Método utilizado en ConsultaClientes para mostrar la información. Tiene 1 FK (Personas).
	{
		String texto = String.format("%-5s %-12s %-9s %-10s %-10s\n", "Id", "Factura", "Visitas", "Socio", "Datos Personas");

		String sentencia = "SELECT * FROM clientes JOIN personas ON idPersona = idPersonaFK1;";
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			resultado = statement.executeQuery(sentencia);
			while(resultado.next()) 
			{
				texto = texto + (String.format("%-5d %-12s %-10s %-15s %-15s\n", 
						resultado.getInt("idCliente"),						
						resultado.getString("facturaCliente"),
						resultado.getString("numeroVisitasCliente"), 
						resultado.getString("serSocio"),
						resultado.getString("nombrePersona")+ " - " +
						resultado.getString("primerApellidoPersona")+ " - " +
						resultado.getString("dniPersona")));
			}
		}
		catch (SQLException e) {}
		return (texto);
	}

	public int altaCliente(int tipoUsuario, String factura, String visitas, String socio, int idPersonaFK1)	//Método creado para clase altaClientes con el FK
	{
		int resultado = 0;
		try
		{	
			statement = connection.createStatement();
			guardarLog(tipoUsuario, "INSERT INTO clientes VALUES(null, '"+factura+"', '"+visitas+"', '"+socio+"', '"+idPersonaFK1+"');");
			statement.executeUpdate("INSERT INTO clientes VALUES(null, '"+factura+"', '"+visitas+"', '"+socio+"', '"+idPersonaFK1+"');");	
		}
		catch (SQLException e) 
		{
			resultado = -1;
		}	
		return (resultado);
	}

	public ResultSet miniChoicePersonas()	//SELECT personas, para rellenar el CHOICE de la clase Clientes y Asistencias: idPersonaFK	
	{
		ResultSet rs = null;
		try
		{	
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT idPersona, dniPersona, nombrePersona, primerApellidoPersona FROM personas;");
		} 
		catch (SQLException e){}
		return(rs);
	}

	public ResultSet rellenarClientes()		//Usado en clase BajaCliente para rellenar el CHOICE
	{
		ResultSet rs = null;
		try
		{	
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT * FROM clientes;");
		} 
		catch (SQLException e){}
		return(rs);
	}

	public int eliminarCliente(int tipoUsuario, int idCliente)				//Usado en clase BajaCliente para borrar un cliente
	{
		int resultado = 0;
		sentencia = "DELETE FROM clientes WHERE idCliente = "+idCliente;
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			statement.executeUpdate(sentencia);	
		}
		catch (SQLException e)
		{
			resultado = -1;
		}
		return(resultado);
	}

	public int modificarCliente(int tipoUsuario, int idCliente, String factura, String visitas, String socio, int idPersona)		//Clase Modificación Cliente. Actualizar info
	{
		int resultado = 0;
		String sentencia = "UPDATE clientes SET idCliente = '"+idCliente+
				"', facturaCliente ='" +factura+ 
				"', numeroVisitasCliente = '" +visitas+
				"', serSocio = '" +socio+"' WHERE idCliente = " +idPersona;
		System.out.println(sentencia);
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			statement.executeUpdate(sentencia);
		} 
		catch (SQLException e)
		{	
			resultado = -1;
		}
		return resultado;
	}

	public String consultarCines(int tipoUsuario)
	{
		String texto = String.format("%-3s %-10s %-7s %-13s %-13s %-13s %-13s\n", "Id", "Nombre", "Ciudad", "Dirección", "Teléfono", "Página Web", "Email");
		String sentencia = "SELECT * FROM cines;";
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			resultado = statement.executeQuery(sentencia);
			while(resultado.next()) 
			{
				texto = texto + (String.format("%-3d %-10s %-9s %-15s %-15s %-14s %-15s\n", 
						resultado.getInt("idCine"),						
						resultado.getString("nombreCine"),
						resultado.getString("ciudadCine"), 
						resultado.getString("direccionCine"),
						resultado.getString("telefonoCine"),
						resultado.getString("paginaWebCine"),
						resultado.getString("emailCine"))); 
			}
		}
		catch (SQLException e) {}
		return(texto);
	}

	public int altaCines(int tipoUsuario, String nombre, String ciudad, String direccion, String telefono, String web, String email)
	{
		int resultado = 0;
		String sentencia = "INSERT INTO cines VALUES(null, '" +nombre+ "', '" +ciudad+ "', '" 
				+direccion+ "', '" +telefono+ "', '" +web+ "', '" +email+ "');";
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			statement.executeUpdate(sentencia);
		}
		catch (SQLException e) 
		{
			resultado = 1;	
		}
		return(resultado);	
	}
	
	public ResultSet miniChoiceCines()		
	{
		ResultSet rs2 = null;
		try
		{	
			statement = connection.createStatement();
			rs2 = statement.executeQuery("SELECT idCine, nombreCine, ciudadCine, telefonoCine FROM cines;");
		} 
		catch (SQLException e){}
		return(rs2);
	}
	
	public ResultSet rellenarCines()			// Método creado para el choice de BajaCine, que se reutiliza en la clase ModificacionCine y ModificacionAsistencia
	{
		ResultSet rs = null;
		try
		{	
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT * FROM cines;");
		} 
		catch (SQLException e){}
		return(rs);
	}
	
	public int eliminarCines(int tipoUsuario, int idCine)	
	{
		int resultado = 0;
		sentencia = "DELETE FROM cines WHERE idCine = "+idCine;
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			statement.executeUpdate(sentencia);	
		}
		catch (SQLException e)
		{
			resultado = -1;
		}
		return(resultado);
	}
	
	public int modificarCines(int tipoUsuario, int idCine, String nombre, String ciudad, String direccion, String telefono, String web, String email)
	{
		int resultado = 0;
		String sentencia = "UPDATE cines SET nombreCine = '"+nombre+
				"', ciudadCine = '" +ciudad+
				"', direccionCine = '" +direccion+
				"', telefonoCine = '" +telefono+
				"', paginaWebCine = '" +web+
				"', emailCine = '"+email+"' WHERE idCine = " +idCine;
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			statement.executeUpdate(sentencia);
		} 
		catch (SQLException e)
		{	
			resultado = -1;
		}
		return resultado;
	}
	
	public String consultarAsistencias(int tipoUsuario)		//Método utilizado en ConsultaAsistencias para mostrar la información. Tiene 2 FK (Personas y Cines).
	{
		String texto = String.format("%-5s %-35s %-50s\n", "Id", "Datos Persona", "Datos Cine");

		String sentencia = "SELECT * FROM asistir JOIN personas ON idPersona = idPersonaFK2 JOIN cines ON idCine = idCineFK3;";
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			resultado = statement.executeQuery(sentencia);
			while(resultado.next()) 
			{
				texto = texto + (String.format("%-5d %-30s %-80s\n", 
						resultado.getInt("idAsistir"),						
						resultado.getString("nombrePersona")+" - "+
						resultado.getString("dniPersona")+" - "+
						resultado.getString("primerApellidoPersona"),
						resultado.getString("nombreCine")+" - "+
						resultado.getString("telefonoCine")));
			}
		}
		catch (SQLException e) {}
		return (texto);
	}
	public int altaAsistencia(int tipoUsuario, int idPersonaFK2, int idCineFK3)	//Método creado para clase altaAsistencias con 2 FKs
	{
		int resultado = 0;
		try
		{	
			statement = connection.createStatement();
			guardarLog(tipoUsuario, "INSERT INTO asistir VALUES(null, '"+idPersonaFK2+"', '"+idCineFK3+"');");
			statement.executeUpdate("INSERT INTO asistir VALUES(null, '"+idPersonaFK2+"', '"+idCineFK3+"');");	
		}
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
			resultado = -1;
		}	
		return (resultado);
	}
	public ResultSet rellenarAsistencias()		// Usado en clase BajaAsistencia y ModificacionAsistencia para rellenar los CHOICES
	{
		ResultSet rs = null;
		try
		{	
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT * FROM asistir;");
		} 
		catch (SQLException e){}
		return(rs);
	}
	
	public int eliminarAsistencia(int tipoUsuario, int idAsistencia)				//Usado en clase BajaAsistencia
	{
		int resultado = 0;
		sentencia = "DELETE FROM asistir WHERE idAsistir = "+idAsistencia;
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			statement.executeUpdate(sentencia);	
		}
		catch (SQLException e)
		{
			resultado = -1;
		}
		return(resultado);
	}

	public int modificarAsistencia(int tipoUsuario, int idAsistencia, int idPersonaFK, int idCineFK)
	{
		int resultado = 0;
		String sentencia = "UPDATE asistir SET idPersonaFK2 = '"+idPersonaFK+
				"', idCineFK3 = '" +idCineFK+
				"' WHERE idAsistir = " +idAsistencia;
		try
		{
			statement = connection.createStatement();
			guardarLog(tipoUsuario, sentencia);
			statement.executeUpdate(sentencia);
		} 
		catch (SQLException e)
		{	
			resultado = -1;
		}
		return resultado;
	}
	
	public void guardarLog(int tipoUsuario, String mensaje)
	{
		String usuario;
		if(tipoUsuario==0)
		{
			usuario = "Básico";
		}
		else
		{
			usuario = "Administrador";
		}
		Date fecha = new Date();
		String pattern = "dd/MM/YYYY HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		try
		{
			FileWriter fw = new FileWriter("historico.log", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter salida = new PrintWriter(bw);

			salida.println("["+simpleDateFormat.format(fecha)+"]["+usuario + "]["+mensaje+"]");

			salida.close();
			bw.close();
			fw.close();
		}
		catch(IOException ioe){}
	}
}	



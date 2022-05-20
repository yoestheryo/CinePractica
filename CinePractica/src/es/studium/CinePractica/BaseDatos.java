package es.studium.CinePractica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDatos
{
	//Conexi�n a BD
	String driver = "com.mysql.cj.jdbc.Driver";			
	String url = "jdbc:mysql://localhost:3306/cines_pr";	
	String login = "admin_cines_pr";					
	String password = "Studium2022;";			
	String sentencia = "";
	Connection connection = null;		
	Statement statement = null;				
	ResultSet resultado = null;

	public BaseDatos(){}

	public void conectar() 			// Cada vez que vayamos a hacer conexi�n, lo llamamos en otras clases
	{
		try
		{
			// Cargar los controladores para el acceso a la BD
			Class.forName(driver);
			// Establecer la conexi�n con la BD Empresa
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
			rs = statement.executeQuery(sentencia);
			if(rs.next())
			{
				resultado = rs.getInt("tipoUsuario");
			}
		}
		catch (SQLException e) {}
		return(resultado);
	}

	// executeUpdate para todo menos consulta: ALTA, BAJA, MODIFICACI�N
	// executeQuery para CONSULTA/SELECT

	public String consultarPersonas()				// M�todo usado en clase ConsultaPersonas (SELECT)
	{
		String texto = String.format("%-3s %-10s %-7s %-13s %-13s %-13s\n", "Id", "Nombre", "Apellido1", "Apellido2", "Tel�fono", "Email");
		String sentencia = "SELECT * FROM personas;";
		try
		{
			statement = connection.createStatement();
			resultado = statement.executeQuery(sentencia);
			while(resultado.next()) 
			{
				texto = texto + (String.format("%-3d %-10s %-9s %-15s %-15s %-14s\n",  //% por cada columnas, - para alineacion a la izq, n�mero de espacios y tipo de dato: d, entero; s: string.
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

	// M�todo para INSERT usado en la clase ALTAPERSONA
	public int altaPersonas(String dni, String nombre, String primerApellido, String segundoApellido, String domicilio, String telefono, String email)
	{
		int resultado = 0; 	// Se inicializa a 0 y significa que todo bien y no da error. Saltar� di�logo con OK
		String sentencia = "INSERT INTO personas VALUES(null, '" +dni+ "', '" +nombre+ "', '" +primerApellido+ "', '" 
				+segundoApellido+ "', '" +domicilio+ "', '" +telefono+ "', '" +email+ "');";
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(sentencia);
		}
		catch (SQLException e) 
		{
			resultado = 1;	// Cualquier n�mero diferente de 0: Error al introducir los datos,no se produce el alta. Salta di�logo con ERROR
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

	public int eliminarPersona(int idPersona)				// Borrar registro Personas de la clase Baja Persona
	{
		int resultado = 0;
		sentencia = "DELETE FROM personas WHERE idPersona = "+idPersona;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(sentencia);	
		}
		catch (SQLException e)
		{
			resultado = -1;
		}
		return(resultado);
	}

	public int modificarPersonas(int idPersona, String dni, String nombre, String apellido1, String apellido2, String dom, String tel, String email)
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
			statement.executeUpdate(sentencia);
		} 
		catch (SQLException e)
		{	
			resultado = -1;
		}
		return resultado;
	}

	public String consultarClientes()		//M�todo utilizado en ConsultaClientes para mostrar la informaci�n. Tiene 1 FK (Personas).
	{
		String texto = String.format("%-5s %-12s %-9s %-10s %-10s\n", "Id", "Factura", "Visitas", "Socio", "idPersona");

		String sentencia = "SELECT * FROM clientes;";
		try
		{
			statement = connection.createStatement();
			resultado = statement.executeQuery(sentencia);
			while(resultado.next()) 
			{
				texto = texto + (String.format("%-5d %-12s %-10s %-15s %-15s\n", 
						resultado.getInt("idCliente"),						
						resultado.getString("facturaCliente"),
						resultado.getString("numeroVisitasCliente"), 
						resultado.getString("serSocio"),
						resultado.getString("idPersonaFK1")));
			}
		}
		catch (SQLException e) {}
		return (texto);
	}

	public int altaCliente(String factura, String visitas, String socio, int idPersonaFK1)	//M�todo creado para clase altaClientes con el FK
	{
		int resultado = 0;
		try
		{	
			statement = connection.createStatement();
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

	public int eliminarCliente(int idCliente)				//Usado en clase BajaCliente para borrar un cliente
	{
		int resultado = 0;
		sentencia = "DELETE FROM clientes WHERE idCliente = "+idCliente;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(sentencia);	
		}
		catch (SQLException e)
		{
			resultado = -1;
		}
		return(resultado);
	}

	public int modificarCliente(int idCliente, String factura, String visitas, String socio, int idPersona)		//Clase Modificaci�n Cliente. Actualizar info
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
			statement.executeUpdate(sentencia);
		} 
		catch (SQLException e)
		{	
			resultado = -1;
		}
		return resultado;
	}

	public String consultarCines()
	{
		String texto = String.format("%-3s %-10s %-7s %-13s %-13s %-13s %-13s\n", "Id", "Nombre", "Ciudad", "Direcci�n", "Tel�fono", "P�gina Web", "Email");
		String sentencia = "SELECT * FROM cines;";
		try
		{
			statement = connection.createStatement();
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

	public int altaCines(String nombre, String ciudad, String direccion, String telefono, String web, String email)
	{
		int resultado = 0;
		String sentencia = "INSERT INTO cines VALUES(null, '" +nombre+ "', '" +ciudad+ "', '" 
				+direccion+ "', '" +telefono+ "', '" +web+ "', '" +email+ "');";
		try
		{
			statement = connection.createStatement();
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
	
	public ResultSet rellenarCines()			// M�todo creado para el choice de BajaCine, que se reutiliza en la clase ModificacionCine y ModificacionAsistencia
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
	
	public int eliminarCines(int idCine)	
	{
		int resultado = 0;
		sentencia = "DELETE FROM cines WHERE idCine = "+idCine;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(sentencia);	
		}
		catch (SQLException e)
		{
			resultado = -1;
		}
		return(resultado);
	}
	
	public int modificarCines(int idCine, String nombre, String ciudad, String direccion, String telefono, String web, String email)
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
			statement.executeUpdate(sentencia);
		} 
		catch (SQLException e)
		{	
			resultado = -1;
		}
		return resultado;
	}
	
	public String consultarAsistencias()		//M�todo utilizado en ConsultaAsistencias para mostrar la informaci�n. Tiene 2 FK (Personas y Cines).
	{
		String texto = String.format("%-5s %-10s %-15s\n", "Id", "idPersona", "idCine");

		String sentencia = "SELECT * FROM asistir;";
		try
		{
			statement = connection.createStatement();
			resultado = statement.executeQuery(sentencia);
			while(resultado.next()) 
			{
				texto = texto + (String.format("%-5d %-16s %-18s\n", 
						resultado.getInt("idAsistir"),						
						resultado.getString("idPersonaFK2"),
						resultado.getString("idCineFK3")));
			}
		}
		catch (SQLException e) {}
		return (texto);
	}
	public int altaAsistencia(int idPersonaFK2, int idCineFK3)	//M�todo creado para clase altaAsistencias con 2 FKs
	{
		int resultado = 0;
		try
		{	
			statement = connection.createStatement();
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
	
	public int eliminarAsistencia(int idAsistencia)				//Usado en clase BajaAsistencia
	{
		int resultado = 0;
		sentencia = "DELETE FROM asistir WHERE idAsistir = "+idAsistencia;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(sentencia);	
		}
		catch (SQLException e)
		{
			resultado = -1;
		}
		return(resultado);
	}

	public int modificarAsistencia(int idAsistencia, int idPersonaFK, int idCineFK)
	{
		int resultado = 0;
		String sentencia = "UPDATE asistir SET idPersonaFK2 = '"+idPersonaFK+
				"', idCineFK3 = '" +idCineFK+
				"' WHERE idAsistir = " +idAsistencia;
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(sentencia);
		} 
		catch (SQLException e)
		{	
			resultado = -1;
		}
		return resultado;
	}
}	



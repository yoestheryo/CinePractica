package es.studium.CinePractica;

import java.awt.Button;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class ConsultaPersonas implements WindowListener, ActionListener
{
	Frame ventanaConsulta = new Frame("Listado de Personas");
	TextArea texto = new TextArea(15,53);
	Label lblPersonas = new Label("Personas");
	Button btnPdf = new Button("Exportar a PDF");
	
	BaseDatos bd = new BaseDatos();	//Creación objeto de la Clase BaseDatos
	public static final String DEST="ConsultaPersonas.pdf";
	PdfFont font;
	PdfWriter writer;
	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet = null;
	
	int tipoUsuario;
	
	public ConsultaPersonas(int tipoUsuario)
	{
		this.tipoUsuario = tipoUsuario;
		ventanaConsulta.setLayout(new FlowLayout());
		ventanaConsulta.addWindowListener(this);
		btnPdf.addActionListener(this);

		ventanaConsulta.add(lblPersonas);
		bd.conectar();
		texto.setEnabled(false);
		texto.setText(bd.consultarPersonas(tipoUsuario)); 	//Método consultar creado en BaseDatos: SELECT * FROM
		bd.desconectar();
		ventanaConsulta.add(texto);
		ventanaConsulta.add(btnPdf);

		ventanaConsulta.setLocationRelativeTo(null);
		ventanaConsulta.setBackground(Color.pink);
		ventanaConsulta.setResizable(false);
		ventanaConsulta.setSize(680,290);
		ventanaConsulta.setVisible(true);
	}
	
	@Override
	public void windowOpened(WindowEvent e){}
	@Override
	public void windowClosing(WindowEvent e)
	{
		ventanaConsulta.setVisible(false);
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
		if(evento.getSource().equals(btnPdf))
		{	
			//Initialize PDF writer
			try
			{
				writer = new PdfWriter(DEST);
			} catch (FileNotFoundException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Initialize PDF document
			PdfDocument pdf = new PdfDocument(writer);
			// Initialize document
			Document document = new Document(pdf);
			//Add paragraph to the document
			document.add(new Paragraph("Personas:"));
			// Create a PdfFont
			try
			{
				font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
			} catch (java.io.IOException e)
			{

				e.printStackTrace();
			}

			bd.conectar();
			document.add(new Paragraph(bd.consultarClientes(tipoUsuario)));
			bd.desconectar();

			document.close();
			// Open the new PDF document just created
			try
			{
				Desktop.getDesktop().open(new File(DEST));
			} catch (java.io.IOException e)
			{

				e.printStackTrace();
			}
		}
	}
}

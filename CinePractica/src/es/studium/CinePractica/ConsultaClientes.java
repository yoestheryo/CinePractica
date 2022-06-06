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

public class ConsultaClientes implements WindowListener, ActionListener
{
	Frame ventanaConsulta = new Frame("Consulta Clientes");
	TextArea texto = new TextArea(11,45);
	Label lblClientes = new Label("Información sobre los Clientes:");
	Button btnPdf = new Button("Exportar a PDF");
	//Button btnExcel = new Button("Exportar a Excel");

	BaseDatos bd = new BaseDatos();
	//Objetos necesarios para exportar a PDF
	public static final String DEST="ConsultaClientes.pdf";
	PdfFont font;
	PdfWriter writer;
	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet = null;

	int tipoUsuario;

	ConsultaClientes(int tipoUsuario)
	{
		this.tipoUsuario=tipoUsuario;
		ventanaConsulta.setLayout(new FlowLayout());
		ventanaConsulta.addWindowListener(this);
		btnPdf.addActionListener(this);
		//btnExcel.addActionListener(this);

		ventanaConsulta.add(lblClientes);
		bd.conectar();
		texto.setText(bd.consultarClientes(tipoUsuario));
		bd.desconectar();
		ventanaConsulta.add(texto);
		ventanaConsulta.add(btnPdf);
		//ventanaConsulta.add(btnExcel);
		texto.setEnabled(false);

		ventanaConsulta.setLocationRelativeTo(null);
		ventanaConsulta.setBackground(Color.gray);
		ventanaConsulta.setResizable(false);
		ventanaConsulta.setSize(670,260);
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
			document.add(new Paragraph("Clientes:"));
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



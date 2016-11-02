package visual;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.mozilla.universalchardet.UniversalDetector;

//Acciones
class Controlador_pestanias_archivos
{
	private HashMap<Path,RTextScrollPane> file_to_component=new HashMap<>();
	private HashMap<RTextScrollPane,Path> component_to_file=new HashMap<>();
	private Component parent;
	public Controlador_pestanias_archivos(Component parent, JTabbedPane tabbedPane) {
		this.parent = parent;
		this.tabbedPane = tabbedPane;
	}
	private JTabbedPane tabbedPane;
	private List<CaretListener> caretListener=new ArrayList<>();
	private void open_file_on_new_tab(File f)
	{
					
		UniversalDetector detector= new UniversalDetector(null);			
		try(FileInputStream fis= new FileInputStream(f))
		{
			int nread;
			byte[] buf = new byte[4096];
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			  detector.handleData(buf, 0, nread);
			}
			detector.dataEnd();
		} catch (IOException e3 ) {
			e3.printStackTrace();
		}
		String charsetName = detector.getDetectedCharset()==null ?  "UTF-8":detector.getDetectedCharset();
		try(BufferedReader reader=Files.newBufferedReader(f.toPath(),Charset.forName(charsetName)))
		{
			Path p = f.toPath();
			RTextScrollPane sp = crea_scroll_pane();
			file_to_component.put(p, sp);
			component_to_file.put(sp, p);
			tabbedPane.addTab(p.getFileName().toString(),sp);
			tabbedPane.setSelectedComponent(sp);
			((RTextArea)sp.getViewport().getView()).read(reader,f);
		}
		catch (MalformedInputException e2) {
			JOptionPane.showMessageDialog(parent,"Error de encoding","Error al Abrir Archivo",JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	private void update_path(Path old_path,Path new_path)
	{
		file_to_component.put(new_path,(RTextScrollPane) tabbedPane.getSelectedComponent());
		if(old_path!=null)file_to_component.remove(old_path);
		tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(),new_path.getFileName().toString());
	}
	private void save_active_file_as()
	{
		JFileChooser fileChooser = new JFileChooser();
		if(fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION)
		{
			Path path=fileChooser.getSelectedFile().toPath();
			if(!Files.exists(path) || 
					JOptionPane.showConfirmDialog(parent, "El archivo "+path.getFileName()+" ya existe. ¿Sobreescribir?") == JOptionPane.OK_OPTION)
			{
				try(Writer writer = Files.newBufferedWriter(path))
				{
					JScrollPane current_component = (JScrollPane)tabbedPane.getSelectedComponent();
					((JTextComponent)current_component.getViewport().getView()).write(writer);
					update_path(component_to_file.get(current_component),path);					
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(parent,"Error de guardado","Error al Guardar Archivo",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		}
	}
	private void aniade_listeners()
	{
		for(RTextScrollPane rt:file_to_component.values())
		{
			aniade_listeners(rt);
		}
	}
	private void aniade_listeners(RTextScrollPane rt){
		for(CaretListener c:caretListener)
		{
			rt.getTextArea().addCaretListener(c);
		}
	}
	private RTextScrollPane crea_scroll_pane()
	{
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setPopupMenu(null);
		RTextScrollPane sp = new RTextScrollPane(textArea);
		aniade_listeners(sp);
		sp.setBorder(null);
		return sp;
	}		
	
	public void accion_abrir_archivo(ActionEvent e)
	{
		JFileChooser file_chooser = new JFileChooser();
		file_chooser.showOpenDialog(parent);
		File selected_file =file_chooser.getSelectedFile();
		if(selected_file != null){
			open_file_on_new_tab(selected_file);
			
		}
	}
	public void accion_guardar_como(ActionEvent e)
	{
		if(tabbedPane.getSelectedIndex()>=0) save_active_file_as();
	}
	public void accion_cerrar_archivo(ContainerEvent e)
	{
		file_to_component.remove(component_to_file.remove(component_to_file));
	}
	public void accion_guardar_archivo(ActionEvent e)
	{
		if(tabbedPane.getSelectedIndex()>=0)
		{
			JScrollPane scrollPane =(JScrollPane) tabbedPane.getSelectedComponent();
			if(component_to_file.containsKey(scrollPane))
			{
				try(Writer writer = Files.newBufferedWriter(component_to_file.get(scrollPane)))
				{
					((JTextComponent)scrollPane.getViewport().getView()).write(writer);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(parent,"Error de guardado","Error al Guardar Archivo",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
			else
			{
				save_active_file_as();
			}
		}
	}
	public void posiciona_caret(Path path,int pos){
		if(file_to_component.containsKey(path)){
			RTextScrollPane rTextScrollPane=file_to_component.get(path);
			tabbedPane.setSelectedComponent(rTextScrollPane);
			rTextScrollPane.getTextArea().setCaretPosition(pos);
			
		}
	}
	public void add_caret_listener(CaretListener c){
		caretListener.add(c);
		aniade_listeners();
	}
	public void accion_nuevo_archivo(ActionEvent e)
	{
		RTextScrollPane sp = crea_scroll_pane();
		tabbedPane.add("Nuevo", sp);
		tabbedPane.setSelectedComponent(sp);
	}
}
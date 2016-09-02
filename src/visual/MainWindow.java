package visual;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerAdapter;
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
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.mozilla.universalchardet.UniversalDetector;
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JTable table;
	private JLabel label_caret;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(
					        UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		Controlador_pestanias_archivos fileActions = new Controlador_pestanias_archivos();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 707, 412);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton btnNewButton = new JButton("A.L.");
		btnNewButton.setEnabled(false);
		btnNewButton.setToolTipText("Analizador L\u00E9xico");
		menuBar.add(btnNewButton);
		
		JButton btnAnalizadorSintctico = new JButton("A. S.");
		btnAnalizadorSintctico.setEnabled(false);
		btnAnalizadorSintctico.setToolTipText("Analizador Sint\u00E1ctico");
		menuBar.add(btnAnalizadorSintctico);
		
		JButton btnAse = new JButton("A.Se.");
		btnAse.setEnabled(false);
		btnAse.setToolTipText("Analizador Sem\u00E1ntico");
		menuBar.add(btnAse);
		
		JButton btnGi = new JButton("G.I.");
		btnGi.setEnabled(false);
		btnGi.setToolTipText("Generaci\u00F3n Intermedia");
		menuBar.add(btnGi);
		
		JButton btnNewButton_1 = new JButton("Ejecuci\u00F3n");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.setToolTipText("Ejecuci\u00F3n");
		menuBar.add(btnNewButton_1);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton btnNuevo = new JButton("Nuevo");
		
		JButton btnAbrir = new JButton("Abrir");
		btnAbrir.addActionListener(fileActions::accion_abrir_archivo);
		btnAbrir.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/Directory.gif")));
		toolBar.add(btnAbrir);
		btnNuevo.setToolTipText("Abrir...");
		btnNuevo.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/File.gif")));
		btnNuevo.addActionListener(fileActions::accion_nuevo_archivo);
		toolBar.add(btnNuevo);
		
		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(fileActions::accion_guardar_archivo);
		btnGuardar.setIcon(new ImageIcon(MainWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		btnGuardar.setToolTipText("Guardar");
		toolBar.add(btnGuardar);
		
		JButton btnGuardarComo = new JButton("Guardar como ....");
		btnGuardarComo.addActionListener(fileActions::accion_guardar_como);
		btnGuardarComo.setIcon(new ImageIcon(MainWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/floppy.gif")));
		btnGuardarComo.setToolTipText("Guardar como...");
		toolBar.add(btnGuardarComo);
		
		JSplitPane panel = new JSplitPane();
		panel.setBorder(null);
		panel.setResizeWeight(1.0);
		panel.setOneTouchExpandable(true);
		panel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(panel);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addContainerListener(new ContainerAdapter() {
			@Override
			public void componentAdded(ContainerEvent arg0) {
				tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1,new ButtonTabComponent(tabbedPane));
			}
			@Override
			public void componentRemoved(ContainerEvent arg0) {
				fileActions.accion_cerrar_archivo(arg0);
			}
		});
		tabbedPane.setBorder(null);
		panel.setLeftComponent(tabbedPane);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		panel.setRightComponent(scrollPane);
		
		table = new JTable();
		table.setBorder(null);
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		panel_1.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		panel_1.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panel_1.setBorder(null);
		contentPane.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue);
		
		label_caret = new JLabel("");
		label_caret.setAlignmentX(Component.RIGHT_ALIGNMENT);
		label_caret.setBorder(null);
		label_caret.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label_caret.setVerticalAlignment(SwingConstants.BOTTOM);
		panel_1.add(label_caret);
		
	}
	
	
	
	
	//Acciones
	private class Controlador_pestanias_archivos
	{
		private HashMap<Path,RTextScrollPane> file_to_component=new HashMap<>();
		private HashMap<RTextScrollPane,Path> component_to_file=new HashMap<>();
		private void add_opened_file(File f)
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
				JOptionPane.showMessageDialog(MainWindow.this,"Error de encoding","Error al Abrir Archivo",JOptionPane.ERROR_MESSAGE);
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		private void modifica_pestana_archivo(Path old_path,Path new_path)
		{
			file_to_component.put(new_path,(RTextScrollPane) tabbedPane.getSelectedComponent());
			if(old_path!=null)file_to_component.remove(old_path);
			tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(),new_path.getFileName().toString());
		}
		public void accion_abrir_archivo(ActionEvent e)
		{
			JFileChooser file_chooser = new JFileChooser();
			file_chooser.showOpenDialog(MainWindow.this);
			File selected_file =file_chooser.getSelectedFile();
			if(selected_file != null){
				add_opened_file(selected_file);
				
			}
		}
		public void accion_guardar_como(ActionEvent e)
		{
			if(tabbedPane.getSelectedIndex()>=0) guardar_como();
		}
		private void guardar_como()
		{
			JFileChooser fileChooser = new JFileChooser();
			if(fileChooser.showSaveDialog(MainWindow.this) == JFileChooser.APPROVE_OPTION)
			{
				Path path=fileChooser.getSelectedFile().toPath();
				if(!Files.exists(path) || 
						JOptionPane.showConfirmDialog(MainWindow.this, "El archivo "+path.getFileName()+" ya existe. ¿Sobreescribir?") == JOptionPane.OK_OPTION)
				{
					try(Writer writer = Files.newBufferedWriter(path))
					{
						JScrollPane current_component = (JScrollPane)tabbedPane.getSelectedComponent();
						((JTextComponent)current_component.getViewport().getView()).write(writer);
						modifica_pestana_archivo(component_to_file.get(current_component),path);					
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MainWindow.this,"Error de guardado","Error al Guardar Archivo",JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
			}
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
						JOptionPane.showMessageDialog(MainWindow.this,"Error de guardado","Error al Guardar Archivo",JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
				else
				{
					guardar_como();
				}
			}
		}
		private RTextScrollPane crea_scroll_pane()
		{
			RSyntaxTextArea textArea = new RSyntaxTextArea();
			textArea.setPopupMenu(null);
			textArea.addCaretListener(new Position_Updater());
			RTextScrollPane sp = new RTextScrollPane(textArea);
			sp.setBorder(null);
			return sp;
		}
		public void accion_nuevo_archivo(ActionEvent e)
		{
			RTextScrollPane sp = crea_scroll_pane();
			tabbedPane.add("Nuevo", sp);
			tabbedPane.setSelectedComponent(sp);
		}
	}
	private class Position_Updater implements CaretListener{
		private int getRow(int pos, JTextComponent editor) {
	        int rn = (pos==0) ? 1 : 0;
	        try {
	            int offs=pos;
	            while( offs>0) {
	                offs=Utilities.getRowStart(editor, offs)-1;
	                rn++;
	            }
	        } catch (BadLocationException e) {
	            e.printStackTrace();
	        }
	        return rn;
	    }
	    private int getColumn(int pos, JTextComponent editor) {
	        try {
	            return pos-Utilities.getRowStart(editor, pos)+1;
	        } catch (BadLocationException e) {
	            e.printStackTrace();
	        }
	        return -1;
	    }
       public void caretUpdate(CaretEvent e) {
    	   label_caret.setText("Lin. "+getRow(e.getDot(), (JTextComponent)e.getSource())+" Col. "
       +getColumn(e.getDot(), (JTextComponent)e.getSource()));
       }
	}
	
}

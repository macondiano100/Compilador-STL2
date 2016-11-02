package visual;
/**TODO
 * Vaciar tabla de errores cada nueva compilacion
 * Desactivar botones al cambiar de archivo
 * **/
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import compilador.Compilador;
import compilador.Error_info;
import visual.tabla_errores.Modelo_Tabla_Errores;
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JTable table;
	private JLabel label_caret;
	private Controlador_pestanias_archivos fileActions;
	private Modelo_Tabla_Errores modelo_Tabla_Errores;
	private Compilador compilador;
	private JButton btnAnalizadorSintctico;
	private JButton btnNewButton;
	private JButton btnAnalizadorSemantico;

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
		compilador=new Compilador();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 707, 412);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		btnNewButton = new JButton("A.L.");
		btnNewButton.addActionListener(this::accion_lexico);
		btnNewButton.setToolTipText("Analizador L\u00E9xico");
		menuBar.add(btnNewButton);
		
		btnAnalizadorSintctico = new JButton("A. S.");
		btnAnalizadorSintctico.addActionListener(this::accion_sintactico);
		btnAnalizadorSintctico.setEnabled(false);
		btnAnalizadorSintctico.setToolTipText("Analizador Sint\u00E1ctico");
		menuBar.add(btnAnalizadorSintctico);
		
		btnAnalizadorSemantico = new JButton("A.Se.");
		btnAnalizadorSemantico.setEnabled(false);
		btnAnalizadorSemantico.setToolTipText("Analizador Sem\u00E1ntico");
		menuBar.add(btnAnalizadorSemantico);
		
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
		JSplitPane panel = new JSplitPane();
		panel.setBorder(null);
		panel.setResizeWeight(1.0);
		panel.setOneTouchExpandable(true);
		panel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(panel);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		fileActions = new Controlador_pestanias_archivos(this,tabbedPane);
		fileActions.add_caret_listener(new Position_Status_Updater());
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
		
		
		
		modelo_Tabla_Errores=new Modelo_Tabla_Errores();
		table.setModel(modelo_Tabla_Errores);
		table.addMouseListener(new Mouse_Adapter_Tabla_Errores());
		
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
		
	}

	private void accion_lexico(ActionEvent e)
	{
		try {
			compilador.inicia_parseo(fileActions.get_active_file());
			modelo_Tabla_Errores.add_items(compilador.get_errores_lexicos());
			if(compilador.get_errores_lexicos().isEmpty())btnAnalizadorSintctico.setEnabled(true);
		} catch (IOException e1) {
			show_exception(e1);
		}
	}
	private void accion_sintactico(ActionEvent e)
	{
		List<Error_info> errores_sintacticos=compilador.get_errores_sintacticos();
		modelo_Tabla_Errores.add_items(errores_sintacticos);
		if(errores_sintacticos.isEmpty()) btnAnalizadorSemantico.setEnabled(true);
	}
	public void show_exception(Exception e)
	{
		JOptionPane.showMessageDialog(this, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
	}
	//helper functions for carets
	private static int getRow(int pos, JTextComponent editor) {
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
    private static int getColumn(int pos, JTextComponent editor) {
        try {
            return pos-Utilities.getRowStart(editor, pos)+1;
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public class Mouse_Adapter_Tabla_Errores extends MouseAdapter{
	    @Override
	    public void mouseClicked(java.awt.event.MouseEvent evt) {
	        int row = table.rowAtPoint(evt.getPoint());
	        if (row >= 0) {
				Error_info error_info = modelo_Tabla_Errores.getValueAt(table.getSelectedRow());
	        	try {
					int pos=fileActions.get_active_text_area().getTextArea().getLineStartOffset(error_info.row-1)+error_info.col;
				fileActions.posiciona_caret(error_info.path, 
						pos-1);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}

	        }
	    }
	}

	private class Position_Status_Updater implements CaretListener{
       public void caretUpdate(CaretEvent e) {
    	   label_caret.setText("Lin. "+getRow(e.getDot(), (JTextComponent)e.getSource())+" Col. "
       +getColumn(e.getDot(), (JTextComponent)e.getSource()));
       }
	}
	
}

package visual;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
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

	private class Position_Status_Updater implements CaretListener{
       public void caretUpdate(CaretEvent e) {
    	   label_caret.setText("Lin. "+getRow(e.getDot(), (JTextComponent)e.getSource())+" Col. "
       +getColumn(e.getDot(), (JTextComponent)e.getSource()));
       }
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
	
}

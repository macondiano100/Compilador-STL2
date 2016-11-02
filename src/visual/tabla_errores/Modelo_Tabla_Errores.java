package visual.tabla_errores;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class Modelo_Tabla_Errores extends AbstractTableModel {
	enum Column_Info_Errores
	{
		RUTA("Archivo",String.class),LINEA("Linea",Integer.class),COLUMNA("Columna",Integer.class),MENSAJE("Error",String.class);
		public final String COL_TITLE;
		public final Class<?> COL_CLASS;
		private Column_Info_Errores(String cOL_TITLE, Class<?> cOL_CLASS) {
			COL_TITLE = cOL_TITLE;
			COL_CLASS = cOL_CLASS;
		}
	}
	private List<error_info> errores;
	public Modelo_Tabla_Errores(){
		 errores=new ArrayList<>();
	}
	public void add_item(error_info error)
	{
		errores.add(error);
		fireTableRowsInserted(errores.size()-1, errores.size()-1);
	}
	public Modelo_Tabla_Errores(List<error_info> errores) {
		this.errores=errores;
	}
	@Override
	public int getColumnCount() {
		return Column_Info_Errores.values().length;
	}

	@Override
	public int getRowCount() {
		return errores.size();
	}
	public error_info getValueAt(int row)
	{
		return errores.get(row);
	}
	 @Override
	public String getColumnName(int col) {
		return Column_Info_Errores.values()[col].COL_TITLE;
	}
	@Override
	public Object getValueAt(int row, int col) {
		error_info error=errores.get(row);
		switch (Column_Info_Errores.values()[col]) {
		case LINEA:
			return error.row;
		case COLUMNA:
			return error.col;
		case MENSAJE:
			return error.error_message;
		case RUTA:
			return error.path.toString();
		default:
			break;
		}
		return null;
	}

}

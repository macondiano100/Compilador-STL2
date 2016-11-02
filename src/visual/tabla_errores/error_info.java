package visual.tabla_errores;

import java.nio.file.Path;

public class error_info {
	public Path path;
	public int pos;
	public int col;
	public int row;
	public String error_message;
	public error_info(Path path,int pos, int col, int row, String error_message) {
		this.path=path;
		this.pos = pos;
		this.col = col;
		this.row = row;
		this.error_message = error_message;
	}
	
}

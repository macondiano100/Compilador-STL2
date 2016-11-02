package compilador;

import java.nio.file.Path;

public class Error_info {
	@Override
	public String toString() {
		return "Error_info [path=" + path + ", col=" + col + ", row=" + row + ", error_message="
				+ error_message + "]";
	}
	public Path path;
	public int col;
	public int row;
	public String error_message;
	public Error_info(Path path,int row, int col,  String error_message) {
		this.path=path;
		this.row = row;
		this.col = col;
		this.error_message = error_message;
	}
	
}

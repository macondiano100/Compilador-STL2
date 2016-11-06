package compilador.tabsim.simbolo;
import java.io.Serializable;
import java.nio.file.Path;

import antlr4.ProgramaParser.Tipo_datoContext;
import compilador.TIPO_DATO;
import compilador.tabsim.Scope;


public class Simbolo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numeroLinea;
	private Path path;
	private String token;
	private String valorIniciail;
	private String valorFinal;
	private TIPO_DATO tipoIdenficador;
	private String posMemoria;
	private Scope scope;
	public int getNumeroLinea() {
		return numeroLinea;
	}
	public void setNumeroLinea(int numeroLinea) {
		this.numeroLinea = numeroLinea;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getValorIniciail() {
		return valorIniciail;
	}
	public void setValorIniciail(String valorIniciail) {
		this.valorIniciail = valorIniciail;
	}
	public String getValorFinal() {
		return valorFinal;
	}
	public void setValorFinal(String valorFinal) {
		this.valorFinal = valorFinal;
	}
	public TIPO_DATO getTipoIdenficador() {
		return tipoIdenficador;
	}
	public void setTipoIdenficador(TIPO_DATO tipoIdenficador) {
		this.tipoIdenficador = tipoIdenficador;
	}
	public String getPosMemoria() {
		return posMemoria;
	}
	public void setPosMemoria(String posMemoria) {
		this.posMemoria = posMemoria;
	}
	public Scope get_scope() {
		return scope;
	}
	public void set_scope(Scope enclosing_scope) {
		this.scope = enclosing_scope;
	}
	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}
}

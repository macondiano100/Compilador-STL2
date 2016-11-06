package compilador.tabsim.simbolo;

import java.nio.file.Path;

import compilador.TIPO_DATO;
import compilador.tabsim.Scope;


public class Simbolo_Builder {
	private Simbolo simbolo=new Simbolo();
	public Simbolo_Builder numeroLinea(int n)
	{
		simbolo.setNumeroLinea(n);
		return this;
	}
	public Simbolo_Builder path(Path p)
	{
		simbolo.setPath(p);
		return this;		
	}
	public Simbolo_Builder token(String t)
	{
		simbolo.setToken(t);
		return this;
	}
	public Simbolo_Builder valorIniciail(String t)
	{
		simbolo.setValorIniciail(t);
		return this;
	}
	public Simbolo_Builder valorFinal(String t)
	{
		simbolo.setValorFinal(t);
		return this;
	}
	public Simbolo_Builder tipoIdenficador(TIPO_DATO tipoIdenficador)
	{
		simbolo.setTipoIdenficador(tipoIdenficador);
		return this;
	}
	public Simbolo_Builder posMemoria(String pos)
	{
		simbolo.setPosMemoria(pos);
		return this;
	}
	public Simbolo_Builder scope (Scope scope)
	{
		simbolo.set_scope(scope);
		return this;
	}
	public Simbolo get(){return simbolo;}
}

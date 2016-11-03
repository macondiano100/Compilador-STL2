package compilador.tabsim;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import compilador.tabsim.simbolo.Simbolo;

public class Scope {

    public final int genId;
    public Scope enclosingScope;
    protected Map<String, Simbolo> simbolos = new LinkedHashMap<String, Simbolo>();

    public Scope( final int genId, Scope enclosingScope) {
        this.genId = genId;
        this.enclosingScope = enclosingScope;
    }
    /** Define a symbol in the current scope */
    public void define(Simbolo Simbolo) {
        Simbolo.set_scope(this);
        simbolos.put(Simbolo.getToken(), Simbolo);
    }
    /**
     * Look up the symbol name in this scope and, if not found, 
     * progressively search the enclosing scopes. 
     * Return null if not found in any applicable scope.
     */
    public Optional<Simbolo> resolve(String name) {
    	Scope current=this;
    	while(current != null){
            Simbolo simbolo = current.simbolos.get(name);
            if(simbolo != null) return Optional.of(simbolo);
            current=current.enclosingScope;
    	}
        return Optional.empty(); // not found
    }
    /** Where to look next for symbol */
    public Scope enclosingScope() {
        return enclosingScope;
    }

    public String toString() {
        return simbolos.keySet().toString();
    }
}

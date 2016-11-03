package compilador.tabsim;
import java.util.ArrayList;
import java.util.Stack;

public class TablaSimbolos {

    protected Stack<Scope> scopeStack;
    protected ArrayList<Scope> allScopes;
    protected int genId;
    
    public TablaSimbolos() {
        init();
    }

    protected void init() {
        scopeStack = new Stack<>();
        allScopes = new ArrayList<>();
        genId = 0;
        Scope globals = new Scope(nextGenId(), null);
        scopeStack.push(globals);
        allScopes.add(globals);
    }

    public Scope pushScope() {
        Scope enclosingScope = scopeStack.peek();
        Scope scope = new Scope(nextGenId(), enclosingScope);
        scopeStack.push(scope);
        allScopes.add(scope);
        return scope;
    }

    public void popScope() {
        scopeStack.pop();
    }

    public Scope currentScope() {
        if (scopeStack.size() > 0) {
            return scopeStack.peek();
        }
        return allScopes.get(0);
    }

    public Scope getScope(int genId) {
        for (Scope scope : scopeStack) {
            if (scope.genId == genId) return scope;
        }
        return null;
    }

    private int nextGenId() {
        genId++;
        return genId;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Scope scope : scopeStack) {
            sb.append(scope.toString());
        }
        return sb.toString();
    }
}
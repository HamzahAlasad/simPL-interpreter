package simpl.parser.ast;

import simpl.interpreter.BoolValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class Cond extends Expr {

    public Expr e1, e2, e3;

    public Cond(Expr e1, Expr e2, Expr e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    public String toString() {
        return "(if " + e1 + " then " + e2 + " else " + e3 + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult res1 = e1.typecheck(E);
        Substitution s1 = res1.t.unify(Type.BOOL);
        
        TypeEnv env2 = s1.compose(res1.s).compose(E);
        
        TypeResult res2 = e2.typecheck(env2);
        TypeResult res3 = e3.typecheck(res2.s.compose(env2));
        
        Substitution s2 = res2.t.unify(res2.s.apply(res3.t));
        
        Substitution all = s2.compose(res3.s).compose(res2.s).compose(s1).compose(res1.s);
        return TypeResult.of(all, all.apply(res2.t));
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        BoolValue cond = (BoolValue) e1.eval(s);
        if (cond.b) return e2.eval(s);
        else return e3.eval(s);
    }
}

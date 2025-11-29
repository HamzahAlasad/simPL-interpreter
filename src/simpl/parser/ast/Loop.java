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

public class Loop extends Expr {

    public Expr e1, e2;

    public Loop(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public String toString() {
        return "(while " + e1 + " do " + e2 + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult res1 = e1.typecheck(E);
        Substitution s1 = res1.t.unify(Type.BOOL);
        
        TypeResult res2 = e2.typecheck(s1.compose(res1.s).compose(E));
        
        return TypeResult.of(res2.s.compose(res2.s).compose(s1).compose(res1.s), Type.UNIT);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        while (((BoolValue) e1.eval(s)).b) {
            e2.eval(s);
        }
        return Value.UNIT;
    }
}

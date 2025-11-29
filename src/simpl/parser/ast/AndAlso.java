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

public class AndAlso extends BinaryExpr {

    public AndAlso(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " andalso " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult res1 = l.typecheck(E);
        Substitution s1 = res1.t.unify(Type.BOOL);
        
        TypeResult res2 = r.typecheck(s1.compose(res1.s).compose(E));
        Substitution s2 = res2.t.unify(Type.BOOL);
        
        return TypeResult.of(s2.compose(res2.s).compose(s1).compose(res1.s), Type.BOOL);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        BoolValue v1 = (BoolValue) l.eval(s);
        if (!v1.b) return new BoolValue(false);
        BoolValue v2 = (BoolValue) r.eval(s);
        return new BoolValue(v2.b);
    }
}

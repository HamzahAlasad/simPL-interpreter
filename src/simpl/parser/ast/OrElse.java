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

public class OrElse extends BinaryExpr {

    public OrElse(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " orelse " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult lRes = l.typecheck(E);
        TypeResult rRes = r.typecheck(lRes.s.compose(E));
        
        Substitution s = rRes.s.compose(lRes.s);
        s = s.compose(lRes.t.unify(Type.BOOL));
        s = s.compose(rRes.t.unify(Type.BOOL));
        
        return TypeResult.of(s, Type.BOOL);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        BoolValue v1 = (BoolValue) l.eval(s);
        if (v1.b) return new BoolValue(true);
        BoolValue v2 = (BoolValue) r.eval(s);
        return new BoolValue(v2.b);
    }
}

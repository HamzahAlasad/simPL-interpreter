package simpl.parser.ast;

import simpl.interpreter.RefValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.RefType;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public class Assign extends BinaryExpr {

    public Assign(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return l + " := " + r;
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult resL = l.typecheck(E);
        TypeResult resR = r.typecheck(resL.s.compose(E));
        
        Substitution s = resL.t.unify(new RefType(resR.t));
        
        return TypeResult.of(s.compose(resR.s).compose(resL.s), Type.UNIT);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        RefValue ptr = (RefValue) l.eval(s);
        Value val = r.eval(s);
        s.M.put(ptr.p, val);
        return Value.UNIT;
    }
}

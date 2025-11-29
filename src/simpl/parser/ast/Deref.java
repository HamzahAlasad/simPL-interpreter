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
import simpl.typing.TypeVar;

public class Deref extends UnaryExpr {

    public Deref(Expr e) {
        super(e);
    }

    public String toString() {
        return "!" + e;
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult res = e.typecheck(E);
        TypeVar alpha = new TypeVar(true);
        Substitution s = res.t.unify(new RefType(alpha));
        return TypeResult.of(s.compose(res.s), s.apply(alpha));
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        RefValue ptr = (RefValue) e.eval(s);
        Value v = s.M.get(ptr.p);
        if (v == null) throw new RuntimeError("Segmentation fault (invalid pointer)");
        return v;
    }
}

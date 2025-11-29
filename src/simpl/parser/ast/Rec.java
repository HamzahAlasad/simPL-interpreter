package simpl.parser.ast;

import simpl.interpreter.Env;
import simpl.interpreter.RecValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class Rec extends Expr {

    public Symbol x;
    public Expr e;

    public Rec(Symbol x, Expr e) {
        this.x = x;
        this.e = e;
    }

    public String toString() {
        return "(rec " + x + "." + e + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeVar alpha = new TypeVar(false);
        TypeEnv newEnv = TypeEnv.of(E, x, alpha);

        TypeResult res = e.typecheck(newEnv);
        Substitution s = res.s.compose(res.t.unify(res.s.apply(alpha)));

        return TypeResult.of(s, s.apply(res.t));
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        RecValue rv = new RecValue(s.E, x, e);
        Env newEnv = new Env(s.E, x, rv);
        return e.eval(State.of(newEnv, s.M, s.p));
    }
}

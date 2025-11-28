package simpl.parser.ast;

import simpl.interpreter.ConsValue;
import simpl.interpreter.Env;
import simpl.interpreter.FunValue;
import simpl.interpreter.PairValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.interpreter.lib.fst;
import simpl.interpreter.lib.hd;
import simpl.interpreter.lib.snd;
import simpl.interpreter.lib.tl;
import simpl.parser.Symbol;
import simpl.typing.ArrowType;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class App extends BinaryExpr {

    public App(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeVar alpha = new TypeVar(false);

        TypeResult resL = l.typecheck(E);
        TypeResult resR = r.typecheck(E);

        Substitution s = resR.s.compose(resL.s);

        ArrowType expectedFuncType = new ArrowType(resR.t, alpha);
        Substitution sUnify = expectedFuncType.unify(resL.t);

        s = sUnify.compose(s);

        return TypeResult.of(s, s.apply(alpha));
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        FunValue f = (FunValue) l.eval(s);
        Value v = r.eval(s);

        if (f instanceof fst) {
            return ((PairValue) v).v1;
        }
        if (f instanceof snd) {
            return ((PairValue) v).v2;
        }
        if (f instanceof hd) {
            if (v.equals(Value.NIL)) throw new RuntimeError("hd nil list");
            return ((ConsValue) v).v1;
        }
        if (f instanceof tl) {
            if (v.equals(Value.NIL)) throw new RuntimeError("tl nil list");
            return ((ConsValue) v).v2;
        }

        Env newEnv = new Env(f.E, f.x, v);
        return f.e.eval(State.of(newEnv, s.M, s.p));
    }
}

package simpl.parser.ast;

import simpl.typing.ListType;
import simpl.typing.PairType;
import simpl.typing.RefType;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public abstract class EqExpr extends BinaryExpr {

    public EqExpr(Expr l, Expr r) {
        super(l, r);
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult lRes = l.typecheck(E);
        TypeResult rRes = r.typecheck(lRes.s.compose(E));
        
        Substitution s = rRes.s.compose(lRes.s);
        s = s.compose(lRes.t.unify(rRes.t));
        
        if (!s.apply(lRes.t).isEqualityType()) {
            throw new TypeError("Equality test on non-equality type");
        }
        
        return TypeResult.of(s, Type.BOOL);
    }
}

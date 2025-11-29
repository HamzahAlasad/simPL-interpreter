package simpl.parser.ast;

import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public abstract class RelExpr extends BinaryExpr {

    public RelExpr(Expr l, Expr r) {
        super(l, r);
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult lRes = l.typecheck(E);
        TypeResult rRes = r.typecheck(lRes.s.compose(E));
        
        Substitution s = rRes.s.compose(lRes.s);
        s = s.compose(lRes.t.unify(Type.INT));
        s = s.compose(rRes.t.unify(Type.INT));
        
        return TypeResult.of(s, Type.BOOL);
    }
}

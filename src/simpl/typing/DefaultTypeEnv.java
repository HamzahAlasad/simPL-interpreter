package simpl.typing;

import simpl.parser.Symbol;

public class DefaultTypeEnv extends TypeEnv {

    private TypeEnv E;

    public DefaultTypeEnv() {
        E = TypeEnv.empty;
        
        TypeVar a = new TypeVar(true);
        TypeVar b = new TypeVar(true);
        
        E = TypeEnv.of(E, Symbol.symbol("fst"), new ArrowType(new PairType(a, b), a));
        
        E = TypeEnv.of(E, Symbol.symbol("snd"), new ArrowType(new PairType(a, b), b));
        
        E = TypeEnv.of(E, Symbol.symbol("hd"), new ArrowType(new ListType(a), a));

        E = TypeEnv.of(E, Symbol.symbol("tl"), new ArrowType(new ListType(a), new ListType(a)));
        
        E = TypeEnv.of(E, Symbol.symbol("iszero"), new ArrowType(Type.INT, Type.BOOL));
        
        E = TypeEnv.of(E, Symbol.symbol("pred"), new ArrowType(Type.INT, Type.INT));
        E = TypeEnv.of(E, Symbol.symbol("succ"), new ArrowType(Type.INT, Type.INT));
    }

    @Override
    public Type get(Symbol x) {
        return E.get(x);
    }
}

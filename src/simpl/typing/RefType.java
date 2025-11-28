package simpl.typing;

public final class RefType extends Type {

    public Type t;

    public RefType(Type t) {
        this.t = t;
    }

    @Override
    public boolean isEqualityType() {
        return false;
    }

    @Override
    public Substitution unify(Type t) throws TypeError {
        if (t instanceof TypeVar) return t.unify(this);
        if (t instanceof RefType) {
            RefType other = (RefType) t;
            return this.t.unify(other.t);
        }
        throw new TypeMismatchError();
    }

    @Override
    public boolean contains(TypeVar tv) {
        return t.contains(tv);
    }

    @Override
    public Type replace(TypeVar a, Type t) {
        Type t1 = this.t.replace(a, t);
        return new RefType(t1);
    }

    public String toString() {
        return t + " ref";
    }
}

package simpl.typing;

public final class ListType extends Type {

    public Type t;

    public ListType(Type t) {
        this.t = t;
    }

    @Override
    public boolean isEqualityType() {
        return false;
    }

    @Override
    public Substitution unify(Type t) throws TypeError {
        if (t instanceof TypeVar) return t.unify(this);
        if (t instanceof ListType) {
            ListType other = (ListType) t;
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
        return new ListType(t1);
    }

    public String toString() {
        return t + " list";
    }
}

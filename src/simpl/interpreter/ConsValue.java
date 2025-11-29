package simpl.interpreter;

public class ConsValue extends Value {

    public final Value v1, v2;

    public ConsValue(Value v1, Value v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public String toString() {
        return "list@" + length();
    }

    private int length() {
        if (v2 instanceof NilValue) return 1;
        if (v2 instanceof ConsValue) return 1 + ((ConsValue) v2).length();
        return 1;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ConsValue) {
            ConsValue c = (ConsValue) other;
            return v1.equals(c.v1) && v2.equals(c.v2);
        }
        return false;
    }
}

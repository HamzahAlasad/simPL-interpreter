package simpl.interpreter;

public class ConsValue extends Value {

    public final Value v1, v2;

    public ConsValue(Value v1, Value v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public String toString() {
        if (v2 instanceof NilValue) {
            return "list@0";
        }
        else {
            return "list@" + (Integer.parseInt(v2.toString().substring(5)) + 1);
        }
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

package fi.muni.cz.reliability.tool.utils;

import java.util.Objects;

/**
 * @author Radoslav Micko, 445611@muni.cz
 * @param <A> first type of Tuple
 * @param <B> second type of Tuple
 */
public class Tuple<A, B> {
    private final A a;
    private final B b;

    /**
     * Constructor
     * @param a first param of Tuple
     * @param b second param of Tuple
     */
    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Tuple)) return false;

        Tuple tuple = (Tuple) o;

        if (!Objects.equals(this.a, tuple.getA())) return false;
        if (!Objects.equals(this.b, tuple.getB())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public String toString() {
        return "Tuple [" + "a=" + a + ", b=" + b + ']';
    }
    
    
}

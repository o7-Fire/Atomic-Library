package Atom.Struct;

@FunctionalInterface
public interface UnstableFunction<T, R> {
  R apply(T t) throws Exception;
}

package Atom.API;

public interface API {
    default String getName() {
        return this.getClass().getSimpleName();
    }
    
    default String getDescription() {
        return "An API";
    }
    
    
}

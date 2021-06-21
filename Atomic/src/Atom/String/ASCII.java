package Atom.String;

public class ASCII {
    public static String generateX(int n){
        String r="";for(int i=0,j;i<n-~n;i++,r+="\n")for(j=0;j<n*3;r+=j>=i&j<i+n|j<n*3-i&++j>n*2-i?"*":" ");return r;
    }
}

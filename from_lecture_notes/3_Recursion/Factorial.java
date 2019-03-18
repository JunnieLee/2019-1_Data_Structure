public class Factorial {

    // 1. non-recursive version
    public int NR_Factorial(int n){
        int tmp = 1;
        for (int i=1; i <= n; i++){
            tmp = tmp*i;
        }
        return tmp;
    }

    // 2. recursive version
    public int R_Factorial(int n){
        if (n==0) return 1; // base case
        else return n * R_Factorial(n-1); // recursion
    }

    // They're both O(n)

    public static void main(String[] args){
        System.out.println("write some code if you want to use the methods above.");
    }

}

public class Fibonacci_Sequence {

    // 1. non-recursive version
    public int NR_Fib(int n){
        int t[]={0, 1, 1}; // t[1] = t[2] = 1

        for (int i = 3; i <= n; i++){
            t[i] = t[i-1] + t[i-2];
        }
        return t[n];
    }



    // 2. recursive version

    /* WARNING : NEVER RUN THIS CODE

    public int R_Fib(int n) {
        if (n<=2) return 1;
        else return R_Fib(n-1) + R_Fib(n-2);
        }

     */



    public static void main(String[] args){
        System.out.println("write some code if you want to use the methods above.");
    }

}

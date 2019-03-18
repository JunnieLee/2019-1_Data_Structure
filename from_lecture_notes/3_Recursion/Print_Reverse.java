public class Print_Reverse {


    // version_1

    public void Print_Reverse_1(String s){
        if (s=="") {} // do nothing - base case
        else {
            int len = s.length();
            // 1. print the last char of s
            System.out.println(s.charAt(len-1));
            // 2. recursion
            Print_Reverse_1(s.substring(0,len-2)); // s minus its last char
        }
    }

    // version_2

    public void Print_Reverse_2(String s){
        if (s=="") {} // do nothing - base case
        else {
            int len = s.length();
            // 1. recursion
            Print_Reverse_2(s.substring(1,len-1)); // s minus its first char
            // 2. print the first char of s
            System.out.println(s.charAt(0));
        }
    }



    public static void main(String[] args){
        System.out.println("write some code if you want to use the methods above.");
    }

}

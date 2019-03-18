public class Binary_Search {

    // 1. non-recursive version

    public int NR_Binary_Search(int A[], int n, int x){
        // A - array, n - # of elements, x - Search key
        int low = 0;
        int high = n-1;
        while (low <= high){
            int mid = (low + high)/2;
            if (A[mid] < x) low = mid+1; // focus on the right side
            else if (A[mid] > x) high = mid-1; // focus on the left side
            else return mid;
        }
        // System.out.println("[Not found] : No element that matches the key.");
        return -1; // not found
    }


    // 2. recursive version

    public int R_Binary_Search(int A[], int x, int low, int high){
        // A - array, x - Search key, low & high - array bounds
        if (low > high) return -1; // not found

        int mid = (low + high)/2;
        if (A[mid] < x) return R_Binary_Search(A, x, mid+1, high); // focus on the right side
        else if (A[mid] > x) return R_Binary_Search(A, x, low, mid-1); // focus on the left side
        else return mid;
    }


    public static void main(String[] args){
        System.out.println("write some code if you want to use the methods above.");
    }

}

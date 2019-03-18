public class Kth_Smallest {

    // want to find the Kth smallest element in an unsorted array


    // 1. elementary-school version

    public int find_smallest(int A[]) {
        if (A.length == 0) return -1;
        else {
            int small = A[0];
            int index = 0;
            for (int i = 0; i < A.length; i++) {
                if (A[i] < small) {
                    small = A[i];
                    index = i;
                }
            }
            return index;
        }
    }

    // find the k-th smallest item in A[1 ... n]
    public int Kth_Smallest_1(int A[], int k, int n) {

        // 1. find the smallest item min in A[1 ... n]
        if (k == 1) return find_smallest(A);
            // 2. find the (k-1)-th smallest item in A[1 ... n-1]
        else return Kth_Smallest_1(A, k - 1, n - 1);
    }



    // 2. Better Version

    // pseudo code - gonna specify these in later lessons (ex. Quick Sort)

    /*
    public int Kth_Smallest_2(int A[], int k, int first, int last){
        // find the k-th smallest element in A[first ... last]

        - select a pivot item p
        // (we'll learn how to select the pivot item more effectively in later lessons)

        int pivot_index = partition(A, p, first, last);
        if (k < pivot_index - first + 1)
            return Kth_Smallest_2(A, k, first, pivot_index-1); // search on the left
        else if (k == pivot_index - first + 1)
            return p;
        else return Kth_Smallest_2(A, k-(pivot_index - first +1), pivot_index+1, last); // search on the right

    }


    public int[] partition(int[] A, int p, int first, int last){

        int[] smaller={};
        int[] bigger={};
        int[] pivot ={p};

        for (int i=first; i < last+1; i++){
            if (A[i] < p) smaller[smaller.length] = A[i];
            else if (A[i] > p) bigger[bigger.length] = A[i];
            else pivot[pivot.length] = A[i];
        }
        return smaller + pivot + bigger;
    }

    */

    public static void main(String[] args){
        System.out.println("write some code if you want to use the methods above.");
    }

}

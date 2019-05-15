import java.io.*;
import java.util.*;

public class SortingTest
{
    public static void main(String args[])
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try
        {
            boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
            int[] value;	// 입력 받을 숫자들의 배열
            String nums = br.readLine();	// 첫 줄을 입력 받음
            if (nums.charAt(0) == 'r')
            {
                // 난수일 경우
                isRandom = true;	// 난수임을 표시

                String[] nums_arg = nums.split(" ");

                int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
                int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
                int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

                Random rand = new Random();	// 난수 인스턴스를 생성한다.

                value = new int[numsize];	// 배열을 생성한다.
                for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
                    value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
            }
            else
            {
                // 난수가 아닐 경우
                int numsize = Integer.parseInt(nums);

                value = new int[numsize];	// 배열을 생성한다.
                for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
                    value[i] = Integer.parseInt(br.readLine());
            }

            // 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
            while (true)
            {
                int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

                String command = br.readLine();

                long t = System.currentTimeMillis();
                switch (command.charAt(0))
                {
                    case 'B':	// Bubble Sort
                        newvalue = DoBubbleSort(newvalue);
                        break;
                    case 'I':	// Insertion Sort
                        newvalue = DoInsertionSort(newvalue);
                        break;
                    case 'H':	// Heap Sort
                        newvalue = DoHeapSort(newvalue);
                        break;
                    case 'M':	// Merge Sort
                        newvalue = DoMergeSort(newvalue);
                        break;
                    case 'Q':	// Quick Sort
                        newvalue = DoQuickSort(newvalue);
                        break;
                    case 'R':	// Radix Sort
                        newvalue = DoRadixSort(newvalue);
                        break;
                    case 'X':
                        return;	// 프로그램을 종료한다.
                    default:
                        throw new IOException("잘못된 정렬 방법을 입력했습니다.");
                }
                if (isRandom)
                {
                    // 난수일 경우 수행시간을 출력한다.
                    System.out.println((System.currentTimeMillis() - t) + " ms");
                }
                else
                {
                    // 난수가 아닐 경우 정렬된 결과값을 출력한다.
                    for (int i = 0; i < newvalue.length; i++)
                    {
                        System.out.println(newvalue[i]);
                    }
                }

            }
        }
        catch (IOException e)
        {
            System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
        }
    }

    // 결과로 정렬된 배열은 리턴해 주어야 하며, 두가지 방법이 있으므로 잘 생각해서 사용할것.
    // 주어진 value 배열에서 안의 값만을 바꾸고 value를 다시 리턴하거나
    // 같은 크기의 새로운 배열을 만들어 그 배열을 리턴할 수도 있다.
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoBubbleSort(int[] value)
    {
        int bigger;
        for(int i = 0; i < value.length-1; i++){ // unsorted된 array의 크기가 하나씩 줄어든다
            for(int j=0;j < value.length-1-i;j++){
                if(value[j] > value[j+1]){ // stable // 오름차순이 되도록 정렬
                    bigger = value[j];
                    value[j] = value[j+1];
                    value[j+1] = bigger;
                }
            }
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoInsertionSort(int[] value)
    {
        for(int i=1; i<value.length; i++) {
            int value_to_insert = value[i];
            int sorted_idx = i-1; // sorted에서 아직 훑지 않은 가장 큰 요소의 index

            while((sorted_idx>=0) && (value[sorted_idx]>value_to_insert)) {
                value[sorted_idx+1]=value[sorted_idx]; // 자기 바로 뒤 원소에 자기값을 복사 (shifting 작업)
                sorted_idx--;
            }
            value[sorted_idx+1]=value_to_insert;
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoHeapSort(int[] value)
    {
        // (1) Heapify the array
        for (int i = (value.length / 2); i >= 0; i--) { // leaf의 부모노드에서부터 시작
            percolate_down(value, i, value.length);
        }
        // (2) Sort
        for (int i = value.length-1; i >= 0; i--) { // 남아있는것들 중 가장 큰 요소를 가장 뒤쪽으로 보낸다
            int temp = value[i];
            value[i] = value[0];
            value[0] = temp; // swap // value[0] <-> value[i]
            // in case the heap property is broken
            percolate_down(value, 0, i-1); // fix - starting from the root node
            // 한번 sort될 때마다 한 요소씩 관심의 대상에서 제외해야 하므로 size도 하나씩 작아지게 pass
        }
        return (value);
    }

    //----helper function (1) --------------------------------------------------------------------------------------------

    private static void percolate_down(int[] arr, int node_idx, int size){ // percolate down until heap property is satisfied
        int leftChild = node_idx * 2;
        int rightChild = (node_idx * 2)+1;
        int biggerChild;

        //////////////// end_condition (1) - 자식 노드가 없을 경우 ////////////////
        if ((leftChild > size) || (rightChild > size)) return;

        if (arr[leftChild] > arr[rightChild]) { biggerChild = leftChild; }
        else { biggerChild = rightChild; } // 자식노드간 대소비교를 통해 biggerChild 값 확정

        //////////////// end_condition (2) - parent node가 bigger_child 보다 클 경우 (= heap property 만족) ////////////////
        if (arr[node_idx] >= arr[biggerChild]) return;

        // end_condition (1), (2)를 모두 만족시키지 못했을 경우
        // 1- swap and fix the heap property issue for this stage
        int temp = arr[node_idx];
        arr[node_idx] = arr[biggerChild];
        arr[biggerChild] = temp; // swap // parent node <-> bigger child
        // 2- percolate down for the next stage
        percolate_down(arr, biggerChild, size);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoMergeSort(int[] value)
    {
        int[] copiedArray = new int[value.length];
        System.arraycopy(value, 0, copiedArray, 0, value.length);
        merge_sort(copiedArray, value,0, value.length-1);
        return (value);
    }

    //----helper function (1) --------------------------------------------------------------------------------------------
    private static void merge_sort(int[] copied, int[] original, int left, int right) {
        if (copied.length == 0 || left >= right) return; // base case

        int mid = (left + right) / 2; // Divide
        merge_sort(copied, original, left, mid); // Conquer_1
        merge_sort(copied, original, mid + 1, right); // Conquer_2
        merge(copied, original, left, mid, right); // Merge two sorted arrays (keep them sorted after the merge)
    }

    //----helper function (2) --------------------------------------------------------------------------------------------
    // copied[left...mid]와 copied[mid+1...right]를 merge --> 실제 정렬이 이루어지는 부분 // merge된 값은 original에 복사됨
    private static void merge(int[] copied, int[] original, int left, int mid, int right){
        int l = left; // 현재까지 정렬된 왼쪽 리스트 인덱스
        int r = mid+1; // 현재까지 정렬된 오른쪽 리스트 인덱스
        int original_idx = left; // 정렬될 original 리스트 인덱스

        while( (l <= mid) && (r <= right) ){ // 오른쪽 list- 왼쪽 list 요소간 대소비교 --> 작은것부터 순서대로 original에 위치시킴
            if (copied[l] <= copied[r]) { original[original_idx] = copied[l]; l++; }
            else { original[original_idx] = copied[r]; r++; }
            original_idx++;
        }

        // 남아있는 값들 복사하기
        while (l <= mid) { original[original_idx++] = copied[l++]; }
        while (r <= right) { original[original_idx++] = copied[r++]; }
        // (생략가능)--> 왼쪽이 끝나고 오른쪽만 남았을 경우, 어짜피 값이 동일하므로 복사할 필요 X

        // sorting이 완료된 original의 리스트를 copied로 재복사
        for(int i = left; i <= right; i++) copied[i] = original[i];
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoQuickSort(int[] value)
    {
        quickSort(value, 0, value.length-1);
        return (value);
    }

    //----helper function (1) --------------------------------------------------------------------------------------------
    private static void quickSort(int[] arr, int left, int right) {
        if (left >= right) return; // base case

        int pivot_idx = partition(arr, left, right);
        quickSort(arr, left, pivot_idx-1);
        quickSort(arr, pivot_idx+1, right);
    }

    //----helper function (2) --------------------------------------------------------------------------------------------
    private static int partition(int[] arr, int left, int right){ // arr을 right을 pivot으로 재배치 후, right의 index 반환
        int pivot = arr[right]; // 가장 오른쪽 element를 pivot값으로 설정
        int smaller_End = left-1; // pivot보다 작거나 같은 부분의 마지막 index
        int undefined_Start = left; // 아직 비교가 이루어지지 않은 부분의 첫 index

        for (; undefined_Start < right; undefined_Start++){ // 아직 비교하지 않은 부분의 크기가 하나씩 줄어든다
            if (arr[undefined_Start] <= pivot){ // pivot보다 작다면 왼쪽 smaller 부분으로 이동시켜준다
                smaller_End++; // smaller 부분의 끝에 arr[undefined_Start]를 삽입할 예정
                int temp = arr[undefined_Start];
                arr[undefined_Start] = arr[smaller_End];
                arr[smaller_End] = temp; // swap arr[undefined_Start] <-> arr[smaller_End]
            }
        } // 이 for loop을 나오면 (smaller)(bigger)(pivot)인 상태

        // swap the pivot value with the bigger_Start(next element of smaller_End)
        int temp = arr[right];
        arr[right] = arr[smaller_End+1];
        arr[smaller_End+1] = temp;

        return (smaller_End+1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoRadixSort(int[] value)
    {
        // 1. 가장 큰 자리수가 얼마인지 찾기
        int max_absolute = value[0];
        for(int i=0; i < value.length; i++)
        {
            int compare_val = value[i];
            if (value[i] < 0) compare_val *= -1;
            if (compare_val > max_absolute) max_absolute = compare_val;
        }
        // int max_digit_size = String.valueOf(max_absolute).length();

        /////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////

        // 2. radix sort 하기 (각 자리별 loop수행)
        RadixSort(value, max_absolute);

        return (value);
    }


    private static void RadixSort(int[] arr, int max_absolute) {

        for(int place=1; max_absolute/place>0; place*=10) { // LSB to MSB
            int[] output = new int[arr.length];
            int[] count = new int[19];
            // 자바가 알아서 int배열 원소들을 0으로 initialize해줌

            // count 값 추출 후 저장
            for(int i=0; i < arr.length; i++) {
                count[ (arr[i]/place)%10 + 9 ] += 1; // -9~9
                // (해당 자릿수의 값이 -9인 애는 count[0]에, 9인 애는 count[18]에서 세어진다)
            }

            // 앞에서 누적된 count까지 포함시킨 값으로 최종 저장
            for(int i=0; i<18; i++) {
                count[i+1] += count[i];
            }

            // sorted 된 형태로 output배열 빌드
            int count_idx;
            for(int i = arr.length-1; i>=0; i--) {
                count_idx = (arr[i]/place) % 10 + 9;
                output[ --count[count_idx] ] = arr[i];
            }
            // output[--count[ (value[i] / d) % 10 + 9 ]] = arr[i];

            // output 배열의 값을 원본 arr에 복사
            for(int i=0; i<arr.length; i++) {
                arr[i] = output[i];
            }
        }
    }

}

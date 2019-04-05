import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("([-|+]?)([0-9]{1,101})([-|+|*])([-|+]?)([0-9]{1,101})");

    private boolean plus = true;
    private int[] nums;
    private int digits; // 총 자리수 (부호값 제외)

    public BigInteger(int[] result)
    {
        result = GetRidOfFrontZeros(result);
        this.digits = result.length;
        this.nums = result;
    }

    public BigInteger(String s)
    {
        if (s.charAt(0)=='-'){
            this.plus = false;
            s = s.substring(1);
        }
        else if (s.charAt(0)=='+'){
            s = s.substring(1);
        }
        this.digits = s.length(); // (3) 자릿수 설정
        this.nums = StringToArray(s);
    }

    public BigInteger add(BigInteger num)
    {
        if (!num.plus){ // (양수)+(음수)
            num.plus=true;
            return this.subtract(num);
        } else if (!this.plus){ // (음수) + (양수)
            this.plus=true;
            return num.subtract(this);
        } // (음수)+(음수)인 경우는 맨 마지막에 부호처리

        // (양수) + (양수)
        int[] result_array = AddByArray(this.nums, num.nums);
        BigInteger result = new BigInteger(result_array);
        if (!this.plus&&!num.plus) result.plus = false; // (음수) + (음수)인 경우 부호 처리
        return result;
    }

    public BigInteger subtract(BigInteger num)
    {
        if (!this.plus && !num.plus){ // (음수)-(음수)
            this.plus = true;
            num.plus = true;
            return num.subtract(this);
        }
        else if (!this.plus && num.plus){ // (음수)-(양수)
            this.plus = true;
            BigInteger result = this.add(num);
            result.plus = false;
            return result;
        }
        else if (this.plus && !num.plus){ // (양수)-(음수)
            num.plus = true;
            return this.add(num);
        }
        // 여기 아래부터는 오로지 (양수)-(양수)인 경우에 한해서만 계산

        // 일단 둘이 절대값 대소비교
        int[] bigger; // 절대값이 큰놈
        int j=0;
        if (this.digits != num.digits){
            bigger = (this.digits > num.digits)? this.nums : num.nums;
        } else {
            while (this.nums[j]==num.nums[j]){
                j++;
                if (j == this.digits) break;
            }
            if (j == this.digits) return new BigInteger(new int[]{0});
            bigger = (this.nums[j]>num.nums[j])?this.nums:num.nums;
        }
        int[] smaller = (bigger==this.nums)? num.nums:this.nums; // 절대값이 작은놈

        int[] result_array = new int[101];
        boolean carry = false;
        for(int i=1; i <= bigger.length; i++){
            int subtracted_digits=0;
            if (carry) bigger[bigger.length-i]--;

            int big = bigger[bigger.length-i];
            int small = (smaller.length-i >= 0 ? smaller[smaller.length-i] : 0);

            if (big < small){
                carry = true;
                subtracted_digits = 10 + big - small;
            } else {
                carry = false;
                subtracted_digits = big - small;
            }
            result_array[101-i] = subtracted_digits;
        }

        BigInteger result = new BigInteger(result_array);
        if (bigger!=this.nums) result.plus = false; // (양수1)-(양수2) 에서, 양수1이 더 작았다면 결과값은 음수
        return result;
    }

    public BigInteger multiply(BigInteger num)
    {
        int[] bigger = (this.digits > num.digits)? this.nums : num.nums;
        int[] smaller = (bigger==this.nums)? num.nums : this.nums;

        int cnt=0; // index of mini multiplied values (from top -> bottom)

        int[] result_array= new int[200];

        for (int j=1; j <= smaller.length; j++){
            int[] mini_result; //= new int[bigger.length+cnt];
            int mini_digit = smaller[smaller.length-j];

            mini_result = MiniMultiply(bigger, mini_digit, cnt);
            result_array = AddByArray(result_array, mini_result);
            cnt++;
        }
        BigInteger result = new BigInteger(result_array);
        if (((this.plus)&&!(num.plus))|(!(this.plus)&&(num.plus))) result.plus = false;
        return result;
    }

    @Override
    public String toString()
    {
        String str = "";
        if (!this.plus && !(this.nums[0]==0)){ str = "-";}
        for (int i=0; i < this.digits; i++) str += this.nums[i];
        return str;
    }

    // 위 코드들에서 활용한 보조함수들

    public int[] StringToArray(String s){
        int[] result = new int[s.length()];
        for(int i=0; i < s.length(); i++){
            result[i] = s.charAt(i)-'0';
        }
        return result;
    }

    public int[] GetRidOfFrontZeros(int[] arr){ // input array에 대하여 앞쪽에 위치한 0들을 제거
        int idx = 0;
        for (int k=0; k < arr.length; k++){ if (arr[k]==0) idx++; else break; }

        String str = "";
        if (idx==arr.length){ str = "0"; } // (special case) 오로지 0으로만 이루어져있는 array라면 [0]으로 만들어줌
        else{ for (int i=idx; i < arr.length; i++) str += arr[i]; }

        return StringToArray(str);
    }

    // array * digit
    public int[] MiniMultiply(int[] arr, int num, int decimal){
        int[] result_array = new int[arr.length+1+decimal];
        int multiplied=0;
        int carry=0;

        for (int i=1; i <=arr.length; i++){
            multiplied += (arr[arr.length-i]*num + carry);
            if (multiplied>=10) { carry = multiplied/10; multiplied %=10;}
            else {carry=0;}
            result_array[result_array.length-i-decimal] = multiplied;
            multiplied=0;
        }
        result_array[0] = carry;
        while (decimal>0){
            result_array[result_array.length-decimal]=0;
            decimal--;
        }
        return result_array;
    }

    public int[] AddByArray(int[] arr1, int[] arr2){ // Array 단위로 각 자릿수 더하기
        int[] result = new int[200];
        int[] bigger = (arr1.length > arr2.length)? arr1 : arr2;
        int[] smaller = (bigger==arr1)? arr2 : arr1;

        boolean carry = false; // 자리올림
        int added_digits=0;
        int i = 1;
        for(; i <= bigger.length; i++){
            added_digits = (smaller.length-i >= 0 ? smaller[smaller.length-i] : 0)
                    + bigger[bigger.length-i];
            if (carry) added_digits++;
            if (added_digits >= 10){ added_digits-=10; carry = true;}
            else {carry=false;}
            result[200-i] = added_digits;
        }
        if (carry) {result[200-i] +=1;}
        return result;
    }

    // 보조함수들 정의 끝

    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
        Matcher m = EXPRESSION_PATTERN.matcher(input);
        if (m.find()) {
            // parsing하기
            String arg1 = m.group(1) + m.group(2);
            String operator = m.group(3);
            String arg2 = m.group(4) + m.group(5);

            BigInteger num1 = new BigInteger(arg1);
            BigInteger num2 = new BigInteger(arg2);

            if (operator.equals("-")) {
                return num1.subtract(num2);
            } else if (operator.equals("+")) {
                return num1.add(num2);
            } else if (operator.equals("*")) {
                return num1.multiply(num2);
            }
        }
        throw new IllegalArgumentException();
    }

    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                { String input = reader.readLine();
                    try
                    {
                        input = input.replaceAll("\\s", ""); // 일단 공백 없애주기
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }

    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
        if (quit) { return true;}
        else {
            if (EXPRESSION_PATTERN.matcher(input).find()){
                BigInteger result = evaluate(input);
                System.out.println(result.toString());
            } else{ // 정규표현식 조건과 불일치하면 throw exception
                throw new IllegalArgumentException();
            }
            return false;
        }
    }

    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
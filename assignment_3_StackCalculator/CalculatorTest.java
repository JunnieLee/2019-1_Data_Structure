import java.io.*;
import java.lang.Math;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class CalculatorTest
{
    public static void main(String args[])
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true)
        {
            try {
                String input = br.readLine();
                if (input.compareTo("q") == 0) break;

                // filter_#1 : (number)+(spaces)+(number) format is not allowed
                Pattern p = Pattern.compile("(\\d+)(\\s+)(\\d+)");
                Matcher m = p.matcher(input);
                if (m.find()) throw new Exception();

                input = input.replaceAll("\\s",""); // filter_#2 : get rid of all the spaces
                command(input); // pass in the naively filtered input to command
            }
            catch (Exception e) { System.out.println("ERROR"); }
        }
    }

    private static void command(String input) throws Exception
    {
        if (!parenthesis_well_paired(input)) throw new Exception();

        String[] infix_tokens = tokenize(input); // this line would throw an error if input contains improper format
        String postfix_expression = covert_to_postfix(infix_tokens); // this line would throw an error if input was not a proper infix format
        String calculated_result = calcualte(postfix_expression); // this line would throw an error if it can't calculate the expression

        System.out.println(postfix_expression);
        System.out.println(calculated_result);
    }

    // [validating input _ no.1]
    private static boolean parenthesis_well_paired(String input){
        if (!( input.contains("(") || input.contains(")"))) return true; // if it doesn't contain "(" or ")" at all, return true
        int cnt = 0;
        for (int i=0;i<input.length();i++) {
            if (input.charAt(i)=='(') cnt++;
            else if (input.charAt(i)==')') cnt--;
        }
        return (cnt==0);
    }

    // [validating input _ no.2] -> ONLY 0~9, '+', '-', '*', '/', '%', '^', '(', ')'
    private static String[] tokenize(String input) throws Exception {
        // () or (+) kind of formats should be filtered
        Pattern improper = Pattern.compile("(\\()(\\D*)(\\))");
        Matcher improper_m = improper.matcher(input);
        if (improper_m.find()) throw new Exception();

        final Pattern PROPER_LETTERS = Pattern.compile("(\\d)+|\\+|\\(|\\)|-|\\*|\\^|%|/");
        String[] tokenized_input = input.split("(?<=[-+*/%^()])|(?=[-+*/%^()])"); // split and put them in a String array
        for (String element : tokenized_input)
        {
            if (!PROPER_LETTERS.matcher(element).matches()) throw new Exception();
        } // if there are any improper letters within the tokens , raise error
        return tokenized_input; // if all tokens are validated, return the well tokenized String array
    }

    private static boolean isOperator(String operator) {
        return (operator.equals("+") ||
                operator.equals("-") ||
                operator.equals("*") ||
                operator.equals("/") ||
                operator.equals("%") ||
                operator.equals("^") ||
                operator.equals("~") );
    }

    private static int operatorPriority(String operator) throws Exception{ // it has higher priority if it has bigger number
        switch (operator) {
            case "^": return 4;
            case "~": return 3;
            case "*": case "/": case "%": return 2;
            case "+": case "-": return 1;
        }
        throw new Exception();
    }

    // [validating input _ no.3] does the input has a proper infix format?
    // --> if not, this function will throw an error
    // if the format is valid, this will return the converted postfix expression String.
    private static String covert_to_postfix(String[] infix_tokens) throws Exception {
        int digit_num = 0;
        int operator_num = 0;

        StringBuilder converted_result =  new StringBuilder("");
        Stack<String> operator_stack = new Stack<>();
        int order = 1; // when order==1, it's operand's turn, and when order==-1, it's operator's turn

        for (int i = 0; i<infix_tokens.length; i++){
            // case 1. if it's a number
            if (infix_tokens[i].matches("(\\d+)")){
                if (order!=1) throw new Exception();
                converted_result.append(infix_tokens[i] + " ");
                digit_num++;
                order*=-1; // change the turn for next loop
            }
            // case 2. if it's a "("
            else if (infix_tokens[i].equals("(")) {
                if (order!=1) throw new Exception();
                operator_stack.push(infix_tokens[i]);
            }
            // case 3. if it's a ")"
            else if (infix_tokens[i].equals(")")){
                if (order==1) throw new Exception();
                while (!operator_stack.peek().equals("(")){
                    converted_result.append(operator_stack.pop() + " ");
                }
                operator_stack.pop(); // it terminates the process with popping the most recent "("
            }
            // case 4. if it's an operator
            else if (isOperator(infix_tokens[i])){
                // 4-(1) in case of unary operator "-"
                if ((infix_tokens[i].equals("-")) && (order==1)){
                    operator_stack.push("~");
                }
                else{ // 4-(2) in case of all the other binary operators
                    if (order==1) { throw new Exception(); }
                    // identify the relationship between the operator stack and the current token, and then decide what to do
                    if (operator_stack.isEmpty()){ // you have to place this first in order to avoid further errors
                        operator_stack.push(infix_tokens[i]);
                    }
                    else if (operator_stack.peek().equals("(")){ // ( is logically similar to starting with a new-empty stack
                        operator_stack.push(infix_tokens[i]);
                    }
                    else if (infix_tokens[i].equals("^") && (operator_stack.peek().equals("^"))){
                        operator_stack.push(infix_tokens[i]); // operator "^" is right-associative, so we have to take care of them respectively
                    }
                    else if (( operatorPriority(operator_stack.peek()) == operatorPriority(infix_tokens[i]) )){
                        // if the binary operator is not "^", and it has same priority as the stack top,
                        converted_result.append(operator_stack.pop() + " "); // then, the previous stack top is appended to the result string
                        operator_stack.push(infix_tokens[i]); // and the token becomes the new stack top element.
                    }
                    else if (( operatorPriority(operator_stack.peek()) < operatorPriority(infix_tokens[i]) )){
                        // if stack top's priority is lower than the current token's priority,
                        operator_stack.push(infix_tokens[i]);  // the current get pushed into the stack and sits on the previous stack top
                    }
                    else{  // if stack top has higher priority than the current token,
                        while (!operator_stack.isEmpty() && !(operator_stack.peek().equals("("))
                                && (( operatorPriority(operator_stack.peek()) >= operatorPriority(infix_tokens[i]) ))){
                            converted_result.append(operator_stack.pop() + " ");
                        } // lower-priority operators that are stored in the stack should be popped and appended to the result string
                        operator_stack.push(infix_tokens[i]); // after all the lower priority operators are kicked out of the stack,
                        // the current token is pushed into the "semi-cleaned" stack
                    }
                    operator_num++;
                    order*=-1; // change the turn for next loop
                }
            } // end of operator's else if

            if (i==infix_tokens.length-1) // if this is the last loop
                while (!operator_stack.isEmpty())
                    {converted_result.append(operator_stack.pop() + " ");} // append all the left-over operators in the stack

        } // end of for loop for each tokens
        if (digit_num-1 != operator_num) throw new Exception(); // - invalid infix expression (2)

        return converted_result.toString().trim(); // get rid of the last space with trim and then return
    }

    // [validating input _ no.4] can we calculate the given expression? --> if not, this function will throw an error
    // if no errors rise during the operation, it will successfully return the calculated result in a string format
    private static String calcualte(String postfix) throws Exception {
        String[] splitted_postfix = postfix.split("\\s+");
        Stack<Long> result_stack = new Stack<Long>();

        for (int i = 0; i < splitted_postfix.length; i++){ // loop for each tokens from the given postfix expression

            // 1. if the token is a number
            if (splitted_postfix[i].matches("(\\d+)")) {
                Long target_number = Long.parseLong(splitted_postfix[i]);
                result_stack.push(target_number);
            }
            // 2. if the token is an operator
            else if (isOperator(splitted_postfix[i])){
                // 2-1) in case of the unary operator
                if (splitted_postfix[i].equals("~")){ result_stack.push(result_stack.pop()*(-1)); }

                else{ // 2-2) in case of binary operators
                    Long num2 = result_stack.pop(); // left operand
                    Long num1 = result_stack.pop(); // right operand
                    Long zero = new Long(0); // Long type zero for validating input

                    switch (splitted_postfix[i]){
                        case "^":
                            if ((num1.equals(zero)) && (num2 < zero)) throw new Exception(); // can't calculate this case -1.
                            result_stack.push((long)Math.pow(num1, num2));
                            break;
                        case "+":
                            result_stack.push(num1+num2);
                            break;
                        case "-":
                            result_stack.push(num1-num2);
                            break;
                        case "*":
                            result_stack.push(num1*num2);
                            break;
                        case "/":
                            if (num2.equals(zero)) throw new Exception(); // can't calculate this case -2.
                            result_stack.push(num1/num2);
                            break;
                        case "%":
                            if (num2.equals(zero)) throw new Exception(); // can't calculate this case -3.
                            result_stack.push(num1%num2);
                            break;
                    } // end of switch
                } // end of binary operator case
            } // end of over-all operator case
            else throw new Exception(); // 3. if the token is not a number, nor an operator
        }
        return result_stack.peek().toString();
    }
}
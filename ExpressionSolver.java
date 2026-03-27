import java.util.Deque;
import java.util.LinkedList;

public class EquatorSolver {
    public static double evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        Deque<Double> values = new LinkedList<>();
        Deque<Character> operators = new LinkedList<>();
        boolean lastWasOperator = true;
        boolean lastWasNegative = false;
        
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ') {
                continue;
            }
            
            if (tokens[i] >= '0' && tokens[i] <= '9' || (tokens[i] == '-' && lastWasOperator)) {
                StringBuilder sb = new StringBuilder();
                if (tokens[i] == '-') {
                    sb.append('-');
                    i++;
                }
                while (i < tokens.length && (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.')) {
                    sb.append(tokens[i++]);
                }
                values.push(Double.parseDouble(sb.toString()));
                lastWasOperator = false;
                lastWasNegative = tokens[i - 1] == '-';
                i--;
            } else if (tokens[i] == '(') {
                operators.push(tokens[i]);
                lastWasOperator = true;
                lastWasNegative = false;
            } else if (tokens[i] == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop();
                lastWasOperator = false;
            } else if (isOperator(tokens[i])) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(tokens[i])) {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(tokens[i]);
                lastWasOperator = true;
                lastWasNegative = false;
            }
        }
        while (!operators.isEmpty()) {
            values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
        }
        return values.pop();
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
        }
        return -1;
    }

    public static double applyOperation(char operator, double second, double first) {
        switch (operator) {
            case '+': return first + second;
            case '-': return first - second;
            case '*': return first * second;
            case '/': if (second == 0) throw new UnsupportedOperationException("Cannot divide by zero"); return first / second;
        }
        return 0;
    }

    public static void main(String[] args) {
        String expression = "-2 + 3 * (4 - 5)";
        System.out.println(evaluate(expression));  // Output: 1.0
    }
}
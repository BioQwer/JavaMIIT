package MathParser;

import MathParser.operations.Operation;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Antony on 27.04.2015.
 */
public class Expression {

    private static String sortingStation(String expression, Map<String, Integer> operations, String leftBracket,
                                        String rightBracket) {
        if (expression == null || expression.length() == 0)
            throw new IllegalStateException("Expression isn't specified.");
        if (operations == null || operations.isEmpty())
            throw new IllegalStateException("Operations aren't specified.");
        // �������� ������, �������� �� "�������" - �������� � ��������..
        List<String> out = new ArrayList<String>();
        // ���� ��������.
        Stack<String> stack = new Stack<String>();

        // �������� �������� �� ���������.
        expression = expression.replace(" ", "");

        // ��������� "��������", �� ���������� ���������� (�������� � ������).
        Set<String> operationSymbols = new HashSet<String>(operations.keySet());
        operationSymbols.add(leftBracket);
        operationSymbols.add(rightBracket);

        // ������, �� ������� ���������� ������ ������ �� ������� ��������.
        int index = 0;
        // ������� ������������� ������ ���������� ��������.
        boolean findNext = true;
        while (findNext) {
            int nextOperationIndex = expression.length();
            String nextOperation = "";
            // ����� ���������� ��������� ��� ������.
            for (String operation : operationSymbols) {
                int i = expression.indexOf(operation, index);
                if (i >= 0 && i < nextOperationIndex) {
                    nextOperation = operation;
                    nextOperationIndex = i;
                }
            }
            // �������� �� ������.
            if (nextOperationIndex == expression.length()) {
                findNext = false;
            } else {
                // ���� ��������� ��� ������ ������������ �������, ��������� ��� � �������� ������.
                if (index != nextOperationIndex) {
                    out.add(expression.substring(index, nextOperationIndex));
                }
                // ��������� ���������� � ������.
                // ����������� ������.
                if (nextOperation.equals(leftBracket)) {
                    stack.push(nextOperation);
                }
                // ����������� ������.
                else if (nextOperation.equals(rightBracket)) {
                    while (!stack.peek().equals(leftBracket)) {
                        out.add(stack.pop());
                        if (stack.empty()) {
                            throw new IllegalArgumentException("Unmatched brackets");
                        }
                    }
                    stack.pop();
                }
                // ��������.
                else {
                    while (!stack.empty() && !stack.peek().equals(leftBracket) &&
                            (operations.get(nextOperation) >= operations.get(stack.peek()))) {
                        out.add(stack.pop());
                    }
                    stack.push(nextOperation);
                }
                index = nextOperationIndex + nextOperation.length();
            }
        }
        // ���������� � �������� ������ ��������� ����� ���������� ��������.
        if (index != expression.length()) {
            out.add(expression.substring(index));
        }
        // ������������� ��������� ������ � �������� ������.
        while (!stack.empty()) {
            out.add(stack.pop());
        }
        StringBuilder result = new StringBuilder();
        if (!out.isEmpty())
            result.append(out.remove(0));
        while (!out.isEmpty())
            result.append(" ").append(out.remove(0));

        return result.toString();
    }

    public static String sortingStation(String expression, Map<String, Integer> operations) {
        return sortingStation(expression, operations, "(", ")");
    }

    public static BigDecimal calculateExpression(String expression) {
        String rpn = sortingStation(expression, Operation.getOperations());
        StringTokenizer tokenizer = new StringTokenizer(rpn, " ");
        Stack<BigDecimal> stack = new Stack<BigDecimal>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            // �������.
            if (!Operation.getOperations().keySet().contains(token)) {
                stack.push(new BigDecimal(token));
            } else {
                BigDecimal operand2 = stack.pop();
                BigDecimal operand1 = stack.empty() ? BigDecimal.ZERO : stack.pop();
                stack.push(Operation.doCalc(token, operand1, operand2));
            }
        }
        if (stack.size() != 1)
            throw new IllegalArgumentException("Expression syntax error.");
        return stack.pop();
    }

    public static void aboutExp(String expression)
    {
        System.out.println("��������� �������:         " + expression);
        String rpn = sortingStation(expression, Operation.getOperations());
        System.out.println("�������� �������� �������: " + rpn);
        System.out.println("\t��������� " + calculateExpression(expression));
    }

    private Expression() {
    }

    public static void main(String[] args) {

    }

}

package com.company;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * класс решения выражения
 */
public class Expression {
    /**
     *стек их инфиксной в постфиксную
     */
    private Stack stackInfToPost = new Stack();
    /**
     *постфиксная запись
     */
    private Queue queuePost = new LinkedList();
    /**
     *очередь для чисел с точкой
     */
    private Queue queueDouble = new LinkedList();
    /**
     *стек для проверки на ошибки
     */
    private Stack stackError = new Stack();
    /**
     *временная переменная
     */
    private double tempVar;
    /**
     *наличие ошибки
     */
    private boolean check = true;
    /**
     *массив символов из строки для проверки на ошибки
     */
    char [] ToCharArr1;
    /**
     *массив символов из строки для вычисления выражения
     */
    char [] ToCharArr2;

    /**
     * конструктор, предаем 2 одинаковые строки
     * @param expression1 строка 1
     * @param expression2 строка 2
     */
    public Expression(String expression1, String expression2)
    {
        ToCharArr1 = expression1.toCharArray();
        ToCharArr2 = expression2.toCharArray();
    }

    /**
     *Получение постфиксной записи
     * @return результат работы с постфиксной записью
     */
    public Queue GetPostOfQueue() {
        return queuePost;
    }

    /**
     * проверка ошибок
     * @return наличие ошибки(есть/нет) при любом моменте ошибки вылетит check = false
     */
    public boolean ErrorCheck()
    {
        for(char element: ToCharArr1) {
            if((element>='0'&&element<='9') || element=='.') {
                stackError.add(element);
            }else
            if(element=='-'|| element=='+'|| element=='*'||element=='/') {
                if (stackError.isEmpty()) {
                    check = false;
                    return check;
                }
                char TopOfStack=(char) stackError.peek();
                if(TopOfStack=='-'||TopOfStack=='+'||TopOfStack=='*'||TopOfStack=='/'||TopOfStack=='('||TopOfStack=='.'){
                    check = false;
                    return check;
                }else stackError.add(element);
            }else
            if(element=='('||element==')') {
                char topOfStack =(char) stackError.peek();
                if(topOfStack == '.'){
                    check = false;
                    return check;
                }
                stackError.add(element);
            }
            else
            {
                check = false;
                return check;
            }
        }
        if (stackError.isEmpty()) {
            check = false;
            return check;
        }
        char EndOfStack=(char) stackError.peek();
        if(EndOfStack=='-'||EndOfStack=='+'||EndOfStack=='*'||EndOfStack=='/') {
        check = false;
        return check;
    }

        Stack StaplesStack = new Stack();
        while(!stackError.isEmpty()) {
            char endOfStack = (char) stackError.pop();
            if(endOfStack=='(') {
                if (StaplesStack.isEmpty()) {
                    check = false;
                    return check;
                } else StaplesStack.pop();
            }
            else if(endOfStack==')')
                StaplesStack.push(endOfStack);
        }
        if(!StaplesStack.isEmpty())
        {
            check = false;
            return check;
        }
        return check;
    }

    /**
     * Перевод из инфиксной в постфиксную
     * @return проверка ошибки - деление на 0
     */
    public boolean InfToPost()
    {
        if(!ErrorCheck())
            return false;

        for(char elem: ToCharArr2)
        {
            if((elem>='0'&&elem<='9')||elem=='.' )
                queueDouble.add(elem);
            else {
                if (!queueDouble.isEmpty()) {
                    tempVar = toDouble();
                    if (tempVar == 0d && !stackInfToPost.isEmpty() && !queuePost.isEmpty())
                        if ((char) stackInfToPost.peek() == '/') {
                            check = false;
                            return check;
                        }
                    queuePost.add(tempVar);
                }
                if (elem == '(') {
                    stackInfToPost.push(elem);
                }
                if (elem == ')') {
                    pop();
                }
                if(elem=='+'||elem=='-') {
                    if (stackInfToPost.empty() || (char) stackInfToPost.peek() == '(') {
                        stackInfToPost.push(elem);
                    }
                    else
                    {
                        popForPlusMinus();
                        stackInfToPost.push(elem);
                    }
                }
                if(elem=='*'||elem=='/') {
                    if (stackInfToPost.empty() || (char) stackInfToPost.peek() == '(')
                        stackInfToPost.push(elem);
                    else {
                        popForMulDiv();
                        stackInfToPost.push(elem);
                    }
                }
            }
        }
        if (!queueDouble.isEmpty()) {
            tempVar = toDouble();
            if(tempVar ==0d&&!stackInfToPost.isEmpty()&&!queuePost.isEmpty())
                if((char) stackInfToPost.peek()=='/'){
                    check = false;
                    return check;
                }
            queuePost.add(tempVar);
        }
        while (!stackInfToPost.empty())
        {
            queuePost.add((char) stackInfToPost.pop());
        }
        return check;
    }

    /**
     * решение выражения
     */
    double solution =0;

    /**
     * вычисление постфиксного выражения
     * @return результат вычисления
     */
    public boolean PostOfResult()
    {
        Stack result = new Stack();

        while(!queuePost.isEmpty())
        {
            double number1, number2;
            String choice = String.valueOf(queuePost.peek());
            switch (choice) {
                case "+":
                    number1 = (double) result.pop();
                    number2 = (double) result.pop();
                    result.push(number1+number2);
                    break;
                case "-":
                    number1 = (double) result.pop();
                    number2 = (double) result.pop();
                    result.push(number2-number1);
                    break;
                case "*":
                    number1 = (double) result.pop();
                    number2 = (double) result.pop();
                    result.push(number1*number2);
                    break;
                case "/":
                    number1 = (double) result.pop();
                    number2 = (double) result.pop();
                    if (number1==0)
                        return false;
                    result.push(number2/number1);
                    break;
                default:
                    double TopQueuePost = (double) queuePost.peek();
                    result.push(TopQueuePost);
                    break;
            }
            queuePost.poll();
        }
        solution = (double)result.peek();
        return true;
    }

    /**
     * получение решения
     * @return решение
     */
    public double getSolution(){
        return solution;
}
    /**
     *метод возвращает элемент, находящийся в верхней части стека, а затем удаляет его
     */
    private void pop()
    {
        while (!stackInfToPost.empty())
        {
            char TopOfStackInfToPost = (char) stackInfToPost.peek();
            if(TopOfStackInfToPost == '(') {
                stackInfToPost.pop();
                break;
            }
            else
            {
                queuePost.add(TopOfStackInfToPost);
                stackInfToPost.pop();
            }
        }

    }
    /**
     *Для вытаскивания +-
     */
    private void popForPlusMinus()
    {
        while (!stackInfToPost.empty())
        {
            char TempVar = (char) stackInfToPost.peek();
            if(TempVar == '(') {
                break;
            }
            else
            {
                queuePost.add(TempVar);
                stackInfToPost.pop();
            }
        }

    }

    /**
     *для вытаскивания * /
     */
    private void popForMulDiv()
    {
        while (!stackInfToPost.empty())
        {
            char TempVar = (char) stackInfToPost.peek();
            if(TempVar == '('||TempVar == '+'||TempVar == '-') {
                break;
            }
            else
            {
                queuePost.add(TempVar);
                stackInfToPost.pop();
            }
        }
    }

    /**
     *переводит в double
     * @return результат
     */
    private double toDouble()
    {
        Queue BeforeDot = new LinkedList();
        Queue AfterDot = new LinkedList();

        double sum = 0;
        boolean flag = true;
        while (!queueDouble.isEmpty())
        {
            if((char) queueDouble.peek()=='.') {
                flag = false;
                queueDouble.poll();
            }else {
                if (flag)
                    BeforeDot.add((char) queueDouble.poll());
                else
                    AfterDot.add((char) queueDouble.poll());
            }
        }
        while(!BeforeDot.isEmpty())
        {
            int result1=1;
            for(int i=0; i < BeforeDot.size()-1;i++)
            {
                result1*=10;
            }
            int tempVar = (char) BeforeDot.poll() - 48;
            result1*=tempVar;
            sum+=result1;
        }

        int k=1;
        while(!AfterDot.isEmpty())
        {
            double result2=1;
            for(int i=0; i < k; i++)
            {
                result2/=10;
            }
            int temp = (char) AfterDot.poll() - 48;
            result2*=temp;
            sum+=result2;
            k++;
        }
        return sum;
    }

}
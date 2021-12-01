package com.company;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * класс решения выражения
 */
public class Expression {

    private Stack st = new Stack();
    private Queue qe = new LinkedList();
    private Queue qeNum = new LinkedList();
    private Stack Compliance_check = new Stack();
    private double temp;
    private boolean check = true;
    char [] charArr1;
    char [] charArr2;

    /**
     * делаем массив из строк
     * @param str1 строка1
     * @param str2 строка2
     */
    public Expression(String str1, String str2)
    {
        charArr1 = str1.toCharArray();
        charArr2 = str2.toCharArray();
    }

    /**
     *Получение постфиксной записи
     * @return результат работы стека с постфиксной записью
     */
    public Queue getQE() {
        return qe;
    }

    /**
     * проверка ошибок
     * @return наличие ошибки(есть/нет)
     */
    public boolean Comp_check()
    {
        for(char elem: charArr1) {
            if((elem>='0'&&elem<='9')||elem=='.' ) {
                if (Compliance_check.isEmpty())
                    Compliance_check.add('$');
                else {
                    char p = (char) Compliance_check.peek();
                    if (p != '$') {
                        Compliance_check.add('$');
                    }
                }

            }else
            if(elem=='-'||elem=='+'||elem=='*'||elem=='/') {
                if (Compliance_check.isEmpty()) {
                    check = false;
                    return check;
                }
                char h=(char)Compliance_check.peek();
                if(h=='-'||h=='+'||h=='*'||h=='/'||h=='('||h==')'){
                    check = false;
                    return check;
                }else Compliance_check.add(elem);
            }else
            if(elem=='('||elem==')')
                Compliance_check.add(elem);
            else
            {
                check = false;
                return check;
            }
        }

        if (Compliance_check.isEmpty()) {
            check = false;
            return check;
        }

        char h=(char)Compliance_check.peek();
        if(h=='-'||h=='+'||h=='*'||h=='/') {
            check = false;
            return check;
        }
        Stack staples = new Stack();
        while(!Compliance_check.isEmpty()) {
            char tm = (char) Compliance_check.pop();
            if(tm=='(') {
                if (staples.isEmpty()) {
                    check = false;
                    return check;
                } else staples.pop();
            }
            else if(tm==')')
                staples.push(tm);
        }
        if(!staples.isEmpty())
        {
            check = false;
            return check;
        }
        return check;
    }

    /**
     * Перевод из инфиксной в постфиксную
     * @return постфиксную форму записи
     */
    public boolean InfToPost()
    {
        if(!Comp_check())
            return false;

        for(char elem: charArr2)
        {
            if((elem>='0'&&elem<='9')||elem=='.' )
                qeNum.add(elem);
            else {
                if (!qeNum.isEmpty()) {
                    temp = toDouble();
                    if(temp==0d&&!st.isEmpty()&&!qe.isEmpty())
                        if((char)st.peek()=='/'){
                            check = false;
                            return check;
                        }
                    qe.add(temp);
                }

                if (elem == '(') {
                    st.push(elem);
                }
                if (elem == ')') {
                    pop();
                }
                if(elem=='+'||elem=='-') {
                    if (st.empty() || (char) st.peek() == '(') {
                        st.push(elem);
                    }
                    else
                    {
                        popPriority1();
                        st.push(elem);
                    }
                }
                if(elem=='*'||elem=='/') {
                    if (st.empty() || (char) st.peek() == '(')
                        st.push(elem);
                    else {
                        popPriority2();
                        st.push(elem);
                    }
                }
            }
        }
        if (!qeNum.isEmpty()) {
            temp = toDouble();
            if(temp==0d&&!st.isEmpty()&&!qe.isEmpty())
                if((char)st.peek()=='/'){
                    check = false;
                    return check;
                }
            qe.add(temp);
        }
        while (!st.empty())
        {
            qe.add((char)st.pop());
        }
        return check;
    }

    /**
     * Вычисление постфиксного выражения
     * @return результат вычисления
     */
    public double PostInRes()
    {
        Stack result = new Stack();

        while(!qe.isEmpty())
        {
            double a, b;
            String ch = String.valueOf(qe.peek());
            switch (ch) {
                case "+":
                    a = (double) result.pop();
                    b = (double) result.pop();
                    result.push(a+b);
                    break;
                case "-":
                    a = (double) result.pop();
                    b = (double) result.pop();
                    result.push(b-a);
                    break;
                case "*":
                    a = (double) result.pop();
                    b = (double) result.pop();
                    result.push(a*b);
                    break;
                case "/":
                    a = (double) result.pop();
                    b = (double) result.pop();
                    result.push(b/a);
                    break;
                default:
                    double res = (double)qe.peek();
                    result.push(res);
                    break;
            }
            qe.poll();
        }
        return (double)result.peek();
    }

    /**
     *метод возвращает элемент, находящийся в верхней части стека, а затем удаляет его
     */
    private void pop()
    {
        while (!st.empty())
        {
            char temp = (char)st.peek();
            if(temp == '(') {
                st.pop();
                break;
            }
            else
            {
                qe.add(temp);
                st.pop();
            }
        }

    }
    /**
     *Для вытаскивания +-
     */
    private void popPriority1()
    {
        while (!st.empty())
        {
            char temp = (char)st.peek();
            if(temp == '(') {
                break;
            }
            else
            {
                qe.add(temp);
                st.pop();
            }
        }

    }

    /**
     *для вытаскивания * /
     */
    private void popPriority2()
    {
        while (!st.empty())
        {
            char temp = (char)st.peek();
            if(temp == '('||temp == '+'||temp == '-') {
                break;
            }
            else
            {
                qe.add(temp);
                st.pop();
            }
        }
    }

    /**
     *переводит в double 5=5.0
     * @return результат
     */
    private double toDouble()
    {
        Queue qe1 = new LinkedList();
        Queue qe2 = new LinkedList();

        double sum = 0;
        boolean flag = true;
        while (!qeNum.isEmpty())
        {
            if((char)qeNum.peek()=='.') {
                flag = false;
                qeNum.poll();
            }else {
                if (flag)
                    qe1.add((char) qeNum.poll());
                else
                    qe2.add((char) qeNum.poll());
            }
        }
        while(!qe1.isEmpty())
        {
            int res=1;
            for(int i=0; i < qe1.size()-1;i++)
            {
                res*=10;
            }
            int temp = (char) qe1.poll() - 48;
            res*=temp;
            sum+=res;
        }

        int k=1;
        while(!qe2.isEmpty())
        {
            double res=1;
            for(int i=0; i < k; i++)
            {
                res/=10;
            }
            int temp = (char) qe2.poll() - 48;
            res*=temp;
            sum+=res;
            k++;
        }
        return sum;
    }

}
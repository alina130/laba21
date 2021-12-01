package com.company;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 * класс мейн
 */
public class Main {
    /**
     *метод для ввода выражения, проверок и решения
     * @param args аргумент командной строки
     */
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.println("Введите выражение для решения : ");
        String str1 = s.nextLine();
        String str2 = str1;
        Expression ToPost = new Expression(str1, str2);
        //Проверка основных ошибок в выражении:
        // Несоответствие скобок, /0 , два знака подряд или лишний знак
        if (ToPost.InfToPost()) {//если удалось преобразовать то
            Queue qe = new LinkedList();
            qe = ToPost.getQE();//результат работы стека в постфиксную запись
            System.out.println("Постфиксная запись:");
            System.out.println(qe);
            double res = ToPost.PostInRes();//результат работы
            System.out.println();
            System.out.println("Результат: " + res);
        } else
            System.out.println("Некорректная запись.");
    }
}
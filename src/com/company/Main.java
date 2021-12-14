package com.company;

import java.util.Scanner;

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
        String expression1 = s.nextLine();
        String expression2 = expression1;
        Expression ToPost = new Expression(expression1, expression2);

        if (ToPost.InfToPost()) {
            System.out.println("Постфиксная запись:");
            System.out.println(ToPost.GetPostOfQueue());
            if (ToPost.PostOfResult()){
            System.out.println();
            System.out.println("Результат: " +ToPost.getSolution());
            }else
                System.out.println("Нельзя делить на ноль.");
        } else
            System.out.println("Некорректная запись.");
    }
}

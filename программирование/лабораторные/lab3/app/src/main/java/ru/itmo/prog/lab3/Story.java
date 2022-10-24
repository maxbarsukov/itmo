package ru.itmo.prog.lab3;

public class Story {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new Story().getGreeting());
    }
}

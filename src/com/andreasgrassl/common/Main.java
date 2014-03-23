package com.andreasgrassl.common;

import static java.awt.EventQueue.invokeLater;

public class Main {
    public static void main(String[] args){
        Mastermind game = new Mastermind();
        invokeLater(game);
    }
}

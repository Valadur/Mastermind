package com.andreasgrassl.common;

import java.util.*;

public class Mastermind implements Runnable {

    private final int numberOfColors = 7;

    private enum MYCOLOR {
        WHITE,
        RED,
        BLUE,
        GREEN,
        YELLOW,
        BLACK,
        PURPLE;

        public static MYCOLOR getRandom(){
            return values()[(int) (Math.random() * values().length)];
        }
    }

    private enum ISRIGHT {
        WHITE("white"),
        BLACK("black"),
        NONE("none");

        private final String description;

        ISRIGHT(String s){
            description = s;
        }
        public String getDescription(){
            return description;
        }
    }

    private enum GAMESTATE {
        ONGOING,
        WIN,
        LOSE,
    }

    private GAMESTATE gamestate;
    private MYCOLOR[] toGuess;
    private ISRIGHT[] whatIsRight;
    private int numbersToGuess, maxTries, triesLeft;
    private List<MYCOLOR[]> guesses = new ArrayList<MYCOLOR[]>();
    private List<String> results = new ArrayList<String>();
    private String playerName;

    public Mastermind(){
    }

    @Override
    public void run() {
        printWelcomeScreen();
        getPlayerName();
        getNewCode();
        getMaxNumberOfTries();
/*
        System.out.print("The code is: ");
        printGuess(toGuess);
*/

        triesLeft = maxTries;
        gamestate = GAMESTATE.ONGOING;
        while (triesLeft > 0 && gamestate == GAMESTATE.ONGOING){
            System.out.println("\nThis is your " + (maxTries - triesLeft +1) + ". try. You have " + triesLeft + " tries left.");
            MYCOLOR[] guess = guess();
            checkGuess(guess);
            guesses.add(guess);
            results.add(getSortedDescriptions().toString());
            System.out.println("Your guesses are: ");
            for(MYCOLOR[] myGuess: guesses){
                printGuess(myGuess);
            }
            System.out.println();
            System.out.println("Your results are:");
            for(String r : results){
                System.out.println(r);
            }
            System.out.println();
        }
        switch (gamestate){
            case LOSE:
                System.out.println("You have lost. Sorry.");
                writeResultToFile("lost");
                break;
            case WIN:
                System.out.println(playerName + " you have won. Great job!");
                System.out.println("You needed " + (maxTries - triesLeft) + " try/tries.");
                writeResultToFile("won");
                break;
            case ONGOING:
                System.out.println("Oops. Something went wrong..");
            default:
                break;
        }
    }

    private void getPlayerName() {
        System.out.println("\nPlease enter your name:");
        Scanner s = new Scanner(System.in);
        playerName = s.nextLine();
    }

    private void printWelcomeScreen(){
        System.out.println("Welcome to my version of Mastermind.");
        System.out.println("Are you a true Mastermind?");
        System.out.println("Let's find out!");
        System.out.println("");
        System.out.println("The basic rules are the following:");
        System.out.println("A random combination of each different colors will determine the \"code\".");
        System.out.println("You have a limited amount of guesses to get the right combination of colors.");
        System.out.println("But be careful!");
        System.out.println("The order in which the colors are typed in matters too.");
        System.out.println("");
        System.out.println("Now you have all the information you need.");
        System.out.println("Good luck!");
     }

    private void getNewCode(){
        System.out.println("Please enter the number of colors your code will have:");
        boolean isInt = false;
        while(!isInt){
            Scanner s = new Scanner(System.in);
            try{
                this.numbersToGuess = s.nextInt();
                if (this.numbersToGuess > this.numberOfColors || this.numbersToGuess < 1){
                    throw new Exception("wrong number of colors");
                }
                isInt = true;
            }
            catch(Exception e){
                System.err.println("Invalid input. Please try again.");
            }
        }

        this.toGuess = new MYCOLOR[this.numbersToGuess];
        boolean hasAlreadyOccured;
        for(int i=0 ; i<toGuess.length ; i++){
            hasAlreadyOccured = false;
            MYCOLOR mc = MYCOLOR.getRandom();
            for(int j=0; j<i ; j++){
                if (mc==toGuess[j]){
                    i--;
                    hasAlreadyOccured = true;
                    break;
                }
                else {
                    hasAlreadyOccured = false;
                }
            }
            if(!hasAlreadyOccured){
                toGuess[i] = mc;
            }
        }
    }

    private void getMaxNumberOfTries(){
        System.out.println("Please enter the maximum amount of tries you want to have:");
        boolean isInt = false;
        while (!isInt){
            Scanner s = new Scanner(System.in);
            try{
                this.maxTries = s.nextInt();
                isInt = true;
            }
            catch(Exception e){
                System.err.println("Invalid input. Please try again.");
            }
        }
    }

    private void printGuess(MYCOLOR[] guess){
        String s = "[";
        for (int i=0 ; i<guess.length ; i++){
            if (i==guess.length-1){
                s += guess[i].toString() + "]";
            }
            else{
                s += guess[i].toString() + ",";
            }
        }
        System.out.println(s);
    }

    private MYCOLOR[] guess(){
        MYCOLOR[] guessed = new MYCOLOR[numbersToGuess];
        try{
            for (int i = 0 ; i < guessed.length ; i++){
                Scanner s = new Scanner(System.in);
                System.out.println("Please enter a color:");
                System.out.print("Your options are ");
                System.out.println(java.util.Arrays.asList(MYCOLOR.values()));
                String st = s.nextLine().toUpperCase();
/*
                System.out.println("Your input was " + st);
*/
                if (st.equals("WHITE")){
                    guessed[i] = MYCOLOR.WHITE;
                }
                else if (st.equals("RED")){
                    guessed[i] = MYCOLOR.RED;
                }
                else if (st.equals("BLUE")){
                    guessed[i] = MYCOLOR.BLUE;
                }
                else if (st.equals("GREEN")){
                    guessed[i] = MYCOLOR.GREEN;
                }
                else if (st.equals("YELLOW")){
                    guessed[i] = MYCOLOR.YELLOW;
                }
                else if (st.equals("PURPLE")){
                    guessed[i] = MYCOLOR.PURPLE;
                }
                else if (st.equals("BLACK")){
                    guessed[i] = MYCOLOR.BLACK;
                }
                else{
                    System.err.println("Your input was invalid. Please try again.");
                    i--;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        triesLeft--;
        return guessed;
    }

    private void checkGuess(MYCOLOR[] guess) {
        if (areEqual(guess,toGuess)){
            gamestate = GAMESTATE.WIN;
        }
        else if (triesLeft == 0){
            gamestate = GAMESTATE.LOSE;
        }
        else{
            System.out.println("Nothing is lost yet. Go for it!\n");
        }
    }

    private boolean areEqual(MYCOLOR[] guess, MYCOLOR[] toGuess) {
        whatIsRight = new ISRIGHT[toGuess.length];
        boolean areEqual = true;
        for (int i = 0 ; i < toGuess.length ; i++){
            if(toGuess[i] != guess[i]){
                areEqual = false;
                boolean alreadyOccured = false;
                for (int j = 0 ; j < toGuess.length ; j++){
                    if (guess[i] == toGuess[j]){
                        alreadyOccured = true;
                    }
                }
                if (alreadyOccured){
                    whatIsRight[i] = ISRIGHT.WHITE;
                }
                else{
                    whatIsRight[i] = ISRIGHT.NONE;
                }
            }
            else{
                whatIsRight[i] = ISRIGHT.BLACK;
            }
        }
        return areEqual;
    }

    private List<String> getSortedDescriptions() {
        List<String> descriptions = new ArrayList<String>();
        for(ISRIGHT e : whatIsRight) {
            descriptions.add(e.getDescription().toUpperCase());
        }
        Collections.sort(descriptions);
        return descriptions;
    }
    private void writeResultToFile(String winOrLose){
        ArrayList<String> results = new ArrayList<String>();
        results.add("Player: " + playerName);
        results.add("Number of colors in the code: " + numbersToGuess);
        results.add("Number of guesses: " + (maxTries-triesLeft));
        results.add("The Player has: " + winOrLose);
        FileReplace f = new FileReplace();
        f.doIt(results);
    }
}
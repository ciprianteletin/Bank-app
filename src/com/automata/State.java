package com.automata;

/**
 * Every state has the purpose to validate a series of letters, until he receive a state-transition character, or the String has
 * no more letters to be checked;
 */
public class State {
    private boolean finalState;
    private String checkExpression;
    private String transition;

    public State(String checkExpression,String transition){
        this(checkExpression,transition,false); //use this constructor for all the states except the last one(final)
    }

    public State(String checkExpression,String transition,boolean finalState){
        this.checkExpression=checkExpression;
        this.finalState=finalState; //I expect that the value that I will receive will be true;
        this.transition=transition;
    }

    boolean isFinalState(){
        return finalState;
    }

    boolean checkPattern(char check){
        String s=check+"";
        return s.matches(checkExpression);
    }

    String getTransition(){
        return this.transition;
    }
}

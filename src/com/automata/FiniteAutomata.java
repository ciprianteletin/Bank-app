package com.automata;

public class FiniteAutomata {
    private Transition transition;
    private String verifyData;

    public FiniteAutomata(String verifyData,State...states){
        transition=new Transition(states);
        this.verifyData=verifyData;
    }

    public boolean validateData(){
        int i=0;
        for(;i<verifyData.length();++i){
            if(transition.nextState(verifyData.charAt(i)))
                continue; //Do nothing at all;
            else if(!transition.verifyCurrentState(verifyData.charAt(i)))
                    return false;
        }
        return (i==verifyData.length() && transition.currentState());
    }
}


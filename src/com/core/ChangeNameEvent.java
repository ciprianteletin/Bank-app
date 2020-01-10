package com.core;
/**
 * La fel ca si interfata Enable, este o interfata functionala ce serveste ca un "actionListener" in cazul unei actiuni generat de o
 * clasa anume, moment in care prin intermediul implementarii acestei metode se realizeaza o actiune dorita, actiune ce trebuie
 * realizata doar in cadrul unui alt eveniment din alta clasa sau pachet.
 **/
public interface ChangeNameEvent {
    void changeEvent();
}

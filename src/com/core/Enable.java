package com.core;
/**
 * O interfata functionala ce este in cadrul acestei aplicatii un mod de apel in cazul unor evenimente. Aceasta interfata a fost
 * gandita cu scopul de a face legatura dintre clase, si a schimba comportamentul altuia in functie de actiunile alteia.
 * Ca si exemplu , aceasta interfata a fost folosita pentru a bloca o interfata cand un buton a fost apasat, astfel ca , dupa ce
 * executia interfetei deschise de buton a fost incheiata, putem sa focalizam iar interfata noastra principala.
 */
public interface Enable {
    void enable();
}

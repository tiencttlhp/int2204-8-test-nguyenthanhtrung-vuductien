/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author TienLuong
 */
class Word{
    private String spelling;    // nội dung từ
    private String explain;     // giải nghĩa

    public Word() {
    }

    public Word(String spelling, String explain) {
        this.spelling = spelling;
        this.explain = explain;
    }
    
    public void Equals(Word outher){
        this.explain = outher.explain;
        this.spelling = outher.spelling;
    }
    
    public void setSPELLING(String st){
        this.spelling = st;
    }
    public String getSPELLING(){
        return this.spelling;
    }
    
    public void setEXPLAIN(String st){
        this.explain = st;
    }
    public String getEXPLAIN(){
        return this.explain;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.spelling);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj){
        if (obj instanceof Word){
            Word outher = (Word) obj;
            if (this.spelling.equals(outher.spelling)) return true;
        }
        return false;
    }
}

class Diction{
    private final ArrayList<Word> listwords;    // danh sách từ
    private final int Length;               // độ dài từ điển

    public Diction() {
        Length=0;
        listwords = new ArrayList<>();
    }

    public int getLength() {
        return Length;
    }

    public ArrayList<Word> getListwords() {
        return listwords;
    }
    
    
}

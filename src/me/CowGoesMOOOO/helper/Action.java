package me.CowGoesMOOOO.helper;

public enum Action {
    RIGHT(0), LEFT(1), UP(2);

    private int number;

    private Action(int number){
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}

package classes;

import java.awt.peer.ScrollbarPeer;

public class Car {
    private int Speed;
    private String Color;

    public Car(){
        this.Speed = 50;
        this.Color = "black";
    }
    public Car(int Speed, String Color){
        this.Speed = Speed;
        this.Color = Color;
    }

    public int increaseSpeed(int delta){
        Speed += delta;
        return Speed;
    }
    public int decreaseSpeed(int delta){
        if (Speed - delta >= 0){
            Speed -= delta;
        }
        return Speed;
    }

    public String changeColor(String Color){
        this.Color = Color;
        return "New color is " + this.Color;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Color=")
                .append(Color)
                .append("\n")
                .append("Speed=")
                .append(Speed)
                .toString();
    }
}

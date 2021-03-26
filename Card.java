import java.util.*;

public class Card {

    public String num;
    public String suit;
    public boolean aceHigh;

    public Card(String num, String suit) {
        this.num = num;
        this.suit = suit;
        this.aceHigh = false;
    }

    public int getValue() {
        if (this.num.equals("A")) {
            if (this.aceHigh) {
                return 14;
            }
            return 1;
        }
        switch (this.num) {
            case "J":
                return 11;
            case "Q":
                return 12;
            case "K":
                return 13;
        }
        return Integer.parseInt(this.num);
    }

    public String getColor() {
        if (this.suit.equals("S") || this.suit.equals("C")) {
            return "B";
        }
        return "R";
    }

    public boolean equals(Card other) {
        return this.num.equals(other.num) && this.suit.equals(other.suit);
    }

    public boolean sameNumber(Card other) {
        return this.num.equals(other.num);
    }

    public boolean sameSuit(Card other) {
        return this.suit.equals(other.suit);
    }

    public String toString() {
        return this.num + this.suit;
    }
}

import java.util.*;
import java.lang.*;

public class Deck {

    public CardStack order;

    public CardStack drawn;

    public int num_decks;

    public Deck() { this(1); }

    public Deck(int num) {
        this.num_decks = num;
        this.order = new CardStack(52 * this.num_decks);
        String[] CARDS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        for (String card : CARDS) {
            String[] SUITS = {"S", "C", "H", "D"};
            for (String suit : SUITS) {
                for (int k = 0; k < this.num_decks; k++) {
                    Card temp = new Card(card, suit);
                    this.order.put(temp);
                }
            }
        }
        this.drawn = new CardStack(52 * this.num_decks);
    }

    public void shuffle() {
        this.reset();
        this.order.shuffle();
    }

    public String toString() {
        return this.order.toString();
    }

    public Card draw() {
        Card result = this.order.draw();
        if (result != null) {
            this.drawn.put(result);
        }
        return result;
    }

    public int size() {
        return this.order.size;
    }

    public Card[] draw(int num_cards) {
        Card[] result = new Card[num_cards];
        for (int i = 0 ; i < num_cards ; i++) {
            result[i] = this.order.draw();
        }
        if (result[num_cards - 1] != null) {
            for (int i = 0; i < num_cards ; i++) {
                this.drawn.put(result[i]);
            }
            return result;
        }
        return null;
    }

    public void reset() {
        Deck temp = new Deck(num_decks);
        this.order = temp.order;
        this.drawn = temp.drawn;
    }
}

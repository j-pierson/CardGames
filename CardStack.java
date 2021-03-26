import java.util.*;

public class CardStack {

    public Card[] cards;

    public int size;

    public CardStack() { this(52); }

    public CardStack(int capacity) {
        this.cards = new Card[capacity];
        this.size = 0;
    }

    public boolean put(Card card) {
        if (this.size == cards.length) {
            return false;
        }
        this.cards[this.size] = card;
        this.size++;
        return true;
    }

    public Card draw() {
        if (this.size == 0) {
            return null;
        }
        this.size--;
        Card result = this.cards[this.size];
        this.cards[this.size] = null;
        return result;
    }

    public void remove(Card card) {
        int idx = this.contains(card);
        if (idx == -1) {
            return;
        }
        System.arraycopy(this.cards, idx + 1, this.cards, idx, this.size - idx);
        this.size--;
    }

    public void shuffle() {
        List<Card> temp = Arrays.asList(this.cards);
        Collections.shuffle(temp);
        temp.toArray(this.cards);
    }

    public int contains(Card given_card) {
        int i = 0;
        for (Card card : this.cards) {
            if (card == null) {
                break;
            }
            if (card.equals(given_card)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public Card[] getCards() {
        Card[] temp = new Card[this.size];
        System.arraycopy(this.cards, 0, temp, 0, this.size);
        return temp;
    }

    public String toString() {
        if (this.size == 0) {
            return null;
        }
        String result = "[";
        for (Card card : this.getCards()) {
            result += card.toString() + ", ";
        }
        return result.substring(0, result.length()-2) + "]";
    }

}

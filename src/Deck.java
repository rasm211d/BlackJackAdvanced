import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> deck;

    public Deck() {
        deck = new ArrayList<>();
        for (Suits s : Suits.values()) {
            for (Ranks r : Ranks.values()) {
                deck.add(new Card(r,s));
            }
        }
        shuffle();
    }

    public Card draw() {
        Card card = deck.get(0);
        deck.remove(0);
        return card;
    }

    private void shuffle() {
        Collections.shuffle(deck);
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }
}

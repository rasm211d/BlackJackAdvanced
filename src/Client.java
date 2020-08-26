public class Client {
    public static void main(String[] args) {
        Deck deck = new Deck();
        for (int i = 0; i < 52; i++) {
            Card card = deck.draw();
            System.out.println(card.getRank().toString() + " of " + card.getSuit().toString() + ". VALUE = " + card.getValue());
        }
    }
}

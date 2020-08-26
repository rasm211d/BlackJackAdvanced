import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client2 {
    DataOutputStream dataOutputStream;
    ObjectInputStream objectInputStream;
    Socket socket;
    Scanner scanner;
    public static void main(String[] args) {
        new Client2();
    }

    public Client2() {
        scanner = new Scanner(System.in);
        try {
            socket = new Socket("localhost", 10000);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            Card card1 = (Card) objectInputStream.readObject();
            Card card2 = (Card) objectInputStream.readObject();

            System.out.println("Your first card is: " + card1.getRank().toString() + " of " + card1.getSuit().toString());
            if (card1.getRank() == Ranks.ACE) {
                System.out.println("Your first card is an ace. Press 1 for 11 press 2 for 1");
                if (scanner.nextInt() == 1) {
                    card1.setValue(11);
                } else if (scanner.nextInt() == 2) {
                    card1.setValue(1);
                }
            }

            System.out.println("Your second card is " + card2.getRank().toString() + " of " + card2.getSuit().toString());
            if (card2.getRank() == Ranks.ACE) {
                System.out.println("Your second card is an ace. Press 1 for 11 press 2 for 1");
                if (scanner.nextInt() == 1) {
                    card2.setValue(11);
                } else if (scanner.nextInt() == 2) {
                    card2.setValue(1);
                }
            }

            int value = card2.getValue()+card1.getValue();
            System.out.println("Your total is: " + value);


            while (true) {
                System.out.println("Do you want to hit or stay?");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("hit")) {
                    dataOutputStream.writeUTF("hit");
                    Card card = (Card) objectInputStream.readObject();
                    System.out.println("Your new card is " + card.getRank().toString() + " of " + card.getSuit().toString());
                    value += card.getValue();
                    dataOutputStream.writeInt(value);
                    System.out.println("Your new total is: " + value);
                    if (value > 21) {
                        System.out.println("Your number is over 21 and your number is " + value + " You are out");
                        dataOutputStream.writeInt(value);
                        break;
                    }
                } else if (input.equalsIgnoreCase("stay")) {
                    dataOutputStream.writeUTF("stay");
                    System.out.println("you decided to stay at: " + value);
                    dataOutputStream.writeInt(value);
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




}

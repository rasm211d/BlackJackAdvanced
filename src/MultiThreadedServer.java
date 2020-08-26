import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class MultiThreadedServer {
    private static Deck deck;
    private int sessionNo = 1;
    private int playerTurn = 1;
    private int player1Total;
    public static void main(String[] args) {
        deck = new Deck();
        System.out.println("Server startet");
        new MultiThreadedServer();
    }

    public MultiThreadedServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(10000);
                while (true) {
                    System.out.println(new Date() + ": Wait for players to join session " + sessionNo + '\n');

                    Socket player1 = serverSocket.accept();
                    System.out.println(new Date() + ": Player 1 joined session " + sessionNo + '\n');
                    System.out.println("Player 1's IP address" + player1.getInetAddress().getHostAddress() + '\n');

                    Socket player2 = serverSocket.accept();
                    System.out.println(new Date() + ": Player 2 joined session " + sessionNo + '\n');
                    System.out.println("Player 2's IP address" + player2.getInetAddress().getHostAddress() + '\n');

                    new Thread(new HandleAClient(player1, player2)).start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    private class HandleAClient implements Runnable {
        Socket player1;
        Socket player2;

        public HandleAClient(Socket player1, Socket player2) {
            this.player1 = player1;
            this.player2 = player2;
        }

        @Override
        public void run() {
            try {
                DataInputStream fromPlayer1 = new DataInputStream(player1.getInputStream());
                ObjectOutputStream toPlayer1 = new ObjectOutputStream(player1.getOutputStream());
                DataInputStream fromPlayer2 = new DataInputStream(player2.getInputStream());
                ObjectOutputStream toPlayer2 = new ObjectOutputStream(player2.getOutputStream());

                try {
                    toPlayer1.writeObject(deck.draw());
                    toPlayer1.writeObject(deck.draw());
                    toPlayer2.writeObject(deck.draw());
                    toPlayer2.writeObject(deck.draw());
                }catch (IOException e) {
                    e.printStackTrace();
                }

                if (playerTurn == 1) {
                    System.out.println("Player 2's turn");
                    inputFromPlayers(fromPlayer1, toPlayer1);
                }

                if (playerTurn == 2) {
                    System.out.println("Player 2's turn");
                    inputFromPlayers(fromPlayer2, toPlayer2);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void inputFromPlayers(DataInputStream fromPlayer, ObjectOutputStream toPlayer) throws IOException {
            while (true) {
                String input = fromPlayer.readUTF();
                if (input.equalsIgnoreCase("hit")) {
                    toPlayer.writeObject(deck.draw());
                    int localTotal = fromPlayer.readInt();
                    if (localTotal == 21) {
                        player1Total = localTotal;
                        System.out.println("You got 21");
                        playerTurn = 2;
                        break;
                    }
                    if (localTotal > 21) {
                        player1Total = localTotal;
                        System.out.println("Player 1 went over 21 and got: " + player1Total);
                        playerTurn = 2;
                        break;
                    }
                } else if (input.equalsIgnoreCase("stay")) {
                    System.out.println("Player 1 decided to stay at: " + fromPlayer.readInt());
                    playerTurn = 2;
                    break;
                }
            }
        }
    }
}

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class MultiThreadedServer {
    private Deck deck;
    private int sessionNo = 1;
    public static void main(String[] args) {
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
                ObjectInputStream fromPlayer1 = new ObjectInputStream(player1.getInputStream());
                ObjectOutputStream toPlayer1 = new ObjectOutputStream(player1.getOutputStream());
                ObjectInputStream fromPlayer2 = new ObjectInputStream(player2.getInputStream());
                ObjectOutputStream toPlayer2 = new ObjectOutputStream(player2.getOutputStream());

                toPlayer1.writeObject(deck.draw());
                toPlayer1.writeObject(deck.draw());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

package agent.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AgentClient implements Runnable {

    final String GEP = "127.0.0.1";
    final int PORT = 12345;

    private int teamNumber;
    private int memberNumber;

    public AgentClient(int teamNumber, int memberNumber) {
        this.teamNumber = teamNumber;
        this.memberNumber = memberNumber;
    }

    @Override
    public void run() {
        System.out.format("[Team %d Member %d ]Hello I will ensure the client side in the run.", teamNumber, memberNumber);
        try {
            Socket s = new Socket(GEP, PORT);
            Scanner sc = new Scanner(s.getInputStream(), "utf-8");
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println("hello world, bla.. bla .. bla..");
            pw.flush();
            String be = sc.nextLine();

            System.out.println(be);

            pw.close();
            sc.close();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package agent.communication;

import agent.main.AgentMain;

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
        System.out.format("[Team %d Member %d ]Hello I will ensure the client side in the run.\n", teamNumber, memberNumber);
        Socket s = null;
        Scanner sc = null;
        PrintWriter pw = null;
        while (!AgentMain.endGame) {
            try {
                System.out.println("[START OF CLIENT LOOP]");
                s = new Socket(GEP, PORT);
                sc = new Scanner(s.getInputStream(), "utf-8");
                pw = new PrintWriter(s.getOutputStream());
                pw.println("hello world, bla.. bla .. bla..");
                pw.flush();
                String be = sc.nextLine();

                System.out.println(be);
                System.out.println("[END OF CLIENT LOOP]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        pw.close();
        sc.close();
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

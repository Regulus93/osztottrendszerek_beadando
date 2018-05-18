package agent.communication;

import agent.Agent;
import agent.main.AgentMain;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class AgentServer implements Runnable {

    final int PORT = 12345;

    private int teamNumber;
    private int memberNumber;

    public AgentServer(int teamNumber, int memberNumber) {
        this.teamNumber = teamNumber;
        this.memberNumber = memberNumber;
    }

    @Override
    public void run() {
        System.out.format("[Team %d Member %d ]Hello I will ensure the server side in the run.\n", teamNumber, memberNumber);
        ServerSocket ss = null;
        Socket s = null;
        Scanner sc = null;
        PrintWriter pw = null;
        while (!AgentMain.endGame) {
            try {
                System.out.println("[START OF SRV LOOP]");
                ss = new ServerSocket(PORT);
                ss.setSoTimeout(Agent.t2);
                s = ss.accept();
                sc = new Scanner(s.getInputStream(), "utf-8");
                pw = new PrintWriter(s.getOutputStream());
                String be = sc.nextLine();
                System.out.println("[SRV] " + be);
                pw.println("Válasz a szervertől: " + be);
                pw.flush();
                System.out.println("[END OF SRV LOOP]");
            } catch (IOException e) {
                e.printStackTrace();
            }
            pw.close();
            sc.close();

            try {
                s.close();
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

package agent.communication;

import agent.Agent;

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
        System.out.format("[Team %d Member %d ]Hello I will ensure the server side in the run.", teamNumber, memberNumber);
        try {
            ServerSocket ss = new ServerSocket(PORT);
            ss.setSoTimeout(Agent.t2);
            Socket s = ss.accept();
            Scanner sc = new Scanner(s.getInputStream(), "utf-8");
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            String be = sc.nextLine();
            System.out.println(be);
            pw.println(be);
            pw.flush();

            pw.close();
            sc.close();
            s.close();
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

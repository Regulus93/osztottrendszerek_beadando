package agent.communication;

import agent.Agent;
import agent.main.AgentMain;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class AgentServer implements Runnable {


    private int teamNumber;
    private int memberNumber;

    public AgentServer(int teamNumber, int memberNumber) {
        this.teamNumber = teamNumber;
        this.memberNumber = memberNumber;
    }

    @Override
    public void run() {
        System.out.format("[Team %d Member %d ]Hello I will ensure the server side in the run.\n", teamNumber, memberNumber);
        Random r = new Random();
        int port = 0;
        int calcPort = 0;
        ServerSocket ss = null;
        Socket s = null;
        Scanner sc = null;
        PrintWriter pw = null;
        while (!AgentMain.endGame) {

            do {
                calcPort = Agent.PORT_INTERVAL_START + r.nextInt(3);
            } while (calcPort == port);
            port = calcPort;
            try
            {
                ss = new ServerSocket(port);
                s        = ss.accept();
                sc      = new Scanner(s.getInputStream(), "utf-8");
                pw  = new PrintWriter(s.getOutputStream());
                System.out.println("[START OF SRV LOOP]");
                ss.setSoTimeout(Agent.t2);
                String be = sc.nextLine();
                System.out.println("[SRV] " + be);
                pw.println("Válasz a szervertől: " + be);
                pw.flush();
                System.out.println("[END OF SRV LOOP]");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sc.close();
                s.close();
                pw.close();
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[FFFF]");
    }
}

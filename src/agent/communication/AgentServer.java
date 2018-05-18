package agent.communication;

import agent.Agent;
import agent.main.AgentMain;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;
import java.util.Scanner;

public class AgentServer implements Runnable {


    private int teamNumber;
    private int memberNumber;
    private Agent agent;

    public AgentServer(int teamNumber, int memberNumber, Agent agent) {
        this.teamNumber = teamNumber;
        this.memberNumber = memberNumber;
        this.agent = agent;
    }

    @Override
    public void run() {
        System.out.format("[Team %d Member %d ]Hello I will ensure the server side in the run.\n", teamNumber, memberNumber);
        ServerSocket ss = null;
        Socket s = null;
        Scanner sc = null;
        PrintWriter pw = null;
        int currentPort = 0;
        while (!AgentMain.endGame) {
                currentPort = Agent.generateNewPortNumber(currentPort);
            try {
                ss = new ServerSocket(currentPort);
                ss.setSoTimeout(Agent.t2);
                System.out.println("[START OF SRV LOOP] PORT NUMBER - " + currentPort + " WITH ENDGAME: " + ss.getSoTimeout());
                s = ss.accept();
                sc = new Scanner(s.getInputStream(), "utf-8");
                pw = new PrintWriter(s.getOutputStream());
                String be = sc.nextLine();
                System.out.println("[SRV] " + be);
                pw.println("Válasz a szervertől: " + be);
                pw.flush();
                System.out.println("[END OF SRV LOOP]");
            } catch (SocketTimeoutException e) {
//                e.printStackTrace();
                System.out.println("!!!!TIMEOUT!");
            } catch (IOException e) {
//                e.printStackTrace();
            }

            if(s != null){
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
        System.out.println("[SRV] Shutdown...");
    }
}

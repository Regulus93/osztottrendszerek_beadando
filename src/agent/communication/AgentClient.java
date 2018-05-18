package agent.communication;

import agent.Agent;
import agent.main.AgentMain;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class AgentClient implements Runnable {

    final String HOST = "127.0.0.1";

    private int teamNumber;
    private int memberNumber;
    private Agent agent;

    public AgentClient(int teamNumber, int memberNumber, Agent agent) {
        this.teamNumber = teamNumber;
        this.memberNumber = memberNumber;
        this.agent = agent;
    }

    @Override
    public void run() {
        System.out.format("[Team %d Member %d ]Hello I will ensure the client side in the run.\n", teamNumber, memberNumber);
        Socket s = null;
        Scanner sc = null;
        PrintWriter pw = null;
        Random r = new Random();
        int retryInterval = 0;
        int calcPort = 0;
        int currentPort = 0;
        while (!AgentMain.endGame) {
            do {
                calcPort = Agent.PORT_INTERVAL_START + r.nextInt(1000);
            } while (currentPort == calcPort);
            currentPort = calcPort;
            try {
                System.out.println("[START OF CLIENT LOOP] PORT NUMBER - " + currentPort + " WITH ENDGAME: " + AgentMain.endGame);

                retryInterval = r.nextInt(Agent.t2 - Agent.t1 + 1) + Agent.t1;
                System.out.format("I will retry on another port after: %d ms\n", retryInterval);
                Thread.sleep(retryInterval);

                s = new Socket(HOST, currentPort);
                sc = new Scanner(s.getInputStream(), "utf-8");
                pw = new PrintWriter(s.getOutputStream());
                pw.println("hello world, bla.. bla .. bla..");
                pw.flush();
                String be = sc.nextLine();

                System.out.println("[CLI] " + be);
                System.out.println("[END OF CLIENT LOOP]");

                sc.close();
                pw.close();
                s.close();
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
        }
        System.out.println("[CLI] Shutdown...");
    }
}

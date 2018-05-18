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

    public AgentClient(int teamNumber, int memberNumber) {
        this.teamNumber = teamNumber;
        this.memberNumber = memberNumber;
    }

    @Override
    public void run() {
        System.out.format("[Team %d Member %d ]Hello I will ensure the client side in the run.\n", teamNumber, memberNumber);
        Random r = new Random();
        int waitInterval;
        int port = 0;
        int calcPort = 0;
        while (!AgentMain.endGame) {

            do {
                calcPort = Agent.PORT_INTERVAL_START + r.nextInt(3);
            } while (calcPort == port);
            port = calcPort;
            try(	Socket s        = new Socket(HOST, port);
                    Scanner sc      = new Scanner(s.getInputStream(), "utf-8");
                    PrintWriter pw  = new PrintWriter(s.getOutputStream())
            )
            {
//                System.out.println("[START OF CLIENT LOOP]");
                pw.println("hello world, bla.. bla .. bla..");
                pw.flush();
                String be = sc.nextLine();

//                System.out.println("[CLI] " + be);
//                System.out.println("[END OF CLIENT LOOP]");
            } catch (NoSuchElementException e) {
//                e.printStackTrace();
            } catch (IOException e) {
//                e.printStackTrace();
            } finally {
                waitInterval = r.nextInt(Agent.t2 - Agent.t1 + 1 ) + Agent.t1;
//                System.out.format("[CLI] I WENT TO SLEEP FOR %d MS.",waitInterval);
                try {
                    Thread.sleep(waitInterval);
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                }
            }
        }
        System.out.println("[CLI] Kijutott a ciklusbol");

    }
}

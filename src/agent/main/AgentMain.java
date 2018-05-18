package agent.main;

import agent.Agent;

import java.util.ArrayList;
import java.util.List;

public class AgentMain {

    public final static Object lock = new Object();
    public static boolean endGame = false;
    public static int n;
    public static int m;

    public static void main(String[] args) {

        if (args.length != 4) {
            System.out.println("The software works with four commandline argument! \n The program exiting..");
            System.exit(0);
        }

        n = Integer.parseInt(args[0]);
        m = Integer.parseInt(args[1]);

        int t1 = Integer.parseInt(args[2]);
        int t2 = Integer.parseInt(args[3]);

        Agent.t1 = t1;
        Agent.t2 = t2;

        List<Agent> team1 = new ArrayList<>();
        List<Agent> team2 = new ArrayList<>();

        new Thread(() -> {
            for (int i = 0; i < n; i++) {
                Agent agent = new Agent(1, i+1);
                team1.add(agent);
                agent.run();
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < m; i++) {
                Agent agent = new Agent(2, i+1);
                team2.add(agent);
                agent.run();
            }
        }).start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        endGame = true;
        System.out.println("Endgame");
    }
}
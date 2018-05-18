package agent.main;

import agent.Agent;
import game.SimpleGameLogic;

public class AgentMain {

    public final static Object lock = new Object();

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

        SimpleGameLogic sgl = new SimpleGameLogic();
        sgl.start();
    }
}
package agent.main;

import agent.Agent;
import agent.secret.Secret;

public class AgentMain{
    public static void main(String[] args){

        /*
            Test for two Agent connection
         */

        if(args.length != 2){
            System.out.println("The software works with two commandline argument! \n The program exiting..");
            System.exit(0);
        }

        int t1 = Integer.parseInt(args[0]);
        int t2 = Integer.parseInt(args[1]);

        //Init
        Agent a1 = new Agent(1,1);
        Agent a2 = new Agent(1,2);

        a1.run();
        a2.run();

    }
}
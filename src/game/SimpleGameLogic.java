package game;

import agent.Agent;
import agent.main.AgentMain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleGameLogic {

    public static boolean endGame = false;
    public static Object lock = new Object();
    public static Agent winner = null;

    private static Map<Agent,Boolean> team1 = new HashMap<>();
    private static Map<Agent,Boolean> team2 = new HashMap<>();

    public static void start() {

        new Thread(() -> {
            for (int i = 0; i < AgentMain.n; i++) {
                Agent agent = new Agent(1, i+1);
                team1.put(agent,true);
                agent.run();
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < AgentMain.m; i++) {
                Agent agent = new Agent(2, i+1);
                team2.put(agent,true);
                agent.run();
            }
        }).start();

        synchronized (lock) {
            try{
                lock.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            endGame = true;
        }

    }

    public static int countArrested(int teamNumber) {
        Collection<Boolean> teamValues;
        if(teamNumber == 1){
            teamValues = team1.values();
        } else {
            teamValues = team2.values();
        }

        int arrestedCount = 0;
        for (boolean b : teamValues) {
            if(b == false){
                arrestedCount++;
            }
        }
        return arrestedCount;
    }

    public static void checkGroupIsArrested(int teamNumber) {
        int arrestedCount = countArrested(teamNumber);
        if(teamNumber == 1){
            if(arrestedCount == AgentMain.n){
                lock.notify();
            }
        } else if (teamNumber == 2){
            if(arrestedCount == AgentMain.m){
                lock.notify();
            }
        }

    }
}

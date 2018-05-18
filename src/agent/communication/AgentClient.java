package agent.communication;

import agent.Agent;
import agent.main.AgentMain;
import agent.secret.Secret;

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
        int currentPort = 0;
//        while (!AgentMain.endGame) {
            currentPort = Agent.generateNewPortNumber(currentPort);
            try {
                System.out.format("[CLI %d-%d] Try to connect with port number: ",agent.getTeamNumber(),agent.getMemberNumber(),currentPort);

                retryInterval = r.nextInt(Agent.t2 - Agent.t1 + 1) + Agent.t1;
                System.out.format("[CLI %d-%d] I will retry on another port after: %d ms\n",agent.getTeamNumber(),agent.getMemberNumber(),retryInterval);
                Thread.sleep(retryInterval);

                s = new Socket(HOST, currentPort);
                sc = new Scanner(s.getInputStream(), "utf-8");
                pw = new PrintWriter(s.getOutputStream());

                clientProtocol(sc,pw);

            } catch (NoSuchElementException e) {
                System.out.format("[CLI %d-%d] [NoSuchElementException] Server closed the connection.\n",agent.getTeamNumber(),agent.getMemberNumber());
            } catch (IOException e) {
                System.out.format("[CLI %d-%d] [IOException] %s.\n",agent.getTeamNumber(),agent.getMemberNumber(),e.getMessage());
            } catch (InterruptedException e) {
                System.out.format("[CLI %d-%d] [InterruptedException] %s.\n",agent.getTeamNumber(),agent.getMemberNumber(),e.getMessage());
            }

            if(s!=null){
                sc.close();
                pw.close();
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


//        }
        System.out.format("[CLI %d-%d] Shutdown...\n",agent.getTeamNumber(),agent.getMemberNumber());
    }

    public void clientProtocol(Scanner in, PrintWriter out){

        //Titkos nev fogadasa
        String srvAlias = in.nextLine();
        System.out.format("[CLI %d-%d] Alias arrived: %s\n",agent.getTeamNumber(),agent.getMemberNumber(),srvAlias);

        //Csapatot tippel
        int teamTip = agent.guessTeam(srvAlias);
        System.out.format("[CLI %d-%d] Team-tip: %d (my teamnumber is %d)\n",agent.getTeamNumber(),agent.getMemberNumber(),teamTip,agent.getTeamNumber());
        out.println(teamTip);
        out.flush();

        //Jol tippelt csapatot a kliens
        String teamTipIsOk = in.nextLine();
        System.out.format("[CLI %d-%d] Server said that teamtip is %s!\n",agent.getTeamNumber(),agent.getMemberNumber(),teamTipIsOk);

        //az informaciocsere tipusanak meghatarozasa
        boolean isSameTeam = teamTip==agent.getTeamNumber();
        int ownTeamIndex = agent.getTeamNumber();
        int otherTeamIndex;
        if(agent.getTeamNumber() == 1){
            otherTeamIndex = 2;
        } else {
            otherTeamIndex = 1;
        }

        if(isSameTeam){
            out.println("OK");
            out.flush();
            System.out.format("[CLI %d-%d] OK let's trade secrets, friend!\n",agent.getTeamNumber(),agent.getMemberNumber());
        } else {
            int memberNumberTip = agent.guessMemberNumber(srvAlias,teamTip);
            out.println("??? " + memberNumberTip);
            out.flush();
            System.out.format("[CLI %d-%d] Hmmm, what's your member number, %d?\n",agent.getTeamNumber(),agent.getMemberNumber(),memberNumberTip);
        }

        //Titok fogadasa (ha azonos a csapat, akkor a kliens is kuld egyet)
        String secretText = in.nextLine();
        if(isSameTeam){
            agent.addSecret(new Secret(secretText,ownTeamIndex));

            System.out.format("[CLI %d-%d] Thank you very much! I send one secret back for you.\n",agent.getTeamNumber(),agent.getMemberNumber());

            out.println(agent.chooseSecret(isSameTeam));
            out.flush();
        } else {
            agent.addSecret(new Secret(secretText,otherTeamIndex));
            System.out.format("[CLI %d-%d] Thank you very much, we will win! MUHAHAHA\n",agent.getTeamNumber(),agent.getMemberNumber());
        }

        }
}

package agent.communication;

import agent.Agent;
import agent.main.AgentMain;
import agent.secret.Secret;

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
//        while (!AgentMain.endGame && !agent.isArrested()) {
                currentPort = Agent.generateNewPortNumber(currentPort);
            try {
                ss = new ServerSocket(currentPort);
                ss.setSoTimeout(Agent.t2);
                System.out.format("[SRV %d-%d] Try to get some client through port number: %d",agent.getTeamNumber(),agent.getMemberNumber(),currentPort);
                s = ss.accept();
                System.out.format("[SRV %d-%d] A client connected to the server\n",agent.getTeamNumber(),agent.getMemberNumber());
                sc = new Scanner(s.getInputStream(), "utf-8");
                pw = new PrintWriter(s.getOutputStream());

                serverProtocol(sc,pw);

            } catch (SocketTimeoutException e) {
                System.out.format("[SRV %d-%d] - TIMEOUT!!!\n",agent.getTeamNumber(),agent.getMemberNumber());
            } catch (IOException e) {
                System.out.format("[SRV %d-%d] [IOException] %s.\n",agent.getTeamNumber(),agent.getMemberNumber(),e.getMessage());
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

//        }
        System.out.format("[SRV %d-%d] Shutdown...\n",agent.getTeamNumber(),agent.getMemberNumber());
    }

    public void serverProtocol(Scanner in, PrintWriter out){

        //Egy sajat alias elkuldese
        String chosenAlias = agent.chooseAlias();
        out.println(chosenAlias);
        out.flush();

        System.out.format("[SRV %d-%d] Chosen alias (%s) sent to client.\n",agent.getTeamNumber(),agent.getMemberNumber(),chosenAlias);

        //Kliens csapat-tippjenek kiertekelese
        String teamTip = in.nextLine();
        if(Integer.parseInt(teamTip) != agent.getTeamNumber()){
            System.out.format("[SRV %d-%d] We don't know each other, and you don't have luck with teamnumbers. Please don't disturb me, bye!\n",agent.getTeamNumber(),agent.getMemberNumber());
            return;
        }

        //Megerosites, hogy helyes a csapat-tipp
        System.out.format("[SRV %d-%d] Correct Team-tip (%s)\n",agent.getTeamNumber(),agent.getMemberNumber(),teamTip);
        out.println("OK");
        out.flush();

        //Titokcsere vagy elarulasa
        String sameTeamResponse = in.nextLine();
        if("OK".equals(sameTeamResponse)){
            out.println(agent.chooseSecret(true));
            out.flush();

            agent.addSecret(new Secret(in.nextLine(),agent.getTeamNumber()));

            System.out.format("[SRV %d-%d] I swapped secret with the client, but it's okay because we're teammates!\n",agent.getTeamNumber(),agent.getMemberNumber());
        } else {
            int memberNumberTip = Integer.parseInt(sameTeamResponse.split(" ")[1]);
            if(memberNumberTip == agent.getMemberNumber()){
                out.println(agent.chooseSecret(false));
                out.flush();

                System.out.format("[SRV %d-%d] Oops! I accidentally betray one secret to the other team...\n",agent.getTeamNumber(),agent.getMemberNumber());
            } else {
                System.out.format("[SRV %d-%d] We aren't know each other, and you don't have luck with membernumbers. Please don't disturb me, bye!\n",agent.getTeamNumber(),agent.getMemberNumber());
            }
        }

    }
}

package agent;

import agent.communication.AgentClient;
import agent.communication.AgentServer;
import agent.secret.Secret;

import java.util.*;

public class Agent implements AgentInterface, Runnable {

    //own properties
    private List<String> ownAliases;
    private List<Secret> secrets;
    private int teamNumber;
    private int memberNumber;
    private boolean isArrested;

    //Agent srv and cli threads
    private AgentServer server;
    private AgentClient client;

    //to the game logic
    private Map<String,Agent> knownAgents;

    public Agent(int teamNumber, int memberNumber){
        this.teamNumber = teamNumber;
        this.memberNumber = memberNumber;
        isArrested = false;

        ownAliases = new ArrayList<>();
        secrets = new ArrayList<>();
        knownAgents = new HashMap<>();
    }

    public List<String> getOwnAliases() {
        return ownAliases;
    }

    public void setOwnAliases(List<String> ownAliases) {
        this.ownAliases = ownAliases;
    }

    public List<Secret> getSecrets() {
        return secrets;
    }

    public void setSecrets(List<Secret> secrets) {
        this.secrets = secrets;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public int getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(int memberNumber) {
        this.memberNumber = memberNumber;
    }

    public Map<String, Agent> getKnownAliases() {
        return knownAgents;
    }

    public void setKnownAliases(Map<String, Agent> knownAliases) {
        this.knownAgents = knownAliases;
    }

    public boolean isArrested() {
        return isArrested;
    }

    public void setArrested(boolean arrested) {
        isArrested = arrested;
    }

    public int countEnemySecrets(){
        int enemySecretCount = 0;
        for (Secret s: secrets) {
            if(s.getTeam() != teamNumber){
                enemySecretCount++;
            }
        }
        return enemySecretCount;
    }

    public int countBetrayedSecrets(){
        int betrayedSecretCount = 0;
        for(Secret s: secrets){
            if(s.isBetrayed()){
                betrayedSecretCount++;
            }
        }
        return betrayedSecretCount;
    }

    @Override
    public int guessTeam(String alias) {
        if(knownAgents.containsKey(alias)){
            return knownAgents.get(alias).getTeamNumber();
        } else {
            Random r = new Random();
            return r.nextInt(2);
        }
    }

    @Override
    public int guessMemberNumber(String alias, int memberNumberRange) {
        if (knownAgents.containsKey(alias)) {
            return knownAgents.get(alias).getTeamNumber();
        } else {
            Random r = new Random();
            return r.nextInt(memberNumberRange);
        }
    }

    @Override
    public Secret chooseSecret(int otherPlayerTeamNumber){
        int randomIndex = 0;
        Random r = new Random();
        if(teamNumber == otherPlayerTeamNumber){
            randomIndex = r.nextInt(secrets.size());
        } else {
            if(secrets.size() > countBetrayedSecrets()){
                boolean isBetrayed = true;
                while(isBetrayed){
                    randomIndex = r.nextInt(secrets.size());
                    isBetrayed = secrets.get(randomIndex).isBetrayed();
                }
                secrets.get(randomIndex).setIsBetrayed(true);
            } else {
                isArrested = true;
                return null;
            }
        }
        return secrets.get(randomIndex);
    }

    @Override
    public String chooseAlias() {
        Random r = new Random();
        int randomIndex = r.nextInt(ownAliases.size());
        return ownAliases.get(randomIndex);
    }

    @Override
    public void run() {
        System.out.format("[Team %d Member %d] Started.\n",teamNumber,memberNumber);

        //srv
        server = new AgentServer();
        server.run();
        //cli
        client = new AgentClient();
        client.run();

    }

}

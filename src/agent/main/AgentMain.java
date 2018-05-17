package agent.main;

import agent.Agent;
import agent.secret.Secret;

public class AgentMain{
    public static void main(String[] args){

        /*
            Test for the first basic methods in Agent
         */

        //Init
        Agent a = new Agent(1,1);
        Secret s1 = new Secret("Asd",1);
        Secret s2 = new Secret("Dsa",1);
        a.getSecrets().add(s1);
        a.getSecrets().add(s2);
        a.getOwnAliases().add("Fredi");
        a.getOwnAliases().add("Krueger");
        a.getOwnAliases().add("Istvan");

        //Secret-handling
        Secret result = a.chooseSecret(2);
        System.out.println("Chosen secret: " + result.getContent() + " (is betrayed? " + result.isBetrayed() + ")");
        Secret result2 = a.chooseSecret(2);
        System.out.println("Chosen secret: " + result2.getContent() + " (is betrayed? " + result2.isBetrayed() + ")");
        System.out.println("Betrayed secrets number: " + a.countBetrayedSecrets());

        //Get random aliases
        System.out.println("Random alias: " + a.chooseAlias());
        System.out.println("Random alias: " + a.chooseAlias());
    }
}
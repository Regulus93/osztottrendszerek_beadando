package agent;

import agent.secret.Secret;

public interface AgentInterface {

    String chooseAlias();

    Secret chooseSecret(int teamNumber);

    int guessTeam(String alias);

    int guessMemberNumber(String alias, int memberNumberRange);


}

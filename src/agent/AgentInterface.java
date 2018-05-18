package agent;

public interface AgentInterface {

    String chooseAlias();

    String chooseSecret(boolean isSameTeam);

    int guessTeam(String alias);

    int guessMemberNumber(String alias, int memberNumberRange);


}

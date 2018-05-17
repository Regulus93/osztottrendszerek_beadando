package agent.secret;

public class Secret {
    private String content;
    private int team;
    private boolean isBetrayed;

    public Secret(String content, int team) {
        this.content = content;
        this.team = team;
        isBetrayed = false;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public boolean isBetrayed() {
        return isBetrayed;
    }

    public void setIsBetrayed(boolean isBetrayed) {
        this.isBetrayed = isBetrayed;
    }
}

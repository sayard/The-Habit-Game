package pl.c0.sayard.thehabitgame.widgets;

/**
 * Created by Karol on 06.05.2017.
 */

public class HabitListItem {

    private int id;
    private String heading;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeading() {
        return heading;
    }

    public String getContent() {
        return content;
    }
}

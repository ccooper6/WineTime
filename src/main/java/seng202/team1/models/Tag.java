package seng202.team1.models;

public class Tag {
    private String name;
    private String type;

    /**
     * Constructor for a Tag.
     * @param name The name of the tag
     * @param type The type of the tag
     */
    public Tag(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the name of the tag.
     * @return The name of the tag
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tag.
     * @param name The name of the tag
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of the tag.
     * @return The type of the tag
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the tag.
     * @param type The type of the tag
     */
    public void setType(String type) {
        this.type = type;
    }
}
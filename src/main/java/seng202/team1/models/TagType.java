package seng202.team1.models;

/**
 * Enum that defines the available tag types sored in the database.
 */
public enum TagType {
    VARIETY,
    PROVINCE,
    REGION,
    VINTAGE,
    COUNTRY,
    WINERY,
    POINTS;

    /**
     * Returns the corresponding TagType given a string
     *
     * @param string The string to match tag type to
     * @return A corresponding TagType or null if string is invalid
     */
    public static TagType fromString(String string) {
        return switch (string.toLowerCase()) {
            case "variety" -> TagType.VARIETY;
            case "province" -> TagType.PROVINCE;
            case "region" -> TagType.REGION;
            case "vintage" -> TagType.VINTAGE;
            case "country" -> TagType.COUNTRY;
            case "winery" -> TagType.WINERY;
            case "points" -> TagType.POINTS;
            default -> null;
        };
    }

    public static String toString(TagType tagType) {
        return switch (tagType) {
            case VARIETY -> "Variety";
            case PROVINCE -> "Province";
            case REGION -> "Region";
            case VINTAGE -> "Vintage";
            case COUNTRY -> "Country";
            case WINERY -> "Winery";
            case POINTS -> "Points";
        };
    }
}

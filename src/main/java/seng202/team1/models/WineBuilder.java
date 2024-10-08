package seng202.team1.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Builder class for the Wine object.
 * @author Yuhao Zhang
 */
public class WineBuilder {
    private int id;
    private String name = null;
    private String description = null;
    private int price = -1;
    private int points = -1;
    private int vintage = -1;
    private String country = null;
    private String province = null;
    private String region1 = null;
    private String region2 = null;
    private String variety = null;
    private String winery = null;

    private static final Logger LOG = LogManager.getLogger(WineBuilder.class);

    /**
     * Returns the built Wine object using the values stored in this class as arguments.
     * Returns null if name is not assigned
     * @return {@link Wine} the built wine object, null if wine has no name
     */
    public Wine build() {
        if (name == null || name.isEmpty()) {
            LOG.error("Error in WineBuilder.build: Wine name is null or empty");
            return null;
        }

        return new Wine(id, name, description, price, points, vintage, country, province, region1, region2, variety, winery);
    }

    /**
     * Basic Setup for WineBuilder taking in arguments found in the wine.
     *
     * @param id          The id of the wine
     * @param name        The name of the wine
     * @param description The description of the wine
     * @param price       The price of the wine
     * @param points
     * @return {@link WineBuilder} with id, name, description and price setup
     */
    public static WineBuilder genericSetup(int id, String name, String description, int price, int points)
    {
        WineBuilder builder = new WineBuilder();
        builder.id = id;
        builder.name = name;
        builder.description = description;
        builder.price = price;
        builder.points = points;
        return builder;
    }

    /**
     * Setter for vintage.
     * @param vintage The vintage of the wine
     */
    private void setVintage(String vintage)
    {
        if (vintage != null && !vintage.isEmpty()) {
            this.vintage = Integer.parseInt(vintage);
        } else {
            this.vintage = -1;
        }
    }

    /**
     * Setter for region.
     * @param region The region of the wine
     */
    private void setRegion(String region) {
        if (this.region1 == null) {
            this.region1 = region;
        } else {
            this.region2 = region;
        }
    }

    /**
     *
     *
     * @param type The type of tag to set
     * @param value The value to set the tag to
     */
    public void setTag(TagType type, String value)
    {
        switch (type) {
            case TagType.VARIETY:
                variety = value;
                break;
            case TagType.PROVINCE:
                province = value;
                break;
            case TagType.REGION:
                setRegion(value);
                break;
            case TagType.VINTAGE:
                setVintage(value);
                break;
            case TagType.COUNTRY:
                country = value;
                break;
            case TagType.WINERY:
                winery = value;
                break;
            case null:
            default:
                LOG.error("Error: Tag type {} is not supported!", type);
        }
    }
}

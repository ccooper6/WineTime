package seng202.team1.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Builder class for the Wine object.
 * @author Yuhao Zhang
 */
public class WineBuilder {
    //TODO tasters?

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
    private String tasterName = null;
    private String tasterTwitter = null;

    private static final Logger LOG = LogManager.getLogger(WineBuilder.class);

    /**
     * Returns the built Wine object using the values stored in this class as arguments.
     * Returns null if name is not assigned
     * @return {@link Wine} the built wine object
     */
    public Wine build() {
        if (name == null || name.isEmpty()) {
            LOG.error("Error in WineBuilder.build: Wine name is null or empty");
            return null;
        }

        return new Wine(id, name, description, price, points, vintage, country, province, region1, region2, variety, winery, tasterName, tasterTwitter);
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
     * Setter for id.
     * @param id The id of the wine
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for name.
     * @param name The name of the wine
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Setter for description.
     * @param description The description of the wine
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Setter for price.
     * @param price The price of the wine
     */
    public void setPrice(int price)
    {
        this.price = price;
    }

    /**
     * Setter for points.
     * @param points The point value of the wine
     */
    public void setPoints(int points) {this.points = points;}
    /**
     * Setter for vintage.
     * @param vintage The vintage of the wine
     */
    public void setVintage(int vintage) {
        this.vintage = vintage;
    }

    /**
     * Setter for province.
     * @param province The province of the wine
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Setter for region.
     * @param region The region of the wine
     */
    public void setRegion(String region) {
        if (this.region1 == null) {
            this.region1 = region;
        } else {
            this.region2 = region;
        }
    }

    /**
     * Setter for variety.
     * @param variety The variety of the wine
     */
    public void setVariety(String variety) {
        this.variety = variety;
    }

    /**
     * Setter for winery.
     * @param winery The winery of the wine
     */
    public void setWinery(String winery) {
        this.winery = winery;
    }

    /**
     * Setter for tasterName.
     * @param tasterName The name of the taster
     */
    public void setTasterName(String tasterName) {
        this.tasterName = tasterName;
    }

    /**
     * Setter for tasterTwitter.
     * @param tasterTwitter The twitter of the taster
     */
    public void setTasterTwitter(String tasterTwitter) {
        this.tasterTwitter = tasterTwitter;
    }

    /**
     * Setter for country.
     * @param country The country of the wine
     */
    public void setCountry(String country) {
        this.country = country;
    }
}

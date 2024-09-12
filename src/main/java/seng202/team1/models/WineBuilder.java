package seng202.team1.models;

public class WineBuilder {
    private int id;
    private String name = null;
    private String description = null;
    private int price = -1;
    private int vintage = -1;

    private String country = null;
    private String province = null;
    private String region1 = null;
    private String region2 = null;

    private String variety = null;

    private String winery = null;

    private String tasterName = null;
    private String tasterTwitter = null;

    /**
     * Returns the built Wine object using the values stored in this class as arguments
     * Returns null if name is not assigned
     *
     * @return {@link Wine} the built wine object
     */
    public Wine build() {
        if (name == null || name.isEmpty()) {
            return null;
        }

        return new Wine(id, name, description, price, vintage, country, province, region1, region2, variety, winery, tasterName, tasterTwitter);
    }

    /**
     * Basic Setup for WineBuilder taking in arguments found in the wine
     *
     * @return {@link WineBuilder} with id, name, description and price setup
     */
    public static WineBuilder generaicSetup(int id, String name, String description, int price)
    {
        WineBuilder builder = new WineBuilder();
        builder.id = id;
        builder.name = name;
        builder.description = description;
        builder.price = price;
        return builder;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public void setVintage(int vintage) {
        this.vintage = vintage;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setRegion(String region) {
        if (this.region1 == null) {
            this.region1 = region;
        } else {
            this.region2 = region;
        }
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public void setWinery(String winery) {
        this.winery = winery;
    }

    public void setTasterName(String tasterName) {
        this.tasterName = tasterName;
    }

    public void setTasterTwitter(String tasterTwitter) {
        this.tasterTwitter = tasterTwitter;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

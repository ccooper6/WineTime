package seng202.team0.models;

/**
 * The model for the Wine Object.
 *<br><br>
 *Created by the page service after data is filtered from database
 * and then displayed via the PageController
 */

public class Wine {
    private int wineId;
    /**
     * The name of the wine.
     */
    private String name;
    /**
     * The description of the wine.
     */
    private String description;
    /**
     * The price of the wine.
     */
    private int price;
    /**
     * The vintage of the wine
     */
    private int vintage;
    /**
     * The province of the wine.
     */
    private String country;
    private String province;
    /**
     * The main region of the wine.
     */
    private String region1;
    /**
     * The secondary region of the wine.
     */
    private String region2;
    /**
     * The variety of the wine.
     */
    private String variety;
    /**
     * The winery that produced the wine.
     */
    private String winery;
    /**
     * The taster of the wine that wrote the description.
     */
    private String tasterName;
    /**
     * The taster's Twitter username.
     */
    private String tasterTwitter;
    /**
     * The wine id of the wine in the db
     */

    /**
     *The constructor for the Wine object.
     * <br><br>
     * Takes in the mentioned values.
     * @param name String {@link Wine#name}
     * @param description String {@link Wine#description}
     * @param price int {@link Wine#price}
     * @param vintage int {@link Wine#vintage}
     * @param country String
     * @param province String {@link Wine#province}
     * @param region1 String {@link Wine#region1}
     * @param region2 String {@link Wine#region2}
     * @param variety String {@link Wine#variety}
     * @param winery String {@link Wine#winery}
     * @param tasterName String {@link Wine#tasterName}
     * @param tasterTwitter String {@link Wine#tasterTwitter}
     * @param wineId int {@link Wine#wineId}
     */
    public Wine(int wineId, String name, String description, int price, int vintage, String country, String province, String region1, String region2,
                String variety, String winery, String tasterName, String tasterTwitter) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.vintage = vintage;
        this.country = country;
        this.province = province;
        this.region1 = region1;
        this.region2 = region2;
        this.variety = variety;
        this.winery = winery;
        this.tasterName = tasterName;
        this.tasterTwitter = tasterTwitter;
        this.wineId = wineId;
    }

    public Wine() {
        this.name = "";
        this.description = "";
        this.price = 0;
        this.vintage = 0;
        this.province = "";
        this.region1 = "";
        this.region2 = "";
        this.variety = "";
        this.winery = "";
        this.tasterName = "";
        this.tasterTwitter = "";
    }

    /**
     * Getter for the name of the wine.
     * @return {@link Wine#name}
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the wine.
     * @param name String value for the wine's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the string description of the wine.
     * @return {@link Wine#description}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the wine.
     * @param description String description of the wine
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the int price of the wine.
     * @return {@link Wine#price}
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets the price of the wine to the int parameter.
     * @param price Integer price
     */
    public void setPrice(int price) {
        this.price = price;
    }



    /**
     * Getter for the int vintage of the wine.
     * @return {@link Wine#vintage}
     */
    public int getVintage() {
        return vintage;
    }

    /**
     * Sets the price of the wine to the int parameter.
     * @param vintage Integer vintage
     */
    public void setVintage(int vintage) {
        this.vintage = vintage;
    }

    /**
     * Getter for the String name of the province of the wine.
     * @return {@link Wine#province}
     */
    public String getProvince() {
        return province;
    }

    /**
     * Sets the String name of province of the wine.
     * @param province String name of province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Getter for the main region of the wine.
     * @return {@link Wine#region1}
     */
    public String getRegion1() {
        return region1;
    }

    /**
     * Sets the wine's main region to the String parameter.
     * @param region1 region String name
     */
    public void setRegion1(String region1) {
        this.region1 = region1;
    }

    /**
     * Getter for the wine's secondary region.
     * @return {@link Wine#region2}
     */
    public String getRegion2() {
        return region2;
    }

    /**
     * Sets the wine's secondary region to the String parameter.
     * @param region2 name of region
     */
    public void setRegion2(String region2) {
        this.region2 = region2;
    }

    /**
     * Getter for the wine's variety.
     * @return {@link Wine#variety}
     */
    public String getVariety() {
        return variety;
    }

    /**
     * Sets the variety of the wine to the String parameter.
     * @param variety String variety of the wine
     */
    public void setVariety(String variety) {
        this.variety = variety;
    }

    /**
     * Getter for the wine's winery.
     * @return {@link Wine#winery}
     */
    public String getWinery() {
        return winery;
    }

    /**
     * Sets the winery's name to the String parameter.
     * @param winery Winery name
     */
    public void setWinery(String winery) {
        this.winery = winery;
    }

    /**
     * Getter for the wine taster's name.
     * @return {@link Wine#tasterName}
     */
    public String getTasterName() {
        return tasterName;
    }

    /**
     * Sets the wine taster's name to the String parameter.
     * @param tasterName name of wine taster
     */
    public void setTasterName(String tasterName) {
        this.tasterName = tasterName;
    }

    /**
     * Getter for the wine's taster's twitter handle.
     * @return {@link Wine#tasterTwitter}
     */
    public String getTasterTwitter() {
        return tasterTwitter;
    }

    /**
     * Sets the wine taster's twitter handle to the String parameter.
     * @param tasterTwitter Wine taster's twitter handle
     */
    public void setTasterTwitter(String tasterTwitter) {
        this.tasterTwitter = tasterTwitter;
    }

    public String getImagePath() {
        String imagePath = "";
        if (variety == null) {
            imagePath = "/images/wine-bottle_pic.png";
        } else {
            switch (variety) {
                case "Red":
                    imagePath = "/images/Red Wine.jpg";
                    break;
                case "White":
                    imagePath = "/images/White Wine.jpg";
                    break;
                case "Rosé":
                    imagePath = "/images/Rose Wine.jpg";
                    break;
                default:
                    imagePath = "/images/wine-bottle_pic.png";
                    break;
            }
        }
        return getClass().getResource(imagePath).toExternalForm();
    }

    /**
     * Returns the wine's wine id
     * @return wine id
     */
    public int getWineId() {
        return this.wineId;
    }
}

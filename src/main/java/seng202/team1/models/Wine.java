package seng202.team1.models;

import seng202.team1.services.WineVarietyService;

import java.text.Normalizer;

/**
 * The model for the Wine Object.
 *<br><br>
 *Created by the page service after data is filtered from database
 * and then displayed via the PageController
 * @author Wen Sheng Thong
 */

public class Wine {
    //TODO are we doing tasters?

    private final int wineId;
    private String name;
    private String description;
    private int price;
    private int vintage;
    private String country;
    private String province;
    private String region1;
    private String region2;
    private String variety;
    private String winery;
    private String tasterName;
    private String tasterTwitter;

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
    public Wine(int wineId, String name, String description, int price, int vintage, String country, String province, String region1,
                String region2, String variety, String winery, String tasterName, String tasterTwitter) {
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
     * Getter for the String name of the country of the wine.
     * @return {@link Wine#country}
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the String name of country of the wine.
     * @param country String name of country
     */

    public void setCountry(String country) {
        this.country = country;
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
     * Getter for the String name of the region of the wine.
     * @return {@link Wine#region1}
     */
    public String getRegion1() {
        return region1;
    }

    /**
     * Sets the String name of region of the wine.
     * @param region String name of region
     */
    public void setRegion1(String region) {
        this.region1 = region;
    }

    /**
     * Getter for the String name of the region of the wine.
     * @return {@link Wine#region1}
     */
    public String getRegion2() {
        return region2;
    }

    /**
     * Sets the String name of region of the wine.
     * @param region2 String name of region
     */
    public void setRegion2(String region2) {
        this.region2 = region2;
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

    /**
     * Gets the correct image path for the wine based on its variety.
     * @return the image path
     */
    public String getImagePath() {
        String imagePath;
        int variety = WineVarietyService.getInstance().getVarietyFromGrape(getVariety());
        switch (variety) {
            case 0:
                imagePath = "/images/Red Wine.jpg";
                break;
            case 1:
                imagePath =  "/images/White Wine.jpg";
                break;
            case 2:
                imagePath =  "/images/Rose Wine.jpg";
                break;
            case 3:
                imagePath =  "/images/Sparkling Wine.jpg";
                break;
            default:
                imagePath = "/images/wine-bottle_pic.png";
        }

        return getClass().getResource(imagePath).toExternalForm();
    }

    /**
     * Returns the wine's wine id.
     * @return wine id
     */
    public int getWineId() {
        return this.wineId;
    }

    /**
     * Returns true if any regions / province / country is equal
     * to the supplied string.
     * @param location A {@link String} for the location
     * @return true if the wine contains the string in a location parameter, false otherwise
     */
    public boolean hasLocation(String location)
    {
        location = Normalizer.normalize(location, Normalizer.Form.NFD).replaceAll("[^\\p{M}]", "").toLowerCase();

        boolean isTrue = false;

        // unfortunately cannot loop here since can't put null values into a list

        if (country != null) {
            String normalisedCountry = Normalizer.normalize(country, Normalizer.Form.NFD).replaceAll("[^\\p{M}]", "").toLowerCase();
            isTrue = isTrue || normalisedCountry.equals(location);
        }
        if (province != null) {
            String normalisedProvince = Normalizer.normalize(province, Normalizer.Form.NFD).replaceAll("[^\\p{M}]", "").toLowerCase();
            isTrue = isTrue || normalisedProvince.equals(location);
        }
        if (region1 != null) {
            String normalisedRegion1 = Normalizer.normalize(region1, Normalizer.Form.NFD).replaceAll("[^\\p{M}]", "").toLowerCase();
            isTrue = isTrue || normalisedRegion1.equals(location);
        }
        if (region2 != null) {
            String normalisedRegion2 = Normalizer.normalize(region2, Normalizer.Form.NFD).replaceAll("[^\\p{M}]", "").toLowerCase();
            isTrue = isTrue || normalisedRegion2.equals(location);
        }

        if (!isTrue) {
            System.out.println(name);
            System.out.println(province);
        }

        return isTrue;
    }

    /**
     * Returns whether the wine has a tag.
     * @param tag the tag to check for
     * @return true if any of the wines tags contains the tag given and false otherwise
     */
    public boolean hasTag(String tag)
    {
        tag = Normalizer.normalize(tag, Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();

        if (Integer.toString(vintage).equals(tag)) {
            return true;
        }

        if (country != null) {
            String checkCountry = Normalizer.normalize(country, Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();
            if (checkCountry.equals(tag)) {
                return true;
            }
        }
        if (province != null) {
            String checkProvince = Normalizer.normalize(province, Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();
            if (checkProvince.equals(tag)) {
                return true;
            }
        }
        if (region1 != null) {
            String checkRegion1 = Normalizer.normalize(region1, Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();
            if (checkRegion1.equals(tag)) {
                return true;
            }
        }
        if (region2 != null) {
            String checkRegion2 = Normalizer.normalize(region2, Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();
            if (checkRegion2.equals(tag)) {
                return true;
            }
        }
        if (variety != null) {
            // using \\{M} for regex here because ^\\{ASCII} removed the first character for unknown reasons
            String checkVariety = Normalizer.normalize(variety, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
            if (checkVariety.equals(tag)) {
                return true;
            }
        }
        if (winery != null) {
            String checkWinery = Normalizer.normalize(winery, Normalizer.Form.NFD).replaceAll("^\\p{M}", "").toLowerCase();
            if (checkWinery.equals(tag)) {
                return true;
            }
        }

        return false;
    }
}

package com.zawadz88.realestate.api.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * POJO containing real estate ad info.
 *
 * @author Piotr Zawadzki
 */
public class Ad implements Serializable {

    /**
     * Ad's unique identifier
     */
	private long adId;

    /**
     * Title of the ad
     */
	private String title;

    /**
     * Price of the ad
     */
	private String price;

    /**
     * Basic information about the ad, such as number of rooms, estate's surface, etc. Can contain HTML tags.
     */
    private String basicInfo;

    /**
     * Ad's description. Can contain HTML tags.
     */
    private String description;

    /**
     * Information about the agency/person to contact about the ad
     */
    private String contactInfo;

    /**
     * An array of image URLs attached to this ad
     */
    private String[] images;

    public long getAdId() {
        return adId;
    }

    public void setAdId(long adId) {
        this.adId = adId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(String basicInfo) {
        this.basicInfo = basicInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Ad{" +
                "adId=" + adId +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", basicInfo='" + basicInfo + '\'' +
                ", description='" + description + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", images=" + Arrays.toString(images) +
                '}';
    }
}

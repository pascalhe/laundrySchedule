package zhaw.ch.laundryschedule.models;

import java.util.ArrayList;
import java.util.List;

public class Location extends AbstractBaseModel {

    private String street;
    private String streetNumber;
    private int zipCode;
    private String city;
    private String country;

    public Location() {
        super();
    }

    public Location(String _street, String _streetNumber, int _zipCode, String _city, String _country) {
        super();
        street = _street;
        streetNumber = _streetNumber;
        zipCode = _zipCode;
        city = _city;
        country = _country;
    }

    public void setStreet(String _street) {
        street = _street;
    }

    public String getStreet() {
        return street;
    }

    public void setStreetNumber(String _streetNumber) {
        streetNumber = _streetNumber;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setZipCode(int _zipCode) {
        zipCode = _zipCode;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setCity(String _city) {
        city = _city;
    }

    public String getCity() {
        return city;
    }

    public void setCountry(String _country) {
        country = _country;
    }

    public String getCountry() {
        return country;
    }

}

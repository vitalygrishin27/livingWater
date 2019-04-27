package entity;

import javax.persistence.*;

@Entity
@Table(name = "adresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column (name = "country")
    private String country;
    @Column (name = "region")
    private String region;
    @Column (name = "district")
    private String district;
    @Column (name = "city")
    private String city;
    @Column (name = "phone")
    private String phone;


    public Address() {

    }


    public static Address createAddress(BuilderAddress builderAddress) {
        Address address = new Address();
        address.id = builderAddress.getId();
        address.country = builderAddress.getCountry();
        address.region = builderAddress.getRegion();
        address.district = builderAddress.getDistrict();
        address.city = builderAddress.getCity();
        address.phone = builderAddress.getPhone();
        return address;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

package entity;

public class BuilderAddress {
    private int id;
    private String country;
    private String region;
    private String district;
    private String city;
    private String phone;


    private BuilderAddress(){}

    public static BuilderAddress getBuilderAddress(){
        return new BuilderAddress();
    }

    public BuilderAddress setId(int id) {
        this.id = id;
        return this;
    }

    public BuilderAddress setCountry(String country) {
        this.country = country;
        return this;
    }

    public BuilderAddress setRegion(String region) {
        this.region = region;
        return this;
    }

    public BuilderAddress setDistrict(String district) {
        this.district = district;
        return this;
    }

    public BuilderAddress setCity(String city) {
        this.city = city;
        return this;
    }

    public BuilderAddress setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Address build(){
        return Address.createAddress(this);
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }
}

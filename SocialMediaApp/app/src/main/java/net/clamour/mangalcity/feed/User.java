
package net.clamour.mangalcity.feed;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User {

    @Expose
    private String address;
    @Expose
    private String city;
    @Expose
    private String country;
    @SerializedName("cover_image")
    private String coverImage;
    @SerializedName("created_at")
    private String createdAt;
    @Expose
    private String district;
    @Expose
    private String dob;
    @Expose
    private String email;
    @SerializedName("first_name")
    private String firstName;
    @Expose
    private String gender;
    @Expose
    private Long id;
    @Expose
    private String image;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("marital_status")
    private String maritalStatus;
    @Expose
    private String mobile;
    @Expose
    private String profession;
    @Expose
    private Long profile;
    @Expose
    private String state;
    @Expose
    private String status;
    @SerializedName("updated_at")
    private String updatedAt;
    @Expose
    private String url;
    @Expose
    private Long verified;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Long getProfile() {
        return profile;
    }

    public void setProfile(Long profile) {
        this.profile = profile;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getVerified() {
        return verified;
    }

    public void setVerified(Long verified) {
        this.verified = verified;
    }

    public String getFullName() {
        return getFirstName() +" "+getLastName();
    }

    @Override
    public String toString() {
        return "User{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", district='" + district + '\'' +
                ", dob='" + dob + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", gender='" + gender + '\'' +
                ", id=" + id +
                ", image='" + image + '\'' +
                ", lastName='" + lastName + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", mobile='" + mobile + '\'' +
                ", profession='" + profession + '\'' +
                ", profile=" + profile +
                ", state='" + state + '\'' +
                ", status='" + status + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", url='" + url + '\'' +
                ", verified=" + verified +
                '}';
    }
}

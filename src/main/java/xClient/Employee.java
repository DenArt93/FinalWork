package xClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {

    public Employee() {
    }

    int id;
    String firstName;
    String lastName;
    String middleName;
    int companyId;
    String email;
    String avatar_url;
    String phone;
    String birthdate;
    Boolean isActive;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return avatar_url;
    }

    public void setUrl(String url) {
        this.avatar_url = avatar_url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


    public Employee(String birthdate, String avatar_url, int companyId) {
        Faker faker = new Faker();
        this.firstName = faker.name().firstName();
        this.lastName = faker.name().lastName();
        this.middleName = faker.name().username();
        this.phone = faker.phoneNumber().subscriberNumber();
        this.email = faker.internet().emailAddress();
        this.birthdate = birthdate;
        this.avatar_url = avatar_url;
        this.companyId = companyId;
        this.isActive = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id && companyId == employee.companyId && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(middleName, employee.middleName) && Objects.equals(email, employee.email) && Objects.equals(avatar_url, employee.avatar_url) && Objects.equals(phone, employee.phone) && Objects.equals(birthdate, employee.birthdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, middleName, companyId, email, avatar_url, phone, birthdate);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", companyId=" + companyId +
                '}';
    }

    public String getCreateDateTime() {
        return LocalDateTime.now().toString();
    }

    public String getLastChangedDateTime() {
        return LocalDateTime.now().toString();
    }

    public String getAvatar_url() {
        return avatar_url;
    }
}

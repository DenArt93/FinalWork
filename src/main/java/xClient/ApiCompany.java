package xClient;

import com.github.javafaker.Faker;

import java.util.Objects;

public class ApiCompany {

    private String name;
    private String description;

    public ApiCompany() {
        Faker faker = new Faker();
        this.name = faker.company().name();
        this.description = faker.company().bs(); // .bs() generates buzzwords, which can serve as a company description
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiCompany company = (ApiCompany) o;
        return Objects.equals(name, company.name) && Objects.equals(description, company.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

package session2;

import java.util.List;

public class RequestPayloadPOJO {

    private String id;
    private String name;
    private String location;
    private String phone;
    private List courses;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List getCourses() {
        return courses;
    }

    public void setCourses(List courses) {
        this.courses = courses;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

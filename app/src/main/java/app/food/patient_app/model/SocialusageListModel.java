package app.food.patient_app.model;

public class SocialusageListModel {

    String name;
    Long usage;

    public SocialusageListModel(String name, Long usage) {
        this.name = name;
        this.usage = usage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUsage() {
        return usage;
    }

    public void setUsage(Long usage) {
        this.usage = usage;
    }
}

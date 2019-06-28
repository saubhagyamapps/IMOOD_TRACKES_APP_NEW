package app.food.patient_app.model;

import java.util.List;

public class RemainingCallModel {

    /**
     * status : 0
     * result : ["2019-03-12","2019-03-13","2019-03-14","2019-03-15"]
     */

    private String status;
    private List<String> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}

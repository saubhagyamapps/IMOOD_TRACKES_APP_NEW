package app.food.patient_app.model;

public class InsertStepCountModel {

    /**
     * status : 0
     * message : Successfully Updated
     * foot_step : 2254
     */

    private int status;
    private String message;
    private String foot_step;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFoot_step() {
        return foot_step;
    }

    public void setFoot_step(String foot_step) {
        this.foot_step = foot_step;
    }
}

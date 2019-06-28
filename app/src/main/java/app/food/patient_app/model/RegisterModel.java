package app.food.patient_app.model;

public class RegisterModel {


    /**
     * status : 0
     * message : signup successful
     * id : 3
     */

    private String status;
    private String message;
    private String id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

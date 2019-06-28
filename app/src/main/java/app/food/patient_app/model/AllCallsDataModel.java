package app.food.patient_app.model;

public class AllCallsDataModel {


    /**
     * status : 0
     * total_call : 3
     * total_missedcall : 0
     * total_callduration : 216
     */

    private String status;
    private String total_call;
    private String total_missedcall;
    private String total_callduration;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_call() {
        return total_call;
    }

    public void setTotal_call(String total_call) {
        this.total_call = total_call;
    }

    public String getTotal_missedcall() {
        return total_missedcall;
    }

    public void setTotal_missedcall(String total_missedcall) {
        this.total_missedcall = total_missedcall;
    }

    public String getTotal_callduration() {
        return total_callduration;
    }

    public void setTotal_callduration(String total_callduration) {
        this.total_callduration = total_callduration;
    }
}

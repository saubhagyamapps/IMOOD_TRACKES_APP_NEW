package app.food.patient_app.model;

public class SocialItegrationModel {

    /**
     * status : 0
     * image_count : 6
     * total_sms_count : 1
     * screenlockout_count : 1
     * countuserdata_count : 1
     * total_call_count : 24
     * missed_call_count : 2
     * total_callduration_count : 802
     */

    private int status;
    private int image_count;
    private int total_sms_count;
    private int screenlockout_count;
    private int countuserdata_count;
    private int total_call_count;
    private int missed_call_count;
    private int total_callduration_count;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getImage_count() {
        return image_count;
    }

    public void setImage_count(int image_count) {
        this.image_count = image_count;
    }

    public int getTotal_sms_count() {
        return total_sms_count;
    }

    public void setTotal_sms_count(int total_sms_count) {
        this.total_sms_count = total_sms_count;
    }

    public int getScreenlockout_count() {
        return screenlockout_count;
    }

    public void setScreenlockout_count(int screenlockout_count) {
        this.screenlockout_count = screenlockout_count;
    }

    public int getCountuserdata_count() {
        return countuserdata_count;
    }

    public void setCountuserdata_count(int countuserdata_count) {
        this.countuserdata_count = countuserdata_count;
    }

    public int getTotal_call_count() {
        return total_call_count;
    }

    public void setTotal_call_count(int total_call_count) {
        this.total_call_count = total_call_count;
    }

    public int getMissed_call_count() {
        return missed_call_count;
    }

    public void setMissed_call_count(int missed_call_count) {
        this.missed_call_count = missed_call_count;
    }

    public int getTotal_callduration_count() {
        return total_callduration_count;
    }

    public void setTotal_callduration_count(int total_callduration_count) {
        this.total_callduration_count = total_callduration_count;
    }
}

package app.food.patient_app.model;



public class CalllogsListModel {

    String name;
    String phone_number;
    String call_type;
    String call_date;
    String call_time;
    String call_duration;

    public String getCall_time() {
        return call_time;
    }

    public void setCall_time(String call_time) {
        this.call_time = call_time;
    }



    public CalllogsListModel(String name, String phone_number, String call_type, String call_date,String call_time, String call_duration) {
        this.name = name;
        this.phone_number = phone_number;
        this.call_type = call_type;
        this.call_date = call_date;
        this.call_time = call_time;
        this.call_duration = call_duration;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCall_type() {
        return call_type;
    }

    public void setCall_type(String call_type) {
        this.call_type = call_type;
    }

    public String getCall_date() {
        return call_date;
    }

    public void setCall_date(String call_date) {
        this.call_date = call_date;
    }

    public String getCall_duration() {
        return call_duration;
    }

    public void setCall_duration(String call_duration) {
        this.call_duration = call_duration;
    }

}

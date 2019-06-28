package app.food.patient_app.model;

import java.util.List;

public class GetCaloriesModel {


    /**
     * status : 0
     * message : data avaliable
     * result : [{"usermanagement_id":2,"date":"2019-03-13","calories":"rtyrtyrtyryry"}]
     */

    private String status;
    private String message;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * usermanagement_id : 2
         * date : 2019-03-13
         * calories : rtyrtyrtyryry
         */

        private int usermanagement_id;
        private String date;
        private String calories;

        public int getUsermanagement_id() {
            return usermanagement_id;
        }

        public void setUsermanagement_id(int usermanagement_id) {
            this.usermanagement_id = usermanagement_id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getCalories() {
            return calories;
        }

        public void setCalories(String calories) {
            this.calories = calories;
        }
    }
}

package app.food.patient_app.model;

import java.util.List;

public class CaloriesDataModel {


    /**
     * status : 0
     * result : [{"id":137,"usermanagement_id":19,"date":"2019-03-20","calories":"561.4565"}]
     */

    private String status;
    private List<ResultBean> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 137
         * usermanagement_id : 19
         * date : 2019-03-20
         * calories : 561.4565
         */

        private int id;
        private int usermanagement_id;
        private String date;
        private String calories;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

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

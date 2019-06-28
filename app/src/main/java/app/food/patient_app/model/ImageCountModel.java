package app.food.patient_app.model;

import java.util.List;

public class ImageCountModel {


    /**
     * status : 0
     * result : [{"id":4,"usermanagement_id":19,"count":9,"date":"2019-03-18","total":"10"}]
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
         * id : 4
         * usermanagement_id : 19
         * count : 9
         * date : 2019-03-18
         * total : 10
         */

        private int id;
        private int usermanagement_id;
        private String count;
        private String date;
        private String total;

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

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
}

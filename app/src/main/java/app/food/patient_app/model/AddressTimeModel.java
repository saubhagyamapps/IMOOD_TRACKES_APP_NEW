package app.food.patient_app.model;

import java.util.List;

public class AddressTimeModel {


    /**
     * status : 0
     * result : [{"address":"Shivam Complex, Science City Rd, Sola, Ahmedabad, Gujarat 380060, India","time_difference":"01:21:18"}]
     * home_result : [{"address":"Shivam Complex, Science City Rd, Sola, Ahmedabad, Gujarat 380060, India","time_difference":"00:17:13"}]
     */

    private int status;
    private List<ResultBean> result;
    private List<HomeResultBean> home_result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public List<HomeResultBean> getHome_result() {
        return home_result;
    }

    public void setHome_result(List<HomeResultBean> home_result) {
        this.home_result = home_result;
    }

    public static class ResultBean {
        /**
         * address : Shivam Complex, Science City Rd, Sola, Ahmedabad, Gujarat 380060, India
         * time_difference : 01:21:18
         */

        private String address;
        private String time_difference;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTime_difference() {
            return time_difference;
        }

        public void setTime_difference(String time_difference) {
            this.time_difference = time_difference;
        }
    }

    public static class HomeResultBean {
        /**
         * address : Shivam Complex, Science City Rd, Sola, Ahmedabad, Gujarat 380060, India
         * time_difference : 00:17:13
         */

        private String address;
        private String time_difference;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTime_difference() {
            return time_difference;
        }

        public void setTime_difference(String time_difference) {
            this.time_difference = time_difference;
        }
    }
}

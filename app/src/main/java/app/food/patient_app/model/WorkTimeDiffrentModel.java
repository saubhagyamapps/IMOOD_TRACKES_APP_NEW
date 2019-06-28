package app.food.patient_app.model;

import java.util.List;

public class WorkTimeDiffrentModel  {


    /**
     * status : 0
     * result : [{"address":"Ahmedabad","time_difference":"00:00:16"}]
     */

    private int status;
    private List<ResultBean> result;

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

    public static class ResultBean {
        /**
         * address : Ahmedabad
         * time_difference : 00:00:16
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

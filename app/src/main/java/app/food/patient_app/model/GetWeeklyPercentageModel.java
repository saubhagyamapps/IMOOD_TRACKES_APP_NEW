package app.food.patient_app.model;

import java.util.List;

public class GetWeeklyPercentageModel {

    /**
     * status : 0
     * result : [{"date":"2019-04-12","socialtime":4,"worktime":100,"callduration":18},{"date":"2019-04-13","socialtime":8,"worktime":64,"callduration":0},{"date":"2019-04-14","socialtime":0,"worktime":0,"callduration":24},{"date":"2019-04-15","socialtime":14,"worktime":100,"callduration":16},{"date":"2019-04-16","socialtime":16,"worktime":70,"callduration":14},{"date":"2019-04-17","socialtime":10,"worktime":100,"callduration":1},{"date":"2019-04-18","socialtime":0,"worktime":0,"callduration":0}]
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
         * date : 2019-04-12
         * socialtime : 4
         * worktime : 100
         * callduration : 18
         */

        private String date;
        private int socialtime;
        private int worktime;
        private int callduration;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getSocialtime() {
            return socialtime;
        }

        public void setSocialtime(int socialtime) {
            this.socialtime = socialtime;
        }

        public int getWorktime() {
            return worktime;
        }

        public void setWorktime(int worktime) {
            this.worktime = worktime;
        }

        public int getCallduration() {
            return callduration;
        }

        public void setCallduration(int callduration) {
            this.callduration = callduration;
        }
    }
}

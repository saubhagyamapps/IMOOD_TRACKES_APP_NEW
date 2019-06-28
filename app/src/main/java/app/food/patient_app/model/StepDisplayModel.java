package app.food.patient_app.model;

import java.util.List;

public class StepDisplayModel {

    /**
     * status : 0
     * total_foot_step : 57730
     * foot_step_data : [{"date":"2019-04-26","foot_step":11000},{"date":"2019-04-27","foot_step":8000},{"date":"2019-04-28","foot_step":10000},{"date":"2019-04-29","foot_step":2500},{"date":"2019-04-30","foot_step":12066},{"date":"2019-05-01","foot_step":13188},{"date":"2019-05-02","foot_step":976}]
     */

    private int status;
    private int total_foot_step;
    private List<FootStepDataBean> foot_step_data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal_foot_step() {
        return total_foot_step;
    }

    public void setTotal_foot_step(int total_foot_step) {
        this.total_foot_step = total_foot_step;
    }

    public List<FootStepDataBean> getFoot_step_data() {
        return foot_step_data;
    }

    public void setFoot_step_data(List<FootStepDataBean> foot_step_data) {
        this.foot_step_data = foot_step_data;
    }

    public static class FootStepDataBean {
        /**
         * date : 2019-04-26
         * foot_step : 11000
         */

        private String date;
        private int foot_step;
        private int foot_move_minute;

        public int getFoot_move_minute() {
            return foot_move_minute;
        }

        public void setFoot_move_minute(int foot_move_minute) {
            this.foot_move_minute = foot_move_minute;
        }



        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getFoot_step() {
            return foot_step;
        }

        public void setFoot_step(int foot_step) {
            this.foot_step = foot_step;
        }
    }
}

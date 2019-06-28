package app.food.patient_app.model;

import java.util.List;

public class GetGooGleFitActivityModel {


    /**
     * status : 0
     * data : [{"usermanagement_id":4,"date":"2019-03-26","activity":"72","duration":"85356986","num_segments":"1"},{"usermanagement_id":4,"date":"2019-03-26","activity":"3","duration":"1036713","num_segments":"1"},{"usermanagement_id":4,"date":"2019-03-26","activity":"72","duration":"85356986","num_segments":"1"},{"usermanagement_id":4,"date":"2019-03-26","activity":"3","duration":"1036713","num_segments":"1"},{"usermanagement_id":4,"date":"2019-03-26","activity":"72","duration":"74300850","num_segments":"1"},{"usermanagement_id":4,"date":"2019-03-26","activity":"3","duration":"8273555","num_segments":"2"},{"usermanagement_id":4,"date":"2019-03-26","activity":"72","duration":"73992461","num_segments":"1"},{"usermanagement_id":4,"date":"2019-03-26","activity":"3","duration":"8273555","num_segments":"2"},{"usermanagement_id":4,"date":"2019-03-26","activity":"72","duration":"73931860","num_segments":"1"},{"usermanagement_id":4,"date":"2019-03-26","activity":"3","duration":"8273555","num_segments":"2"}]
     */

    private String status;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * usermanagement_id : 4
         * date : 2019-03-26
         * activity : 72
         * duration : 85356986
         * num_segments : 1
         */

        private int usermanagement_id;
        private String date;
        private String activity;
        private String duration;
        private String num_segments;

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

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getNum_segments() {
            return num_segments;
        }

        public void setNum_segments(String num_segments) {
            this.num_segments = num_segments;
        }
    }
}

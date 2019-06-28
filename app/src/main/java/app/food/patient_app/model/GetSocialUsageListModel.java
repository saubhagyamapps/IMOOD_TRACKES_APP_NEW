package app.food.patient_app.model;

import java.util.List;

public class GetSocialUsageListModel {

    /**
     * status : 0
     * result : [{"application":"Instagram","totalTime":319078}]
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
         * application : Instagram
         * totalTime : 319078
         */

        private String application;
        private long totalTime;

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }

        public long getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(long totalTime) {
            this.totalTime = totalTime;
        }
    }
}

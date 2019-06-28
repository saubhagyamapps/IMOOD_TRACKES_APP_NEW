package app.food.patient_app.model;

import java.util.List;

public class GetHomeLocationModel {


    /**
     * status : 0
     * result : [{"address":"Shivam Complex,sciencity,Ahemdabad","latitude":"23.076841","longitude":"72.508029"}]
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
         * address : Shivam Complex,sciencity,Ahemdabad
         * latitude : 23.076841
         * longitude : 72.508029
         */

        private String address;
        private String latitude;
        private String longitude;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }
}

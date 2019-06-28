package app.food.patient_app.model;

import java.util.List;

public class LoginWithGmailModel {

    /**
     * status : 0
     * messgae : login success
     * path : http://192.168.1.200/patient_apppublic/profile_image/
     * result : [{"id":2,"username":"testnew","image":"1552111409.jpg","gender":"Female","address":"ahemdabad","email":"test@gmail.com","mobile":"54545542112","firebase_id":"fay7jL_Plw0:APA91bGfjWvtFlQR-pfDSccuY5JdGCKob1lZ90BEqakiSvpG37bqE7FGlPl5zQMnAgEoCidqr3zcCKTNFZ1Rctu-afTeEq6eU4Q8NLH_B26WDvoUb6ywI41RiNVNTDYGmlxzlS4Qy4Qj","device_id":"c449633b6e6dd00"}]
     */

    private String status;
    private String messgae;
    private String path;
    private List<ResultBean> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessgae() {
        return messgae;
    }

    public void setMessgae(String messgae) {
        this.messgae = messgae;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 2
         * username : testnew
         * image : 1552111409.jpg
         * gender : Female
         * address : ahemdabad
         * email : test@gmail.com
         * mobile : 54545542112
         * firebase_id : fay7jL_Plw0:APA91bGfjWvtFlQR-pfDSccuY5JdGCKob1lZ90BEqakiSvpG37bqE7FGlPl5zQMnAgEoCidqr3zcCKTNFZ1Rctu-afTeEq6eU4Q8NLH_B26WDvoUb6ywI41RiNVNTDYGmlxzlS4Qy4Qj
         * device_id : c449633b6e6dd00
         */

        private String id;
        private String username;
        private String image;
        private String gender;
        private String address;
        private String email;
        private String mobile;
        private String firebase_id;
        private String device_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getFirebase_id() {
            return firebase_id;
        }

        public void setFirebase_id(String firebase_id) {
            this.firebase_id = firebase_id;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }
    }
}

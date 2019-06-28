package app.food.patient_app.model;

import java.util.List;

public class LoginWithFbModel {

    /**
     * status : 0
     * result : [{"id":22,"username":"hemuuuuu","image":"hemuuuuu","gender":null,"address":null,"email":"hemuuuuu","mobile":null,"password":null,"firebase_id":"abcd","device_id":"abc","fbid":"12313"}]
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
         * id : 22
         * username : hemuuuuu
         * image : hemuuuuu
         * gender : null
         * address : null
         * email : hemuuuuu
         * mobile : null
         * password : null
         * firebase_id : abcd
         * device_id : abc
         * fbid : 12313
         */

        private String id;
        private String username;
        private String image;
        private Object gender;
        private Object address;
        private String email;
        private Object mobile;
        private Object password;
        private String firebase_id;
        private String device_id;
        private String fbid;

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

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getMobile() {
            return mobile;
        }

        public void setMobile(Object mobile) {
            this.mobile = mobile;
        }

        public Object getPassword() {
            return password;
        }

        public void setPassword(Object password) {
            this.password = password;
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

        public String getFbid() {
            return fbid;
        }

        public void setFbid(String fbid) {
            this.fbid = fbid;
        }
    }
}

package app.food.patient_app.model;

import java.util.List;

public class EditProfileModel {

    /**
     * status : 0
     * path : http://www.charmhdwallpapers.com/patient_app/public/profile_image/
     * result : [{"user_id":55,"username":"kkk","email":"kkk@gmail.com","mobile":"1234567899","image":"15583502135ce28985bf6bd.jpg"}]
     */

    private int status;
    private String path;
    private List<ResultBean> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
         * user_id : 55
         * username : kkk
         * email : kkk@gmail.com
         * mobile : 1234567899
         * image : 15583502135ce28985bf6bd.jpg
         */

        private int user_id;
        private String username;
        private String email;
        private String mobile;
        private String image;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}

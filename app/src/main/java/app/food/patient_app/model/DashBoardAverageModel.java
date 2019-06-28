package app.food.patient_app.model;

public class DashBoardAverageModel {

    /**
     * status : 0
     * social_media_usage : 288
     * work : 0
     * callduration : 800
     * home : 1411
     */

    private int status;
    private int social_media_usage;
    private int work;
    private int callduration;
    private int home;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSocial_media_usage() {
        return social_media_usage;
    }

    public void setSocial_media_usage(int social_media_usage) {
        this.social_media_usage = social_media_usage;
    }

    public int getWork() {
        return work;
    }

    public void setWork(int work) {
        this.work = work;
    }

    public int getCallduration() {
        return callduration;
    }

    public void setCallduration(int callduration) {
        this.callduration = callduration;
    }

    public int getHome() {
        return home;
    }

    public void setHome(int home) {
        this.home = home;
    }
}

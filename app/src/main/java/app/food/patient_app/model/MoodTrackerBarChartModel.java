package app.food.patient_app.model;

public class MoodTrackerBarChartModel {

    /**
     * status : 0
     * good_mood : 25
     * rad_mood : 25
     * meh_mood : 17
     * bad_mood : 17
     * awful_mood : 17
     */

    private int status;
    private int good_mood;
    private int rad_mood;
    private int meh_mood;
    private int bad_mood;
    private int awful_mood;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGood_mood() {
        return good_mood;
    }

    public void setGood_mood(int good_mood) {
        this.good_mood = good_mood;
    }

    public int getRad_mood() {
        return rad_mood;
    }

    public void setRad_mood(int rad_mood) {
        this.rad_mood = rad_mood;
    }

    public int getMeh_mood() {
        return meh_mood;
    }

    public void setMeh_mood(int meh_mood) {
        this.meh_mood = meh_mood;
    }

    public int getBad_mood() {
        return bad_mood;
    }

    public void setBad_mood(int bad_mood) {
        this.bad_mood = bad_mood;
    }

    public int getAwful_mood() {
        return awful_mood;
    }

    public void setAwful_mood(int awful_mood) {
        this.awful_mood = awful_mood;
    }
}

package app.food.patient_app.model;

import java.util.List;

public class GetMoodNotesModel {

    /**
     * status : 0
     * result : [{"id":5,"date":"2019-03-12","time":"03:53:12","mode":"active","activities":"Music","notes":"happy"},{"id":8,"date":"2019-03-12","time":"12:46:26","mode":"null","activities":"Work","notes":"hiii"},{"id":9,"date":"2019-03-12","time":"12:53:56","mode":"null","activities":null,"notes":null},{"id":10,"date":"2019-03-12","time":"13:17:37","mode":"null","activities":"Work","notes":"gggff"},{"id":11,"date":"2019-03-12","time":"13:19:07","mode":"null","activities":"Relax","notes":"dbgkfj"},{"id":12,"date":"2019-03-12","time":"13:23:12","mode":"null","activities":"Relax","notes":"fyuvuv"},{"id":13,"date":"2019-03-12","time":"13:25:12","mode":"null","activities":null,"notes":null},{"id":14,"date":"2019-03-12","time":"13:29:40","mode":"null","activities":null,"notes":null},{"id":16,"date":"2019-03-12","time":"13:31:22","mode":"null","activities":null,"notes":null},{"id":19,"date":"2019-03-12","time":"14:26:28","mode":"null","activities":"Work","notes":"hello"},{"id":22,"date":"2019-03-12","time":"14:27:52","mode":"null","activities":"Work","notes":"hellll"},{"id":24,"date":"2019-03-12","time":"14:30:33","mode":null,"activities":"Work","notes":null},{"id":25,"date":"2019-03-12","time":"14:41:57","mode":null,"activities":"Work","notes":"ggh"},{"id":26,"date":"2019-03-12","time":"14:47:12","mode":null,"activities":"Work","notes":"dgsgsdfg"},{"id":27,"date":"2019-03-12","time":"15:24:53","mode":null,"activities":null,"notes":null},{"id":28,"date":"2019-03-12","time":"15:36:26","mode":"Happy","activities":"Work","notes":"hello"},{"id":29,"date":"2019-03-12","time":"16:08:28","mode":"Happy","activities":"Work","notes":"hello"},{"id":30,"date":"2019-03-12","time":"16:11:26","mode":"Very Happy","activities":"Work","notes":"Hello"},{"id":31,"date":"2019-03-12","time":"16:13:46","mode":"In Love","activities":"Relax","notes":"Hello hi"},{"id":32,"date":"2019-03-12","time":"16:14:58","mode":"In Love","activities":"Work","notes":"new notes"},{"id":34,"date":"2019-03-12","time":"16:38:49","mode":"In Love","activities":null,"notes":"new Notes"},{"id":35,"date":"2019-03-12","time":"16:40:18","mode":"Happy","activities":"Work","notes":"new notes"},{"id":36,"date":"2019-03-12","time":"16:41:40","mode":"Very Happy","activities":"Work","notes":"notes"},{"id":37,"date":"2019-03-12","time":"16:45:42","mode":"In Love","activities":"Work","notes":"Note"},{"id":38,"date":"2019-03-12","time":"16:47:12","mode":"In Love","activities":"Work","notes":"new"},{"id":40,"date":"2019-03-12","time":"17:05:57","mode":"Happy","activities":"Work","notes":"notes"},{"id":41,"date":"2019-03-12","time":"17:11:08","mode":"Very Happy","activities":"Work","notes":"notes"}]
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
         * id : 5
         * date : 2019-03-12
         * time : 03:53:12
         * mode : active
         * activities : Music
         * notes : happy
         */

        private int id;
        private String date;
        private String time;
        private String mode;
        private String activities;
        private String notes;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getActivities() {
            return activities;
        }

        public void setActivities(String activities) {
            this.activities = activities;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }
}

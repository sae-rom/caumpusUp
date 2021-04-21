package seoulsoft.kr.campusup.Model;

public class University {
    private String univ_name;
    private String univ_mail;
    private long localID;

    public String getUniv_name() {
        return univ_name;
    }

    public String getUniv_mail() {
        return univ_mail;
    }

    public long getLocalID() {
        return localID;
    }

    @Override
    public String toString() {
        return "University{" +
                "univ_name='" + univ_name + '\'' +
                '}';
    }

    public void setUniv_name(String univ_name) {
        this.univ_name = univ_name;
    }

    public void setUniv_mail(String univ_mail) {
        this.univ_mail = univ_mail;
    }

    public void setLocalID(long localID) {
        this.localID = localID;
    }

    public University(String univ_name, String univ_mail, long localID) {
        this.univ_name = univ_name;
        this.univ_mail = univ_mail;
        this.localID = localID;
    }

    public University(String univ_name) {
        this.univ_name = univ_name;
    }

    public University(String univ_name, String univ_mail ) {
        this.univ_name = univ_name;
        this.univ_mail = univ_mail;
    }

}

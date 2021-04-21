package seoulsoft.kr.campusup.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String documentID;
    private String nickname;
    private String email;
    private String profileUrl;

    public User(String documentID, String nickname, String email, String profileUrl) {
        this.documentID = documentID;
        this.nickname = nickname;
        this.email = email;
        this.profileUrl = profileUrl;
    }

    protected User(Parcel in) {
        documentID = in.readString();
        nickname = in.readString();
        email = in.readString();
        profileUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "documentID='" + documentID + '\'' +
                ", name='" + nickname + '\'' +
                ", studentID='" + email + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(documentID);
        parcel.writeString(nickname);
        parcel.writeString(email);
        parcel.writeString(profileUrl);
    }
}

package in.bloomboxkjsce.engistat;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Aniket on 24-06-2017
 */

@IgnoreExtraProperties
public class User {

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }


    private String aadhar;
    private String mobile;
    private String emailId;
    private String firstName;

    public String getMobile() {
        return mobile;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmailId() {
        return emailId;
    }

    private String lastName;
    private String collegeName;

    public User(){

    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setCollegeName(String collegeName) {this.collegeName = collegeName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

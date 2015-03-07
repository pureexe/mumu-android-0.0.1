package th.in.pureapp.mumu;


/**
 * Created by Pakkapon on 6/3/2558.
 */
public class MessageStructure {
    String user ="";
    String message ="";
    String id = null;
    MessageStructure(String who, String answer){
        user = who;
        message = answer;
    }
    MessageStructure(String who, String answer, String pic){
        user = who;
        message = answer;
        id = pic;
    }
    public void setUser(String who){
        user = who;
    }
    public void setMessage(String msg){
        message=msg;
    }
    public void setId(String b){
        id = b;
    }
    public String getUser(){
        return user;
    }
    public String getMessage(){
        return message;
    }
    public String getId(){
        return id;
    }
}

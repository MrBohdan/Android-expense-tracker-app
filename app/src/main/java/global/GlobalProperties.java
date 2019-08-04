package global;

import android.app.Application;

public class GlobalProperties extends Application {
    private int userToken;

    public int getUserTokenVariable() {
        return userToken;
    }

    public void setUserTokenVariable(int userToken) {
        this.userToken = userToken;
    }
}


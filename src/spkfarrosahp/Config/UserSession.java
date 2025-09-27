package spkfarrosahp.Config;

import spkfarrosahp.Model.Admin; // Pastikan import Admin

public class UserSession {
    private static Admin loggedInAdmin = null;
    private static String loggedInAdminNameDisplay = null;

    public static void login(Admin admin, String displayName) {
        loggedInAdmin = admin;
        loggedInAdminNameDisplay = displayName;
    }

    public static void logout() {
        loggedInAdmin = null;
        loggedInAdminNameDisplay = null;
    }

    public static boolean isLoggedIn() {
        return loggedInAdmin != null;
    }

    public static Admin getLoggedInAdmin() {
        return loggedInAdmin;
    }

    public static String getLoggedInAdminNameDisplay() {
        return loggedInAdminNameDisplay;
    }
}
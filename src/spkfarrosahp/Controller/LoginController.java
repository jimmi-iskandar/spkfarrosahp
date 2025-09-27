package spkfarrosahp.Controller;

import spkfarrosahp.Model.Admin;
import spkfarrosahp.Model.AdminDAO;
import spkfarrosahp.View.DashboardForm;
import spkfarrosahp.View.LoginForm;
import spkfarrosahp.Config.UserSession;
import org.mindrot.jbcrypt.BCrypt;
// Hapus import java.sql.SQLException; JIKA AdminDAO tidak lagi melemparnya

public class LoginController {
    private LoginForm viewLogin;
    private AdminDAO adminDAO;

    public LoginController(LoginForm view) {
        this.viewLogin = view;
        this.adminDAO = new AdminDAO();
    }

    public void prosesLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            viewLogin.showErrorMessage("Username dan Password tidak boleh kosong!", "Error Login");
            if (username.isEmpty()) viewLogin.requestUsernameFocus();
            else viewLogin.requestPasswordFocus();
            return;
        }

        try { // Blok try ini sekarang mungkin hanya untuk Exception umum
            Admin admin = adminDAO.getAdminByUsername(username); // Asumsi ini tidak throws SQLException lagi

            if (admin != null) {
                if (BCrypt.checkpw(password, admin.getPassword())) {
                    String adminNameToDisplay = admin.getNamaLengkap();
                    if (adminNameToDisplay == null || adminNameToDisplay.trim().isEmpty()) {
                        adminNameToDisplay = admin.getUsername();
                    }
                    
                    UserSession.login(admin, adminNameToDisplay);
                    
                    viewLogin.showSuccessMessage("Login Berhasil! Selamat datang, " + adminNameToDisplay + ".", "Login Sukses");
                    
                    DashboardForm dashboard = new DashboardForm(adminNameToDisplay); 
                    dashboard.setVisible(true);
                    
                    viewLogin.dispose();
                } else {
                    viewLogin.showErrorMessage("Password salah!", "Error Login");
                    viewLogin.clearPasswordFields();
                    viewLogin.requestPasswordFocus();
                }
            } else {
                viewLogin.showErrorMessage("Username tidak ditemukan!", "Error Login");
                viewLogin.clearAllFields();
                viewLogin.requestUsernameFocus();
            }
        
        } catch (Exception e) { // Tangkap Exception umum jika ada error lain
            viewLogin.showErrorMessage("Terjadi kesalahan tak terduga saat login: " + e.getMessage(), "Error Aplikasi");
            e.printStackTrace();
        }
    }
}
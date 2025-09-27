package spkfarrosahp.Controller;

import spkfarrosahp.Model.Admin;
import spkfarrosahp.Model.AdminDAO;
import spkfarrosahp.View.LoginForm;
import spkfarrosahp.View.RegistrasiForm;
import java.sql.SQLException;

public class RegistrasiController {
    private RegistrasiForm view;
    private AdminDAO adminDAO;

    public RegistrasiController(RegistrasiForm view) {
        this.view = view;
        this.adminDAO = new AdminDAO();
    }

    // --- UBAH SIGNATURE METHOD INI ---
    public void prosesRegistrasi(String namaLengkap, String username, String email, String password, String konfirmasiPassword) {
        if (namaLengkap.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || konfirmasiPassword.isEmpty()) {
            view.showErrorMessage("Semua field harus diisi!", "Error Registrasi");
            // Anda bisa tambahkan logika untuk fokus ke field yang kosong
            return;
        }
        // ... (validasi lain tetap sama) ...
        if (!password.equals(konfirmasiPassword)) {
            // ...
            return;
        }
        // ...

        try {
            if (adminDAO.isUsernameExists(username)) {
                view.showErrorMessage("Username '" + username + "' sudah digunakan. Pilih username lain.", "Error Registrasi");
                view.requestUsernameFocus();
                return;
            }
            // Cek Email jika di DAO sudah diimplementasikan dan di tabel email UNIQUE
            if (adminDAO.isEmailExists(email.trim())) {
                 view.showErrorMessage("Email '" + email + "' sudah terdaftar.", "Error Registrasi");
                 // Anda perlu method requestEmailFocus() di RegistrasiForm jika ingin fokus
                 return;
            }
            
            Admin newAdmin = new Admin(namaLengkap, username, password, email); // Kirim email ke konstruktor Admin
            
            if (adminDAO.saveAdmin(newAdmin)) { 
                view.showSuccessMessage("Registrasi Admin berhasil! Silakan login.", "Registrasi Sukses");
                kembaliKeLogin();
            } else {
                view.showErrorMessage("Registrasi gagal. Terjadi kesalahan saat menyimpan data.", "Error Registrasi");
            }
        } catch (SQLException e) {
            view.showErrorMessage("Terjadi kesalahan database: " + e.getMessage(), "Database Error");
            e.printStackTrace();
        } catch (Exception e) {
            view.showErrorMessage("Terjadi kesalahan tak terduga: " + e.getMessage(), "Error Aplikasi");
            e.printStackTrace();
        }
    }

    public void kembaliKeLogin() {
        view.dispose();
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
    }
}
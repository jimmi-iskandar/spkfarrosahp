package spkfarrosahp.Controller;

import spkfarrosahp.Model.Admin;
import spkfarrosahp.Model.AdminDAO;
import spkfarrosahp.View.PanelPengaturanAdmin;
import spkfarrosahp.Config.UserSession; // Untuk mendapatkan info admin yang login
import org.mindrot.jbcrypt.BCrypt;

public class PengaturanAdminController {
    private PanelPengaturanAdmin view;
    private AdminDAO adminDAO;
    private Admin loggedInAdmin; // Menyimpan objek admin yang sedang login

    public PengaturanAdminController(PanelPengaturanAdmin view) {
        this.view = view;
        this.adminDAO = new AdminDAO();
        loadLoggedInAdminData(); // Langsung muat data admin saat controller dibuat
    }

    public void loadLoggedInAdminData() {
        // Ambil admin yang sedang login dari UserSession
        this.loggedInAdmin = UserSession.getLoggedInAdmin(); 
        if (this.loggedInAdmin != null) {
            view.setUsername(this.loggedInAdmin.getUsername());
        } else {
            // Handle jika tidak ada admin yang login (seharusnya tidak terjadi jika panel ini diakses setelah login)
            view.showMessage("Error: Tidak ada data admin yang login.", true);
            view.setUsername("Tidak diketahui");
        }
    }

    public void ubahPassword(String passwordLama, String passwordBaru, String konfirmasiPasswordBaru) {
        if (loggedInAdmin == null) {
            view.showMessage("Sesi admin tidak ditemukan. Silakan login ulang.", true);
            return;
        }

        // Validasi input
        if (passwordLama.isEmpty() || passwordBaru.isEmpty() || konfirmasiPasswordBaru.isEmpty()) {
            view.showMessage("Semua field password harus diisi.", true);
            return;
        }

        // Verifikasi password lama
        // Perlu mengambil ulang data admin dari DB untuk password hash terbaru
        Admin currentAdminData = adminDAO.getAdminById(loggedInAdmin.getAdminId());
        if (currentAdminData == null || !BCrypt.checkpw(passwordLama, currentAdminData.getPassword())) {
            view.showMessage("Password lama salah.", true);
            return;
        }

        if (!passwordBaru.equals(konfirmasiPasswordBaru)) {
            view.showMessage("Password baru dan konfirmasi password baru tidak cocok.", true);
            return;
        }

        // Validasi tambahan untuk password baru (opsional)
        if (passwordBaru.length() < 6) { // Contoh: minimal 6 karakter
            view.showMessage("Password baru minimal harus 6 karakter.", true);
            return;
        }

        // Proses update password
        if (adminDAO.updatePassword(loggedInAdmin.getAdminId(), passwordBaru)) {
            view.showMessage("Password berhasil diubah.", false);
            view.clearPasswordFields(); // Kosongkan field setelah berhasil
            // Update password di UserSession jika perlu (meskipun admin object di UserSession punya hash lama)
            // Namun, untuk login berikutnya, data baru akan diambil.
        } else {
            view.showMessage("Gagal mengubah password. Terjadi kesalahan pada database.", true);
        }
    }
}
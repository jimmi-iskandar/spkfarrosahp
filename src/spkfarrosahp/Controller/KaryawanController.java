package spkfarrosahp.Controller;

import spkfarrosahp.Model.Karyawan;
import spkfarrosahp.Model.KaryawanDAO;
import spkfarrosahp.View.PanelKaryawan; // Asumsi PanelKaryawan ada dan memiliki method yang dibutuhkan
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class KaryawanController {
    private PanelKaryawan view;
    private KaryawanDAO dao;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy"); // Untuk format tanggal di tabel

    // Regex untuk validasi telepon (opsional, contoh sederhana: hanya angka, 10-15 digit)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10,15}$");

    public KaryawanController(PanelKaryawan view) {
        this.view = view;
        this.dao = new KaryawanDAO();
        loadData(); // Langsung muat data saat controller dibuat
    }

    public void loadData() {
        try {
            List<Karyawan> daftarKaryawan = dao.getAllKaryawan();
            view.displayDataInTable(daftarKaryawan);
        } catch (SQLException e) {
            view.showMessage("Gagal memuat data karyawan: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    public String generateNewKodeKaryawan(String jabatan) {
        String prefix = "";
        if (jabatan != null) {
            if (jabatan.equals("Screen Printing")) prefix = "SP";
            else if (jabatan.equals("Printing Support")) prefix = "PS";
        }
        if (prefix.isEmpty()) return ""; // Jika tidak ada prefix, kembalikan kosong

        String nomorUrutBerikutnya = "001";
        try {
            String kodeTerakhir = dao.getKodeKaryawanTerakhirByPrefix(prefix);
            if (kodeTerakhir != null && kodeTerakhir.length() > prefix.length()) {
                try {
                    int angka = Integer.parseInt(kodeTerakhir.substring(prefix.length()));
                    nomorUrutBerikutnya = String.format("%03d", angka + 1);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing nomor urut dari kode terakhir: " + kodeTerakhir + " - " + e.getMessage());
                    nomorUrutBerikutnya = "001";
                }
            } else {
                nomorUrutBerikutnya = "001";
            }
        } catch (SQLException e) {
            System.err.println("Error saat generate kode karyawan dari DB: " + e.getMessage());
            view.showMessage("Error DB saat generate kode: " + e.getMessage(), true);
            return prefix + "ERR"; // Beri kode error jika gagal query
        }
        return prefix + nomorUrutBerikutnya;
    }


    public void simpanKaryawan() {
        String kodeKaryawan = view.getKodeKaryawan();
        String namaLengkap = view.getNamaLengkap();
        String jabatan = view.getJabatan();
        Date tanggalLahir = view.getTanggalLahir();
        String alamat = view.getAlamat();
        String telepon = view.getTelepon();

        // Validasi dasar
        if (kodeKaryawan.isEmpty() || namaLengkap.isEmpty() || jabatan == null || jabatan.isEmpty()) {
            view.showMessage("Kode Karyawan, Nama Lengkap, dan Jabatan tidak boleh kosong.", true);
            return;
        }
        if (kodeKaryawan.endsWith("ERR")) {
            view.showMessage("Gagal generate Kode Karyawan. Cek koneksi database.", true);
            return;
        }
        if (!telepon.isEmpty() && !PHONE_PATTERN.matcher(telepon).matches()) {
             view.showMessage("Format nomor telepon tidak valid (harus 10-15 digit angka).", true);
             view.requestTeleponFocus(); // Minta view untuk fokus ke field telepon
             return;
        }


        Karyawan karyawan = new Karyawan(kodeKaryawan, namaLengkap, jabatan, tanggalLahir, alamat, telepon);

        try {
             // Cek apakah kode karyawan sudah ada (seharusnya tidak jika generate kode benar)
            if (dao.isKodeKaryawanExists(kodeKaryawan)) {
                view.showMessage("Kode Karyawan '" + kodeKaryawan + "' sudah ada. Kode seharusnya unik.", true);
                view.requestKodeKaryawanFocus(); // Minta view untuk fokus (meski read-only, ini indikasi)
                return;
            }
            
            if (dao.addKaryawan(karyawan)) {
                view.showMessage("Data karyawan berhasil disimpan.", false);
                loadData(); // Muat ulang data di tabel
                view.clearFieldsAndSelection(); // Bersihkan form
            } else {
                view.showMessage("Gagal menyimpan data karyawan.", true);
            }
        } catch (SQLException e) {
            view.showMessage("Error database saat menyimpan: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    public void updateKaryawan() {
        String kodeKaryawan = view.getKodeKaryawan(); // Kode karyawan tidak boleh diubah saat update
        String namaLengkap = view.getNamaLengkap();
        String jabatan = view.getJabatan();
        Date tanggalLahir = view.getTanggalLahir();
        String alamat = view.getAlamat();
        String telepon = view.getTelepon();

        if (kodeKaryawan.isEmpty()) { // Seharusnya terisi jika dari tabel
            view.showMessage("Pilih data dari tabel untuk diubah atau Kode Karyawan tidak valid.", true);
            return;
        }
        if (namaLengkap.isEmpty() || jabatan == null || jabatan.isEmpty()) {
            view.showMessage("Nama Lengkap dan Jabatan tidak boleh kosong.", true);
            return;
        }
         if (!telepon.isEmpty() && !PHONE_PATTERN.matcher(telepon).matches()) {
             view.showMessage("Format nomor telepon tidak valid (harus 10-15 digit angka).", true);
             view.requestTeleponFocus();
             return;
        }


        Karyawan karyawan = new Karyawan(kodeKaryawan, namaLengkap, jabatan, tanggalLahir, alamat, telepon);
        // karyawan.setKaryawanId(view.getSelectedKaryawanId()); // Jika ID diperlukan untuk update

        try {
            if (dao.updateKaryawan(karyawan)) {
                view.showMessage("Data karyawan berhasil diubah.", false);
                loadData();
                view.clearFieldsAndSelection();
            } else {
                view.showMessage("Gagal mengubah data karyawan atau data tidak ditemukan.", true);
            }
        } catch (SQLException e) {
            view.showMessage("Error database saat mengubah: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    public void hapusKaryawan() {
        String kodeKaryawan = view.getSelectedKodeKaryawanFromTable(); // Method ini perlu ada di PanelKaryawan
        if (kodeKaryawan == null || kodeKaryawan.isEmpty()) {
            view.showMessage("Pilih data karyawan dari tabel yang akan dihapus.", true);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                null, // Atau this.view jika PanelKaryawan adalah Component
                "Anda yakin ingin menghapus karyawan dengan kode: " + kodeKaryawan + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (dao.deleteKaryawan(kodeKaryawan)) {
                    view.showMessage("Data karyawan berhasil dihapus.", false);
                    loadData();
                    view.clearFieldsAndSelection();
                } else {
                    view.showMessage("Gagal menghapus data karyawan atau data tidak ditemukan.", true);
                }
            } catch (SQLException e) {
                view.showMessage("Error database saat menghapus: " + e.getMessage(), true);
                e.printStackTrace();
            }
        }
    }

    public void cariKaryawan(String keyword) {
        try {
            List<Karyawan> daftarKaryawan = dao.searchKaryawan(keyword);
            view.displayDataInTable(daftarKaryawan);
             if(daftarKaryawan.isEmpty()){
                view.showMessage("Data tidak ditemukan untuk keyword: '" + keyword + "'", false);
            }
        } catch (SQLException e) {
            view.showMessage("Error saat mencari data karyawan: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }
    
    public void isiFormDariTabel(String kodeKaryawan) {
        try {
            // Ambil data lengkap karyawan berdasarkan kodeKaryawan dari DAO (atau dari list yang sudah diload jika ada)
            // Ini contoh jika kita query lagi. Lebih efisien jika data sudah ada di list di controller/view
            List<Karyawan> allKaryawan = dao.getAllKaryawan(); // Atau dari pencarian
            Karyawan selectedKaryawan = null;
            for (Karyawan k : allKaryawan) {
                if (k.getKodeKaryawan().equals(kodeKaryawan)) {
                    selectedKaryawan = k;
                    break;
                }
            }

            if (selectedKaryawan != null) {
                view.setFormData(
                    selectedKaryawan.getKodeKaryawan(),
                    selectedKaryawan.getNamaLengkap(),
                    selectedKaryawan.getJabatan(),
                    selectedKaryawan.getTanggalLahir(),
                    selectedKaryawan.getAlamat(),
                    selectedKaryawan.getTelepon()
                );
                view.setTombolUpdateMode(true); // Ubah state tombol Simpan menjadi Update, dll.
            }
        } catch (SQLException e) {
            view.showMessage("Error mengambil data detail karyawan: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }
}
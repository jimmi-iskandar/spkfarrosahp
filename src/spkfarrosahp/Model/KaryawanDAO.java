package spkfarrosahp.Model;

import spkfarrosahp.Config.KoneksiDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Date; // Untuk java.util.Date

public class KaryawanDAO {

    // Mendapatkan semua karyawan
    public List<Karyawan> getAllKaryawan() throws SQLException {
        List<Karyawan> daftarKaryawan = new ArrayList<>();
        // --- PERUBAHAN DI SINI ---
        String sql = "SELECT * FROM karyawan ORDER BY karyawan_id ASC"; // Diurutkan berdasarkan karyawan_id
        // --- AKHIR PERUBAHAN ---
        try (Connection conn = KoneksiDatabase.configDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Karyawan karyawan = new Karyawan();
                karyawan.setKaryawanId(rs.getInt("karyawan_id"));
                karyawan.setKodeKaryawan(rs.getString("kode_karyawan"));
                karyawan.setNamaLengkap(rs.getString("nama_lengkap"));
                karyawan.setJabatan(rs.getString("jabatan"));
                karyawan.setTanggalLahir(rs.getDate("tanggal_lahir"));
                karyawan.setAlamat(rs.getString("alamat"));
                karyawan.setTelepon(rs.getString("telepon"));
                daftarKaryawan.add(karyawan);
            }
        }
        return daftarKaryawan;
    }

    // Menambahkan karyawan baru (Tidak ada perubahan di sini terkait urutan)
    public boolean addKaryawan(Karyawan karyawan) throws SQLException {
        String sql = "INSERT INTO karyawan (kode_karyawan, nama_lengkap, jabatan, tanggal_lahir, alamat, telepon) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, karyawan.getKodeKaryawan());
            pstmt.setString(2, karyawan.getNamaLengkap());
            pstmt.setString(3, karyawan.getJabatan());
            if (karyawan.getTanggalLahir() != null) {
                pstmt.setDate(4, new java.sql.Date(karyawan.getTanggalLahir().getTime()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }
            pstmt.setString(5, karyawan.getAlamat());
            pstmt.setString(6, karyawan.getTelepon());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Mengubah data karyawan (Tidak ada perubahan di sini terkait urutan)
    public boolean updateKaryawan(Karyawan karyawan) throws SQLException {
        String sql = "UPDATE karyawan SET nama_lengkap = ?, jabatan = ?, tanggal_lahir = ?, alamat = ?, telepon = ? WHERE kode_karyawan = ?";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, karyawan.getNamaLengkap());
            pstmt.setString(2, karyawan.getJabatan());
             if (karyawan.getTanggalLahir() != null) {
                pstmt.setDate(3, new java.sql.Date(karyawan.getTanggalLahir().getTime()));
            } else {
                pstmt.setNull(3, java.sql.Types.DATE);
            }
            pstmt.setString(4, karyawan.getAlamat());
            pstmt.setString(5, karyawan.getTelepon());
            pstmt.setString(6, karyawan.getKodeKaryawan());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Menghapus karyawan berdasarkan kode_karyawan (Tidak ada perubahan di sini terkait urutan)
    public boolean deleteKaryawan(String kodeKaryawan) throws SQLException {
        String sql = "DELETE FROM karyawan WHERE kode_karyawan = ?";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kodeKaryawan);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    // Mencari karyawan berdasarkan keyword (bisa kode atau nama)
    public List<Karyawan> searchKaryawan(String keyword) throws SQLException {
        List<Karyawan> daftarKaryawan = new ArrayList<>();
        // --- PERUBAHAN DI SINI ---
        String sql = "SELECT * FROM karyawan WHERE kode_karyawan LIKE ? OR nama_lengkap LIKE ? ORDER BY karyawan_id ASC"; // Diurutkan berdasarkan karyawan_id
        // --- AKHIR PERUBAHAN ---
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Karyawan karyawan = new Karyawan();
                    karyawan.setKaryawanId(rs.getInt("karyawan_id"));
                    karyawan.setKodeKaryawan(rs.getString("kode_karyawan"));
                    karyawan.setNamaLengkap(rs.getString("nama_lengkap"));
                    karyawan.setJabatan(rs.getString("jabatan"));
                    karyawan.setTanggalLahir(rs.getDate("tanggal_lahir"));
                    karyawan.setAlamat(rs.getString("alamat"));
                    karyawan.setTelepon(rs.getString("telepon"));
                    daftarKaryawan.add(karyawan);
                }
            }
        }
        return daftarKaryawan;
    }

    // Mendapatkan kode karyawan terakhir berdasarkan prefix (Tidak ada perubahan di sini)
    public String getKodeKaryawanTerakhirByPrefix(String prefix) throws SQLException {
        String kodeTerakhir = null;
        String sql = "SELECT kode_karyawan FROM karyawan WHERE kode_karyawan LIKE ? ORDER BY kode_karyawan DESC LIMIT 1";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, prefix + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    kodeTerakhir = rs.getString("kode_karyawan");
                }
            }
        }
        return kodeTerakhir;
    }
    
    // Cek apakah kode karyawan sudah ada (Tidak ada perubahan di sini)
    public boolean isKodeKaryawanExists(String kodeKaryawan) throws SQLException {
        String sql = "SELECT COUNT(*) FROM karyawan WHERE kode_karyawan = ?";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kodeKaryawan);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
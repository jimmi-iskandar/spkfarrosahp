package spkfarrosahp.Model;

import spkfarrosahp.Config.KoneksiDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlternatifDAO {

    // Method untuk mengambil semua data Karyawan (sebagai Alternatif)
    // Ini bisa jadi duplikat dengan KaryawanDAO.getAllKaryawan(),
    // pertimbangkan untuk memanggil method KaryawanDAO jika sudah ada dan sesuai.
    // Untuk sekarang, kita buat di sini agar modul Alternatif lebih mandiri.
    public List<Karyawan> getAllKaryawanAlternatif() throws SQLException {
        List<Karyawan> daftarKaryawan = new ArrayList<>();
        // Mengambil data yang relevan untuk ditampilkan sebagai alternatif
        String sql = "SELECT karyawan_id, kode_karyawan, nama_lengkap FROM karyawan ORDER BY karyawan_id ASC";
        try (Connection conn = KoneksiDatabase.configDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Karyawan karyawan = new Karyawan();
                karyawan.setKaryawanId(rs.getInt("karyawan_id"));
                karyawan.setKodeKaryawan(rs.getString("kode_karyawan"));
                karyawan.setNamaLengkap(rs.getString("nama_lengkap"));
                // Tidak perlu mengambil semua detail karyawan, hanya yang penting untuk identifikasi alternatif
                daftarKaryawan.add(karyawan);
            }
        }
        return daftarKaryawan;
    }

    // Method untuk mengambil semua data Kriteria (untuk ComboBox Kriteria Acuan)
    // Ini juga bisa jadi duplikat dengan KriteriaDAO.getAllKriteria().
    public List<Kriteria> getAllKriteriaUntukAcuan() throws SQLException {
        List<Kriteria> daftarKriteria = new ArrayList<>();
        String sql = "SELECT kriteria_id, kode_kriteria, nama_kriteria FROM kriteria ORDER BY kode_kriteria ASC";
        try (Connection conn = KoneksiDatabase.configDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Kriteria kriteria = new Kriteria();
                kriteria.setKriteriaId(rs.getInt("kriteria_id"));
                kriteria.setKodeKriteria(rs.getString("kode_kriteria"));
                kriteria.setNamaKriteria(rs.getString("nama_kriteria"));
                // Tidak perlu bobot di sini, hanya untuk pilihan acuan
                daftarKriteria.add(kriteria);
            }
        }
        return daftarKriteria;
    }

    // Method untuk menyimpan atau memperbarui nilai bobot lokal alternatif per kriteria
    // ke tabel penilaian_alternatif.
    // Menggunakan INSERT ... ON DUPLICATE KEY UPDATE untuk menangani insert baru atau update jika sudah ada.
    public boolean saveOrUpdatePenilaianAlternatif(List<PenilaianAlternatif> daftarPenilaian) throws SQLException {
        // Pastikan kolom di tabel penilaian_alternatif adalah karyawan_id, kriteria_id, nilai
        String sql = "INSERT INTO penilaian_alternatif (karyawan_id, kriteria_id, nilai) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE nilai = VALUES(nilai)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        try {
            conn = KoneksiDatabase.configDB();
            conn.setAutoCommit(false); // Mulai transaksi
            pstmt = conn.prepareStatement(sql);

            for (PenilaianAlternatif pa : daftarPenilaian) {
                pstmt.setInt(1, pa.getKaryawanId());
                pstmt.setInt(2, pa.getKriteriaId());
                pstmt.setDouble(3, pa.getNilai());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit(); // Commit transaksi
            success = true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback jika ada error
                } catch (SQLException ex) {
                    System.err.println("Error saat rollback: " + ex.getMessage());
                }
            }
            throw e; // Lemparkan exception asli
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Kembalikan ke auto commit
                } catch (SQLException ex) {
                     System.err.println("Error saat reset autocommit: " + ex.getMessage());
                }
                conn.close();
            }
        }
        return success;
    }
    
    // Method untuk mengambil nilai penilaian yang sudah ada untuk satu karyawan pada semua kriteria
    // Berguna jika ingin menampilkan nilai yang sudah ada saat memilih karyawan dan kriteria
    public List<PenilaianAlternatif> getPenilaianForKaryawan(int karyawanId) throws SQLException {
        List<PenilaianAlternatif> penilaianList = new ArrayList<>();
        String sql = "SELECT kriteria_id, nilai FROM penilaian_alternatif WHERE karyawan_id = ?";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, karyawanId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PenilaianAlternatif pa = new PenilaianAlternatif();
                    pa.setKaryawanId(karyawanId);
                    pa.setKriteriaId(rs.getInt("kriteria_id"));
                    pa.setNilai(rs.getDouble("nilai"));
                    penilaianList.add(pa);
                }
            }
        }
        return penilaianList;
    }

    // Method untuk mengambil nilai perbandingan antar alternatif untuk satu kriteria tertentu
    // (Jika Anda menyimpan perbandingan AHP antar alternatif di database terpisah, mirip perbandingan_kriteria)
    // Namun, berdasarkan desain tabel `penilaian_alternatif` kita, kita langsung simpan hasil bobot lokalnya.
    // Jadi method ini mungkin tidak relevan jika AHP alternatif dihitung on-the-fly dan hasilnya
    // langsung disimpan sebagai 'nilai' di `penilaian_alternatif`.
    // Jika Anda ingin menyimpan matriks perbandingan alternatif per kriteria, Anda perlu tabel baru.
}
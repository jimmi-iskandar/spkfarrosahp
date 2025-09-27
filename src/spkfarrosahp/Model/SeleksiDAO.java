package spkfarrosahp.Model;

import spkfarrosahp.Config.KoneksiDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp; // Untuk konversi ke Timestamp SQL
import java.util.ArrayList;
import java.util.List;

public class SeleksiDAO {

    // Method untuk menyimpan hasil seleksi ke tabel hasil_seleksi_akhir
    // Ini akan menghapus data lama dan memasukkan yang baru
    public boolean saveHasilSeleksi(List<HasilSeleksi> daftarHasil) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmtDelete = null;
        PreparedStatement pstmtInsert = null;
        boolean success = false;

        String sqlDelete = "DELETE FROM hasil_seleksi_akhir";
        String sqlInsert = "INSERT INTO hasil_seleksi_akhir (karyawan_id, nilai_total_spk, peringkat, tanggal_perhitungan) VALUES (?, ?, ?, ?)";

        try {
            conn = KoneksiDatabase.configDB();
            conn.setAutoCommit(false); // Mulai transaksi

            // 1. Hapus semua data lama
            pstmtDelete = conn.prepareStatement(sqlDelete);
            pstmtDelete.executeUpdate();

            // 2. Masukkan data baru
            pstmtInsert = conn.prepareStatement(sqlInsert);
            for (HasilSeleksi hasil : daftarHasil) {
                pstmtInsert.setInt(1, hasil.getKaryawanId());
                pstmtInsert.setDouble(2, hasil.getNilaiTotalSpk());
                pstmtInsert.setInt(3, hasil.getPeringkat());
                pstmtInsert.setTimestamp(4, new Timestamp(System.currentTimeMillis())); // Tanggal perhitungan saat ini
                pstmtInsert.addBatch();
            }
            pstmtInsert.executeBatch();

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
            if (pstmtDelete != null) pstmtDelete.close();
            if (pstmtInsert != null) pstmtInsert.close();
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

    // Method untuk mengambil data hasil seleksi terakhir (semua karyawan yang ada di tabel)
    public List<HasilSeleksi> getAllHasilSeleksiTerakhir() throws SQLException {
        List<HasilSeleksi> daftarHasil = new ArrayList<>();
        // Mengambil juga kode dan nama karyawan untuk ditampilkan
        String sql = "SELECT hs.karyawan_id, k.kode_karyawan, k.nama_lengkap, hs.nilai_total_spk, hs.peringkat, hs.tanggal_perhitungan " +
                     "FROM hasil_seleksi_akhir hs " +
                     "JOIN karyawan k ON hs.karyawan_id = k.karyawan_id " +
                     "ORDER BY hs.peringkat ASC, hs.nilai_total_spk DESC"; // Urutkan berdasarkan peringkat

        try (Connection conn = KoneksiDatabase.configDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                HasilSeleksi hasil = new HasilSeleksi();
                hasil.setKaryawanId(rs.getInt("karyawan_id"));
                hasil.setKodeKaryawan(rs.getString("kode_karyawan"));
                hasil.setNamaKaryawan(rs.getString("nama_lengkap"));
                hasil.setNilaiTotalSpk(rs.getDouble("nilai_total_spk"));
                hasil.setPeringkat(rs.getInt("peringkat"));
                hasil.setTanggalPerhitungan(rs.getTimestamp("tanggal_perhitungan"));
                daftarHasil.add(hasil);
            }
        }
        return daftarHasil;
    }

    // (Opsional) Method untuk mengambil nilai alternatif per kriteria
    // Ini mungkin lebih baik ada di AlternatifDAO atau KriteriaDAO, tapi bisa juga di sini jika
    // SeleksiController membutuhkan data mentah ini.
    public List<PenilaianAlternatif> getAllPenilaianAlternatif() throws SQLException {
        List<PenilaianAlternatif> daftarPenilaian = new ArrayList<>();
        String sql = "SELECT karyawan_id, kriteria_id, nilai FROM penilaian_alternatif";
        try (Connection conn = KoneksiDatabase.configDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PenilaianAlternatif pa = new PenilaianAlternatif(
                        rs.getInt("karyawan_id"),
                        rs.getInt("kriteria_id"),
                        rs.getDouble("nilai")
                );
                daftarPenilaian.add(pa);
            }
        }
        return daftarPenilaian;
    }
}
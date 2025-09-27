package spkfarrosahp.Model;

import spkfarrosahp.Config.KoneksiDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
// Import Kriteria dan PerbandinganKriteria jika belum ada di package yang sama
// import spkfarrosahp.Model.Kriteria; 
// import spkfarrosahp.Model.PerbandinganKriteria;

public class KriteriaDAO {

    // == Operasi untuk Tabel 'kriteria' ==

    public List<Kriteria> getAllKriteria() throws SQLException {
        List<Kriteria> daftarKriteria = new ArrayList<>();
        String sql = "SELECT kriteria_id, kode_kriteria, nama_kriteria, bobot FROM kriteria ORDER BY kode_kriteria ASC";
        try (Connection conn = KoneksiDatabase.configDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Kriteria kriteria = new Kriteria();
                kriteria.setKriteriaId(rs.getInt("kriteria_id"));
                kriteria.setKodeKriteria(rs.getString("kode_kriteria"));
                kriteria.setNamaKriteria(rs.getString("nama_kriteria"));
                kriteria.setBobot(rs.getDouble("bobot"));
                // createdAt dan updatedAt bisa diambil jika perlu
                daftarKriteria.add(kriteria);
            }
        }
        return daftarKriteria;
    }

    public Kriteria getKriteriaByKode(String kodeKriteria) throws SQLException {
        String sql = "SELECT kriteria_id, kode_kriteria, nama_kriteria, bobot FROM kriteria WHERE kode_kriteria = ?";
        Kriteria kriteria = null;
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kodeKriteria);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    kriteria = new Kriteria();
                    kriteria.setKriteriaId(rs.getInt("kriteria_id"));
                    kriteria.setKodeKriteria(rs.getString("kode_kriteria"));
                    kriteria.setNamaKriteria(rs.getString("nama_kriteria"));
                    kriteria.setBobot(rs.getDouble("bobot"));
                }
            }
        }
        return kriteria;
    }
    
    public Kriteria getKriteriaById(int id) throws SQLException {
        String sql = "SELECT kriteria_id, kode_kriteria, nama_kriteria, bobot FROM kriteria WHERE kriteria_id = ?";
        Kriteria kriteria = null;
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    kriteria = new Kriteria();
                    kriteria.setKriteriaId(rs.getInt("kriteria_id"));
                    kriteria.setKodeKriteria(rs.getString("kode_kriteria"));
                    kriteria.setNamaKriteria(rs.getString("nama_kriteria"));
                    kriteria.setBobot(rs.getDouble("bobot"));
                }
            }
        }
        return kriteria;
    }


    public boolean addKriteria(Kriteria kriteria) throws SQLException {
        // Bobot awal saat menambah kriteria baru mungkin 0 atau nilai default,
        // karena bobot utama akan dihitung melalui AHP.
        // Untuk sekarang, kita asumsikan bobot diisi dengan nilai awal (misal 0).
        String sql = "INSERT INTO kriteria (kode_kriteria, nama_kriteria, bobot) VALUES (?, ?, ?)";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, kriteria.getKodeKriteria());
            pstmt.setString(2, kriteria.getNamaKriteria());
            pstmt.setDouble(3, kriteria.getBobot()); // Bobot awal saat kriteria dibuat
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        kriteria.setKriteriaId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean updateKriteria(Kriteria kriteria) throws SQLException {
        // Method ini bisa digunakan untuk update nama kriteria.
        // Update bobot akan lebih spesifik melalui method lain setelah AHP.
        String sql = "UPDATE kriteria SET nama_kriteria = ?, kode_kriteria = ? WHERE kriteria_id = ?";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kriteria.getNamaKriteria());
            pstmt.setString(2, kriteria.getKodeKriteria());
            pstmt.setInt(3, kriteria.getKriteriaId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    public boolean updateBobotKriteria(int kriteriaId, double bobotBaru) throws SQLException {
        String sql = "UPDATE kriteria SET bobot = ? WHERE kriteria_id = ?";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, bobotBaru);
            pstmt.setInt(2, kriteriaId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }


    public boolean deleteKriteria(int kriteriaId) throws SQLException {
        // Ingat, tabel perbandingan_kriteria memiliki FOREIGN KEY ON DELETE CASCADE,
        // jadi perbandingan terkait akan otomatis terhapus.
        String sql = "DELETE FROM kriteria WHERE kriteria_id = ?";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, kriteriaId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean isKodeKriteriaExists(String kodeKriteria, Integer kriteriaIdToExclude) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM kriteria WHERE kode_kriteria = ?");
        if (kriteriaIdToExclude != null) {
            sql.append(" AND kriteria_id != ?");
        }
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setString(1, kodeKriteria);
            if (kriteriaIdToExclude != null) {
                pstmt.setInt(2, kriteriaIdToExclude);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


    // == Operasi untuk Tabel 'perbandingan_kriteria' ==

    public boolean saveOrUpdatePerbandingan(List<PerbandinganKriteria> daftarPerbandingan) throws SQLException {
        // Method ini akan menghapus perbandingan lama (untuk pasangan kriteria yang ada di list)
        // dan memasukkan yang baru. Atau bisa menggunakan ON DUPLICATE KEY UPDATE jika lebih disukai.
        // Untuk AHP, biasanya lebih mudah untuk menghapus semua perbandingan sebelumnya dan menyimpan set yang baru.
        // Namun, jika ingin lebih granular, perlu logika update yang lebih detail.
        // Kita asumsikan untuk AHP per sesi, kita simpan semua nilai perbandingan yang relevan.

        // Sederhana: Hapus semua perbandingan yang melibatkan kriteria yang ada di list, lalu insert.
        // Atau, yang lebih umum: hapus semua perbandingan, lalu insert semua yang baru dari matriks.
        // Untuk contoh ini, kita pakai UPSERT (INSERT ... ON DUPLICATE KEY UPDATE)
        
        String sql = "INSERT INTO perbandingan_kriteria (kriteria1_id, kriteria2_id, nilai_perbandingan) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE nilai_perbandingan = VALUES(nilai_perbandingan)";
        
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            conn.setAutoCommit(false); // Mulai transaksi

            for (PerbandinganKriteria pk : daftarPerbandingan) {
                // Pastikan kriteria1_id < kriteria2_id untuk konsistensi jika diperlukan
                // atau handle di UI/Controller untuk hanya menyimpan setengah matriks.
                // Untuk sekarang, kita asumsikan data pk sudah benar.
                pstmt.setInt(1, pk.getKriteria1Id());
                pstmt.setInt(2, pk.getKriteria2Id());
                pstmt.setDouble(3, pk.getNilaiPerbandingan());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            conn.commit(); // Commit transaksi
            return true;
        } catch (SQLException e) {
            // Rollback jika ada error
            try (Connection conn = KoneksiDatabase.configDB()) { // Dapatkan koneksi baru untuk rollback
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                 System.err.println("Error saat rollback: " + ex.getMessage());
            }
            throw e; // Lemparkan exception asli
        } finally {
             try (Connection conn = KoneksiDatabase.configDB()) { // Dapatkan koneksi baru untuk setAutoCommit
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException ex) {
                 System.err.println("Error saat reset autocommit: " + ex.getMessage());
            }
        }
    }
    

    // Mengambil semua data perbandingan yang tersimpan (untuk mengisi matriks di UI)
    public List<PerbandinganKriteria> getAllPerbandinganKriteria() throws SQLException {
        List<PerbandinganKriteria> daftarPerbandingan = new ArrayList<>();
        // Query ini mengambil juga nama kriteria untuk kemudahan, meskipun model PerbandinganKriteria
        // utama hanya menyimpan ID. Kita bisa populate objek Kriteria di DAO atau di Controller.
        String sql = "SELECT pk.perbandingan_id, pk.kriteria1_id, k1.kode_kriteria AS kode1, k1.nama_kriteria AS nama1, " +
                     "pk.kriteria2_id, k2.kode_kriteria AS kode2, k2.nama_kriteria AS nama2, pk.nilai_perbandingan " +
                     "FROM perbandingan_kriteria pk " +
                     "JOIN kriteria k1 ON pk.kriteria1_id = k1.kriteria_id " +
                     "JOIN kriteria k2 ON pk.kriteria2_id = k2.kriteria_id";
        
        try (Connection conn = KoneksiDatabase.configDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PerbandinganKriteria pk = new PerbandinganKriteria();
                pk.setPerbandinganId(rs.getInt("perbandingan_id"));
                pk.setKriteria1Id(rs.getInt("kriteria1_id"));
                pk.setKriteria2Id(rs.getInt("kriteria2_id"));
                pk.setNilaiPerbandingan(rs.getDouble("nilai_perbandingan"));

                // Opsional: Buat dan set objek Kriteria jika ingin info lengkap di sini
                Kriteria k1 = new Kriteria();
                k1.setKriteriaId(rs.getInt("kriteria1_id"));
                k1.setKodeKriteria(rs.getString("kode1"));
                k1.setNamaKriteria(rs.getString("nama1"));
                pk.setKriteria1(k1);

                Kriteria k2 = new Kriteria();
                k2.setKriteriaId(rs.getInt("kriteria2_id"));
                k2.setKodeKriteria(rs.getString("kode2"));
                k2.setNamaKriteria(rs.getString("nama2"));
                pk.setKriteria2(k2);
                
                daftarPerbandingan.add(pk);
            }
        }
        return daftarPerbandingan;
    }
    
    // Method untuk menghapus semua perbandingan (berguna sebelum menyimpan set baru)
    public void deleteAllPerbandingan() throws SQLException {
        String sql = "DELETE FROM perbandingan_kriteria";
        try (Connection conn = KoneksiDatabase.configDB();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
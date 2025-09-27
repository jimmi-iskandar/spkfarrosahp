package spkfarrosahp.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class KoneksiDatabase {
    private static Connection mysqlconfig;

    private static final String NAMA_DATABASE = "dbspkfarrosahp"; // Sesuai permintaan Anda
    private static final String URL_DATABASE = "jdbc:mysql://localhost:3306/" + NAMA_DATABASE;
    private static final String USER_DATABASE = "root"; // Ganti jika user Anda berbeda
    private static final String PASS_DATABASE = "";     // Ganti dengan password MySQL Anda, kosongkan jika tidak ada

    public static Connection configDB() throws SQLException {
        try {
            // Driver untuk MySQL Connector/J 8.0+
            Class.forName("com.mysql.cj.jdbc.Driver");
            mysqlconfig = DriverManager.getConnection(URL_DATABASE, USER_DATABASE, PASS_DATABASE);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL tidak ditemukan (Pastikan MySQL Connector/J ada di library): " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Driver database tidak ditemukan!", "Database Error", JOptionPane.ERROR_MESSAGE);
            throw new SQLException("Driver tidak ditemukan", e);
        } catch (SQLException e) {
            System.err.println("Koneksi ke database gagal: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Gagal terhubung ke database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
        return mysqlconfig;
    }

    // Opsional: Main method untuk tes koneksi cepat
    public static void main(String[] args) {
        try {
            Connection testConnection = KoneksiDatabase.configDB();
            if (testConnection != null) {
                JOptionPane.showMessageDialog(null, "Tes koneksi ke database '" + NAMA_DATABASE + "' berhasil!");
                testConnection.close();
            }
        } catch (SQLException e) {
            // Pesan error sudah ditangani di configDB()
        }
    }
}
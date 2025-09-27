package spkfarrosahp.Model;

import spkfarrosahp.Config.KoneksiDatabase;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admin_users WHERE username = ?";
        Admin admin = null;
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    admin = new Admin();
                    admin.setAdminId(rs.getInt("admin_id"));
                    admin.setNamaLengkap(rs.getString("full_name")); // Sesuaikan dengan nama kolom DB
                    admin.setUsername(rs.getString("username"));
                    admin.setPassword(rs.getString("password")); 
                    admin.setEmail(rs.getString("email"));
                    admin.setCreatedAt(rs.getTimestamp("created_at"));
                    admin.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil admin berdasarkan username: " + e.getMessage());
        }
        return admin;
    }
    
    public boolean saveAdmin(Admin admin) {
        // Kolom di DB: admin_id, username, password, full_name, email, created_at, updated_at
        String sql = "INSERT INTO admin_users (username, password, full_name, email, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String hashedPassword = BCrypt.hashpw(admin.getPassword(), BCrypt.gensalt());
            pstmt.setString(1, admin.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, admin.getNamaLengkap());
            pstmt.setString(4, admin.getEmail());
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menyimpan admin baru: " + e.getMessage());
            return false;
        }
    }
    
    public List<Admin> getAllAdmin() {
        List<Admin> daftarAdmin = new ArrayList<>();
        String sql = "SELECT admin_id, username, full_name, email, created_at, updated_at FROM admin_users ORDER BY full_name ASC";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setNamaLengkap(rs.getString("full_name"));
                admin.setUsername(rs.getString("username"));
                admin.setEmail(rs.getString("email"));
                admin.setCreatedAt(rs.getTimestamp("created_at"));
                admin.setUpdatedAt(rs.getTimestamp("updated_at"));
                daftarAdmin.add(admin);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua admin: " + e.getMessage());
        }
        return daftarAdmin;
    }

    public boolean updatePassword(int adminId, String newPassword) {
        String sql = "UPDATE admin_users SET password = ?, updated_at = ? WHERE admin_id = ?";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            pstmt.setString(1, newHashedPassword);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, adminId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error saat update password admin: " + e.getMessage());
            return false;
        }
    }

    public Admin getAdminById(int adminId) {
        String sql = "SELECT * FROM admin_users WHERE admin_id = ?";
        Admin admin = null;
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, adminId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    admin = new Admin();
                    admin.setAdminId(rs.getInt("admin_id"));
                    admin.setNamaLengkap(rs.getString("full_name"));
                    admin.setUsername(rs.getString("username"));
                    admin.setPassword(rs.getString("password"));
                    admin.setEmail(rs.getString("email"));
                    admin.setCreatedAt(rs.getTimestamp("created_at"));
                    admin.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil admin berdasarkan ID: " + e.getMessage());
        }
        return admin;
    }

    public boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admin_users WHERE username = ?";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean isEmailExists(String email) throws SQLException {
         // Jika tidak ada kolom email di tabel admin_users atau tidak ingin dicek, kembalikan false
        if (email == null || email.trim().isEmpty()) {
            return false; // Anggap email kosong tidak ada
        }
        String sql = "SELECT COUNT(*) FROM admin_users WHERE email = ?";
        try (Connection conn = KoneksiDatabase.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
package spkfarrosahp.Model;

import java.util.Date; // Atau java.sql.Timestamp jika itu yang Anda gunakan

public class Admin {
    private int adminId;
    private String namaLengkap; // Sesuai dengan 'full_name' di database Anda
    private String username;
    private String password;
    private String email; // Sesuai dengan database Anda
    private Date createdAt;   // Sesuai dengan database Anda
    private Date updatedAt;   // Sesuai dengan database Anda

    // Konstruktor
    public Admin() {}

    public Admin(String namaLengkap, String username, String password, String email) {
        this.namaLengkap = namaLengkap;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getter dan Setter
    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getNamaLengkap() { // Menggunakan nama ini untuk konsistensi
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
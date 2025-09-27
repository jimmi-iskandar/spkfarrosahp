package spkfarrosahp.Model;

import java.util.Date; // Untuk tanggal_lahir

public class Karyawan {
    private int karyawanId;
    private String kodeKaryawan;
    private String namaLengkap;
    private String jabatan;
    private Date tanggalLahir;
    private String alamat;
    private String telepon;
    // created_at dan updated_at biasanya tidak perlu di model untuk input form,
    // karena dihandle oleh database.

    // Konstruktor
    public Karyawan() {
    }

    public Karyawan(String kodeKaryawan, String namaLengkap, String jabatan, Date tanggalLahir, String alamat, String telepon) {
        this.kodeKaryawan = kodeKaryawan;
        this.namaLengkap = namaLengkap;
        this.jabatan = jabatan;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.telepon = telepon;
    }

    // Getter dan Setter
    public int getKaryawanId() {
        return karyawanId;
    }

    public void setKaryawanId(int karyawanId) {
        this.karyawanId = karyawanId;
    }

    public String getKodeKaryawan() {
        return kodeKaryawan;
    }

    public void setKodeKaryawan(String kodeKaryawan) {
        this.kodeKaryawan = kodeKaryawan;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(Date tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }
}
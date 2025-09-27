package spkfarrosahp.Model;

import java.util.Date; // Untuk tanggal_perhitungan

public class HasilSeleksi {
    // Atribut bisa merujuk langsung ke Karyawan jika perlu info lebih,
    // atau cukup simpan ID dan data yang relevan untuk perangkingan.
    private int karyawanId; 
    private String kodeKaryawan; // Untuk tampilan di tabel hasil
    private String namaKaryawan; // Untuk tampilan di tabel hasil
    private double nilaiTotalSpk;
    private int peringkat;
    private Date tanggalPerhitungan;

    // Konstruktor
    public HasilSeleksi() {
    }

    public HasilSeleksi(int karyawanId, String kodeKaryawan, String namaKaryawan, double nilaiTotalSpk, int peringkat) {
        this.karyawanId = karyawanId;
        this.kodeKaryawan = kodeKaryawan;
        this.namaKaryawan = namaKaryawan;
        this.nilaiTotalSpk = nilaiTotalSpk;
        this.peringkat = peringkat;
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

    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public void setNamaKaryawan(String namaKaryawan) {
        this.namaKaryawan = namaKaryawan;
    }

    public double getNilaiTotalSpk() {
        return nilaiTotalSpk;
    }

    public void setNilaiTotalSpk(double nilaiTotalSpk) {
        this.nilaiTotalSpk = nilaiTotalSpk;
    }

    public int getPeringkat() {
        return peringkat;
    }

    public void setPeringkat(int peringkat) {
        this.peringkat = peringkat;
    }

    public Date getTanggalPerhitungan() {
        return tanggalPerhitungan;
    }

    public void setTanggalPerhitungan(Date tanggalPerhitungan) {
        this.tanggalPerhitungan = tanggalPerhitungan;
    }
}
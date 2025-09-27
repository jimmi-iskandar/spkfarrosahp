package spkfarrosahp.Model;

import java.util.Date; // Meskipun tidak ada di tabel kriteria, jaga-jaga jika ada timestamp

public class Kriteria {
    private int kriteriaId;
    private String kodeKriteria;
    private String namaKriteria;
    private double bobot; // Menggunakan double untuk bobot desimal
    private Date createdAt; // Opsional, untuk melacak kapan dibuat
    private Date updatedAt; // Opsional, untuk melacak kapan diupdate

    // Konstruktor default
    public Kriteria() {
    }

    // Konstruktor dengan parameter dasar
    public Kriteria(String kodeKriteria, String namaKriteria, double bobot) {
        this.kodeKriteria = kodeKriteria;
        this.namaKriteria = namaKriteria;
        this.bobot = bobot;
    }

    // Getter dan Setter
    public int getKriteriaId() {
        return kriteriaId;
    }

    public void setKriteriaId(int kriteriaId) {
        this.kriteriaId = kriteriaId;
    }

    public String getKodeKriteria() {
        return kodeKriteria;
    }

    public void setKodeKriteria(String kodeKriteria) {
        this.kodeKriteria = kodeKriteria;
    }

    public String getNamaKriteria() {
        return namaKriteria;
    }

    public void setNamaKriteria(String namaKriteria) {
        this.namaKriteria = namaKriteria;
    }

    public double getBobot() {
        return bobot;
    }

    public void setBobot(double bobot) {
        this.bobot = bobot;
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

    // Override toString() bisa berguna untuk debugging atau tampilan di ComboBox
    @Override
    public String toString() {
        return namaKriteria + " (" + kodeKriteria + ")";
    }
}
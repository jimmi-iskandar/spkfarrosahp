package spkfarrosahp.Model;

public class PerbandinganKriteria {
    private int perbandinganId;
    private int kriteria1Id; // ID dari Kriteria pertama
    private int kriteria2Id; // ID dari Kriteria kedua
    private double nilaiPerbandingan;

    // Referensi ke objek Kriteria (opsional, tapi bisa berguna)
    private Kriteria kriteria1;
    private Kriteria kriteria2;

    // Konstruktor default
    public PerbandinganKriteria() {
    }

    // Konstruktor dengan ID
    public PerbandinganKriteria(int kriteria1Id, int kriteria2Id, double nilaiPerbandingan) {
        this.kriteria1Id = kriteria1Id;
        this.kriteria2Id = kriteria2Id;
        this.nilaiPerbandingan = nilaiPerbandingan;
    }

    // Konstruktor dengan objek Kriteria (lebih deskriptif)
    public PerbandinganKriteria(Kriteria kriteria1, Kriteria kriteria2, double nilaiPerbandingan) {
        this.kriteria1 = kriteria1;
        this.kriteria2 = kriteria2;
        if (kriteria1 != null) this.kriteria1Id = kriteria1.getKriteriaId();
        if (kriteria2 != null) this.kriteria2Id = kriteria2.getKriteriaId();
        this.nilaiPerbandingan = nilaiPerbandingan;
    }


    // Getter dan Setter
    public int getPerbandinganId() {
        return perbandinganId;
    }

    public void setPerbandinganId(int perbandinganId) {
        this.perbandinganId = perbandinganId;
    }

    public int getKriteria1Id() {
        return kriteria1Id;
    }

    public void setKriteria1Id(int kriteria1Id) {
        this.kriteria1Id = kriteria1Id;
    }

    public int getKriteria2Id() {
        return kriteria2Id;
    }

    public void setKriteria2Id(int kriteria2Id) {
        this.kriteria2Id = kriteria2Id;
    }

    public double getNilaiPerbandingan() {
        return nilaiPerbandingan;
    }

    public void setNilaiPerbandingan(double nilaiPerbandingan) {
        this.nilaiPerbandingan = nilaiPerbandingan;
    }

    public Kriteria getKriteria1() {
        return kriteria1;
    }

    public void setKriteria1(Kriteria kriteria1) {
        this.kriteria1 = kriteria1;
        if (kriteria1 != null) {
            this.kriteria1Id = kriteria1.getKriteriaId();
        }
    }

    public Kriteria getKriteria2() {
        return kriteria2;
    }

    public void setKriteria2(Kriteria kriteria2) {
        this.kriteria2 = kriteria2;
        if (kriteria2 != null) {
            this.kriteria2Id = kriteria2.getKriteriaId();
        }
    }
}
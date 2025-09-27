package spkfarrosahp.Model;

// Tidak perlu import Date jika tidak ada kolom timestamp di model ini (sudah dihandle DB)

public class PenilaianAlternatif {
    private int penilaianId;
    private int karyawanId; // Merujuk ke Karyawan (Alternatif)
    private int kriteriaId; // Merujuk ke Kriteria Acuan
    private double nilai;   // Bobot lokal hasil AHP untuk alternatif ini pada kriteria ini

    // Konstruktor
    public PenilaianAlternatif() {
    }

    public PenilaianAlternatif(int karyawanId, int kriteriaId, double nilai) {
        this.karyawanId = karyawanId;
        this.kriteriaId = kriteriaId;
        this.nilai = nilai;
    }

    // Getter dan Setter
    public int getPenilaianId() {
        return penilaianId;
    }

    public void setPenilaianId(int penilaianId) {
        this.penilaianId = penilaianId;
    }

    public int getKaryawanId() {
        return karyawanId;
    }

    public void setKaryawanId(int karyawanId) {
        this.karyawanId = karyawanId;
    }

    public int getKriteriaId() {
        return kriteriaId;
    }

    public void setKriteriaId(int kriteriaId) {
        this.kriteriaId = kriteriaId;
    }

    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }
}
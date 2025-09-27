package spkfarrosahp.Controller;

import spkfarrosahp.Model.Kriteria;
import spkfarrosahp.Model.Karyawan;
import spkfarrosahp.Model.AlternatifDAO; // DAO yang baru kita buat
import spkfarrosahp.Model.PenilaianAlternatif; // Model untuk menyimpan hasil
import spkfarrosahp.View.PanelAlternatif;  // View yang akan dikontrol

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.swing.JOptionPane;

public class AlternatifController {
    private PanelAlternatif view;
    private AlternatifDAO dao;
    private List<Kriteria> daftarKriteriaAcuan; // Menyimpan daftar kriteria untuk ComboBox
    private List<Karyawan> daftarKaryawanAlternatif; // Menyimpan daftar karyawan untuk tabel dan perbandingan
    private Kriteria kriteriaAcuanSaatIni; // Kriteria yang sedang dipilih

    // Indeks Rasio Konsistensi (RI) - sama seperti di KriteriaController
    private static final double[] randomIndex = {0, 0, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51, 1.54, 1.56, 1.57, 1.59};

    public AlternatifController(PanelAlternatif view) {
        this.view = view;
        this.dao = new AlternatifDAO();
        loadInitialData();
    }

    public void loadInitialData() {
        try {
            daftarKriteriaAcuan = dao.getAllKriteriaUntukAcuan();
            view.populateCmbKriteriaAcuan(daftarKriteriaAcuan);

            daftarKaryawanAlternatif = dao.getAllKaryawanAlternatif();
            view.displayDaftarKaryawan(daftarKaryawanAlternatif);

            // Jika ada kriteria, pilih yang pertama dan update UI perbandingan
            if (!daftarKriteriaAcuan.isEmpty()) {
                kriteriaAcuanDipilih(daftarKriteriaAcuan.get(0));
            } else {
                // Jika tidak ada kriteria, UI perbandingan mungkin perlu dikosongkan atau diberi pesan
                view.updateInputPerbandinganAlternatifUI(new ArrayList<>(), null);
                view.updateMatriksPerbandinganAlternatifHeader(new ArrayList<>(), null);
            }
            view.clearHasilLokalAHP(); // Kosongkan hasil AHP awal
        } catch (SQLException e) {
            view.showMessage("Gagal memuat data awal untuk alternatif: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    // Dipanggil oleh View ketika kriteria di ComboBox berubah
    public void kriteriaAcuanDipilih(Kriteria kriteriaTerpilih) {
        this.kriteriaAcuanSaatIni = kriteriaTerpilih;
        if (kriteriaTerpilih != null && daftarKaryawanAlternatif != null) {
            view.updateInputPerbandinganAlternatifUI(daftarKaryawanAlternatif, kriteriaTerpilih);
            view.updateMatriksPerbandinganAlternatifHeader(daftarKaryawanAlternatif, kriteriaTerpilih);
            view.clearHasilLokalAHP(); // Setiap ganti kriteria, hasil AHP lama dibersihkan
            // Anda bisa tambahkan logika untuk memuat nilai perbandingan yang sudah ada untuk kriteria ini jika disimpan
        }
    }

    // Dipanggil dari View setelah tombol "Proses Input Nilai Alternatif" diklik
    public void prosesInputNilaiAlternatif(List<PenilaianAlternatif> perbandinganDariView, Kriteria kriteriaAcuan) {
        if (kriteriaAcuan == null) {
            view.showMessage("Pilih kriteria acuan terlebih dahulu.", true);
            return;
        }
        if (daftarKaryawanAlternatif == null || daftarKaryawanAlternatif.size() < 2) {
            view.showMessage("Minimal ada 2 karyawan (alternatif) untuk melakukan perbandingan.", true);
            return;
        }
        if (perbandinganDariView == null || perbandinganDariView.isEmpty()) {
            view.showMessage("Tidak ada nilai perbandingan yang diinput dari form.", true);
            return;
        }

        int n = daftarKaryawanAlternatif.size();
        double[][] matriksPerbandingan = new double[n][n];

        // Buat map dari ID Karyawan ke Indeks matriks untuk kemudahan
        Map<Integer, Integer> karyawanIdToIndexMap = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            karyawanIdToIndexMap.put(daftarKaryawanAlternatif.get(i).getKaryawanId(), i);
        }

        // Inisialisasi diagonal matriks dengan 1
        for (int i = 0; i < n; i++) {
            matriksPerbandingan[i][i] = 1.0;
        }

        // Isi matriks dari input pengguna
        // PerbandinganKriteria yang diterima dari view adalah antara Karyawan1 vs Karyawan2
        for (PenilaianAlternatif paInput : perbandinganDariView) {
            // paInput.getKaryawanId() adalah Karyawan1_id
            // paInput.getKriteriaId() adalah Karyawan2_id (dalam konteks ini)
            // paInput.getNilai() adalah nilai perbandingan Karyawan1 vs Karyawan2
            Integer idx1 = karyawanIdToIndexMap.get(paInput.getKaryawanId());
            Integer idx2 = karyawanIdToIndexMap.get(paInput.getKriteriaId()); // Ini adalah Karyawan2_id
            
            if (idx1 != null && idx2 != null) {
                matriksPerbandingan[idx1][idx2] = paInput.getNilai();
                matriksPerbandingan[idx2][idx1] = (paInput.getNilai() == 0) ? Double.POSITIVE_INFINITY : 1.0 / paInput.getNilai();
            }
        }
        view.displayMatriksPerbandinganAlternatif(matriksPerbandingan, daftarKaryawanAlternatif, kriteriaAcuan);
        view.showMessage("Matriks perbandingan alternatif berhasil diproses.", false);
    }


    public void hitungBobotLokalAlternatif(Kriteria kriteriaAcuan) {
        if (kriteriaAcuan == null) {
            view.showMessage("Pilih kriteria acuan terlebih dahulu.", true);
            return;
        }
        // Ambil matriks dari view (yang sudah diisi/diproses)
        double[][] matriksPerbandingan = view.getMatriksPerbandinganAlternatifFromTable(); // Method ini perlu ada di PanelAlternatif

        if (matriksPerbandingan == null || matriksPerbandingan.length == 0 ||
            daftarKaryawanAlternatif == null || daftarKaryawanAlternatif.isEmpty() ||
            daftarKaryawanAlternatif.size() != matriksPerbandingan.length) {
            view.showMessage("Matriks perbandingan alternatif belum diisi atau tidak valid.", true);
            return;
        }

        int n = matriksPerbandingan.length;
        if (n == 0) {
             view.showMessage("Tidak ada alternatif untuk dihitung.", true);
            return;
        }

        // Perhitungan AHP (Normalisasi, Priority Vector, Konsistensi) - sama seperti di KriteriaController
        double[] columnSums = new double[n];
        double[][] normalizedMatrix = new double[n][n];
        double[] priorityVector = new double[n]; // Ini adalah bobot lokal alternatif

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                columnSums[j] += matriksPerbandingan[i][j];
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                normalizedMatrix[i][j] = (columnSums[j] == 0) ? 0 : matriksPerbandingan[i][j] / columnSums[j];
            }
        }
        view.displayMatriksNormalisasiAlternatif(normalizedMatrix, daftarKaryawanAlternatif, kriteriaAcuan);

        double sumOfPriorityVector = 0;
        for (int i = 0; i < n; i++) {
            double rowSum = 0;
            for (int j = 0; j < n; j++) {
                rowSum += normalizedMatrix[i][j];
            }
            priorityVector[i] = (n == 0) ? 0 : rowSum / n;
            sumOfPriorityVector += priorityVector[i];
        }
        
        if (sumOfPriorityVector > 0 && Math.abs(sumOfPriorityVector - 1.0) > 0.00001) {
            for (int i = 0; i < n; i++) {
                priorityVector[i] = (sumOfPriorityVector == 0) ? 0 : priorityVector[i] / sumOfPriorityVector;
            }
        }

        double[] weightedSumVector = new double[n];
        double lambdaMax = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                weightedSumVector[i] += matriksPerbandingan[i][j] * priorityVector[j];
            }
        }
        for (int i = 0; i < n; i++) {
            lambdaMax += (priorityVector[i] == 0) ? 0 : (weightedSumVector[i] / priorityVector[i]);
        }
        lambdaMax = (n == 0) ? 0 : lambdaMax / n;

        double ci = (n <= 1) ? 0 : (lambdaMax - n) / (n - 1);
        double cr = 0;
        if (n > 0 && n - 1 < randomIndex.length) {
             double riValue = randomIndex[n-1 < 0 ? 0 : n-1];
             if (riValue == 0 && n > 2) cr = (Math.abs(ci) < 0.00001) ? 0 : Double.POSITIVE_INFINITY; 
             else if (riValue != 0) cr = ci / riValue;
             else if (n <= 2 && Math.abs(ci) < 0.00001) cr = 0;
             else if (n <=2 && riValue == 0) cr = Double.POSITIVE_INFINITY;
        } else if (n > randomIndex.length && randomIndex.length > 0) {
            double largestRI = randomIndex[randomIndex.length - 1];
            if (largestRI != 0) cr = ci / largestRI;
            else cr = (Math.abs(ci) < 0.00001) ? 0 : Double.POSITIVE_INFINITY;
        } else if (n == 0){
            lambdaMax = 0; ci = 0; cr = 0;
        }

        view.displayHasilBobotLokalAlternatif(daftarKaryawanAlternatif, priorityVector, kriteriaAcuan);
        view.displayNilaiKonsistensiLokal(lambdaMax, ci, cr);

        // Beri opsi untuk menyimpan jika konsisten
        view.enableSimpanBobotLokalButton(cr <= 0.10); // Method ini perlu ada di PanelAlternatif
    }
    
    public void simpanBobotLokalKeDB(Kriteria kriteriaAcuan) {
        if (kriteriaAcuan == null) {
            view.showMessage("Kriteria acuan belum dipilih.", true);
            return;
        }
        // Ambil bobot lokal dari view (yang sudah dihitung dan ditampilkan)
        // atau idealnya dari state controller jika sudah dihitung
        double[] bobotLokal = view.getHasilBobotLokalFromDisplay(); // Method ini perlu ada di PanelAlternatif

        if (bobotLokal == null || bobotLokal.length != daftarKaryawanAlternatif.size()) {
            view.showMessage("Tidak ada data bobot lokal yang valid untuk disimpan.", true);
            return;
        }

        List<PenilaianAlternatif> daftarPenilaianUntukSimpan = new ArrayList<>();
        for (int i = 0; i < daftarKaryawanAlternatif.size(); i++) {
            Karyawan karyawan = daftarKaryawanAlternatif.get(i);
            double nilaiBobot = bobotLokal[i];
            daftarPenilaianUntukSimpan.add(new PenilaianAlternatif(karyawan.getKaryawanId(), kriteriaAcuan.getKriteriaId(), nilaiBobot));
        }

        try {
            if (dao.saveOrUpdatePenilaianAlternatif(daftarPenilaianUntukSimpan)) {
                view.showMessage("Bobot lokal alternatif untuk kriteria '" + kriteriaAcuan.getNamaKriteria() + "' berhasil disimpan.", false);
            } else {
                view.showMessage("Gagal menyimpan bobot lokal alternatif.", true);
            }
        } catch (SQLException e) {
            view.showMessage("Error database saat menyimpan bobot lokal: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }
}
package spkfarrosahp.Controller;

import spkfarrosahp.Model.Kriteria;
import spkfarrosahp.Model.KriteriaDAO;
import spkfarrosahp.Model.PerbandinganKriteria;
import spkfarrosahp.View.PanelKriteriaAHP;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.swing.JOptionPane;

public class KriteriaController {
    private PanelKriteriaAHP view;
    private KriteriaDAO dao;
    private List<Kriteria> currentKriteriaList;

    private static final double[] randomIndex = {0, 0, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51, 1.54, 1.56, 1.57, 1.59};

    public KriteriaController(PanelKriteriaAHP view) {
        this.view = view;
        this.dao = new KriteriaDAO();
        this.currentKriteriaList = new ArrayList<>(); // Pastikan diinisialisasi
        loadInitialKriteria();
    }

    // --- PENAMBAHAN METHOD BARU ---
    public List<Kriteria> getCurrentKriteriaList() {
        return this.currentKriteriaList;
    }
    // --- AKHIR PENAMBAHAN ---

    public void loadInitialKriteria() {
        try {
            currentKriteriaList = dao.getAllKriteria(); // currentKriteriaList diisi di sini
            view.displayDaftarKriteria(currentKriteriaList);
            loadAndDisplayPerbandinganTersimpan();
        } catch (SQLException e) {
            view.showMessage("Gagal memuat daftar kriteria awal: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }
    
    public void loadAndDisplayPerbandinganTersimpan() {
        if (currentKriteriaList == null || currentKriteriaList.isEmpty()) return;
        try {
            List<PerbandinganKriteria> perbandinganTersimpan = dao.getAllPerbandinganKriteria();
            view.populatePerbandinganMatrixFromDB(perbandinganTersimpan, currentKriteriaList);
        } catch (SQLException e) {
            view.showMessage("Gagal memuat data perbandingan tersimpan: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    public void buatKodeKriteriaOtomatis() {
        String prefix = "K";
        int nextNum = 1; 
        String newKode;
        try {
            // currentKriteriaList sudah di-populate oleh loadInitialKriteria
            if (currentKriteriaList != null && !currentKriteriaList.isEmpty()) {
                for (Kriteria k : currentKriteriaList) { // Gunakan currentKriteriaList
                    if (k.getKodeKriteria().startsWith(prefix)) {
                        try {
                            int num = Integer.parseInt(k.getKodeKriteria().substring(prefix.length()));
                            if (num >= nextNum) {
                                nextNum = num + 1;
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Format kode kriteria tidak valid saat generate: " + k.getKodeKriteria() + " - " + e.getMessage());
                        }
                    }
                }
            }
            newKode = prefix + nextNum; 
        } catch (Exception e) { // Tangkap Exception umum jika currentKriteriaList belum siap
            view.showMessage("Error saat generate kode kriteria: " + e.getMessage(), true);
            e.printStackTrace();
            newKode = ""; 
        }
        view.setKodeKriteriaField(newKode);
    }

    public void tambahKriteria() {
        String kode = view.getKodeKriteriaInput();
        String nama = view.getNamaKriteriaInput();

        if (kode.isEmpty() || nama.isEmpty()) {
            view.showMessage("Kode dan Nama Kriteria tidak boleh kosong.", true);
            return;
        }
        if (!kode.matches("^K\\d+$")) {
            view.showMessage("Format Kode Kriteria salah.\nHarus diawali 'K' diikuti angka (Contoh: K1, K10, K12).", true);
            view.requestKodeKriteriaFocus();
            return;
        }

        try {
            if (dao.isKodeKriteriaExists(kode, null)) {
                view.showMessage("Kode Kriteria '" + kode + "' sudah ada.", true);
                view.requestKodeKriteriaFocus();
                return;
            }
            Kriteria kriteriaBaru = new Kriteria(kode, nama, 0.0);
            if (dao.addKriteria(kriteriaBaru)) {
                view.showMessage("Kriteria berhasil ditambahkan.", false);
                loadInitialKriteria(); 
                view.clearInputKriteriaFields();
            } else {
                view.showMessage("Gagal menambahkan kriteria.", true);
            }
        } catch (SQLException e) {
            view.showMessage("Error database saat menambah kriteria: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    public void ubahKriteria() {
        String idStr = view.getSelectedKriteriaIdFromTable();
        String kode = view.getKodeKriteriaInput();
        String nama = view.getNamaKriteriaInput();

        if (idStr == null || idStr.isEmpty()) {
            view.showMessage("Pilih kriteria dari tabel yang akan diubah.", true);
            return;
        }
        if (kode.isEmpty() || nama.isEmpty()) {
            view.showMessage("Kode dan Nama Kriteria tidak boleh kosong.", true);
            return;
        }
        if (!kode.matches("^K\\d+$")) {
            view.showMessage("Format Kode Kriteria salah.\nHarus diawali 'K' diikuti angka (Contoh: K1, K10, K12).", true);
            view.requestKodeKriteriaFocus();
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            if (dao.isKodeKriteriaExists(kode, id)) {
                 view.showMessage("Kode Kriteria '" + kode + "' sudah digunakan oleh kriteria lain.", true);
                 view.requestKodeKriteriaFocus();
                return;
            }
            
            Kriteria kriteriaUntukUpdate = dao.getKriteriaById(id);
            if(kriteriaUntukUpdate == null) {
                view.showMessage("Kriteria tidak ditemukan untuk diubah.", true);
                return;
            }
            
            kriteriaUntukUpdate.setKodeKriteria(kode);
            kriteriaUntukUpdate.setNamaKriteria(nama);

            if (dao.updateKriteria(kriteriaUntukUpdate)) {
                view.showMessage("Kriteria berhasil diubah.", false);
                loadInitialKriteria();
                view.clearInputKriteriaFields();
            } else {
                view.showMessage("Gagal mengubah kriteria.", true);
            }
        } catch (NumberFormatException e) {
             view.showMessage("ID Kriteria tidak valid.", true);
        } catch (SQLException e) {
            view.showMessage("Error database saat mengubah kriteria: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    public void hapusKriteria() {
        String idStr = view.getSelectedKriteriaIdFromTable();
        String kodeKriteria = view.getSelectedKodeKriteriaFromTable(); 

        if (idStr == null || idStr.isEmpty()) {
            view.showMessage("Pilih kriteria dari tabel yang akan dihapus.", true);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
                view, 
                "Anda yakin ingin menghapus kriteria '" + kodeKriteria + "'?\nSemua data perbandingan terkait juga akan terhapus.",
                "Konfirmasi Hapus Kriteria",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);
                if (dao.deleteKriteria(id)) {
                    view.showMessage("Kriteria berhasil dihapus.", false);
                    loadInitialKriteria();
                    view.clearInputKriteriaFields();
                } else {
                    view.showMessage("Gagal menghapus kriteria.", true);
                }
            } catch (NumberFormatException e) {
                view.showMessage("ID Kriteria tidak valid.", true);
            } catch (SQLException e) {
                view.showMessage("Error database saat menghapus kriteria: " + e.getMessage(), true);
                e.printStackTrace();
            }
        }
    }
    
    public void bersihkanInputKriteria(){
        view.clearInputKriteriaFields();
        buatKodeKriteriaOtomatis();
    }

    public void prosesInputNilaiMatriks(List<PerbandinganKriteria> daftarInputPerbandingan) {
        if (currentKriteriaList == null || currentKriteriaList.size() < 2) {
            view.showMessage("Minimal ada 2 kriteria untuk melakukan perbandingan.", true);
            return;
        }
        if (daftarInputPerbandingan == null || daftarInputPerbandingan.isEmpty()) {
             view.showMessage("Tidak ada nilai perbandingan yang diinput atau input tidak valid.", true);
            return;
        }

        int n = currentKriteriaList.size();
        double[][] matriksPerbandingan = new double[n][n];
        
        Map<Integer, Integer> kriteriaIdToIndexMap = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            kriteriaIdToIndexMap.put(currentKriteriaList.get(i).getKriteriaId(), i);
        }
        
        for (int i = 0; i < n; i++) matriksPerbandingan[i][i] = 1.0;

        for (PerbandinganKriteria pkInput : daftarInputPerbandingan) {
            Integer idx1 = kriteriaIdToIndexMap.get(pkInput.getKriteria1Id());
            Integer idx2 = kriteriaIdToIndexMap.get(pkInput.getKriteria2Id());
            double nilai = pkInput.getNilaiPerbandingan();

            if (idx1 != null && idx2 != null) {
                matriksPerbandingan[idx1][idx2] = nilai;
                matriksPerbandingan[idx2][idx1] = (nilai == 0) ? Double.POSITIVE_INFINITY : 1.0 / nilai;
            }
        }
        
        view.displayMatriksPerbandingan(matriksPerbandingan, currentKriteriaList);

        try {
            List<PerbandinganKriteria> perbandinganUntukDisimpan = new ArrayList<>();
             for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    int id1 = currentKriteriaList.get(i).getKriteriaId();
                    int id2 = currentKriteriaList.get(j).getKriteriaId();
                    double nilai = matriksPerbandingan[i][j];
                    perbandinganUntukDisimpan.add(new PerbandinganKriteria(id1, id2, nilai));
                }
            }
            dao.deleteAllPerbandingan(); 
            dao.saveOrUpdatePerbandingan(perbandinganUntukDisimpan);
            view.showMessage("Nilai perbandingan berhasil diproses dan disimpan.", false);
        } catch (SQLException e) {
            view.showMessage("Gagal menyimpan nilai perbandingan ke database: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    public void hitungBobotDanKonsistensi() {
        double[][] matriksPerbandingan = view.getMatriksPerbandinganFromTable();
        if (matriksPerbandingan == null || matriksPerbandingan.length == 0 || 
            currentKriteriaList == null || currentKriteriaList.isEmpty() || 
            currentKriteriaList.size() != matriksPerbandingan.length) {
            view.showMessage("Matriks perbandingan belum diisi atau tidak valid untuk perhitungan.", true);
            return;
        }

        int n = matriksPerbandingan.length;
        if (n == 0) {
             view.showMessage("Tidak ada kriteria untuk dihitung.", true);
            return;
        }

        double[] columnSums = new double[n];
        double[][] normalizedMatrix = new double[n][n];
        double[] priorityVector = new double[n];

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
        
        view.displayMatriksNormalisasi(normalizedMatrix, columnSums, currentKriteriaList);

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
             if (riValue == 0 && n > 2) {
                cr = (Math.abs(ci) < 0.00001) ? 0 : Double.POSITIVE_INFINITY; 
            } else if (riValue != 0) {
                cr = ci / riValue;
            } else if (n <= 2 && Math.abs(ci) < 0.00001) { 
                 cr = 0;
            } else if (n <=2 && riValue == 0) { 
                 cr = Double.POSITIVE_INFINITY;
            }
        } else if (n > randomIndex.length && randomIndex.length > 0) {
            System.err.println("Jumlah kriteria (" + n + ") melebihi data Random Index ("+randomIndex.length+"). Menggunakan RI untuk n terbesar.");
            double largestRI = randomIndex[randomIndex.length - 1];
            if (largestRI != 0) {
                 cr = ci / largestRI;
            } else {
                 cr = (Math.abs(ci) < 0.00001) ? 0 : Double.POSITIVE_INFINITY;
            }
        } else if (n == 0){
            lambdaMax = 0; ci = 0; cr = 0;
        }

        view.displayHasilPembobotan(currentKriteriaList, priorityVector);
        view.displayNilaiKonsistensi(lambdaMax, ci, cr);

        if (cr <= 0.10) {
            try {
                for (int i = 0; i < n; i++) {
                    dao.updateBobotKriteria(currentKriteriaList.get(i).getKriteriaId(), priorityVector[i]);
                }
                view.showMessage("Bobot kriteria berhasil dihitung, konsisten (CR = " + String.format("%.3f", cr) + "), dan disimpan.", false);
                loadInitialKriteria(); 
            } catch (SQLException e) {
                view.showMessage("Gagal menyimpan bobot kriteria ke database: " + e.getMessage(), true);
                e.printStackTrace();
            }
        } else {
             view.showMessage("Perbandingan TIDAK KONSISTEN (CR = " + String.format("%.3f", cr) + "). Harap perbaiki nilai perbandingan.", true);
        }
    }
}
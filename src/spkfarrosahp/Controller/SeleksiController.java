package spkfarrosahp.Controller;

import spkfarrosahp.Model.Kriteria;
import spkfarrosahp.Model.Karyawan;
import spkfarrosahp.Model.PenilaianAlternatif;
import spkfarrosahp.Model.HasilSeleksi;
import spkfarrosahp.Model.KriteriaDAO; // Untuk mengambil bobot global kriteria
import spkfarrosahp.Model.AlternatifDAO; // Untuk mengambil nilai alternatif per kriteria
import spkfarrosahp.Model.SeleksiDAO;    // Untuk menyimpan dan mengambil hasil seleksi
import spkfarrosahp.View.PanelSeleksi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.DecimalFormat; // Untuk format nilai

public class SeleksiController {
    private PanelSeleksi view;
    private KriteriaDAO kriteriaDAO;
    private AlternatifDAO alternatifDAO; // Untuk mengambil data penilaian alternatif & karyawan
    private SeleksiDAO seleksiDAO;
    private List<Karyawan> daftarKaryawan; // Menyimpan daftar karyawan yang akan diseleksi
    private List<Kriteria> daftarKriteria; // Menyimpan daftar kriteria dengan bobot globalnya
    private Map<Integer, Map<Integer, Double>> matriksNilaiAlternatif; // <KaryawanID, <KriteriaID, NilaiLokal>>
    private List<HasilSeleksi> hasilPerangkinganTerakhir; // Menyimpan hasil terakhir

    private DecimalFormat df = new DecimalFormat("#.###");


    public SeleksiController(PanelSeleksi view) {
        this.view = view;
        this.kriteriaDAO = new KriteriaDAO();
        this.alternatifDAO = new AlternatifDAO();
        this.seleksiDAO = new SeleksiDAO();
        this.hasilPerangkinganTerakhir = new ArrayList<>();
        loadInitialDataForView();
    }

    public void loadInitialDataForView() {
        try {
            // 1. Muat Bobot Global Kriteria
            daftarKriteria = kriteriaDAO.getAllKriteria();
            List<Object[]> dataBobotKriteria = new ArrayList<>();
            if (daftarKriteria != null) {
                for (Kriteria k : daftarKriteria) {
                    dataBobotKriteria.add(new Object[]{
                        k.getKodeKriteria(),
                        k.getNamaKriteria(),
                        df.format(k.getBobot()) // Ambil bobot global
                    });
                }
            }
            view.displayBobotKriteria(dataBobotKriteria);

            // 2. Muat Nilai Alternatif (Karyawan) per Kriteria
            // Ini adalah tabel 'Matriks Keputusan' di UI
            // Kita perlu mengambil semua karyawan dan semua nilai penilaian
            daftarKaryawan = alternatifDAO.getAllKaryawanAlternatif(); // Hanya kode dan nama
            List<PenilaianAlternatif> semuaPenilaian = seleksiDAO.getAllPenilaianAlternatif(); // Ambil semua nilai dari penilaian_alternatif

            // Bangun matriks nilai <KaryawanID, <KriteriaID, NilaiLokal>>
            matriksNilaiAlternatif = new HashMap<>();
            for (PenilaianAlternatif pa : semuaPenilaian) {
                matriksNilaiAlternatif.computeIfAbsent(pa.getKaryawanId(), k -> new HashMap<>())
                                      .put(pa.getKriteriaId(), pa.getNilai());
            }

            // Siapkan data untuk tabel 'Nilai Alternatif (Karyawan) per Kriteria' di view
            String[] headerMatriksKeputusan = new String[daftarKriteria.size() + 2];
            headerMatriksKeputusan[0] = "Kode Karyawan";
            headerMatriksKeputusan[1] = "Nama Karyawan";
            for (int i = 0; i < daftarKriteria.size(); i++) {
                headerMatriksKeputusan[i+2] = daftarKriteria.get(i).getKodeKriteria(); // + " (" + df.format(daftarKriteria.get(i).getBobot()) + ")";
            }
            
            List<Object[]> dataMatriksKeputusan = new ArrayList<>();
            if (daftarKaryawan != null) {
                for (Karyawan karyawan : daftarKaryawan) {
                    Object[] row = new Object[headerMatriksKeputusan.length];
                    row[0] = karyawan.getKodeKaryawan();
                    row[1] = karyawan.getNamaLengkap();
                    Map<Integer, Double> nilaiKaryawan = matriksNilaiAlternatif.get(karyawan.getKaryawanId());
                    for (int i = 0; i < daftarKriteria.size(); i++) {
                        Kriteria kriteria = daftarKriteria.get(i);
                        Double nilai = (nilaiKaryawan != null) ? nilaiKaryawan.get(kriteria.getKriteriaId()) : null;
                        row[i+2] = (nilai != null) ? df.format(nilai) : "-"; 
                    }
                    dataMatriksKeputusan.add(row);
                }
            }
            view.displayNilaiAlternatifPerKriteria(headerMatriksKeputusan, dataMatriksKeputusan);

            
            List<HasilSeleksi> hasilTersimpan = seleksiDAO.getAllHasilSeleksiTerakhir();
            view.displayHasilPerangkingan(hasilTersimpan);
            if (!hasilTersimpan.isEmpty()) {
                view.enableSimpanButton(true); 
            }


        } catch (SQLException e) {
            view.showMessage("Gagal memuat data awal untuk seleksi: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    public void hitungProsesSeleksi() {
        if (daftarKaryawan == null || daftarKaryawan.isEmpty() || 
            daftarKriteria == null || daftarKriteria.isEmpty() ||
            matriksNilaiAlternatif == null) {
            view.showMessage("Data karyawan, kriteria, atau nilai alternatif belum lengkap untuk perhitungan.", true);
            return;
        }

        // Pastikan semua kriteria memiliki bobot > 0 atau sesuai aturan
        for (Kriteria k : daftarKriteria) {
            if (k.getBobot() <= 0) {
                // view.showMessage("Bobot untuk kriteria " + k.getKodeKriteria() + " belum valid (harus > 0). Harap lakukan pembobotan AHP Kriteria terlebih dahulu.", true);
                // return; 
                // Atau beri bobot default jika memang belum ada dari AHP Kriteria, tapi idealnya AHP Kriteria dulu
            }
        }
        
        // Logika SPK (Contoh menggunakan SAW)
        // 1. Matriks Keputusan (Xij) - sudah ada di matriksNilaiAlternatif
        // 2. Normalisasi Matriks (Rij) - JIKA DIPERLUKAN. 
        //    Jika nilai di matriksNilaiAlternatif adalah hasil AHP lokal (0-1), maka normalisasi mungkin tidak perlu.
        //    Jika nilainya adalah skala mentah (misal 1-5), maka perlu normalisasi.
        //    Kita asumsikan nilai di matriksNilaiAlternatif adalah bobot lokal (0-1), jadi bisa langsung dipakai.
        
        // Untuk tampilan "Detail Perhitungan SPK", kita bisa tampilkan matriks nilai dikalikan bobot kriteria
        List<Object[]> dataDetailPerhitungan = new ArrayList<>();
        String[] headerDetail = new String[daftarKriteria.size() + 2];
        headerDetail[0] = "Kode Karyawan";
        headerDetail[1] = "Nama Karyawan";
        for(int i=0; i<daftarKriteria.size(); i++) headerDetail[i+2] = daftarKriteria.get(i).getKodeKriteria() + " (Terbobot)";


        hasilPerangkinganTerakhir.clear(); // Kosongkan hasil lama

        for (Karyawan karyawan : daftarKaryawan) {
            double nilaiTotalSPK = 0;
            Object[] rowDetail = new Object[headerDetail.length];
            rowDetail[0] = karyawan.getKodeKaryawan();
            rowDetail[1] = karyawan.getNamaLengkap();

            Map<Integer, Double> nilaiKaryawan = matriksNilaiAlternatif.get(karyawan.getKaryawanId());
            if (nilaiKaryawan == null) {
                System.err.println("Karyawan " + karyawan.getKodeKaryawan() + " tidak memiliki data nilai, akan diabaikan.");
                for(int i=0; i<daftarKriteria.size(); i++) rowDetail[i+2] = df.format(0.0); // Nilai 0 jika tidak ada
                // continue; // Abaikan karyawan ini jika tidak ada nilainya sama sekali
            }


            for (int i = 0; i < daftarKriteria.size(); i++) {
                Kriteria kriteria = daftarKriteria.get(i);
                double bobotKriteriaGlobal = kriteria.getBobot();
                // Ambil nilai lokal alternatif untuk kriteria ini
                double nilaiAlternatifLokal = (nilaiKaryawan != null && nilaiKaryawan.get(kriteria.getKriteriaId()) != null) 
                                             ? nilaiKaryawan.get(kriteria.getKriteriaId()) 
                                             : 0.0; // Jika tidak ada nilai, anggap 0

                // Untuk metode SAW sederhana: V = sum(Rij * Wj)
                // Di sini Rij adalah nilaiAlternatifLokal (yang sudah 0-1 dari AHP alternatif per kriteria)
                // Wj adalah bobotKriteriaGlobal
                double nilaiTerbobot = nilaiAlternatifLokal * bobotKriteriaGlobal;
                nilaiTotalSPK += nilaiTerbobot;
                rowDetail[i+2] = df.format(nilaiTerbobot);
            }
            dataDetailPerhitungan.add(rowDetail);
            
            HasilSeleksi hasil = new HasilSeleksi(
                karyawan.getKaryawanId(),
                karyawan.getKodeKaryawan(),
                karyawan.getNamaLengkap(),
                nilaiTotalSPK,
                0 // Peringkat akan diisi setelah diurutkan
            );
            hasilPerangkinganTerakhir.add(hasil);
        }
        
        view.displayDetailPerhitungan(headerDetail, dataDetailPerhitungan);

        // Urutkan hasil berdasarkan nilaiTotalSPK (descending)
        Collections.sort(hasilPerangkinganTerakhir, new Comparator<HasilSeleksi>() {
            @Override
            public int compare(HasilSeleksi o1, HasilSeleksi o2) {
                return Double.compare(o2.getNilaiTotalSpk(), o1.getNilaiTotalSpk()); // Descending
            }
        });

        // Set peringkat
        for (int i = 0; i < hasilPerangkinganTerakhir.size(); i++) {
            hasilPerangkinganTerakhir.get(i).setPeringkat(i + 1);
        }

        view.displayHasilPerangkingan(hasilPerangkinganTerakhir);
        view.enableSimpanButton(!hasilPerangkinganTerakhir.isEmpty()); // Aktifkan tombol simpan jika ada hasil
        view.showMessage("Proses perhitungan seleksi selesai.", false);
    }

    public void simpanHasilSeleksi() {
        if (hasilPerangkinganTerakhir == null || hasilPerangkinganTerakhir.isEmpty()) {
            view.showMessage("Tidak ada hasil seleksi untuk disimpan.", true);
            return;
        }

        try {
            if (seleksiDAO.saveHasilSeleksi(hasilPerangkinganTerakhir)) {
                view.showMessage("Hasil seleksi berhasil disimpan ke database.", false);
            } else {
                view.showMessage("Gagal menyimpan hasil seleksi.", true);
            }
        } catch (SQLException e) {
            view.showMessage("Error database saat menyimpan hasil seleksi: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }
}
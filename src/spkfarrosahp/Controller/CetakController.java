package spkfarrosahp.Controller;

import spkfarrosahp.View.PanelCetak;
import spkfarrosahp.View.DashboardForm;
import spkfarrosahp.Config.KoneksiDatabase;

import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

// Import untuk JasperReports
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JasperPrintManager;

// Import untuk format tanggal
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CetakController {
    private PanelCetak viewCetak;
    private DashboardForm viewDashboard; // Untuk mendapatkan nama admin yang login
    private String namaAdminYangMencetak;

    public CetakController(PanelCetak viewCetak, DashboardForm viewDashboard) {
        this.viewCetak = viewCetak;
        this.viewDashboard = viewDashboard;
        if (this.viewDashboard != null) {
            this.namaAdminYangMencetak = this.viewDashboard.getLoggedInAdminName();
        } else {
            this.namaAdminYangMencetak = "Admin Sistem"; // Fallback jika DashboardForm null
        }
    }

    private String getTanggalLaporanFormatted() {
        Date tanggalSekarang = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMMM yyyy", new Locale("id", "ID"));
        String tanggalTerformat = formatter.format(tanggalSekarang);
        String namaKota = "Tangerang";
        return namaKota + ", " + tanggalTerformat;
    }

    private Map<String, Object> prepareParameters(String reportTitle) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("PRINTED_BY_ADMIN", this.namaAdminYangMencetak);
        parameters.put("TANGGAL_LAPORAN", getTanggalLaporanFormatted());
        parameters.put("REPORT_TITLE", reportTitle); // Judul laporan spesifik
        
        // Contoh parameter tambahan jika diperlukan oleh .jrxml Anda
        // Misalnya, jika Anda ingin mengirim path ke logo perusahaan:
        // try {
        //     InputStream logoStream = getClass().getResourceAsStream("/spkfarrosahp/images/LogoCV.png");
        //     if (logoStream != null) {
        //         parameters.put("LOGO_PERUSAHAAN", logoStream);
        //     } else {
        //         System.err.println("Logo perusahaan tidak ditemukan.");
        //     }
        // } catch (Exception e) {
        //     System.err.println("Error memuat logo perusahaan: " + e.getMessage());
        // }
        return parameters;
    }

    private void tampilkanLaporan(String jasperFilePath, Map<String, Object> parameters, boolean langsungCetak) {
        Connection conn = null;
        try {
            InputStream jasperStream = getClass().getResourceAsStream(jasperFilePath);
            if (jasperStream == null) {
                viewCetak.showMessage("File laporan (" + jasperFilePath.substring(jasperFilePath.lastIndexOf('/') + 1) + ") tidak ditemukan di path: " + jasperFilePath, true);
                System.err.println(jasperFilePath + " tidak ditemukan di classpath.");
                return;
            }

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
            
            conn = KoneksiDatabase.configDB();
            if (conn == null) {
                viewCetak.showMessage("Koneksi ke database gagal untuk laporan.", true);
                return;
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

            if (langsungCetak) {
                boolean printDialogTampil = JasperPrintManager.printReport(jasperPrint, true);
                if (printDialogTampil) {
                    viewCetak.showMessage("Laporan dikirim ke printer.", false);
                } else {
                    // Pengguna membatalkan dialog print
                    // viewCetak.showMessage("Pencetakan dibatalkan oleh pengguna.", false); // Pesan ini mungkin tidak perlu jika dialog print sistem yang handle
                }
            } else {
                if (jasperPrint.getPages().isEmpty()) {
                    viewCetak.showMessage("Laporan tidak memiliki halaman untuk ditampilkan (kemungkinan tidak ada data).", false);
                } else {
                    JasperViewer.viewReport(jasperPrint, false); 
                }
            }

        } catch (JRException e) {
            viewCetak.showMessage("Error JasperReports: " + e.getMessage(), true);
            e.printStackTrace();
        } catch (SQLException e) {
            viewCetak.showMessage("Error Database untuk Laporan: " + e.getMessage(), true);
            e.printStackTrace();
        } catch (Exception e) { // Tangkap Exception umum lainnya
            viewCetak.showMessage("Terjadi kesalahan tak terduga: " + e.getMessage(), true);
            e.printStackTrace();
        }
        finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error menutup koneksi: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public void handlePreviewLaporan(String jenisLaporan) {
        String pathReport = "";
        String reportTitle = "";

        switch (jenisLaporan) {
            case "Laporan Data Karyawan":
                pathReport = "/spkfarrosahp/reports/LaporanKaryawan.jasper"; // Pastikan nama file .jasper benar
                reportTitle = "LAPORAN DATA KARYAWAN";
                break;
            case "Laporan Data Kriteria & Bobot":
                pathReport = "/spkfarrosahp/reports/LaporanKriteria.jasper"; // Pastikan nama file .jasper benar
                reportTitle = "LAPORAN DATA KRITERIA DAN BOBOT";
                break;
            case "Laporan Hasil Seleksi Karyawan":
                pathReport = "/spkfarrosahp/reports/LaporanHasilSeleksi.jasper"; // Pastikan nama file .jasper benar
                reportTitle = "LAPORAN HASIL SELEKSI KARYAWAN";
                break;
            default:
                viewCetak.showMessage("Jenis laporan tidak dikenal.", true);
                return;
        }
        Map<String, Object> parameters = prepareParameters(reportTitle);
        tampilkanLaporan(pathReport, parameters, false); // false untuk preview
    }

    public void handleCetakLaporan(String jenisLaporan) {
        String pathReport = "";
        String reportTitle = "";

        switch (jenisLaporan) {
            case "Laporan Data Karyawan":
                pathReport = "/spkfarrosahp/reports/LaporanKaryawan.jasper";
                reportTitle = "LAPORAN DATA KARYAWAN";
                break;
            case "Laporan Data Kriteria & Bobot":
                pathReport = "/spkfarrosahp/reports/LaporanKriteria.jasper";
                reportTitle = "LAPORAN DATA KRITERIA DAN BOBOT";
                break;
            case "Laporan Hasil Seleksi Karyawan":
                pathReport = "/spkfarrosahp/reports/LaporanHasilSeleksi.jasper";
                reportTitle = "LAPORAN HASIL SELEKSI KARYAWAN";
                break;
            default:
                viewCetak.showMessage("Jenis laporan tidak dikenal.", true);
                return;
        }
        Map<String, Object> parameters = prepareParameters(reportTitle);
        tampilkanLaporan(pathReport, parameters, true); // true untuk cetak langsung
    }

    // Jika tombol refresh di PanelCetak akan digunakan untuk memuat ulang sesuatu
    public void handleRefreshDataLaporan() {
        // Logika untuk me-refresh data yang mungkin digunakan oleh laporan
        // Karena laporan mengambil data dari DB, method ini mungkin hanya memberi notifikasi
        viewCetak.showMessage("Data terbaru akan digunakan saat laporan dibuat/dicetak.", false);
        // Atau jika ada daftar Kriteria/Karyawan yang ditampilkan di PanelCetak untuk filter,
        // bisa dimuat ulang di sini.
    }
}
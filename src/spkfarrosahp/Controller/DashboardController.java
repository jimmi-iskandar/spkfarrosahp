package spkfarrosahp.Controller;

import spkfarrosahp.View.DashboardForm;
import spkfarrosahp.View.LoginForm;
import spkfarrosahp.Config.UserSession;
import spkfarrosahp.View.PanelKaryawan;
import spkfarrosahp.View.PanelKriteriaAHP;
import spkfarrosahp.View.PanelAlternatif;
import spkfarrosahp.View.PanelSeleksi;
import spkfarrosahp.View.PanelCetak;
import spkfarrosahp.View.PanelPengaturanAdmin;
import javax.swing.JButton;
import java.awt.Component;

public class DashboardController {
    private DashboardForm viewDashboard;

    public DashboardController(DashboardForm view) {
        this.viewDashboard = view;
        navigateToDashboard();
    }

    public void navigateTo(String menuName) {
        String pageTitle = menuName;
        String cardName = "PLACEHOLDER_PANEL";

        if (viewDashboard != null) {
            JButton targetButton = viewDashboard.getMenuButtonByText(menuName);
            viewDashboard.setActiveMenuButton(targetButton);
        }

        switch (menuName) {
            case "Dashboard":
                pageTitle = "CV FARROS SABLON";
                cardName = "DASHBOARD_PANEL";
                if (viewDashboard != null) viewDashboard.setActiveMenuButton(null);
                break;
            case "Karyawan":
                pageTitle = "Manajemen Data Karyawan";
                cardName = "KARYAWAN_PANEL";
                if (viewDashboard != null) {
                    Component compKaryawan = viewDashboard.getPanelFromCardLayout("KARYAWAN_PANEL");
                    if (compKaryawan instanceof PanelKaryawan) {
                        PanelKaryawan panelKaryawan = (PanelKaryawan) compKaryawan;
                        if (panelKaryawan.getController() != null) {
                            panelKaryawan.getController().loadData();
                        }
                    }
                }
                break;
             case "Kriteria":
                pageTitle = "Manajemen Data & Bobot Kriteria (AHP)";
                cardName = "KRITERIA_PANEL";
                 if (viewDashboard != null) {
                    Component compKriteria = viewDashboard.getPanelFromCardLayout("KRITERIA_PANEL");
                    if(compKriteria instanceof PanelKriteriaAHP){
                        PanelKriteriaAHP panelKriteria = (PanelKriteriaAHP) compKriteria;
                        if(panelKriteria.getController() != null){
                           panelKriteria.getController().loadInitialKriteria();
                        }
                    }
                }
                break;
            case "Alternatif":
                pageTitle = "Penilaian Alternatif terhadap Kriteria (AHP)";
                cardName = "ALTERNATIF_PANEL";
                if (viewDashboard != null) {
                    Component compAlternatif = viewDashboard.getPanelFromCardLayout("ALTERNATIF_PANEL");
                    if (compAlternatif instanceof PanelAlternatif) {
                        PanelAlternatif panelAlternatif = (PanelAlternatif) compAlternatif;
                        if (panelAlternatif.getController() != null) {
                            panelAlternatif.getController().loadInitialData();
                        }
                    }
                }
                break;
            case "Seleksi":
                pageTitle = "Proses Seleksi & Hasil Perangkingan";
                cardName = "SELEKSI_PANEL";
                if (viewDashboard != null) {
                    Component compSeleksi = viewDashboard.getPanelFromCardLayout("SELEKSI_PANEL");
                    if(compSeleksi instanceof PanelSeleksi){
                         PanelSeleksi panelSeleksi = (PanelSeleksi) compSeleksi;
                        if(panelSeleksi.getController() != null){
                            panelSeleksi.getController().loadInitialDataForView();
                        }
                    }
                }
                break;
            case "Cetak":
                pageTitle = "Cetak Laporan";
                cardName = "CETAK_PANEL";
                if (viewDashboard != null) {
                    Component compCetak = viewDashboard.getPanelFromCardLayout("CETAK_PANEL");
                    if(compCetak instanceof PanelCetak){
                         PanelCetak panelCetak = (PanelCetak) compCetak;
                        if(panelCetak.getController() != null){
                            // panelCetak.getController().refreshDataIfNeeded(); // Jika ada
                        }
                    }
                }
                break;
            case "Pengaturan Admin":
                pageTitle = "Pengaturan Akun Admin";
                cardName = "PENGATURAN_ADMIN_PANEL";
                if (viewDashboard != null) {
                    Component compPengaturan = viewDashboard.getPanelFromCardLayout("PENGATURAN_ADMIN_PANEL");
                    if (compPengaturan instanceof PanelPengaturanAdmin) {
                        PanelPengaturanAdmin panelPengaturan = (PanelPengaturanAdmin) compPengaturan;
                        if (panelPengaturan.getController() != null) {
                            panelPengaturan.getController().loadLoggedInAdminData();
                        }
                    }
                }
                break;
            default:
                pageTitle = "Halaman Tidak Ditemukan";
                cardName = "PLACEHOLDER_PANEL";
                if (viewDashboard != null) viewDashboard.setActiveMenuButton(null);
                break;
        }
        if (viewDashboard != null) {
            viewDashboard.setPageTitle(pageTitle);
            viewDashboard.showView(cardName);
        }
    }
    
    public void navigateToDashboard() {
        if (viewDashboard != null) {
            viewDashboard.setPageTitle("CV FARROS SABLON");
            viewDashboard.showView("DASHBOARD_PANEL"); 
            viewDashboard.setActiveMenuButton(null); 
        }
    }

    public void handleLogout() {
        if (viewDashboard != null) {
            UserSession.logout(); 
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
            viewDashboard.dispose(); 
        }
    }
}
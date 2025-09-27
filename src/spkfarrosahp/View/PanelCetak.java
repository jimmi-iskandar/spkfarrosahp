package spkfarrosahp.View;
import spkfarrosahp.Controller.CetakController;
import spkfarrosahp.Config.UserSession; 
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder; 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area; 
import java.awt.geom.RoundRectangle2D; 
import java.io.IOException; 
import java.net.URL;       
import javax.imageio.ImageIO; 

public class PanelCetak extends JPanel {

    // Warna
    private static final Color WARNA_FRAME_FALLBACK = new Color(45, 52, 71);
    private static final Color WARNA_TOP_BAR_PANEL = new Color(220, 230, 245); 
    private static final Color WARNA_KONTEN_UTAMA_PANEL = Color.WHITE;      
    private static final Color WARNA_TEKS_TOP_BAR = new Color(60, 60, 60);
    private static final Color WARNA_TOMBOL_UTAMA_BG = new Color(0, 123, 255); // Biru untuk tombol utama
    private static final Color WARNA_TOMBOL_UTAMA_FG = Color.WHITE;
    private static final Color WARNA_TOMBOL_REFRESH_BG = new Color(23, 162, 184); 
    private static final Color WARNA_TOMBOL_REFRESH_FG = Color.WHITE;
    private static final Color WARNA_BORDER_FIELD = new Color(200,200,200); // Untuk border field


    // Path Resource untuk background panel cetak
    private static final String PANEL_BACKGROUND_RESOURCE_PATH = "/spkfarrosahp/images/bglogin.png"; // Ganti dengan gambar Anda

    private JComboBox<String> cmbJenisLaporan;
    private JButton btnRefreshDataCetak; 
    private JButton btnTampilkanLaporan; 

    private CetakController controller;
    private DashboardForm dashboardForm; 
    private Image panelBackgroundImage;

    public PanelCetak(DashboardForm dashboardForm) {
        this.dashboardForm = dashboardForm; 
        setLayout(new GridBagLayout()); 
        
        loadPanelResources();
        initComponents();
        this.controller = new CetakController(this, this.dashboardForm); 
    }
    
    private void loadPanelResources() {
        try {
            URL panelBgUrl = PanelCetak.class.getResource(PANEL_BACKGROUND_RESOURCE_PATH);
            if (panelBgUrl != null) {
                panelBackgroundImage = ImageIO.read(panelBgUrl);
            } else {
                System.err.println("Resource background Panel Cetak tidak ditemukan: " + PANEL_BACKGROUND_RESOURCE_PATH);
                panelBackgroundImage = null; 
            }
        } catch (IOException e) {
            System.err.println("Tidak dapat memuat gambar background Panel Cetak: " + e.getMessage());
            panelBackgroundImage = null;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (panelBackgroundImage != null) {
            int panelWidth = getWidth(); int panelHeight = getHeight();
            int imgWidth = panelBackgroundImage.getWidth(this); int imgHeight = panelBackgroundImage.getHeight(this);
            if (imgWidth <= 0 || imgHeight <= 0) {
                g.setColor(WARNA_FRAME_FALLBACK); g.fillRect(0, 0, panelWidth, panelHeight); return;
            }
            double imgAspect = (double) imgHeight / imgWidth; double panelAspect = (double) panelHeight / panelWidth;
            int newImgWidth, newImgHeight, x = 0, y = 0;
            if (imgAspect < panelAspect) { 
                newImgHeight = panelHeight; newImgWidth = (int) (panelHeight / imgAspect); x = (panelWidth - newImgWidth) / 2;
            } else { 
                newImgWidth = panelWidth; newImgHeight = (int) (panelWidth * imgAspect); y = (panelHeight - newImgHeight) / 2;
            }
            g.drawImage(panelBackgroundImage, x, y, newImgWidth, newImgHeight, this);
        } else {
            g.setColor(WARNA_FRAME_FALLBACK); 
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    

    private void initComponents() {
        MainUIPanel contentPanel = new MainUIPanel(20, 20); 
        contentPanel.setLayout(new BorderLayout(0,10)); // Top bar dan form area
        contentPanel.setPreferredSize(new Dimension(500, 250)); // Ukuran panel tengah disesuaikan
        contentPanel.setOpaque(false);

        // 1. Top Bar Area (di dalam MainUIPanel)
        JPanel topBarArea = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        topBarArea.setOpaque(false); 
        topBarArea.setPreferredSize(new Dimension(0, 55)); // Tinggi top bar
        JLabel lblPanelTitle = new JLabel("Cetak Laporan"); 
        lblPanelTitle.setFont(new Font("Arial", Font.BOLD, 20)); // Font lebih besar
        lblPanelTitle.setForeground(WARNA_TEKS_TOP_BAR);
        topBarArea.add(lblPanelTitle);
        contentPanel.add(topBarArea, BorderLayout.NORTH);

        // 2. Form Area (di dalam MainUIPanel)
        JPanel formArea = new JPanel(new GridBagLayout());
        formArea.setOpaque(false); 
        formArea.setBorder(new EmptyBorder(15, 25, 20, 25)); 
        
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.insets = new Insets(8, 5, 8, 5); // Jarak antar komponen

        // Baris 1: Pilih Jenis Laporan (ComboBox dan Tombol Refresh)
        gbcForm.gridx = 0; gbcForm.gridy = 0; 
        gbcForm.anchor = GridBagConstraints.LINE_END;
        JLabel lblJenis = new JLabel("Jenis Laporan:");
        styleLabel(lblJenis);
        formArea.add(lblJenis, gbcForm);

        gbcForm.gridx = 1; 
        gbcForm.weightx = 1.0; // ComboBox mengambil sisa lebar
        gbcForm.anchor = GridBagConstraints.LINE_START; 
        cmbJenisLaporan = new JComboBox<>();
        cmbJenisLaporan.setFont(new Font("Arial", Font.PLAIN, 13));
        cmbJenisLaporan.addItem("Laporan Data Karyawan");
        cmbJenisLaporan.addItem("Laporan Data Kriteria & Bobot");
        cmbJenisLaporan.addItem("Laporan Hasil Seleksi Karyawan");
        cmbJenisLaporan.setPreferredSize(new Dimension(250, 30)); 
        formArea.add(cmbJenisLaporan, gbcForm);

        gbcForm.gridx = 2; 
        gbcForm.weightx = 0.0; // Tombol refresh tidak expand
        gbcForm.fill = GridBagConstraints.NONE; 
        gbcForm.anchor = GridBagConstraints.LINE_START;
        btnRefreshDataCetak = new JButton("Refresh");
        styleButton(btnRefreshDataCetak, WARNA_TOMBOL_REFRESH_BG, WARNA_TOMBOL_REFRESH_FG, new Dimension(90, 30)); // Ukuran tombol disesuaikan
        btnRefreshDataCetak.addActionListener(e -> {
            if (controller != null) {
                controller.handleRefreshDataLaporan(); 
            }
        });
        formArea.add(btnRefreshDataCetak, gbcForm);


        // Baris 2: Tombol Aksi Utama (Tampilkan Laporan)
        gbcForm.gridx = 0; gbcForm.gridy = 1; 
        gbcForm.gridwidth = 3; // Span 3 kolom
        gbcForm.anchor = GridBagConstraints.CENTER; 
        gbcForm.fill = GridBagConstraints.NONE;
        gbcForm.insets = new Insets(25, 5, 5, 5); // Jarak atas lebih besar
        
        btnTampilkanLaporan = new RoundedButton("Tampilkan Laporan (Preview)", WARNA_TOMBOL_UTAMA_BG, WARNA_TOMBOL_UTAMA_FG, 15); 
        btnTampilkanLaporan.setFont(new Font("Arial", Font.BOLD, 14));
        btnTampilkanLaporan.setPreferredSize(new Dimension(300, 40)); // Ukuran tombol disesuaikan
        btnTampilkanLaporan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller != null) {
                    String selectedReport = (String) cmbJenisLaporan.getSelectedItem();
                    if (selectedReport != null && !selectedReport.isEmpty()) {
                        controller.handlePreviewLaporan(selectedReport); 
                    } else {
                        showMessage("Pilih jenis laporan terlebih dahulu.", true);
                    }
                }
            }
        });
        formArea.add(btnTampilkanLaporan, gbcForm);
        
        contentPanel.add(formArea, BorderLayout.CENTER);

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.anchor = GridBagConstraints.CENTER;
        add(contentPanel, gbcMain); 
    }
    
    private void styleLabel(JLabel label) { // Helper untuk style label
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        // label.setForeground(WARNA_TEKS_LABEL); // Jika ada warna khusus
    }

    private void styleButton(JButton button, Color bg, Color fg, Dimension size) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Arial", Font.BOLD, 12)); // Ukuran font tombol disesuaikan
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(7, 15, 7, 15)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (size != null) {
            button.setPreferredSize(size);
        }
    }

    public void showMessage(String message, boolean isError) {
        if (isError) JOptionPane.showMessageDialog(this, message, "Error Cetak Laporan", JOptionPane.ERROR_MESSAGE);
        else JOptionPane.showMessageDialog(this, message, "Informasi Cetak Laporan", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public JComboBox<String> getCmbJenisLaporan() {
        return cmbJenisLaporan;
    }
    
    public CetakController getController() {
        return this.controller;
    }
    
    public void refreshDataIfNeeded() {
        if (controller != null) {
            controller.handleRefreshDataLaporan();
        }
    }

    // Inner class untuk panel konten dengan sudut membulat
    class MainUIPanel extends JPanel {
        private int arcWidth, arcHeight;
        public MainUIPanel(int arcW, int arcH) {
            this.arcWidth = arcW; this.arcHeight = arcH; setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int panelWidth = getWidth(); int panelHeight = getHeight(); int topBarHeight = 55; // Tinggi top bar
            
            Shape overallPanelShape = new RoundRectangle2D.Float(0, 0, panelWidth -1 , panelHeight -1 , arcWidth, arcHeight); // -1 agar border terlihat
            
            Area topPart = new Area(overallPanelShape);
            topPart.intersect(new Area(new Rectangle(0, 0, panelWidth, topBarHeight)));
            g2d.setColor(WARNA_TOP_BAR_PANEL); 
            g2d.fill(topPart);
            
            Area contentPart = new Area(overallPanelShape);
            contentPart.intersect(new Area(new Rectangle(0, topBarHeight, panelWidth, panelHeight - topBarHeight)));
            g2d.setColor(WARNA_KONTEN_UTAMA_PANEL); 
            g2d.fill(contentPart);

            // Optional: Tambahkan border untuk panel utama
            g2d.setColor(new Color(200,200,200)); // Warna border
            g2d.draw(overallPanelShape);
            
            g2d.dispose();
        }
         @Override
        protected void paintChildren(Graphics g) { 
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setClip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arcWidth, arcHeight));
            super.paintChildren(g2d);
            g2d.dispose();
        }
    }
    
    // Inner class RoundedButton (jika ingin tombol custom)
    class RoundedButton extends JButton {
        private Color buttonBackgroundColor, buttonForegroundColor; private int arc;
        public RoundedButton(String text, Color bgColor, Color fgColor, int arc) {
            super(text); this.buttonBackgroundColor = bgColor; this.buttonForegroundColor = fgColor; this.arc = arc;
            setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false); setForeground(this.buttonForegroundColor);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isArmed()) g2.setColor(buttonBackgroundColor.darker());
            else if (getModel().isRollover()) g2.setColor(buttonBackgroundColor.brighter());
            else g2.setColor(buttonBackgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
            //AWALCEK SESI LOGIN
            if (!UserSession.isLoggedIn()) {
               
                System.out.println("PanelKaryawan.main(): Tidak ada sesi login, mengarahkan ke LoginForm.");
                new LoginForm().setVisible(true);
                return; // Hentikan pembuatan frame PanelKaryawan jika tidak ada sesi
            }
            // --- AKHIR PENGECEKAN SESI LOGIN ---
            JFrame frame = new JFrame("Panel Cetak Laporan");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            DashboardForm dummyDashboard = new DashboardForm("Admin Test untuk Cetak"); 
            PanelCetak panel = new PanelCetak(dummyDashboard); 
            
            frame.getContentPane().add(panel);
            frame.setSize(new Dimension(800, 600));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
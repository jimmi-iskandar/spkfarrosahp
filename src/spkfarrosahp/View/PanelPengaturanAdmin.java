package spkfarrosahp.View;
import spkfarrosahp.Controller.PengaturanAdminController;
import spkfarrosahp.View.DashboardForm; 
import spkfarrosahp.Model.Admin;
import spkfarrosahp.Config.UserSession;
import spkfarrosahp.Model.Admin;  
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D; 
import java.io.IOException; 
import java.net.URL;      
import javax.imageio.ImageIO; 
import javax.swing.border.Border;

public class PanelPengaturanAdmin extends JPanel { // Tetap JPanel, paintComponent akan di-override

    // Warna (disesuaikan dengan gaya form login)
    private static final Color WARNA_FRAME_FALLBACK = new Color(45, 52, 71); // Background jika gambar gagal
    private static final Color WARNA_TOP_BAR_PANEL = new Color(220, 230, 245); // Warna top bar panel pengaturan
    private static final Color WARNA_KONTEN_UTAMA_PANEL = Color.WHITE;      // Warna konten panel pengaturan
    private static final Color WARNA_TEKS_TOP_BAR = new Color(60, 60, 60);
    private static final Color WARNA_BORDER = new Color(220, 225, 230); // Untuk border field jika perlu
    private static final Color WARNA_TEKS_LABEL = new Color(80, 80, 110);
    private static final Color WARNA_TOMBOL_SIMPAN_BG = new Color(40, 167, 69); // Hijau
    private static final Color WARNA_TOMBOL_SIMPAN_FG = Color.WHITE;
    private static final Color WARNA_READ_ONLY_FIELD_BG = new Color(238, 238, 238);


    // Path Resource untuk background panel pengaturan (GANTI DENGAN GAMBAR ANDA)
    private static final String PANEL_BACKGROUND_RESOURCE_PATH = "/spkfarrosahp/images/bglogin.png"; // Contoh, ganti ini

    private JTextField txtUsername;
    private JPasswordField pfPasswordLama;
    private JPasswordField pfPasswordBaru;
    private JPasswordField pfKonfirmasiPasswordBaru;
    private JButton btnSimpanPassword;

    private PengaturanAdminController controller;
    private DashboardForm dashboardForm; 
    private Image panelBackgroundImage;

    public PanelPengaturanAdmin(DashboardForm dashboardForm) {
        this.dashboardForm = dashboardForm; 
        setLayout(new GridBagLayout()); // Layout utama untuk menengahkan panel konten
        // setBackground tidak perlu, akan di-handle paintComponent
        
        loadPanelResources();
        initComponents();
        this.controller = new PengaturanAdminController(this);
    }
    
    private void loadPanelResources() {
        try {
            URL panelBgUrl = PanelPengaturanAdmin.class.getResource(PANEL_BACKGROUND_RESOURCE_PATH);
            if (panelBgUrl != null) {
                panelBackgroundImage = ImageIO.read(panelBgUrl);
            } else {
                System.err.println("Resource background Panel Pengaturan Admin tidak ditemukan: " + PANEL_BACKGROUND_RESOURCE_PATH);
                panelBackgroundImage = null; 
            }
        } catch (IOException e) {
            System.err.println("Tidak dapat memuat gambar background Panel Pengaturan Admin: " + e.getMessage());
            panelBackgroundImage = null;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (panelBackgroundImage != null) {
            // Logika scaling "cover"
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
        MainUIPanel contentPanel = new MainUIPanel(20, 20); // arcWidth, arcHeight
        contentPanel.setLayout(new BorderLayout(0,10)); 
        contentPanel.setPreferredSize(new Dimension(450, 420)); // Ukuran panel tengah
        contentPanel.setOpaque(false);

        // 1. Top Bar Area (di dalam MainUIPanel)
        JPanel topBarArea = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        topBarArea.setOpaque(false); 
        topBarArea.setPreferredSize(new Dimension(0, 50)); 
        JLabel lblPanelTitle = new JLabel("Ubah Password Akun"); // Judul di dalam panel
        lblPanelTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblPanelTitle.setForeground(WARNA_TEKS_TOP_BAR);
        topBarArea.add(lblPanelTitle);
        contentPanel.add(topBarArea, BorderLayout.NORTH);

        // 2. Form Area (di dalam MainUIPanel)
        JPanel formArea = new JPanel(new GridBagLayout());
        formArea.setOpaque(false); 
        formArea.setBorder(new EmptyBorder(15, 25, 20, 25)); 
        
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.insets = new Insets(7, 5, 7, 5); 
        gbcForm.anchor = GridBagConstraints.WEST;

        // Username
        gbcForm.gridx = 0; gbcForm.gridy = 0; gbcForm.gridwidth = 2;
        formArea.add(createLabel("Username:"), gbcForm);
        gbcForm.gridy++;
        txtUsername = new JTextField(20);
        txtUsername.setEditable(false);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 13));
        txtUsername.setBackground(WARNA_READ_ONLY_FIELD_BG);
        txtUsername.setBorder(createFieldBorder());
        formArea.add(txtUsername, gbcForm);

        // Password Lama
        gbcForm.gridy++;
        formArea.add(createLabel("Password Lama:"), gbcForm);
        gbcForm.gridy++;
        pfPasswordLama = createPasswordField();
        formArea.add(pfPasswordLama, gbcForm);

        // Password Baru
        gbcForm.gridy++;
        formArea.add(createLabel("Password Baru:"), gbcForm);
        gbcForm.gridy++;
        pfPasswordBaru = createPasswordField();
        formArea.add(pfPasswordBaru, gbcForm);

        // Konfirmasi Password Baru
        gbcForm.gridy++;
        formArea.add(createLabel("Konfirmasi Password Baru:"), gbcForm);
        gbcForm.gridy++;
        pfKonfirmasiPasswordBaru = createPasswordField();
        formArea.add(pfKonfirmasiPasswordBaru, gbcForm);

        // Tombol Simpan
        gbcForm.gridy++; 
        gbcForm.gridwidth = 2; 
        gbcForm.anchor = GridBagConstraints.CENTER; 
        gbcForm.fill = GridBagConstraints.NONE; 
        gbcForm.insets = new Insets(25, 5, 5, 5); 
        
        btnSimpanPassword = new RoundedButton("Simpan Perubahan Password", WARNA_TOMBOL_SIMPAN_BG, WARNA_TOMBOL_SIMPAN_FG, 20);
        btnSimpanPassword.setFont(new Font("Arial", Font.BOLD, 14));
        btnSimpanPassword.setPreferredSize(new Dimension(280, 40)); // Ukuran tombol disesuaikan
        btnSimpanPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller != null) {
                    controller.ubahPassword(
                            new String(pfPasswordLama.getPassword()),
                            new String(pfPasswordBaru.getPassword()),
                            new String(pfKonfirmasiPasswordBaru.getPassword())
                    );
                }
            }
        });
        formArea.add(btnSimpanPassword, gbcForm);
        
        contentPanel.add(formArea, BorderLayout.CENTER);

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0; gbcMain.gridy = 0;
        gbcMain.anchor = GridBagConstraints.CENTER;
        add(contentPanel, gbcMain); 
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(WARNA_TEKS_LABEL);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private Border createFieldBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,200,200), 1), // Warna border field
            new EmptyBorder(5, 8, 5, 8)
        );
    }
    
    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordField.setBorder(createFieldBorder());
        return passwordField;
    }

    private void styleButton(JButton button, Color bg, Color fg, Dimension size) { // Method ini mungkin tidak terpakai jika RoundedButton digunakan
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 18, 8, 18)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (size != null) {
            button.setPreferredSize(size);
        }
    }

    public void setUsername(String username) {
        if (txtUsername != null) {
            txtUsername.setText(username);
        }
    }

    public void clearPasswordFields() {
        if (pfPasswordLama != null) pfPasswordLama.setText("");
        if (pfPasswordBaru != null) pfPasswordBaru.setText("");
        if (pfKonfirmasiPasswordBaru != null) pfKonfirmasiPasswordBaru.setText("");
    }

    public void showMessage(String message, boolean isError) {
        if (isError) {
            JOptionPane.showMessageDialog(this, message, "Error Pengaturan Akun", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, message, "Informasi Pengaturan Akun", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public PengaturanAdminController getController() {
        return this.controller;
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
            int panelWidth = getWidth(); int panelHeight = getHeight(); int topBarHeight = 60; 
            
            Shape overallPanelShape = new RoundRectangle2D.Float(0, 0, panelWidth, panelHeight, arcWidth, arcHeight);
            
            Area topPart = new Area(overallPanelShape);
            topPart.intersect(new Area(new Rectangle(0, 0, panelWidth, topBarHeight)));
            g2d.setColor(WARNA_TOP_BAR_PANEL); 
            g2d.fill(topPart);
            
            Area contentPart = new Area(overallPanelShape);
            contentPart.intersect(new Area(new Rectangle(0, topBarHeight, panelWidth, panelHeight - topBarHeight)));
            g2d.setColor(WARNA_KONTEN_UTAMA_PANEL); 
            g2d.fill(contentPart);
            
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
        //AWALCEK SESI LOGIN
            if (!UserSession.isLoggedIn()) {
               
                System.out.println("PanelKaryawan.main(): Tidak ada sesi login, mengarahkan ke LoginForm.");
                new LoginForm().setVisible(true);
                return; // Hentikan pembuatan frame PanelKaryawan jika tidak ada sesi
            }
            // --- AKHIR PENGECEKAN SESI LOGIN ---
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
                // Inisialisasi UserSession dengan admin dummy untuk testing
                Admin dummyAdminForTest = new Admin();
                dummyAdminForTest.setAdminId(1); 
                dummyAdminForTest.setUsername("testadmin");
                dummyAdminForTest.setNamaLengkap("Admin Tester");
                UserSession.login(dummyAdminForTest, dummyAdminForTest.getNamaLengkap());

            } catch (Exception e) {
                e.printStackTrace();
            }
            
            JFrame frame = new JFrame("Panel Pengaturan Admin - Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            DashboardForm dummyDashboard = new DashboardForm("Admin Test untuk Pengaturan"); 
            PanelPengaturanAdmin panel = new PanelPengaturanAdmin(dummyDashboard);
            
            frame.getContentPane().add(panel); // PanelPengaturanAdmin sekarang adalah ImageBackgroundPanel
            frame.setSize(new Dimension(800, 600));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
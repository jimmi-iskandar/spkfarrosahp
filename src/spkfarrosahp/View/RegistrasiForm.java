package spkfarrosahp.View;

import spkfarrosahp.Controller.RegistrasiController;
import spkfarrosahp.Config.UserSession;
import spkfarrosahp.Model.Admin; // Untuk dummy admin di main() jika perlu

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class RegistrasiForm extends JFrame {

    // Warna
    private static final Color WARNA_FRAME_FALLBACK = new Color(45, 52, 71);
    private static final Color WARNA_TOP_BAR_PANEL = new Color(200, 215, 230);
    private static final Color WARNA_KONTEN_UTAMA_PANEL = Color.WHITE;
    private static final Color WARNA_TEKS_TOP_BAR = new Color(60, 60, 60);
    private static final Color WARNA_REGISTRASI_BUTTON_BG = new Color(140, 198, 63);
    private static final Color WARNA_REGISTRASI_BUTTON_FG = new Color(30, 30, 30);
    private static final Color WARNA_KEMBALI_BUTTON_BG = new Color(0, 70, 130);
    private static final Color WARNA_KEMBALI_BUTTON_FG = Color.WHITE;
    private static final Color WARNA_CHECKBOX_TEXT = new Color(50,50,50);

    // Path Resource
    private static final String LOGO_RESOURCE_PATH = "/spkfarrosahp/images/LogoCV.png";
    private static final String FRAME_BACKGROUND_RESOURCE_PATH = "/spkfarrosahp/images/bglogin.png";

    private JTextField namaLengkapField;
    private JTextField usernameField;
    private JTextField emailField; // <-- FIELD EMAIL BARU
    private JPasswordField passwordField;
    private JPasswordField konfirmasiPasswordField;
    private JCheckBox showPasswordCheckBox;
    private JButton registrasiButton;
    private JButton kembaliButton;

    private Image logoImage;
    private Image frameBackgroundImage;
    private char defaultEchoCharPassword;
    private char defaultEchoCharKonfirmasi;

    private RegistrasiController controller;

    public RegistrasiForm() {
        this.controller = new RegistrasiController(this);

        try {
            URL logoUrl = RegistrasiForm.class.getResource(LOGO_RESOURCE_PATH);
            if (logoUrl != null) {
                logoImage = ImageIO.read(logoUrl).getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            } else {
                System.err.println("Resource logo tidak ditemukan: " + LOGO_RESOURCE_PATH);
            }
        } catch (IOException e) {
            System.err.println("Tidak dapat memuat logo: " + e.getMessage());
        }

        try {
            URL frameBgUrl = RegistrasiForm.class.getResource(FRAME_BACKGROUND_RESOURCE_PATH);
            if (frameBgUrl != null) {
                frameBackgroundImage = ImageIO.read(frameBgUrl);
            } else {
                System.err.println("Resource background frame tidak ditemukan: " + FRAME_BACKGROUND_RESOURCE_PATH);
            }
        } catch (IOException e) {
            System.err.println("Tidak dapat memuat gambar background frame: " + e.getMessage() + ". Menggunakan warna fallback.");
        }

        setTitle("Registrasi Admin Baru");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageBackgroundPanel mainContentPane = new ImageBackgroundPanel(frameBackgroundImage, new GridBagLayout());
        setContentPane(mainContentPane);

        MainUIPanel registrasiPanel = new MainUIPanel(25, 25);
        registrasiPanel.setLayout(new BorderLayout(0,10));
        registrasiPanel.setPreferredSize(new Dimension(430, 650)); // Tinggikan sedikit untuk field email
        registrasiPanel.setOpaque(false);

        JPanel topBarArea = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        topBarArea.setOpaque(false);
        topBarArea.setPreferredSize(new Dimension(0, 60));
        JLabel registrasiLabelTitle = new JLabel("Registrasi Admin");
        registrasiLabelTitle.setFont(new Font("Arial", Font.BOLD, 20));
        registrasiLabelTitle.setForeground(WARNA_TEKS_TOP_BAR);
        topBarArea.add(registrasiLabelTitle);
        registrasiPanel.add(topBarArea, BorderLayout.NORTH);

        JPanel formArea = new JPanel(new GridBagLayout());
        formArea.setOpaque(false);
        formArea.setBorder(new EmptyBorder(10, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 5, 7, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        if (logoImage != null) {
            JLabel logoLabelInternal = new JLabel(new ImageIcon(logoImage));
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
            gbc.insets = new Insets(0, 0, 15, 0);
            formArea.add(logoLabelInternal, gbc);
            gbc.gridwidth = 1; gbc.insets = new Insets(7, 5, 7, 5);
            gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.HORIZONTAL;
        }
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        formArea.add(createLabel("Nama Lengkap:"), gbc);
        gbc.gridy++;
        namaLengkapField = createTextField();
        formArea.add(namaLengkapField, gbc);

        gbc.gridy++;
        formArea.add(createLabel("Username:"), gbc);
        gbc.gridy++;
        usernameField = createTextField();
        formArea.add(usernameField, gbc);
        
        // --- TAMBAHKAN FIELD EMAIL ---
        gbc.gridy++;
        formArea.add(createLabel("Email:"), gbc);
        gbc.gridy++;
        emailField = createTextField(); // Gunakan createTextField() yang sudah ada
        formArea.add(emailField, gbc);
        // --- AKHIR TAMBAHAN FIELD EMAIL ---
        
        gbc.gridy++;
        formArea.add(createLabel("Password:"), gbc);
        gbc.gridy++;
        passwordField = createPasswordField();
        defaultEchoCharPassword = passwordField.getEchoChar();
        formArea.add(passwordField, gbc);

        gbc.gridy++;
        formArea.add(createLabel("Konfirmasi Password:"), gbc);
        gbc.gridy++;
        konfirmasiPasswordField = createPasswordField();
        defaultEchoCharKonfirmasi = konfirmasiPasswordField.getEchoChar();
        formArea.add(konfirmasiPasswordField, gbc);

        gbc.gridy++; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 5, 15, 5);
        showPasswordCheckBox = new JCheckBox("Tampilkan Password");
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
        showPasswordCheckBox.setOpaque(false);
        showPasswordCheckBox.setForeground(WARNA_CHECKBOX_TEXT);
        showPasswordCheckBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                passwordField.setEchoChar((char) 0);
                konfirmasiPasswordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar(defaultEchoCharPassword);
                konfirmasiPasswordField.setEchoChar(defaultEchoCharKonfirmasi);
            }
        });
        formArea.add(showPasswordCheckBox, gbc);
        gbc.insets = new Insets(7, 5, 7, 5); 
        gbc.anchor = GridBagConstraints.CENTER;

        // --- PERUBAHAN PENEMPATAN TOMBOL ---
        gbc.gridy++; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; // Agar tombol tidak stretch
        gbc.insets = new Insets(15, 5, 8, 5); 
        registrasiButton = new RoundedButton("Registrasi", WARNA_REGISTRASI_BUTTON_BG, WARNA_REGISTRASI_BUTTON_FG, 20);
        registrasiButton.setFont(new Font("Arial", Font.BOLD, 14));
        registrasiButton.setPreferredSize(new Dimension(200, 40)); // Ukuran preferensi tombol
        registrasiButton.addActionListener(e -> {
            controller.prosesRegistrasi(
                namaLengkapField.getText(),
                usernameField.getText(),
                emailField.getText(), // <-- TAMBAHKAN EMAIL KE PEMANGGILAN
                new String(passwordField.getPassword()),
                new String(konfirmasiPasswordField.getPassword())
            );
        });
        formArea.add(registrasiButton, gbc);

        gbc.gridy++; 
        gbc.insets = new Insets(5, 5, 15, 5);
        kembaliButton = new RoundedButton("Ke Login", WARNA_KEMBALI_BUTTON_BG, WARNA_KEMBALI_BUTTON_FG, 20);
        kembaliButton.setFont(new Font("Arial", Font.BOLD, 14));
        kembaliButton.setPreferredSize(new Dimension(200, 35)); // Ukuran preferensi tombol
        kembaliButton.addActionListener(e -> controller.kembaliKeLogin());
        formArea.add(kembaliButton, gbc);
        // --- AKHIR PERUBAHAN PENEMPATAN TOMBOL ---
        
        registrasiPanel.add(formArea, BorderLayout.CENTER);

        GridBagConstraints gbcMainPanel = new GridBagConstraints();
        gbcMainPanel.anchor = GridBagConstraints.CENTER;
        gbcMainPanel.insets = new Insets(20, 20, 20, 20);
        mainContentPane.add(registrasiPanel, gbcMainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        return passwordField;
    }

    public void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccessMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void clearPasswordFields() {
        if (passwordField != null) passwordField.setText("");
        if (konfirmasiPasswordField != null) konfirmasiPasswordField.setText("");
    }
    
    public void clearAllFields() {
        if (namaLengkapField != null) namaLengkapField.setText("");
        if (usernameField != null) usernameField.setText("");
        if (emailField != null) emailField.setText(""); // <-- BERSIHKAN FIELD EMAIL
        clearPasswordFields();
        if (showPasswordCheckBox != null) showPasswordCheckBox.setSelected(false);
    }

    public void requestUsernameFocus() {
        if (usernameField != null) usernameField.requestFocusInWindow();
    }
    
    // --- TAMBAHKAN GETTER UNTUK EMAIL JIKA DIPERLUKAN CONTROLLER ---
    public String getEmailFieldValue() {
        return emailField != null ? emailField.getText() : "";
    }
    // --- AKHIR TAMBAHAN ---

    // Inner class (ImageBackgroundPanel, MainUIPanel, RoundedButton SAMA seperti sebelumnya)
    class ImageBackgroundPanel extends JPanel {
        private Image image;
        public ImageBackgroundPanel(Image image, LayoutManager layout) {
            super(layout); this.image = image;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                int panelWidth = getWidth(); int panelHeight = getHeight();
                int imgWidth = image.getWidth(this); int imgHeight = image.getHeight(this);
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
                g.drawImage(image, x, y, newImgWidth, newImgHeight, this);
            } else {
                g.setColor(WARNA_FRAME_FALLBACK); g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

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
            Shape overallPanelShape = new RoundRectangle2D.Float(0, 0, panelWidth -1 , panelHeight -1 , arcWidth, arcHeight);
            Area topPart = new Area(overallPanelShape);
            topPart.intersect(new Area(new Rectangle(0, 0, panelWidth, topBarHeight)));
            g2d.setColor(WARNA_TOP_BAR_PANEL); 
            g2d.fill(topPart);
            Area contentPart = new Area(overallPanelShape);
            contentPart.intersect(new Area(new Rectangle(0, topBarHeight, panelWidth, panelHeight - topBarHeight)));
            g2d.setColor(WARNA_KONTEN_UTAMA_PANEL); 
            g2d.fill(contentPart);
            g2d.setColor(new Color(200,200,200)); 
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
            } catch (Exception e) { 
                System.err.println("Nimbus L&F not found.");
            }
            new RegistrasiForm().setVisible(true);
        });
    }
}
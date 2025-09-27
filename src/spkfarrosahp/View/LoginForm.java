package spkfarrosahp.View;

import spkfarrosahp.Controller.LoginController; 

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class LoginForm extends JFrame {

    
    private static final Color WARNA_FRAME_FALLBACK = new Color(45, 52, 71);
    private static final Color WARNA_TOP_BAR_PANEL_LOGIN = new Color(220, 230, 245);
    private static final Color WARNA_KONTEN_UTAMA_PANEL_LOGIN = Color.WHITE;
    private static final Color WARNA_TEKS_TOP_BAR = new Color(70, 70, 70);
    private static final Color WARNA_LOGIN_BUTTON_BG = new Color(0, 70, 130);
    private static final Color WARNA_LOGIN_BUTTON_FG = Color.WHITE;
    private static final Color WARNA_REGISTRASI_BUTTON_BG = new Color(140, 198, 63);
    private static final Color WARNA_REGISTRASI_BUTTON_FG = new Color(30, 30, 30);
    private static final Color WARNA_CHECKBOX_TEXT = new Color(50,50,50);

    
    private static final String LOGO_RESOURCE_PATH = "/spkfarrosahp/images/LogoCV.png";
    private static final String FRAME_BACKGROUND_RESOURCE_PATH = "/spkfarrosahp/images/bglogin.png";

    private JTextField usernameField;
    private JPasswordField passwordField;
    private RoundedButton loginButton;
    private RoundedButton registrasiButton;
    private JCheckBox showPasswordCheckBox;
    private Image logoImage;
    private Image frameBackgroundImage;
    private char defaultEchoChar;

    private LoginController loginController;


    public LoginForm() {
        this.loginController = new LoginController(this);

        try {
            URL logoUrl = LoginForm.class.getResource(LOGO_RESOURCE_PATH);
            if (logoUrl != null) {
                logoImage = ImageIO.read(logoUrl).getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            } else {
                throw new IOException("Resource logo tidak ditemukan: " + LOGO_RESOURCE_PATH);
            }
        } catch (IOException e) {
            System.err.println("Tidak dapat memuat logo: " + e.getMessage());
            logoImage = null;
        }

        try {
            URL frameBgUrl = LoginForm.class.getResource(FRAME_BACKGROUND_RESOURCE_PATH);
            if (frameBgUrl != null) {
                frameBackgroundImage = ImageIO.read(frameBgUrl);
            } else {
                throw new IOException("Resource background frame tidak ditemukan: " + FRAME_BACKGROUND_RESOURCE_PATH);
            }
        } catch (IOException e) {
            System.err.println("Tidak dapat memuat gambar background frame: " + e.getMessage() + ". Menggunakan warna fallback.");
            frameBackgroundImage = null;
        }

        setTitle("Login Aplikasi");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageBackgroundPanel mainContentPane = new ImageBackgroundPanel(frameBackgroundImage, new GridBagLayout());
        setContentPane(mainContentPane);

        MainUIPanel loginPanel = new MainUIPanel(30, 30);
        loginPanel.setLayout(new BorderLayout());
        loginPanel.setPreferredSize(new Dimension(380, 530));
        loginPanel.setOpaque(false);

        JPanel topBarArea = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        topBarArea.setOpaque(false);
        topBarArea.setPreferredSize(new Dimension(0, 50));
        JLabel loginLabelTitle = new JLabel("Login");
        loginLabelTitle.setFont(new Font("Arial", Font.BOLD, 18));
        loginLabelTitle.setForeground(WARNA_TEKS_TOP_BAR);
        topBarArea.add(loginLabelTitle);

        JPanel formArea = new JPanel(new GridBagLayout());
        formArea.setOpaque(false);
        formArea.setBorder(new EmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);

        if (logoImage != null) {
            JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
            gbc.insets = new Insets(0, 0, 20, 0);
            formArea.add(logoLabel, gbc);
            gbc.insets = new Insets(8, 5, 8, 5);
        }

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formArea.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 2, 8, 5);
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        formArea.add(usernameField, gbc);
        gbc.insets = new Insets(8, 5, 8, 5);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel passwordLabelText = new JLabel("Password");
        passwordLabelText.setFont(new Font("Arial", Font.PLAIN, 14));
        formArea.add(passwordLabelText, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 2, 8, 5);
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        defaultEchoChar = passwordField.getEchoChar();
        formArea.add(passwordField, gbc);
        gbc.insets = new Insets(8, 5, 8, 5);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 5, 15, 5);
        showPasswordCheckBox = new JCheckBox("Tampilkan Password");
        showPasswordCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
        showPasswordCheckBox.setOpaque(false);
        showPasswordCheckBox.setForeground(WARNA_CHECKBOX_TEXT);
        showPasswordCheckBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar(defaultEchoChar);
            }
        });
        formArea.add(showPasswordCheckBox, gbc);
        gbc.insets = new Insets(8, 5, 8, 5);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(10, 5, 8, 5);
        loginButton = new RoundedButton("Login", WARNA_LOGIN_BUTTON_BG, WARNA_LOGIN_BUTTON_FG, 20);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(200, 40));
        formArea.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(8, 5, 15, 5);
        registrasiButton = new RoundedButton("Registrasi", WARNA_REGISTRASI_BUTTON_BG, WARNA_REGISTRASI_BUTTON_FG, 20);
        registrasiButton.setFont(new Font("Arial", Font.BOLD, 14));
        registrasiButton.setPreferredSize(new Dimension(200, 40));
        formArea.add(registrasiButton, gbc);

        loginPanel.add(topBarArea, BorderLayout.NORTH);
        loginPanel.add(formArea, BorderLayout.CENTER);

        GridBagConstraints gbcMainPanel = new GridBagConstraints();
        gbcMainPanel.anchor = GridBagConstraints.CENTER;
        gbcMainPanel.insets = new Insets(20, 20, 20, 20);
        mainContentPane.add(loginPanel, gbcMainPanel);

        loginButton.addActionListener(e -> loginController.prosesLogin(
                usernameField.getText().trim(),
                new String(passwordField.getPassword())
        ));
        registrasiButton.addActionListener(e -> performRegistrasi());
    }

    public void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccessMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void clearPasswordFields() {
        if (passwordField != null) {
            passwordField.setText("");
        }
    }
    
    public void clearAllFields() {
        if (usernameField != null) {
            usernameField.setText("");
        }
        if (passwordField != null) {
            passwordField.setText("");
        }
        if (showPasswordCheckBox != null) {
            showPasswordCheckBox.setSelected(false);
        }
        if (passwordField != null) { 
             passwordField.setEchoChar(defaultEchoChar);
        }
    }

    public void requestUsernameFocus() {
        if (usernameField != null) {
            usernameField.requestFocusInWindow();
        }
    }

    public void requestPasswordFocus() {
        if (passwordField != null) {
            passwordField.requestFocusInWindow();
        }
    }
    

    private void performRegistrasi() {
        RegistrasiForm formRegistrasi = new RegistrasiForm();
        formRegistrasi.setVisible(true);
        this.dispose();
    }

   
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
            int panelWidth = getWidth(); int panelHeight = getHeight(); int topBarHeight = 50;
            Shape overallPanelShape = new RoundRectangle2D.Float(0, 0, panelWidth, panelHeight, arcWidth, arcHeight);
            Area topPart = new Area(overallPanelShape);
            topPart.intersect(new Area(new Rectangle(0, 0, panelWidth, topBarHeight)));
            g2d.setColor(WARNA_TOP_BAR_PANEL_LOGIN); g2d.fill(topPart);
            Area contentPart = new Area(overallPanelShape);
            contentPart.intersect(new Area(new Rectangle(0, topBarHeight, panelWidth, panelHeight - topBarHeight)));
            g2d.setColor(WARNA_KONTEN_UTAMA_PANEL_LOGIN); g2d.fill(contentPart);
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
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) { /* Nimbus L&F not found */ }
            new LoginForm().setVisible(true);
        });
    }
}
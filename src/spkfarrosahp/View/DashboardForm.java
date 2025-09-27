package spkfarrosahp.View;

import spkfarrosahp.Controller.DashboardController;
import spkfarrosahp.Config.UserSession; // <-- IMPORT UserSession


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;


public class DashboardForm extends JFrame {

    // Warna
    private static final Color WARNA_SIDEBAR_BG = new Color(35, 45, 65);
    private static final Color WARNA_SIDEBAR_TEXT = new Color(220, 220, 220);
    private static final Color WARNA_SIDEBAR_TEXT_HOVER = Color.WHITE;
    private static final Color WARNA_MENU_ACTIVE_BG = new Color(55, 70, 95);
    private static final Color WARNA_MENU_HOVER_BG = new Color(50, 60, 80);
    private static final Color WARNA_CONTENT_HEADER_BG = new Color(240, 245, 250);
    private static final Color WARNA_CONTENT_HEADER_TEXT = new Color(50, 50, 50);
    private static final Color WARNA_LOGGED_IN_USER_TEXT = new Color(80, 80, 80);
    private static final Color WARNA_CONTENT_AREA_BG = Color.WHITE;
    private static final Color WARNA_FRAME_FALLBACK = new Color(45, 52, 71);

    // Path Resource
    private static final String LOGO_RESOURCE_PATH = "/spkfarrosahp/images/LogoCV.png";
    private static final String TOGGLE_ICON_OPEN_PATH = "/spkfarrosahp/images/menu.png"; // Seharusnya ikon untuk membuka (misal >)
    private static final String TOGGLE_ICON_CLOSE_PATH = "/spkfarrosahp/images/menu.png"; // Seharusnya ikon untuk menutup (misal < atau X)
    private static final String DASHBOARD_MAIN_IMAGE_PATH = "/spkfarrosahp/images/bglogin.png";

    private static final Map<String, String> MENU_ITEMS_WITH_ICONS = new LinkedHashMap<>();
    static {
        MENU_ITEMS_WITH_ICONS.put("Karyawan", "/spkfarrosahp/images/karyawan.png");
        MENU_ITEMS_WITH_ICONS.put("Kriteria", "/spkfarrosahp/images/kriteria.png");
        MENU_ITEMS_WITH_ICONS.put("Alternatif", "/spkfarrosahp/images/alternatif.png");
        MENU_ITEMS_WITH_ICONS.put("Seleksi", "/spkfarrosahp/images/seleksi.png");
        MENU_ITEMS_WITH_ICONS.put("Cetak", "/spkfarrosahp/images/printer.png");
        MENU_ITEMS_WITH_ICONS.put("Pengaturan Admin", "/spkfarrosahp/images/user-gear.png");
    }
    private static final String LOGOUT_ICON_PATH = "/spkfarrosahp/images/log-out.png";

    private JPanel sidebarPanel;
    private JPanel mainCardPanel;
    private CardLayout cardLayout;
    private JLabel pageTitleLabel;
    private JLabel logoLabel;
    private JLabel loggedInUserLabel;

    private Image logoImageOriginal;
    private ImageIcon logoutIcon;
    private ImageIcon toggleIconOpen;
    private ImageIcon toggleIconClose;
    private Image dashboardMainImage;

    private JButton activeButton = null;
    private JButton sidebarToggleButton;

    private java.util.List<JButton> menuButtonsList = new ArrayList<>();

    private DashboardController controller;
    private String loggedInAdminName;

    
    private boolean isSidebarExpanded = false; 
    
    private final int SIDEBAR_EXPANDED_WIDTH = 260;
    private final int SIDEBAR_COLLAPSED_WIDTH = 80;
    private final int LOGO_EXPANDED_SIZE = 90;
    private final int LOGO_COLLAPSED_SIZE = 50;
    private final int MENU_ICON_SIZE = 20;
    private final int TOGGLE_ICON_SIZE = 24;

    private Timer sidebarAnimationTimer;
    private final int ANIMATION_DURATION = 150;
    private final int ANIMATION_STEPS = 13;


    public DashboardForm(String loggedInAdminName) {
        this.loggedInAdminName = loggedInAdminName;
        setTitle("Dashboard Aplikasi SPK CV. Farros Sablon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        loadResources();
        initComponents();
        this.controller = new DashboardController(this);
        updateSidebarAppearance(isSidebarExpanded, true); // Set tampilan awal sidebar sesuai state baru
    }
    
    public DashboardForm() {
        this("Pengguna Tes"); 
    }

    private void loadResources() {
        try {
            URL logoUrl = DashboardForm.class.getResource(LOGO_RESOURCE_PATH);
            if (logoUrl != null) logoImageOriginal = ImageIO.read(logoUrl);
            else System.err.println("Resource logo tidak ditemukan: " + LOGO_RESOURCE_PATH);

            URL toggleOpenUrl = DashboardForm.class.getResource(TOGGLE_ICON_OPEN_PATH);
            if (toggleOpenUrl != null) toggleIconOpen = new ImageIcon(ImageIO.read(toggleOpenUrl).getScaledInstance(TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, Image.SCALE_SMOOTH));
            else System.err.println("Icon toggle (open) tidak ditemukan: " + TOGGLE_ICON_OPEN_PATH);
            
            // Pastikan Anda punya ikon berbeda untuk close jika TOGGLE_ICON_CLOSE_PATH berbeda
            URL toggleCloseUrl = DashboardForm.class.getResource(TOGGLE_ICON_CLOSE_PATH);
            if (toggleCloseUrl != null) {
                toggleIconClose = new ImageIcon(ImageIO.read(toggleCloseUrl).getScaledInstance(TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, Image.SCALE_SMOOTH));
            } else {
                System.err.println("Icon toggle (close) tidak ditemukan: " + TOGGLE_ICON_CLOSE_PATH + ". Menggunakan ikon open.");
                toggleIconClose = toggleIconOpen; 
            }


            URL logoutIconUrl = DashboardForm.class.getResource(LOGOUT_ICON_PATH);
            if (logoutIconUrl != null) logoutIcon = new ImageIcon(ImageIO.read(logoutIconUrl).getScaledInstance(MENU_ICON_SIZE, MENU_ICON_SIZE, Image.SCALE_SMOOTH));
            else System.err.println("Resource icon logout tidak ditemukan: " + LOGOUT_ICON_PATH);

            URL dashboardMainUrl = DashboardForm.class.getResource(DASHBOARD_MAIN_IMAGE_PATH);
            if (dashboardMainUrl != null) {
                dashboardMainImage = ImageIO.read(dashboardMainUrl);
            } else {
                System.err.println("Resource gambar utama dashboard tidak ditemukan: " + DASHBOARD_MAIN_IMAGE_PATH);
                dashboardMainImage = null;
            }

        } catch (IOException e) {
            System.err.println("Error saat memuat resource gambar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initComponents() {
        // ... (Kode initComponents lainnya SAMA seperti versi lengkap terakhir Anda) ...
        // Pastikan PanelKaryawan, PanelKriteriaAHP, PanelAlternatif, PanelSeleksi,
        // PanelCetak, PanelPengaturanAdmin sudah memiliki method getController() masing-masing.
        setLayout(new BorderLayout());

        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(WARNA_SIDEBAR_BG);
        sidebarPanel.setLayout(new GridBagLayout());
        add(sidebarPanel, BorderLayout.WEST);

        GridBagConstraints gbcSidebar = new GridBagConstraints();
        gbcSidebar.gridwidth = GridBagConstraints.REMAINDER;
        gbcSidebar.fill = GridBagConstraints.HORIZONTAL;
        gbcSidebar.weightx = 1;
        
        logoLabel = new JLabel();
        logoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null) {
                    controller.navigateTo("Dashboard"); 
                    setActiveMenuButton(null); 
                }
            }
        });
        gbcSidebar.gridy = 0;
        gbcSidebar.fill = GridBagConstraints.NONE; 
        gbcSidebar.anchor = GridBagConstraints.CENTER;
        gbcSidebar.insets = new Insets(15, 5, 15, 5);
        sidebarPanel.add(logoLabel, gbcSidebar);
        
        sidebarToggleButton = new JButton(); 
        sidebarToggleButton.setBackground(WARNA_SIDEBAR_BG);
        sidebarToggleButton.setFocusPainted(false);
        sidebarToggleButton.setBorderPainted(false);
        sidebarToggleButton.setContentAreaFilled(false);
        sidebarToggleButton.setOpaque(true);
        sidebarToggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Alignment dan border akan diatur di updateSidebarAppearance
        sidebarToggleButton.addActionListener(e -> toggleSidebarAction());
        
        gbcSidebar.gridy = 1;
        gbcSidebar.fill = GridBagConstraints.HORIZONTAL; 
        gbcSidebar.anchor = GridBagConstraints.WEST; 
        gbcSidebar.insets = new Insets(10, 0, 15, 0);
        sidebarPanel.add(sidebarToggleButton, gbcSidebar);
        
        gbcSidebar.insets = new Insets(3, 0, 3, 0); 

        gbcSidebar.gridy = 2; 
        menuButtonsList.clear();

        for (Map.Entry<String, String> entry : MENU_ITEMS_WITH_ICONS.entrySet()) {
            createAndAddMenuButton(entry.getKey(), entry.getValue(), gbcSidebar);
        }

        gbcSidebar.weighty = 1.0; 
        gbcSidebar.fill = GridBagConstraints.VERTICAL;
        JPanel spacer = new JPanel();
        spacer.setOpaque(false); 
        sidebarPanel.add(spacer, gbcSidebar);
        gbcSidebar.weighty = 0; 
        gbcSidebar.fill = GridBagConstraints.HORIZONTAL;

        JButton logoutButton = createCustomMenuButton("Logout", logoutIcon, "Logout", isSidebarExpanded); // Kirim state awal
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Anda yakin ingin logout?", "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (controller != null) controller.handleLogout();
            }
        });
        gbcSidebar.gridy++; 
        gbcSidebar.insets = new Insets(10,0,20,0); 
        sidebarPanel.add(logoutButton, gbcSidebar);

        JPanel contentWrapperPanel = new JPanel(new BorderLayout());
        contentWrapperPanel.setBackground(WARNA_CONTENT_AREA_BG);
        add(contentWrapperPanel, BorderLayout.CENTER);

        JPanel contentHeaderPanel = new JPanel(new BorderLayout(20, 0));
        contentHeaderPanel.setBackground(WARNA_CONTENT_HEADER_BG);
        contentHeaderPanel.setPreferredSize(new Dimension(getWidth(), 60));
        contentHeaderPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        pageTitleLabel = new JLabel("Memuat...");
        pageTitleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        pageTitleLabel.setForeground(WARNA_CONTENT_HEADER_TEXT);
        contentHeaderPanel.add(pageTitleLabel, BorderLayout.WEST);

        loggedInUserLabel = new JLabel("Login sebagai: " + this.loggedInAdminName);
        loggedInUserLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        loggedInUserLabel.setForeground(WARNA_LOGGED_IN_USER_TEXT);
        loggedInUserLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        contentHeaderPanel.add(loggedInUserLabel, BorderLayout.EAST);
        
        contentWrapperPanel.add(contentHeaderPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);
        mainCardPanel.setBackground(WARNA_CONTENT_AREA_BG);
        contentWrapperPanel.add(mainCardPanel, BorderLayout.CENTER);

        mainCardPanel.add(new ImageDisplayPanel(dashboardMainImage), "DASHBOARD_PANEL"); 
        mainCardPanel.add(new PanelKaryawan(), "KARYAWAN_PANEL");
        mainCardPanel.add(new PanelKriteriaAHP(), "KRITERIA_PANEL"); 
        mainCardPanel.add(new PanelAlternatif(), "ALTERNATIF_PANEL");
        mainCardPanel.add(new PanelSeleksi(), "SELEKSI_PANEL");
        mainCardPanel.add(new PanelCetak(this), "CETAK_PANEL");
        mainCardPanel.add(new PanelPengaturanAdmin(this), "PENGATURAN_ADMIN_PANEL"); 
        mainCardPanel.add(createPlaceholderPanel("Selamat Datang! Ini Halaman Default."), "PLACEHOLDER_PANEL"); 
    }

    private JPanel createPlaceholderPanel(String message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(WARNA_CONTENT_AREA_BG);
        JLabel label = new JLabel(message);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(Color.DARK_GRAY);
        panel.add(label);
        return panel;
    }
    
    private void createAndAddMenuButton(String text, String iconPath, GridBagConstraints gbc) {
        ImageIcon icon = null;
        if (iconPath != null && !iconPath.isEmpty()) {
            try {
                URL iconUrl = DashboardForm.class.getResource(iconPath);
                if (iconUrl != null) icon = new ImageIcon(ImageIO.read(iconUrl).getScaledInstance(MENU_ICON_SIZE, MENU_ICON_SIZE, Image.SCALE_SMOOTH));
                else System.err.println("Icon resource not found: " + iconPath + " for button " + text);
            } catch (IOException e) { System.err.println("Error loading icon " + iconPath + ": " + e.getMessage()); }
        }
        JButton button = createCustomMenuButton(text, icon, text, isSidebarExpanded);
        menuButtonsList.add(button);
        sidebarPanel.add(button, gbc);
        gbc.gridy++;
    }
    
    private JButton createCustomMenuButton(String textToDisplay, ImageIcon icon, String actionCommand, boolean isInitiallyExpanded) {
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.putClientProperty("originalText", textToDisplay);
        button.putClientProperty("originalIcon", icon);

        // updateSingleButtonAppearance(button, isSidebarExpanded); // Dipanggil oleh updateSidebarAppearance
                                                              // jadi tidak perlu di sini lagi jika isInitiallyExpanded adalah isSidebarExpanded

        button.setForeground(WARNA_SIDEBAR_TEXT);
        button.setBackground(WARNA_SIDEBAR_BG);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button != activeButton) button.setBackground(WARNA_MENU_HOVER_BG);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button != activeButton) button.setBackground(WARNA_SIDEBAR_BG);
            }
        });

        if (!actionCommand.equals("Logout")) {
             button.addActionListener(e -> {
                if (controller != null) controller.navigateTo(e.getActionCommand());
                setActiveMenuButton(button);
            });
        }
        return button;
    }

    private void toggleSidebarAction() {
        isSidebarExpanded = !isSidebarExpanded;
        int targetWidth = isSidebarExpanded ? SIDEBAR_EXPANDED_WIDTH : SIDEBAR_COLLAPSED_WIDTH;
        animateSidebarWidth(targetWidth);
    }

    private void animateSidebarWidth(int targetWidth) {
        if (sidebarAnimationTimer != null && sidebarAnimationTimer.isRunning()) {
            sidebarAnimationTimer.stop();
        }

        int initialWidth = sidebarPanel.getWidth();
        int totalChange = targetWidth - initialWidth;
        if (totalChange == 0) { // Jika sudah di target, update tampilan saja
            updateSidebarAppearance(isSidebarExpanded, true);
            return;
        }
        int delay = ANIMATION_DURATION / ANIMATION_STEPS;
        if (delay == 0) delay = 1;

        sidebarAnimationTimer = new Timer(delay, new ActionListener() {
            private int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep++;
                if (currentStep >= ANIMATION_STEPS) {
                    sidebarPanel.setPreferredSize(new Dimension(targetWidth, getHeight()));
                    updateSidebarAppearance(isSidebarExpanded, false); 
                    ((Timer) e.getSource()).stop();
                    sidebarPanel.revalidate(); 
                    sidebarPanel.repaint();
                    DashboardForm.this.revalidate(); 
                    DashboardForm.this.repaint();
                } else {
                    float fraction = (float) currentStep / ANIMATION_STEPS;
                    int newWidth = initialWidth + (int) (totalChange * fraction);
                    sidebarPanel.setPreferredSize(new Dimension(newWidth, getHeight()));
                    updateSidebarAppearance(isSidebarExpanded, false); 
                    sidebarPanel.revalidate(); 
                    sidebarPanel.repaint();
                }
            }
        });
        sidebarAnimationTimer.start();
    }

    private void updateSidebarAppearance(boolean expanded, boolean instant) {
        int targetWidth = expanded ? SIDEBAR_EXPANDED_WIDTH : SIDEBAR_COLLAPSED_WIDTH;
        if (instant) { 
            sidebarPanel.setPreferredSize(new Dimension(targetWidth, getHeight()));
        }
        
        if (logoImageOriginal != null) {
            int logoSize = expanded ? LOGO_EXPANDED_SIZE : LOGO_COLLAPSED_SIZE;
            logoLabel.setIcon(new ImageIcon(logoImageOriginal.getScaledInstance(logoSize, logoSize, Image.SCALE_SMOOTH)));
        }
        
        if (sidebarToggleButton != null) {
            sidebarToggleButton.setIcon(expanded ? toggleIconClose : toggleIconOpen);
            sidebarToggleButton.setToolTipText(expanded ? "Tutup Menu" : "Buka Menu");
             if (expanded) {
                sidebarToggleButton.setHorizontalAlignment(SwingConstants.LEFT);
                sidebarToggleButton.setBorder(new EmptyBorder(8, 20, 8, 20));
            } else {
                sidebarToggleButton.setHorizontalAlignment(SwingConstants.LEFT); 
                int togglePaddingLeft = 15; 
                sidebarToggleButton.setBorder(new EmptyBorder(8, togglePaddingLeft , 8, SIDEBAR_COLLAPSED_WIDTH - TOGGLE_ICON_SIZE - togglePaddingLeft) );
            }
        }

        for (JButton button : menuButtonsList) {
            updateSingleButtonAppearance(button, expanded);
        }
        
        Component[] components = sidebarPanel.getComponents();
        for(Component comp : components) {
            if (comp instanceof JButton && "Logout".equals(((JButton)comp).getActionCommand())) {
                 updateSingleButtonAppearance((JButton) comp, expanded);
                 break; 
            }
        }

        if(instant){ 
            sidebarPanel.revalidate();
            sidebarPanel.repaint();
            //this.revalidate(); // Revalidate frame mungkin tidak perlu di sini, cukup sidebar
            //this.repaint();
        }
    }

    private void updateSingleButtonAppearance(JButton button, boolean expanded) {
        String originalText = (String) button.getClientProperty("originalText");
        ImageIcon originalIcon = (ImageIcon) button.getClientProperty("originalIcon");
        
        button.setIcon(originalIcon); 

        if (expanded) {
            button.setText(originalText);
            button.setHorizontalTextPosition(SwingConstants.RIGHT);
            button.setIconTextGap(15);
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setBorder(new EmptyBorder(12, 20, 12, 20));
        } else {
            button.setText(""); 
            button.setHorizontalAlignment(SwingConstants.LEFT); 
            // Padding kiri 15px, padding kanan disesuaikan agar total lebar tetap
            button.setBorder(new EmptyBorder(12, 15, 12, SIDEBAR_COLLAPSED_WIDTH - MENU_ICON_SIZE - 15)); 
        }
    }

    public void setActiveMenuButton(JButton buttonToActivate) {
        if (activeButton != null && activeButton != buttonToActivate) {
            activeButton.setBackground(WARNA_SIDEBAR_BG);
            activeButton.setForeground(WARNA_SIDEBAR_TEXT);
        }
        activeButton = buttonToActivate;
        if (activeButton != null) {
            activeButton.setBackground(WARNA_MENU_ACTIVE_BG);
            activeButton.setForeground(Color.WHITE);
        }
    }
    
    public void showView(String panelName) {
        if (cardLayout != null && mainCardPanel != null) {
             try {
                cardLayout.show(mainCardPanel, panelName);
             } catch (IllegalArgumentException e) {
                 System.err.println("Error: Card dengan nama '" + panelName + "' tidak ditemukan. Menampilkan PLACEHOLDER_PANEL.");
                 cardLayout.show(mainCardPanel, "PLACEHOLDER_PANEL");
             }
        }
    }

    public void setPageTitle(String title) {
        if (pageTitleLabel != null) {
            pageTitleLabel.setText(title);
        }
    }
    
    public String getLoggedInAdminName() {
        return loggedInAdminName;
    }

    public Component getPanelFromCardLayout(String cardName) {
        if (mainCardPanel == null) return null;
        for (Component comp : mainCardPanel.getComponents()) {
            if (cardName.equals("KARYAWAN_PANEL") && comp instanceof PanelKaryawan) return comp;
            if (cardName.equals("KRITERIA_PANEL") && comp instanceof PanelKriteriaAHP) return comp;
            if (cardName.equals("ALTERNATIF_PANEL") && comp instanceof PanelAlternatif) return comp;
            if (cardName.equals("SELEKSI_PANEL") && comp instanceof PanelSeleksi) return comp;
            if (cardName.equals("CETAK_PANEL") && comp instanceof PanelCetak) return comp;
            if (cardName.equals("PENGATURAN_ADMIN_PANEL") && comp instanceof PanelPengaturanAdmin) return comp;
            if (cardName.equals("DASHBOARD_PANEL") && comp.getClass().getSimpleName().equals("ImageDisplayPanel")) return comp;
        }
        System.err.println("Panel dengan nama card '" + cardName + "' tidak ditemukan atau bukan instance yang diharapkan di DashboardForm.");
        return null; 
    }
    
    public JButton getMenuButtonByText(String menuName) {
        if (menuButtonsList != null) {
            for (JButton button : menuButtonsList) {
                if (button.getActionCommand() != null && button.getActionCommand().equals(menuName)) {
                    return button;
                }
                if (button.getText() != null && button.getText().equals(menuName)) { 
                    return button;
                }
            }
        }
        return null; 
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

    class ImageDisplayPanel extends JPanel {
        private Image imageToDisplay;
        public ImageDisplayPanel(Image image) {
            this.imageToDisplay = image; this.setBackground(WARNA_CONTENT_AREA_BG); 
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imageToDisplay != null) {
                int panelWidth = getWidth(); int panelHeight = getHeight();
                int imgWidth = imageToDisplay.getWidth(this); int imgHeight = imageToDisplay.getHeight(this);
                if (imgWidth <= 0 || imgHeight <= 0) return; 
                double imgAspect = (double) imgWidth / imgHeight; double panelAspect = (double) panelWidth / panelHeight;
                int scaledWidth; int scaledHeight; int x = 0; int y = 0;
                if (panelAspect > imgAspect) {
                    scaledHeight = panelHeight; scaledWidth = (int) (panelHeight * imgAspect); x = (panelWidth - scaledWidth) / 2;
                } else {
                    scaledWidth = panelWidth; scaledHeight = (int) (panelWidth / imgAspect); y = (panelHeight - scaledHeight) / 2;
                }
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(imageToDisplay, x, y, scaledWidth, scaledHeight, this);
                g2d.dispose();
            } else {
                String message = "Gambar Dashboard Utama Tidak Tersedia";
                g.setColor(Color.GRAY); FontMetrics metrics = g.getFontMetrics();
                int stringWidth = metrics.stringWidth(message);
                g.drawString(message, (getWidth() - stringWidth) / 2, getHeight() / 2);
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
            Shape overallPanelShape = new RoundRectangle2D.Float(0, 0, panelWidth -1 , panelHeight -1 , arcWidth, arcHeight);
            Area topPart = new Area(overallPanelShape);
            topPart.intersect(new Area(new Rectangle(0, 0, panelWidth, topBarHeight)));
            g2d.setColor(DashboardForm.WARNA_CONTENT_HEADER_BG); 
            g2d.fill(topPart);
            Area contentPart = new Area(overallPanelShape);
            contentPart.intersect(new Area(new Rectangle(0, topBarHeight, panelWidth, panelHeight - topBarHeight)));
            g2d.setColor(DashboardForm.WARNA_CONTENT_AREA_BG); 
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
            setFont(new Font("Arial", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(7, 15, 7, 15));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isArmed()) g2.setColor(buttonBackgroundColor.darker());
            else if (getModel().isRollover()) g2.setColor(buttonBackgroundColor.brighter());
            else g2.setColor(buttonBackgroundColor);
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, arc, arc);
            super.paintComponent(g2);
            g2.dispose();
        }
        @Override
        public Dimension getPreferredSize() {
            Dimension dim = super.getPreferredSize();
            if (isPreferredSizeSet()) return dim;
            Insets ins = getInsets();
            dim.width += ins.left + ins.right + 20; 
            dim.height += ins.top + ins.bottom + 10; 
            return dim;
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
            new DashboardForm("Admin Test").setVisible(true); 
        });
    }
}
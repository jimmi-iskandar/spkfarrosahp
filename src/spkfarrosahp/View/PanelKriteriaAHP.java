package spkfarrosahp.View;

import spkfarrosahp.Controller.KriteriaController;
import spkfarrosahp.Model.Kriteria;
import spkfarrosahp.Model.PerbandinganKriteria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.text.NumberFormatter;
import spkfarrosahp.Config.UserSession;


public class PanelKriteriaAHP extends JPanel {

    // Warna
    private static final Color WARNA_BACKGROUND_PANEL = new Color(245, 248, 251);
    private static final Color WARNA_PANEL_BAGIAN = Color.WHITE;
    private static final Color WARNA_BORDER = new Color(220, 225, 230);
    private static final Color WARNA_HEADER_PANEL_UTAMA = new Color(230, 235, 240);
    private static final Color WARNA_JUDUL_TEXT = new Color(50, 50, 90);
    private static final Color WARNA_SUBJUDUL_TEXT = new Color(80, 80, 110);
    private static final Color WARNA_TOMBOL_AKSI_UTAMA_BG = new Color(0, 123, 255);
    private static final Color WARNA_TOMBOL_AKSI_UTAMA_FG = Color.WHITE;
    private static final Color WARNA_TOMBOL_CRUD_BG = new Color(108, 117, 125);
    private static final Color WARNA_TOMBOL_CRUD_FG = Color.WHITE;
    private static final Color WARNA_TOMBOL_HITUNG_BG = new Color(40, 167, 69);
    private static final Color WARNA_TOMBOL_HITUNG_FG = Color.WHITE;
    private static final Color WARNA_READ_ONLY_FIELD_BG = new Color(238, 238, 238);

    private JTextField txtIdKriteriaHidden;
    private JTextField txtKodeKriteria;
    private JButton btnBuatKode;
    private JTextField txtNamaKriteria;
    private JButton btnTambahKriteria;
    private JButton btnUbahKriteria;
    private JButton btnHapusKriteria;
    private JButton btnBersihkanKriteria;

    private JTable tabelDaftarKriteria;
    private DefaultTableModel modelTabelDaftarKriteria;

    private JPanel panelInputPerbandinganList;
    private List<JFormattedTextField> listInputPerbandinganFields;
    private List<PerbandinganKriteria> listPasanganPerbandingan; // Untuk menyimpan pasangan kriteria

    private JButton btnProsesInputNilaiMatriks;
    private JButton btnHitungBobotKonsistensi;

    private JTable tabelMatriksPerbandingan;
    private DefaultTableModel modelMatriksPerbandingan;

    private JTable tabelMatriksNormalisasi;
    private DefaultTableModel modelMatriksNormalisasi;

    private JPanel panelHasilBobotFieldList;
    private List<JLabel> listLabelBobotKriteria;
    private List<JTextField> listFieldBobotKriteria;
    
    private JTextField txtLambdaMax;
    private JTextField txtCI;
    private JTextField txtCR;
    private JLabel lblStatusKonsistensi;

    private KriteriaController controller;
    private DecimalFormat dfMatrix = new DecimalFormat("0.000");
    private DecimalFormat dfBobot = new DecimalFormat("0.000");
    private DecimalFormat dfKonsistensi = new DecimalFormat("0.000");


    public PanelKriteriaAHP() {
        setLayout(new BorderLayout(0, 0));
        setBackground(WARNA_BACKGROUND_PANEL);
        setBorder(new EmptyBorder(10, 15, 15, 15));
        
        listLabelBobotKriteria = new ArrayList<>();
        listFieldBobotKriteria = new ArrayList<>();
        listPasanganPerbandingan = new ArrayList<>(); // Inisialisasi
        
        initComponents();
        this.controller = new KriteriaController(this);
        btnUbahKriteria.setEnabled(false);
        btnHapusKriteria.setEnabled(false);
    }

    private void initComponents() {
        

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.40); // Sedikit penyesuaian untuk lebar panel kiri
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        splitPane.setDividerSize(8);

        JPanel panelKiri = createPanelKiri();
        JScrollPane scrollPaneKiri = new JScrollPane(panelKiri);
        scrollPaneKiri.setBorder(null);
        scrollPaneKiri.getVerticalScrollBar().setUnitIncrement(16);
        splitPane.setLeftComponent(scrollPaneKiri);

        JPanel panelKanan = createPanelKanan();
        JScrollPane scrollPaneKanan = new JScrollPane(panelKanan);
        scrollPaneKanan.setBorder(null);
        scrollPaneKanan.getVerticalScrollBar().setUnitIncrement(16);
        splitPane.setRightComponent(scrollPaneKanan);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createPanelKiri() {
        JPanel panelKiri = new JPanel();
        panelKiri.setLayout(new BoxLayout(panelKiri, BoxLayout.Y_AXIS));
        panelKiri.setBackground(WARNA_BACKGROUND_PANEL);
        panelKiri.setBorder(new EmptyBorder(10,10,10,5));

        // 1. Input/Edit Kriteria (SAMA SEPERTI SEBELUMNYA)
        JPanel panelEditKriteria = new JPanel(new GridBagLayout());
        panelEditKriteria.setBackground(WARNA_PANEL_BAGIAN);
        panelEditKriteria.setBorder(createTitledBorder("Input/Edit Kriteria"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5,8,5,8);
        txtIdKriteriaHidden = new JTextField(); 
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        panelEditKriteria.add(new JLabel("Kode Kriteria:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5;
        txtKodeKriteria = new JTextField(10);
        panelEditKriteria.add(txtKodeKriteria, gbc);
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.2; gbc.fill = GridBagConstraints.NONE;
        btnBuatKode = new JButton("Auto");
        styleButton(btnBuatKode, WARNA_TOMBOL_CRUD_BG, WARNA_TOMBOL_CRUD_FG, new Dimension(65,28));
        btnBuatKode.addActionListener(e -> { if(controller != null) controller.buatKodeKriteriaOtomatis(); });
        panelEditKriteria.add(btnBuatKode, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        panelEditKriteria.add(new JLabel("Nama Kriteria:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7; gbc.gridwidth = 2;
        txtNamaKriteria = new JTextField();
        panelEditKriteria.add(txtNamaKriteria, gbc);
        gbc.gridwidth = 1;
        JPanel panelTombolKriteria = new JPanel(new FlowLayout(FlowLayout.CENTER, 8,0));
        panelTombolKriteria.setOpaque(false);
        btnTambahKriteria = new JButton("Tambah");
        btnUbahKriteria = new JButton("Ubah");
        btnHapusKriteria = new JButton("Hapus");
        btnBersihkanKriteria = new JButton("Bersihkan");
        styleButton(btnTambahKriteria, WARNA_TOMBOL_CRUD_BG, WARNA_TOMBOL_CRUD_FG, null);
        styleButton(btnUbahKriteria, WARNA_TOMBOL_CRUD_BG, WARNA_TOMBOL_CRUD_FG, null);
        styleButton(btnHapusKriteria, WARNA_TOMBOL_CRUD_BG, WARNA_TOMBOL_CRUD_FG, null);
        styleButton(btnBersihkanKriteria, WARNA_TOMBOL_CRUD_BG, WARNA_TOMBOL_CRUD_FG, null);
        btnTambahKriteria.addActionListener(e -> { if(controller != null) controller.tambahKriteria(); });
        btnUbahKriteria.addActionListener(e -> { if(controller != null) controller.ubahKriteria(); });
        btnHapusKriteria.addActionListener(e -> { if(controller != null) controller.hapusKriteria(); });
        btnBersihkanKriteria.addActionListener(e -> { if(controller != null) controller.bersihkanInputKriteria(); });
        panelTombolKriteria.add(btnTambahKriteria);
        panelTombolKriteria.add(btnUbahKriteria);
        panelTombolKriteria.add(btnHapusKriteria);
        panelTombolKriteria.add(btnBersihkanKriteria);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.CENTER; gbc.insets = new Insets(10,0,5,0);
        panelEditKriteria.add(panelTombolKriteria, gbc);
        panelKiri.add(panelEditKriteria);
        panelKiri.add(Box.createRigidArea(new Dimension(0,15)));

        // 2. Daftar Kriteria (SAMA SEPERTI SEBELUMNYA - hanya Kode & Nama)
        JPanel panelDaftarKriteria = new JPanel(new BorderLayout());
        panelDaftarKriteria.setBackground(WARNA_PANEL_BAGIAN);
        panelDaftarKriteria.setBorder(createTitledBorder("Daftar Kriteria")); // Judul disingkat
        modelTabelDaftarKriteria = new DefaultTableModel(new Object[]{"Kode", "Nama Kriteria"}, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelDaftarKriteria = new JTable(modelTabelDaftarKriteria);
        styleTable(tabelDaftarKriteria);
        tabelDaftarKriteria.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isiFormKriteriaDariTabel();
            }
        });
        panelDaftarKriteria.add(new JScrollPane(tabelDaftarKriteria), BorderLayout.CENTER);
        panelDaftarKriteria.setPreferredSize(new Dimension(350, 150)); // Tinggi bisa disesuaikan
        
        panelKiri.add(panelDaftarKriteria);
        panelKiri.add(Box.createRigidArea(new Dimension(0,15)));

        // 3. Input Nilai Perbandingan (REVISI GAYA INPUT)
        panelInputPerbandinganList = new JPanel(new GridBagLayout()); // Menggunakan GridBagLayout
        panelInputPerbandinganList.setBackground(WARNA_PANEL_BAGIAN);
        panelInputPerbandinganList.setBorder(createTitledBorder("Input Nilai Perbandingan (Skala 1-9)"));
        listInputPerbandinganFields = new ArrayList<>();
        
        JScrollPane scrollInputPerbandingan = new JScrollPane(panelInputPerbandinganList);
        // Atur preferred size berdasarkan konten atau beri nilai tetap yang cukup
        scrollInputPerbandingan.setPreferredSize(new Dimension(360, 250)); // Sesuaikan tinggi
        scrollInputPerbandingan.getVerticalScrollBar().setUnitIncrement(12);

        panelKiri.add(scrollInputPerbandingan);
        panelKiri.add(Box.createVerticalGlue());

        return panelKiri;
    }

    private JPanel createPanelKanan() {
        // (Implementasi createPanelKanan SAMA seperti sebelumnya, dengan revisi pada bagian Hasil Pembobotan)
        JPanel panelKanan = new JPanel();
        panelKanan.setLayout(new BoxLayout(panelKanan, BoxLayout.Y_AXIS));
        panelKanan.setBackground(WARNA_BACKGROUND_PANEL);
        panelKanan.setBorder(new EmptyBorder(10,5,10,10));

        JPanel panelTombolAHP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelTombolAHP.setOpaque(false);
        btnProsesInputNilaiMatriks = new JButton("Proses Input & Simpan Perbandingan");
        btnHitungBobotKonsistensi = new JButton("Hitung Bobot & Konsistensi AHP");
        styleButton(btnProsesInputNilaiMatriks, WARNA_TOMBOL_AKSI_UTAMA_BG, WARNA_TOMBOL_AKSI_UTAMA_FG, null);
        styleButton(btnHitungBobotKonsistensi, WARNA_TOMBOL_HITUNG_BG, WARNA_TOMBOL_HITUNG_FG, null);
        btnProsesInputNilaiMatriks.addActionListener(e -> { if(controller != null) controller.prosesInputNilaiMatriks(getNilaiPerbandinganDariInputForm()); });
        btnHitungBobotKonsistensi.addActionListener(e -> { if(controller != null) controller.hitungBobotDanKonsistensi(); });
        panelTombolAHP.add(btnProsesInputNilaiMatriks);
        panelTombolAHP.add(btnHitungBobotKonsistensi);
        panelKanan.add(panelTombolAHP);

        JPanel panelDisplayMatriks = new JPanel(new BorderLayout());
        panelDisplayMatriks.setBackground(WARNA_PANEL_BAGIAN);
        panelDisplayMatriks.setBorder(createTitledBorder("1. Matriks Perbandingan Berpasangan"));
        modelMatriksPerbandingan = new DefaultTableModel() {
            @Override public boolean isCellEditable(int row, int column) { return column != 0; }
        };
        tabelMatriksPerbandingan = new JTable(modelMatriksPerbandingan);
        styleTable(tabelMatriksPerbandingan);
        panelDisplayMatriks.add(new JScrollPane(tabelMatriksPerbandingan), BorderLayout.CENTER);
        panelDisplayMatriks.setPreferredSize(new Dimension(450, 160)); // Tinggi disesuaikan
        panelKanan.add(panelDisplayMatriks);
        panelKanan.add(Box.createRigidArea(new Dimension(0,10)));

        JPanel panelMatriksNormalisasi = new JPanel(new BorderLayout());
        panelMatriksNormalisasi.setBackground(WARNA_PANEL_BAGIAN);
        panelMatriksNormalisasi.setBorder(createTitledBorder("2. Matriks Normalisasi"));
        modelMatriksNormalisasi = new DefaultTableModel(){
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelMatriksNormalisasi = new JTable(modelMatriksNormalisasi);
        styleTable(tabelMatriksNormalisasi);
        panelMatriksNormalisasi.add(new JScrollPane(tabelMatriksNormalisasi), BorderLayout.CENTER);
        panelMatriksNormalisasi.setPreferredSize(new Dimension(450, 160)); // Tinggi disesuaikan
        panelKanan.add(panelMatriksNormalisasi);
        panelKanan.add(Box.createRigidArea(new Dimension(0,10)));

        // --- REVISI BAGIAN HASIL PEMBOBOTAN & KONSISTENSI ---
        JPanel panelHasilAkhirContainer = new JPanel(new BorderLayout(20, 0)); // Beri gap horizontal
        panelHasilAkhirContainer.setBackground(WARNA_PANEL_BAGIAN);
        panelHasilAkhirContainer.setBorder(createTitledBorder("3. Hasil Pembobotan & Konsistensi"));

        // Panel Kiri untuk daftar bobot (menggunakan BoxLayout atau GridBagLayout)
        panelHasilBobotFieldList = new JPanel(); 
        panelHasilBobotFieldList.setLayout(new BoxLayout(panelHasilBobotFieldList, BoxLayout.Y_AXIS));
        panelHasilBobotFieldList.setOpaque(false);
        panelHasilBobotFieldList.setBorder(new EmptyBorder(5,5,5,5));
        
        JScrollPane scrollBobotList = new JScrollPane(panelHasilBobotFieldList);
        scrollBobotList.setBorder(null);
        scrollBobotList.setOpaque(false);
        scrollBobotList.getViewport().setOpaque(false);
        // Atur preferred size untuk scroll pane ini agar tidak terlalu memakan tempat
        scrollBobotList.setPreferredSize(new Dimension(280, 130)); 
        panelHasilAkhirContainer.add(scrollBobotList, BorderLayout.CENTER); // Bobot di kiri/tengah

        // Panel Kanan untuk Lambda, CI, CR, Status
        JPanel panelNilaiKonsistensi = new JPanel(new GridBagLayout());
        panelNilaiKonsistensi.setOpaque(false);
        panelNilaiKonsistensi.setBorder(new EmptyBorder(5,10,5,5)); // Padding kiri
        GridBagConstraints gbcHasil = new GridBagConstraints();
        gbcHasil.anchor = GridBagConstraints.WEST;
        gbcHasil.insets = new Insets(3,5,3,5); // Jarak antar field
        gbcHasil.fill = GridBagConstraints.HORIZONTAL;

        gbcHasil.gridx = 0; gbcHasil.gridy = 0; gbcHasil.weightx = 0.4;
        panelNilaiKonsistensi.add(new JLabel("λ maks:"), gbcHasil);
        gbcHasil.gridx = 1; gbcHasil.weightx = 0.6;
        txtLambdaMax = createReadOnlyTextField();
        panelNilaiKonsistensi.add(txtLambdaMax, gbcHasil);

        gbcHasil.gridx = 0; gbcHasil.gridy = 1;
        panelNilaiKonsistensi.add(new JLabel("CI:"), gbcHasil);
        gbcHasil.gridx = 1;
        txtCI = createReadOnlyTextField();
        panelNilaiKonsistensi.add(txtCI, gbcHasil);
        
        gbcHasil.gridx = 0; gbcHasil.gridy = 2;
        panelNilaiKonsistensi.add(new JLabel("CR:"), gbcHasil);
        gbcHasil.gridx = 1;
        txtCR = createReadOnlyTextField();
        panelNilaiKonsistensi.add(txtCR, gbcHasil);
        
        gbcHasil.gridx = 0; gbcHasil.gridy = 3; gbcHasil.gridwidth = 2; gbcHasil.anchor = GridBagConstraints.CENTER;
        gbcHasil.insets = new Insets(10,5,5,5);
        lblStatusKonsistensi = new JLabel("Status: -");
        lblStatusKonsistensi.setFont(new Font("Arial", Font.BOLD, 13));
        panelNilaiKonsistensi.add(lblStatusKonsistensi, gbcHasil);
        
        panelHasilAkhirContainer.add(panelNilaiKonsistensi, BorderLayout.EAST); // Konsistensi di kanan
        panelHasilAkhirContainer.setPreferredSize(new Dimension(450, 160)); // Sesuaikan tinggi keseluruhan

        panelKanan.add(panelHasilAkhirContainer);
        panelKanan.add(Box.createVerticalGlue());

        return panelKanan;
    }
    
    // (Method createTitledBorder, createReadOnlyTextField, styleButton, styleTable SAMA seperti sebelumnya)
    private TitledBorder createTitledBorder(String title){
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(WARNA_BORDER, 1), title);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titledBorder.setTitleColor(WARNA_SUBJUDUL_TEXT);
        return titledBorder;
    }

    private JTextField createReadOnlyTextField(){
        JTextField textField = new JTextField(8); 
        textField.setEditable(false);
        textField.setBackground(WARNA_READ_ONLY_FIELD_BG);
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        textField.setBorder(new LineBorder(WARNA_BORDER));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        return textField;
    }

    private void styleButton(JButton button, Color bg, Color fg, Dimension size) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(7, 12, 7, 12)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (size != null) {
            button.setPreferredSize(size);
            button.setMinimumSize(size);
            button.setMaximumSize(size);
        }
    }
    
    private void styleTable(JTable table){
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(new Color(220,225,230));
        header.setForeground(new Color(50,50,80));
        header.setBorder(new LineBorder(new Color(200,200,210)));
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(210,210,210));
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }

    // --- IMPLEMENTASI METHOD UNTUK CONTROLLER ---
    public void displayDaftarKriteria(List<Kriteria> daftarKriteria) {
        modelTabelDaftarKriteria.setRowCount(0); 
        if (daftarKriteria != null) {
            for (Kriteria k : daftarKriteria) {
                // --- PERUBAHAN: Hanya Kode dan Nama di tabel ---
                Object[] rowData = {
                    // k.getKriteriaId(), // ID tidak ditampilkan di tabel ini lagi
                    k.getKodeKriteria(),
                    k.getNamaKriteria()
                    // dfBobot.format(k.getBobot()) // Bobot tidak ditampilkan di tabel ini lagi
                };
                modelTabelDaftarKriteria.addRow(rowData);
            }
        }
        updateInputPerbandinganUI(daftarKriteria);
        updateMatriksPerbandinganHeader(daftarKriteria);
        // clearAHPNilaiDanMatriks(); // Tidak langsung clear
        
        btnUbahKriteria.setEnabled(false);
        btnHapusKriteria.setEnabled(false);
        btnTambahKriteria.setEnabled(true);
        if (txtKodeKriteria != null) { 
             if(controller != null && (txtIdKriteriaHidden == null || txtIdKriteriaHidden.getText().isEmpty())) {
                 controller.buatKodeKriteriaOtomatis();
             } else if (txtIdKriteriaHidden != null && !txtIdKriteriaHidden.getText().isEmpty()){
                 // Biarkan kode
             } else {
                 txtKodeKriteria.setText("");
             }
        }
        if(txtNamaKriteria != null) txtNamaKriteria.setText("");
    }
    
    public void clearAHPNilaiDanMatriks(){
        // (Sama seperti sebelumnya)
        if(modelMatriksPerbandingan != null) {
            for (int i = 0; i < modelMatriksPerbandingan.getRowCount(); i++) {
                for (int j = 1; j < modelMatriksPerbandingan.getColumnCount(); j++) { 
                    if (i != (j - 1)) { 
                        modelMatriksPerbandingan.setValueAt("", i, j);
                    } else {
                         modelMatriksPerbandingan.setValueAt(dfMatrix.format(1.0), i, j);
                    }
                }
            }
        }
        if(modelMatriksNormalisasi != null) modelMatriksNormalisasi.setRowCount(0);
        
        panelHasilBobotFieldList.removeAll(); // Bersihkan panel field bobot
        listLabelBobotKriteria.clear();
        listFieldBobotKriteria.clear();
        panelHasilBobotFieldList.revalidate();
        panelHasilBobotFieldList.repaint();

        if(txtLambdaMax != null) txtLambdaMax.setText("");
        if(txtCI != null) txtCI.setText("");
        if(txtCR != null) txtCR.setText("");
        if(lblStatusKonsistensi != null) lblStatusKonsistensi.setText("Status: -");
    }

    public void updateInputPerbandinganUI(List<Kriteria> kriteriaList) {
        panelInputPerbandinganList.removeAll();
        listInputPerbandinganFields.clear();
        listPasanganPerbandingan.clear(); // Bersihkan juga list pasangan

        if (kriteriaList == null || kriteriaList.size() < 2) {
            panelInputPerbandinganList.add(new JLabel("   Minimal 2 kriteria untuk perbandingan."));
        } else {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(3); // Untuk input desimal
            NumberFormatter formatter = new NumberFormatter(numberFormat);
            formatter.setValueClass(Double.class);
            formatter.setMinimum(0.111); // Kira-kira 1/9
            formatter.setMaximum(9.0);
            formatter.setAllowsInvalid(false);
            formatter.setCommitsOnValidEdit(true);

            GridBagConstraints gbcPair = new GridBagConstraints();
            gbcPair.anchor = GridBagConstraints.WEST;
            gbcPair.insets = new Insets(2,2,2,2);
            gbcPair.gridy = 0;

            for (int i = 0; i < kriteriaList.size(); i++) {
                for (int j = i + 1; j < kriteriaList.size(); j++) {
                    Kriteria k1 = kriteriaList.get(i);
                    Kriteria k2 = kriteriaList.get(j);
                    
                    // Simpan pasangan untuk referensi saat mengambil nilai
                    listPasanganPerbandingan.add(new PerbandinganKriteria(k1.getKriteriaId(), k2.getKriteriaId(), 1.0));


                    gbcPair.gridx = 0; gbcPair.weightx = 0.8; gbcPair.fill = GridBagConstraints.NONE;
                    JLabel lblPair = new JLabel(k1.getKodeKriteria() + " (" + k1.getNamaKriteria() + ") vs " + k2.getKodeKriteria() + " (" + k2.getNamaKriteria() + "):");
                    lblPair.setFont(new Font("Arial", Font.PLAIN, 12)); // Font sedikit lebih besar
                    panelInputPerbandinganList.add(lblPair, gbcPair);
                    
                    gbcPair.gridx = 1; gbcPair.weightx = 0.2; gbcPair.fill = GridBagConstraints.HORIZONTAL;
                    JFormattedTextField inputField = new JFormattedTextField(formatter);
                    inputField.setColumns(4); // Lebar field disesuaikan
                    inputField.setFont(new Font("Arial", Font.PLAIN, 12));
                    inputField.setHorizontalAlignment(JTextField.CENTER);
                    inputField.setValue(1.0); // Nilai default 1
                    // Simpan ID kriteria untuk referensi
                    inputField.putClientProperty("kriteria1_id", k1.getKriteriaId());
                    inputField.putClientProperty("kriteria2_id", k2.getKriteriaId());
                    
                    listInputPerbandinganFields.add(inputField);
                    panelInputPerbandinganList.add(inputField, gbcPair);
                    gbcPair.gridy++;
                }
            }
        }
        panelInputPerbandinganList.revalidate();
        panelInputPerbandinganList.repaint();
    }
    
    public void updateMatriksPerbandinganHeader(List<Kriteria> kriteriaList){
        // (Sama seperti sebelumnya)
        if(modelMatriksPerbandingan == null || tabelMatriksPerbandingan == null || kriteriaList == null) return;
        Object[] columnHeaders = new Object[kriteriaList.size() + 1];
        columnHeaders[0] = " "; 
        for(int i=0; i < kriteriaList.size(); i++) columnHeaders[i+1] = kriteriaList.get(i).getKodeKriteria();
        modelMatriksPerbandingan.setColumnIdentifiers(columnHeaders);
        modelMatriksPerbandingan.setRowCount(0); 
        if(!kriteriaList.isEmpty()){
            for(int i=0; i < kriteriaList.size(); i++){
                Object[] rowData = new Object[kriteriaList.size() + 1];
                rowData[0] = kriteriaList.get(i).getKodeKriteria();
                for(int j=1; j <= kriteriaList.size(); j++) rowData[j] = (i == (j-1)) ? dfMatrix.format(1.0) : ""; 
                modelMatriksPerbandingan.addRow(rowData);
            }
        }
        if (tabelMatriksPerbandingan.getColumnModel().getColumnCount() > 0) {
            tabelMatriksPerbandingan.getColumnModel().getColumn(0).setPreferredWidth(60);
            tabelMatriksPerbandingan.getColumnModel().getColumn(0).setMaxWidth(80);
             for(int i=1; i < tabelMatriksPerbandingan.getColumnModel().getColumnCount(); i++) tabelMatriksPerbandingan.getColumnModel().getColumn(i).setPreferredWidth(50);
        }
    }

    public void populatePerbandinganMatrixFromDB(List<PerbandinganKriteria> perbandinganTersimpan, List<Kriteria> kriteriaList) {
        // (Sama seperti sebelumnya)
        if (modelMatriksPerbandingan == null || kriteriaList == null || kriteriaList.isEmpty() ) {
            updateMatriksPerbandinganHeader(kriteriaList != null ? kriteriaList : new ArrayList<>());
            return;
        }
        Map<Integer, Integer> kriteriaIdToIndexMap = new LinkedHashMap<>();
        for (int i = 0; i < kriteriaList.size(); i++) kriteriaIdToIndexMap.put(kriteriaList.get(i).getKriteriaId(), i);
        for(int i=0; i < kriteriaList.size(); i++){ // Set diagonal ke 1.000 dulu
            for(int j=1; j <= kriteriaList.size(); j++) modelMatriksPerbandingan.setValueAt((i == (j-1)) ? dfMatrix.format(1.0) : "", i, j);
        }
        if (perbandinganTersimpan == null || perbandinganTersimpan.isEmpty()) return;
        for (PerbandinganKriteria pk : perbandinganTersimpan) {
            Integer idx1 = kriteriaIdToIndexMap.get(pk.getKriteria1Id());
            Integer idx2 = kriteriaIdToIndexMap.get(pk.getKriteria2Id());
            double nilai = pk.getNilaiPerbandingan();
            if (idx1 != null && idx2 != null) {
                modelMatriksPerbandingan.setValueAt(dfMatrix.format(nilai), idx1, idx2 + 1);
                if (nilai != 0) modelMatriksPerbandingan.setValueAt(dfMatrix.format(1.0 / nilai), idx2, idx1 + 1);
                else modelMatriksPerbandingan.setValueAt("Inf", idx2, idx1 + 1);
                // Update listInputPerbandinganFields
                int fieldIndex = 0;
                for (int i = 0; i < kriteriaList.size(); i++) {
                    for (int k = i + 1; k < kriteriaList.size(); k++) {
                        Kriteria kr1 = kriteriaList.get(i);
                        Kriteria kr2 = kriteriaList.get(k);
                        if ((kr1.getKriteriaId() == pk.getKriteria1Id() && kr2.getKriteriaId() == pk.getKriteria2Id())) {
                            if (fieldIndex < listInputPerbandinganFields.size()) listInputPerbandinganFields.get(fieldIndex).setValue(nilai);
                            break;
                        } else if ((kr1.getKriteriaId() == pk.getKriteria2Id() && kr2.getKriteriaId() == pk.getKriteria1Id())) {
                            if (fieldIndex < listInputPerbandinganFields.size()) {
                                if (nilai != 0) listInputPerbandinganFields.get(fieldIndex).setValue(1.0/nilai); else listInputPerbandinganFields.get(fieldIndex).setValue(null);
                            }
                            break;
                        }
                        fieldIndex++;
                    }
                }
            }
        }
    }

    public String getKodeKriteriaInput() { return txtKodeKriteria.getText().trim(); }
    public String getNamaKriteriaInput() { return txtNamaKriteria.getText().trim(); }
    public void setKodeKriteriaField(String kode) { txtKodeKriteria.setText(kode); }

    public void showMessage(String message, boolean isError) {
        if (isError) JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        else JOptionPane.showMessageDialog(this, message, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public void clearInputKriteriaFields() {
        txtIdKriteriaHidden.setText("");
        txtNamaKriteria.setText("");
        btnTambahKriteria.setEnabled(true);
        btnUbahKriteria.setEnabled(false);
        btnHapusKriteria.setEnabled(false);
        if(controller != null) controller.buatKodeKriteriaOtomatis(); 
        for(JFormattedTextField field : listInputPerbandinganFields) field.setValue(1.0); 
        txtKodeKriteria.requestFocusInWindow();
    }

    public String getSelectedKriteriaIdFromTable() {
        // (Sama seperti sebelumnya)
        int selectedRow = tabelDaftarKriteria.getSelectedRow();
        if (selectedRow != -1 && controller != null && controller.getCurrentKriteriaList() != null && selectedRow < controller.getCurrentKriteriaList().size()){
            return String.valueOf(controller.getCurrentKriteriaList().get(selectedRow).getKriteriaId());
        }
        return null;
    }
    
    public String getSelectedKodeKriteriaFromTable() {
        // (Sama seperti sebelumnya)
        int selectedRow = tabelDaftarKriteria.getSelectedRow();
        if (selectedRow != -1) return modelTabelDaftarKriteria.getValueAt(selectedRow, 0).toString();
        return null;
    }

    public List<PerbandinganKriteria> getNilaiPerbandinganDariInputForm() {
        // (Sama seperti sebelumnya)
        List<PerbandinganKriteria> perbandinganList = new ArrayList<>();
        boolean validInput = true;
        for (JFormattedTextField field : listInputPerbandinganFields) {
            if (field.getValue() == null || field.getText().trim().isEmpty()) {
                showMessage("Semua nilai perbandingan harus diisi.", true); field.requestFocusInWindow(); validInput = false; break; 
            }
            Object val = field.getValue(); double nilai;
            if (val instanceof Number) nilai = ((Number) val).doubleValue();
            else {
                 try { nilai = Double.parseDouble(field.getText().replace(",", ".")); }
                 catch (NumberFormatException e){
                    String k1 = field.getClientProperty("kriteria1_kode") != null ? field.getClientProperty("kriteria1_kode").toString() : "N/A";
                    String k2 = field.getClientProperty("kriteria2_kode") != null ? field.getClientProperty("kriteria2_kode").toString() : "N/A";
                    showMessage("Input nilai perbandingan tidak valid untuk " + k1 + " vs " + k2, true);
                    field.requestFocusInWindow(); return new ArrayList<>();
                 }
            }
            if (nilai <= 0) {
                String k1 = field.getClientProperty("kriteria1_kode") != null ? field.getClientProperty("kriteria1_kode").toString() : "N/A";
                String k2 = field.getClientProperty("kriteria2_kode") != null ? field.getClientProperty("kriteria2_kode").toString() : "N/A";
                showMessage("Nilai perbandingan harus > 0 untuk " + k1 + " vs " + k2, true);
                field.requestFocusInWindow(); validInput = false; break;
            }
            int id1 = (int) field.getClientProperty("kriteria1_id");
            int id2 = (int) field.getClientProperty("kriteria2_id");
            perbandinganList.add(new PerbandinganKriteria(id1, id2, nilai));
        }
        return validInput ? perbandinganList : new ArrayList<>();
    }

    public void displayMatriksPerbandingan(double[][] matriks, List<Kriteria> kriteriaList) {
        // (Sama seperti sebelumnya)
        if (matriks == null || kriteriaList == null || matriks.length != kriteriaList.size()) {
            updateMatriksPerbandinganHeader(kriteriaList != null ? kriteriaList : new ArrayList<>()); return;
        }
        updateMatriksPerbandinganHeader(kriteriaList);
        for (int i = 0; i < matriks.length; i++) {
            for (int j = 0; j < matriks[i].length; j++) modelMatriksPerbandingan.setValueAt(dfMatrix.format(matriks[i][j]), i, j + 1);
        }
    }
    
    public double[][] getMatriksPerbandinganFromTable() {
        // (Sama seperti sebelumnya)
        if (modelMatriksPerbandingan == null || modelMatriksPerbandingan.getRowCount() == 0) return null;
        int n = modelMatriksPerbandingan.getRowCount(); double[][] matriks = new double[n][n];
        try {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Object valObj = modelMatriksPerbandingan.getValueAt(i, j + 1);
                    if (valObj != null && !valObj.toString().isEmpty()) matriks[i][j] = Double.parseDouble(valObj.toString().replace(",","."));
                    else { showMessage("Nilai kosong di matriks perbandingan tidak diizinkan untuk perhitungan.", true); return null; }
                }
            }
        } catch (NumberFormatException e){ showMessage("Format angka pada matriks perbandingan tidak valid: " + e.getMessage(), true); return null; }
        return matriks;
    }

    public void displayMatriksNormalisasi(double[][] normalizedMatrix, double[] columnSums, List<Kriteria> kriteriaList) {
        // (Sama seperti sebelumnya, tapi tanpa txtAreaDetailNormalisasi)
        if (normalizedMatrix == null || kriteriaList == null || normalizedMatrix.length != kriteriaList.size()) {
             modelMatriksNormalisasi.setRowCount(0); modelMatriksNormalisasi.setColumnCount(0);
            return;
        }
        Object[] columnHeaders = new Object[kriteriaList.size() + 1];
        columnHeaders[0] = " ";
        for(int i=0; i < kriteriaList.size(); i++) columnHeaders[i+1] = kriteriaList.get(i).getKodeKriteria();
        modelMatriksNormalisasi.setColumnIdentifiers(columnHeaders);
        modelMatriksNormalisasi.setRowCount(0);
        for (int i = 0; i < normalizedMatrix.length; i++) {
            Object[] rowData = new Object[normalizedMatrix.length + 1];
            rowData[0] = kriteriaList.get(i).getKodeKriteria();
            for (int j = 0; j < normalizedMatrix[i].length; j++) rowData[j+1] = dfMatrix.format(normalizedMatrix[i][j]);
            modelMatriksNormalisasi.addRow(rowData);
        }
         if (tabelMatriksNormalisasi.getColumnModel().getColumnCount() > 0) {
            tabelMatriksNormalisasi.getColumnModel().getColumn(0).setPreferredWidth(60);
            tabelMatriksNormalisasi.getColumnModel().getColumn(0).setMaxWidth(80);
            for(int i=1; i < tabelMatriksNormalisasi.getColumnModel().getColumnCount(); i++) tabelMatriksNormalisasi.getColumnModel().getColumn(i).setPreferredWidth(50);
        }
    }

    // --- REVISI METHOD INI ---
    public void displayHasilPembobotan(List<Kriteria> kriteriaList, double[] priorityVector) {
        panelHasilBobotFieldList.removeAll(); 
        listLabelBobotKriteria.clear();
        listFieldBobotKriteria.clear();

        if (kriteriaList != null && priorityVector != null && kriteriaList.size() == priorityVector.length) {
            GridBagConstraints gbcBobot = new GridBagConstraints();
            gbcBobot.anchor = GridBagConstraints.WEST;
            gbcBobot.insets = new Insets(2,2,2,10); // Jarak antar field bobot
            gbcBobot.gridy = 0;

            for (int i = 0; i < kriteriaList.size(); i++) {
                Kriteria k = kriteriaList.get(i);
                
                gbcBobot.gridx = 0; gbcBobot.weightx = 0.6; gbcBobot.fill = GridBagConstraints.NONE;
                JLabel lblBobotDesc = new JLabel("Bobot " + k.getKodeKriteria() + " ("+k.getNamaKriteria()+"):");
                lblBobotDesc.setFont(new Font("Arial", Font.PLAIN, 12));
                listLabelBobotKriteria.add(lblBobotDesc);
                panelHasilBobotFieldList.add(lblBobotDesc, gbcBobot);

                gbcBobot.gridx = 1; gbcBobot.weightx = 0.4; gbcBobot.fill = GridBagConstraints.HORIZONTAL;
                JTextField fieldBobotVal = createReadOnlyTextField();
                fieldBobotVal.setText(dfBobot.format(priorityVector[i]));
                fieldBobotVal.setColumns(6); // Sesuaikan lebar field bobot
                listFieldBobotKriteria.add(fieldBobotVal);
                panelHasilBobotFieldList.add(fieldBobotVal, gbcBobot);
                
                gbcBobot.gridy++;
            }
            // Tambahkan komponen kosong untuk mendorong ke atas jika panelHasilBobotFieldList menggunakan GridBagLayout
            gbcBobot.gridy++; gbcBobot.weighty = 1.0;
            panelHasilBobotFieldList.add(new JLabel(), gbcBobot);
        }
        panelHasilBobotFieldList.revalidate();
        panelHasilBobotFieldList.repaint();
    }
    // --- AKHIR REVISI ---

    public void displayNilaiKonsistensi(double lambdaMax, double ci, double cr) {
        // (Sama seperti sebelumnya)
        txtLambdaMax.setText(dfKonsistensi.format(lambdaMax));
        txtCI.setText(dfKonsistensi.format(ci));
        txtCR.setText(dfKonsistensi.format(cr));
        if (cr <= 0.10) {
            lblStatusKonsistensi.setText("Status: KONSISTEN (CR <= 0.10)");
            lblStatusKonsistensi.setForeground(new Color(0, 128, 0));
        } else {
            lblStatusKonsistensi.setText("Status: TIDAK KONSISTEN (CR > 0.10)");
            lblStatusKonsistensi.setForeground(Color.RED);
        }
    }
    
    public void isiFormKriteriaDariTabel() {
        // (Sama seperti sebelumnya)
        int selectedRow = tabelDaftarKriteria.getSelectedRow();
        if (selectedRow != -1 && controller != null && controller.getCurrentKriteriaList() != null && 
            selectedRow < controller.getCurrentKriteriaList().size()) {
            Kriteria kriteriaTerpilih = controller.getCurrentKriteriaList().get(selectedRow);
            txtIdKriteriaHidden.setText(String.valueOf(kriteriaTerpilih.getKriteriaId()));
            txtKodeKriteria.setText(kriteriaTerpilih.getKodeKriteria());
            txtNamaKriteria.setText(kriteriaTerpilih.getNamaKriteria());
            btnTambahKriteria.setEnabled(false);
            btnUbahKriteria.setEnabled(true);
            btnHapusKriteria.setEnabled(true);
        }
    }
    
    public void requestKodeKriteriaFocus(){
        if(txtKodeKriteria != null){
            txtKodeKriteria.requestFocusInWindow();
        }
    }
    public KriteriaController getController() {
    return this.controller;
}

    public static void main(String[] args) {
        // (Sama seperti sebelumnya)
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
            JFrame frame = new JFrame("Panel Manajemen Kriteria & AHP");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            PanelKriteriaAHP panel = new PanelKriteriaAHP();
            frame.getContentPane().add(panel);
            frame.setMinimumSize(new Dimension(1100, 750)); 
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
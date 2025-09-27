package spkfarrosahp.View;
import spkfarrosahp.Controller.AlternatifController;
import spkfarrosahp.Model.Kriteria;
import spkfarrosahp.Model.Karyawan;
import spkfarrosahp.Model.PenilaianAlternatif;
import spkfarrosahp.Model.PerbandinganKriteria; 
import spkfarrosahp.Config.UserSession; 
import spkfarrosahp.View.LoginForm;   
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.text.NumberFormatter;
import spkfarrosahp.Config.UserSession;
import spkfarrosahp.Controller.KriteriaController;

public class PanelAlternatif extends JPanel {

    // Warna
    private static final Color WARNA_BACKGROUND_PANEL = new Color(245, 248, 251);
    private static final Color WARNA_PANEL_BAGIAN = Color.WHITE;
    private static final Color WARNA_BORDER = new Color(220, 225, 230);
    private static final Color WARNA_HEADER_PANEL_UTAMA = new Color(230, 235, 240);
    private static final Color WARNA_JUDUL_TEXT = new Color(50, 50, 90);
    private static final Color WARNA_SUBJUDUL_TEXT = new Color(80, 80, 110);
    private static final Color WARNA_TOMBOL_AKSI_UTAMA_BG = new Color(0, 123, 255);
    private static final Color WARNA_TOMBOL_AKSI_UTAMA_FG = Color.WHITE;
    private static final Color WARNA_TOMBOL_HITUNG_BG = new Color(40, 167, 69);
    private static final Color WARNA_TOMBOL_HITUNG_FG = Color.WHITE;
    private static final Color WARNA_TOMBOL_SIMPAN_BOBOT_BG = new Color(23, 162, 184); // Warna disamakan dengan refresh atau warna lain
    private static final Color WARNA_TOMBOL_SIMPAN_BOBOT_FG = Color.WHITE;
    private static final Color WARNA_READ_ONLY_FIELD_BG = new Color(238, 238, 238);
    private static final Color WARNA_TOMBOL_REFRESH_BG = new Color(23, 162, 184);
    private static final Color WARNA_TOMBOL_REFRESH_FG = Color.WHITE;


    private JComboBox<Kriteria> cmbKriteriaAcuan;
    private JButton btnRefreshData;
    private JTable tabelDaftarKaryawan;
    private DefaultTableModel modelTabelDaftarKaryawan;
    private JPanel panelInputPerbandinganAlternatif;
    private List<JFormattedTextField> listInputPerbandinganFieldsAlternatif;
    private List<PenilaianAlternatifHelper> listPasanganPerbandinganAlternatif;

    private JButton btnProsesInputNilaiAlternatif;
    private JButton btnHitungBobotLokal;
    private JButton btnSimpanBobotLokal;

    private JTable tabelMatriksPerbandinganAlternatif;
    private DefaultTableModel modelMatriksPerbandinganAlternatif;

    private JTable tabelMatriksNormalisasiAlternatif;
    private DefaultTableModel modelMatriksNormalisasiAlternatif;

    private JPanel panelHasilBobotLokalList;
    private List<JLabel> listLabelBobotLokalKaryawan;
    private List<JTextField> listFieldBobotLokalKaryawan;
    
    private JTextField txtLambdaMaxLokal;
    private JTextField txtCILokal;
    private JTextField txtCRLokal;
    private JLabel lblStatusKonsistensiLokal;
    
    private TitledBorder borderHasilAkhir;

    private AlternatifController controller;
    private DecimalFormat dfMatrix = new DecimalFormat("0.000");
    private DecimalFormat dfBobotLokal = new DecimalFormat("0.000");
    private DecimalFormat dfKonsistensi = new DecimalFormat("0.000");
    
    private static class PenilaianAlternatifHelper {
        int alternatif1Id; String alternatif1Kode; String alternatif1Nama;
        int alternatif2Id; String alternatif2Kode; String alternatif2Nama;
        public PenilaianAlternatifHelper(int id1, String kode1, String nama1, int id2, String kode2, String nama2) {
            this.alternatif1Id = id1; this.alternatif1Kode = kode1; this.alternatif1Nama = nama1;
            this.alternatif2Id = id2; this.alternatif2Kode = kode2; this.alternatif2Nama = nama2;
        }
    }

    public PanelAlternatif() {
        setLayout(new BorderLayout(0, 0));
        setBackground(WARNA_BACKGROUND_PANEL);
        setBorder(new EmptyBorder(10, 15, 15, 15));
        
        listLabelBobotLokalKaryawan = new ArrayList<>();
        listFieldBobotLokalKaryawan = new ArrayList<>();
        listInputPerbandinganFieldsAlternatif = new ArrayList<>();
        listPasanganPerbandinganAlternatif = new ArrayList<>();
        
        initComponents();
        this.controller = new AlternatifController(this);
    }

    private void initComponents() {
       

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.40); 
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        splitPane.setDividerSize(10);

        JPanel panelKiri = createPanelKiri();
        splitPane.setLeftComponent(panelKiri);

        JPanel panelKanan = createPanelKanan();
        JScrollPane scrollPaneKanan = new JScrollPane(panelKanan);
        scrollPaneKanan.setBorder(BorderFactory.createLineBorder(WARNA_BORDER));
        scrollPaneKanan.getVerticalScrollBar().setUnitIncrement(16);
        splitPane.setRightComponent(scrollPaneKanan);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createPanelKiri() {
        JPanel panelKiri = new JPanel();
        panelKiri.setLayout(new BorderLayout(0, 10)); 
        panelKiri.setBackground(WARNA_PANEL_BAGIAN); 
        panelKiri.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(WARNA_BORDER),
            new EmptyBorder(10,10,10,10))
        );

        JPanel panelPilihKriteria = new JPanel(new BorderLayout(10, 0)); 
        panelPilihKriteria.setOpaque(false);
        panelPilihKriteria.setBorder(createTitledBorder("Pilih Kriteria Acuan"));
        
        JPanel wrapperCmb = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
        wrapperCmb.setOpaque(false);
        wrapperCmb.add(new JLabel("Kriteria: ")); 
        cmbKriteriaAcuan = new JComboBox<>();
        cmbKriteriaAcuan.setFont(new Font("Arial", Font.PLAIN, 13));
        cmbKriteriaAcuan.setPreferredSize(new Dimension(250, 28)); 
        cmbKriteriaAcuan.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && controller != null) {
                    controller.kriteriaAcuanDipilih((Kriteria) cmbKriteriaAcuan.getSelectedItem());
                }
            }
        });
        wrapperCmb.add(cmbKriteriaAcuan);
        panelPilihKriteria.add(wrapperCmb, BorderLayout.CENTER);

        btnRefreshData = new JButton("Refresh"); 
        styleButton(btnRefreshData, WARNA_TOMBOL_REFRESH_BG, WARNA_TOMBOL_REFRESH_FG, new Dimension(90, 28)); 
        btnRefreshData.addActionListener(e -> {
            if (controller != null) {
                controller.loadInitialData(); 
            }
        });
        JPanel panelTombolRefresh = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0,0)); 
        panelTombolRefresh.setOpaque(false);
        panelTombolRefresh.add(btnRefreshData);
        panelPilihKriteria.add(panelTombolRefresh, BorderLayout.EAST);
        
        panelKiri.add(panelPilihKriteria, BorderLayout.NORTH);

        JPanel panelKiriContent = new JPanel();
        panelKiriContent.setLayout(new BoxLayout(panelKiriContent, BoxLayout.Y_AXIS));
        panelKiriContent.setOpaque(false);
        panelKiriContent.setBorder(new EmptyBorder(10,0,0,0));

        JPanel panelDaftarKaryawan = new JPanel(new BorderLayout(0,5));
        panelDaftarKaryawan.setOpaque(false); 
        panelDaftarKaryawan.setBorder(createTitledBorder("Daftar Karyawan (Alternatif)"));
        modelTabelDaftarKaryawan = new DefaultTableModel(new Object[]{"Kode", "Nama Karyawan"}, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelDaftarKaryawan = new JTable(modelTabelDaftarKaryawan);
        styleTable(tabelDaftarKaryawan, new int[]{100, 0}); 
        JScrollPane scrollDaftarKaryawan = new JScrollPane(tabelDaftarKaryawan);
        scrollDaftarKaryawan.setPreferredSize(new Dimension(100, 150)); 
        panelDaftarKaryawan.add(scrollDaftarKaryawan, BorderLayout.CENTER);
        panelDaftarKaryawan.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDaftarKaryawan.setMaximumSize(new Dimension(Short.MAX_VALUE, 200)); 
        panelKiriContent.add(panelDaftarKaryawan);
        panelKiriContent.add(Box.createRigidArea(new Dimension(0,10)));

        panelInputPerbandinganAlternatif = new JPanel(new GridBagLayout());
        panelInputPerbandinganAlternatif.setOpaque(false); 
        panelInputPerbandinganAlternatif.setBorder(createTitledBorder("Input Perbandingan Alternatif (untuk Kriteria Terpilih)"));
        listInputPerbandinganFieldsAlternatif = new ArrayList<>();
        
        JScrollPane scrollInputPerbandingan = new JScrollPane(panelInputPerbandinganAlternatif);
        scrollInputPerbandingan.setPreferredSize(new Dimension(100, 180)); 
        scrollInputPerbandingan.setMinimumSize(new Dimension(250,120));
        scrollInputPerbandingan.getVerticalScrollBar().setUnitIncrement(12);
        scrollInputPerbandingan.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollInputPerbandingan.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        panelKiriContent.add(scrollInputPerbandingan);
        
        panelKiri.add(panelKiriContent, BorderLayout.CENTER);

        return panelKiri;
    }

    private JPanel createPanelKanan() {
        JPanel panelKanan = new JPanel();
        panelKanan.setLayout(new BoxLayout(panelKanan, BoxLayout.Y_AXIS)); 
        panelKanan.setBackground(WARNA_PANEL_BAGIAN); 
        panelKanan.setBorder(new EmptyBorder(10,10,10,10)); 

        // --- PERUBAHAN POSISI TOMBOL SIMPAN BOBOT ---
        JPanel panelTombolAHP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelTombolAHP.setOpaque(false);
        btnProsesInputNilaiAlternatif = new JButton("Proses Input Nilai");
        styleButton(btnProsesInputNilaiAlternatif, WARNA_TOMBOL_AKSI_UTAMA_BG, WARNA_TOMBOL_AKSI_UTAMA_FG, null);
        btnProsesInputNilaiAlternatif.addActionListener(e -> { 
            if(controller != null) controller.prosesInputNilaiAlternatif(getNilaiPerbandinganAlternatifDariInput(), (Kriteria) cmbKriteriaAcuan.getSelectedItem()); 
        });

        btnHitungBobotLokal = new JButton("Hitung Bobot Lokal & Konsistensi");
        styleButton(btnHitungBobotLokal, WARNA_TOMBOL_HITUNG_BG, WARNA_TOMBOL_HITUNG_FG, null);
        btnHitungBobotLokal.addActionListener(e -> { 
            if(controller != null) controller.hitungBobotLokalAlternatif((Kriteria) cmbKriteriaAcuan.getSelectedItem()); 
        });

        btnSimpanBobotLokal = new JButton("Simpan Bobot Alternatif"); // Label diubah
        styleButton(btnSimpanBobotLokal, WARNA_TOMBOL_SIMPAN_BOBOT_BG, WARNA_TOMBOL_SIMPAN_BOBOT_FG, null);
        btnSimpanBobotLokal.setEnabled(false);
        btnSimpanBobotLokal.addActionListener(e -> {
            if(controller != null) controller.simpanBobotLokalKeDB((Kriteria) cmbKriteriaAcuan.getSelectedItem());
        });

        panelTombolAHP.add(btnProsesInputNilaiAlternatif);
        panelTombolAHP.add(btnHitungBobotLokal);
        panelTombolAHP.add(btnSimpanBobotLokal); // Tombol Simpan ditambahkan di sini
        panelTombolAHP.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTombolAHP.setMaximumSize(new Dimension(Short.MAX_VALUE, panelTombolAHP.getPreferredSize().height));
        panelKanan.add(panelTombolAHP);
        // --- AKHIR PERUBAHAN POSISI TOMBOL ---

        // Sisa panel kanan (matriks, hasil) tetap sama
        JPanel panelDisplayMatriks = new JPanel(new BorderLayout());
        panelDisplayMatriks.setOpaque(false); 
        panelDisplayMatriks.setBorder(createTitledBorder("1. Matriks Perbandingan Alternatif"));
        modelMatriksPerbandinganAlternatif = new DefaultTableModel() {
            @Override public boolean isCellEditable(int row, int column) { return false; } 
        };
        tabelMatriksPerbandinganAlternatif = new JTable(modelMatriksPerbandinganAlternatif);
        styleTable(tabelMatriksPerbandinganAlternatif, null); 
        JScrollPane scrollMatriksPerbandingan = new JScrollPane(tabelMatriksPerbandinganAlternatif);
        panelDisplayMatriks.add(scrollMatriksPerbandingan, BorderLayout.CENTER);
        panelDisplayMatriks.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDisplayMatriks.setMaximumSize(new Dimension(Short.MAX_VALUE, 200));
        panelKanan.add(panelDisplayMatriks);
        panelKanan.add(Box.createRigidArea(new Dimension(0,10)));

        JPanel panelMatriksNormalisasi = new JPanel(new BorderLayout());
        panelMatriksNormalisasi.setOpaque(false);
        panelMatriksNormalisasi.setBorder(createTitledBorder("2. Matriks Normalisasi Alternatif"));
        modelMatriksNormalisasiAlternatif = new DefaultTableModel(){
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelMatriksNormalisasiAlternatif = new JTable(modelMatriksNormalisasiAlternatif);
        styleTable(tabelMatriksNormalisasiAlternatif, null);
        JScrollPane scrollMatriksNormalisasi = new JScrollPane(tabelMatriksNormalisasiAlternatif);
        panelMatriksNormalisasi.add(scrollMatriksNormalisasi, BorderLayout.CENTER);
        panelMatriksNormalisasi.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelMatriksNormalisasi.setMaximumSize(new Dimension(Short.MAX_VALUE, 200));
        panelKanan.add(panelMatriksNormalisasi);
        panelKanan.add(Box.createRigidArea(new Dimension(0,10)));

        JPanel panelHasilAkhirContainer = new JPanel(new BorderLayout(10, 0)); 
        panelHasilAkhirContainer.setOpaque(false);
        borderHasilAkhir = createTitledBorder("3. Hasil Bobot Lokal & Konsistensi");
        panelHasilAkhirContainer.setBorder(borderHasilAkhir);

        panelHasilBobotLokalList = new JPanel(new GridBagLayout());
        panelHasilBobotLokalList.setOpaque(false);
        panelHasilBobotLokalList.setBorder(new EmptyBorder(5,5,5,5));
        JScrollPane scrollBobotList = new JScrollPane(panelHasilBobotLokalList);
        scrollBobotList.setBorder(null);
        scrollBobotList.setOpaque(false);
        scrollBobotList.getViewport().setOpaque(false);
        panelHasilAkhirContainer.add(scrollBobotList, BorderLayout.CENTER);

        JPanel panelNilaiKonsistensi = new JPanel(new GridBagLayout());
        panelNilaiKonsistensi.setOpaque(false);
        panelNilaiKonsistensi.setBorder(new EmptyBorder(5,5,5,5)); 
        GridBagConstraints gbcHasil = new GridBagConstraints();
        gbcHasil.anchor = GridBagConstraints.WEST;
        gbcHasil.insets = new Insets(3,5,3,5);
        gbcHasil.fill = GridBagConstraints.HORIZONTAL;
        gbcHasil.gridx = 0; gbcHasil.gridy = 0; gbcHasil.weightx = 0.4;
        panelNilaiKonsistensi.add(new JLabel("λ maks:"), gbcHasil);
        gbcHasil.gridx = 1; gbcHasil.weightx = 0.6;
        txtLambdaMaxLokal = createReadOnlyTextField();
        panelNilaiKonsistensi.add(txtLambdaMaxLokal, gbcHasil);
        gbcHasil.gridx = 0; gbcHasil.gridy = 1;
        panelNilaiKonsistensi.add(new JLabel("CI:"), gbcHasil);
        gbcHasil.gridx = 1;
        txtCILokal = createReadOnlyTextField();
        panelNilaiKonsistensi.add(txtCILokal, gbcHasil);
        gbcHasil.gridx = 0; gbcHasil.gridy = 2;
        panelNilaiKonsistensi.add(new JLabel("CR:"), gbcHasil);
        gbcHasil.gridx = 1;
        txtCRLokal = createReadOnlyTextField();
        panelNilaiKonsistensi.add(txtCRLokal, gbcHasil);
        gbcHasil.gridx = 0; gbcHasil.gridy = 3; gbcHasil.gridwidth = 2; gbcHasil.anchor = GridBagConstraints.CENTER;
        gbcHasil.insets = new Insets(10,5,5,5);
        lblStatusKonsistensiLokal = new JLabel("Status: -");
        lblStatusKonsistensiLokal.setFont(new Font("Arial", Font.BOLD, 13));
        panelNilaiKonsistensi.add(lblStatusKonsistensiLokal, gbcHasil);
        panelHasilAkhirContainer.add(panelNilaiKonsistensi, BorderLayout.EAST);
        panelHasilAkhirContainer.setMaximumSize(new Dimension(Short.MAX_VALUE, 200));
        panelHasilAkhirContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelKanan.add(panelHasilAkhirContainer);
        
        // Tombol Simpan Bobot Lokal sudah dipindahkan ke panelTombolAHP di atas
        // JPanel panelSimpanBobot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // ... (kode panelSimpanBobot dihapus dari sini) ...
        // panelKanan.add(panelSimpanBobot);

        return panelKanan;
    }
    
    // ... (Sisa method seperti createTitledBorder, createReadOnlyTextField, styleButton, styleTable,
    //      dan semua method untuk interaksi dengan controller, serta main method SAMA seperti sebelumnya) ...
    //      PASTIKAN ANDA MENYALIN SEMUA METHOD TERSEBUT DARI KODE LENGKAP PanelAlternatif.java
    //      YANG SUDAH ADA SEBELUMNYA.
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
    
    private void styleTable(JTable table, int[] columnWidths){
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
        
        if (columnWidths != null && table.getColumnModel().getColumnCount() >= columnWidths.length) {
            boolean hasFlexibleColumn = false;
            for (int i = 0; i < columnWidths.length; i++) {
                 if (columnWidths[i] > 0) {
                    table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
                 } else { 
                    hasFlexibleColumn = true; 
                 }
            }
            table.setAutoResizeMode(hasFlexibleColumn ? JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS : JTable.AUTO_RESIZE_OFF);
        } else {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
        }
    }

    public void populateCmbKriteriaAcuan(List<Kriteria> daftarKriteria) {
        cmbKriteriaAcuan.removeAllItems();
        if (daftarKriteria != null) {
            for (Kriteria k : daftarKriteria) {
                cmbKriteriaAcuan.addItem(k);
            }
        }
    }

    public void displayDaftarKaryawan(List<Karyawan> daftarKaryawan) {
        modelTabelDaftarKaryawan.setRowCount(0); 
        if (daftarKaryawan != null) {
            for (Karyawan k : daftarKaryawan) {
                Object[] rowData = {
                    k.getKodeKaryawan(),
                    k.getNamaLengkap()
                };
                modelTabelDaftarKaryawan.addRow(rowData);
            }
        }
        Kriteria kriteriaTerpilih = (Kriteria) cmbKriteriaAcuan.getSelectedItem();
        updateInputPerbandinganAlternatifUI(daftarKaryawan, kriteriaTerpilih);
        updateMatriksPerbandinganAlternatifHeader(daftarKaryawan, kriteriaTerpilih);
        clearHasilLokalAHP();
    }
    
    public void updateInputPerbandinganAlternatifUI(List<Karyawan> karyawanList, Kriteria kriteriaAcuan) {
        panelInputPerbandinganAlternatif.removeAll();
        listInputPerbandinganFieldsAlternatif.clear();
        listPasanganPerbandinganAlternatif.clear();

        String title = "Input Perbandingan Alternatif";
        if (kriteriaAcuan != null) {
            title += " (Thd. Kriteria: " + kriteriaAcuan.getKodeKriteria() + ")";
        }
        if (panelInputPerbandinganAlternatif.getBorder() instanceof TitledBorder) {
            ((TitledBorder) panelInputPerbandinganAlternatif.getBorder()).setTitle(title);
        }

        if (karyawanList == null || karyawanList.size() < 2 || kriteriaAcuan == null) {
            panelInputPerbandinganAlternatif.setLayout(new FlowLayout(FlowLayout.CENTER));
            panelInputPerbandinganAlternatif.add(new JLabel("   Pilih Kriteria Acuan dan minimal 2 Karyawan terdaftar."));
        } else {
            panelInputPerbandinganAlternatif.setLayout(new GridBagLayout());
            GridBagConstraints gbcPair = new GridBagConstraints();
            gbcPair.anchor = GridBagConstraints.WEST;
            gbcPair.insets = new Insets(2,2,2,2);
            gbcPair.gridy = 0;
            
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(3);
            NumberFormatter formatter = new NumberFormatter(numberFormat);
            formatter.setValueClass(Double.class);
            formatter.setMinimum(0.111); 
            formatter.setMaximum(9.0);
            formatter.setAllowsInvalid(false);
            formatter.setCommitsOnValidEdit(true);

            String kodeKriteriaAcuan = kriteriaAcuan.getKodeKriteria(); 

            for (int i = 0; i < karyawanList.size(); i++) {
                for (int j = i + 1; j < karyawanList.size(); j++) {
                    Karyawan alt1 = karyawanList.get(i);
                    Karyawan alt2 = karyawanList.get(j);
                    
                    listPasanganPerbandinganAlternatif.add(
                        new PenilaianAlternatifHelper(
                            alt1.getKaryawanId(), alt1.getKodeKaryawan(), alt1.getNamaLengkap(), 
                            alt2.getKaryawanId(), alt2.getKodeKaryawan(), alt2.getNamaLengkap()
                        )
                    );

                    gbcPair.gridx = 0; gbcPair.weightx = 0.85; gbcPair.fill = GridBagConstraints.NONE;
                    String kodeLabelAlt1 = kodeKriteriaAcuan + (i + 1); 
                    String kodeLabelAlt2 = kodeKriteriaAcuan + (j + 1);
                    String labelText = String.format("%s (%s) vs %s (%s):", 
                                                     kodeLabelAlt1, alt1.getNamaLengkap(),  
                                                     kodeLabelAlt2, alt2.getNamaLengkap());
                    JLabel lblPair = new JLabel(labelText);
                    lblPair.setFont(new Font("Arial", Font.PLAIN, 11));
                    panelInputPerbandinganAlternatif.add(lblPair, gbcPair);
                    
                    gbcPair.gridx = 1; gbcPair.weightx = 0.15; gbcPair.fill = GridBagConstraints.HORIZONTAL;
                    JFormattedTextField inputField = new JFormattedTextField(formatter);
                    inputField.setColumns(4);
                    inputField.setFont(new Font("Arial", Font.PLAIN, 11));
                    inputField.setHorizontalAlignment(JTextField.CENTER);
                    inputField.setValue(1.0); 
                    listInputPerbandinganFieldsAlternatif.add(inputField);
                    panelInputPerbandinganAlternatif.add(inputField, gbcPair);
                    gbcPair.gridy++;
                }
            }
            gbcPair.gridy++; gbcPair.weighty = 1.0;
            panelInputPerbandinganAlternatif.add(new JLabel(), gbcPair);
        }
        panelInputPerbandinganAlternatif.revalidate();
        panelInputPerbandinganAlternatif.repaint();
    }
    
    public void updateMatriksPerbandinganAlternatifHeader(List<Karyawan> karyawanList, Kriteria kriteriaAcuan){
        if(modelMatriksPerbandinganAlternatif == null || tabelMatriksPerbandinganAlternatif == null || karyawanList == null) return;
        
        String title = "1. Matriks Perbandingan Alternatif";
        if(kriteriaAcuan != null) {
            title += " (Kriteria: " + kriteriaAcuan.getKodeKriteria() + ")";
        }
        
        Component parent = tabelMatriksPerbandinganAlternatif.getParent(); 
        if (parent != null) parent = parent.getParent(); 
        if (parent != null && parent instanceof JPanel) { 
            JPanel parentPanel = (JPanel) parent;
            if (parentPanel.getBorder() instanceof TitledBorder) {
                 ((TitledBorder) parentPanel.getBorder()).setTitle(title);
                 parentPanel.repaint();
            }
        }

        Object[] columnHeaders = new Object[karyawanList.size() + 1];
        columnHeaders[0] = " "; 
        for(int i=0; i < karyawanList.size(); i++) columnHeaders[i+1] = karyawanList.get(i).getKodeKaryawan();
        modelMatriksPerbandinganAlternatif.setColumnIdentifiers(columnHeaders);

        modelMatriksPerbandinganAlternatif.setRowCount(0); 
        if(!karyawanList.isEmpty()){
            for(int i=0; i < karyawanList.size(); i++){
                Object[] rowData = new Object[karyawanList.size() + 1];
                rowData[0] = karyawanList.get(i).getKodeKaryawan();
                for(int j=1; j <= karyawanList.size(); j++) rowData[j] = (i == (j-1)) ? dfMatrix.format(1.0) : ""; 
                modelMatriksPerbandinganAlternatif.addRow(rowData);
            }
        }
        if (tabelMatriksPerbandinganAlternatif.getColumnModel().getColumnCount() > 0) {
            tabelMatriksPerbandinganAlternatif.getColumnModel().getColumn(0).setPreferredWidth(70);
            tabelMatriksPerbandinganAlternatif.getColumnModel().getColumn(0).setMaxWidth(90);
             for(int i=1; i < tabelMatriksPerbandinganAlternatif.getColumnModel().getColumnCount(); i++){
                tabelMatriksPerbandinganAlternatif.getColumnModel().getColumn(i).setPreferredWidth(60);
             }
        }
    }

    public void clearHasilLokalAHP(){
        if(modelMatriksPerbandinganAlternatif != null) {
             for (int i = 0; i < modelMatriksPerbandinganAlternatif.getRowCount(); i++) {
                for (int j = 1; j < modelMatriksPerbandinganAlternatif.getColumnCount(); j++) {
                    if (i != (j - 1)) modelMatriksPerbandinganAlternatif.setValueAt("", i, j);
                    else modelMatriksPerbandinganAlternatif.setValueAt(dfMatrix.format(1.0), i, j);
                }
            }
        }
        if(modelMatriksNormalisasiAlternatif != null) modelMatriksNormalisasiAlternatif.setRowCount(0);
        if(panelHasilBobotLokalList != null) panelHasilBobotLokalList.removeAll();
        if(listLabelBobotLokalKaryawan != null) listLabelBobotLokalKaryawan.clear();
        if(listFieldBobotLokalKaryawan != null) listFieldBobotLokalKaryawan.clear();
        if(panelHasilBobotLokalList != null) {
            panelHasilBobotLokalList.revalidate(); panelHasilBobotLokalList.repaint();
        }
        if(txtLambdaMaxLokal != null) txtLambdaMaxLokal.setText("");
        if(txtCILokal != null) txtCILokal.setText("");
        if(txtCRLokal != null) txtCRLokal.setText("");
        if(lblStatusKonsistensiLokal != null) lblStatusKonsistensiLokal.setText("Status: -");
        enableSimpanBobotLokalButton(false);
    }

    public void populatePerbandinganMatrixFromDB(List<PerbandinganKriteria> perbandinganTersimpan, List<Kriteria> kriteriaList) {
        // Tidak relevan untuk PanelAlternatif dengan alur saat ini
    }

    public List<PenilaianAlternatif> getNilaiPerbandinganAlternatifDariInput() {
        List<PenilaianAlternatif> perbandinganList = new ArrayList<>();
        Kriteria kriteriaAcuan = (Kriteria) cmbKriteriaAcuan.getSelectedItem();
        if (kriteriaAcuan == null) {
            showMessage("Pilih Kriteria Acuan terlebih dahulu!", true); return perbandinganList;
        }
        boolean validInput = true;
        for (int i = 0; i < listInputPerbandinganFieldsAlternatif.size(); i++) {
            JFormattedTextField field = listInputPerbandinganFieldsAlternatif.get(i);
            PenilaianAlternatifHelper pasangan = listPasanganPerbandinganAlternatif.get(i);
            if (field.getValue() == null || field.getText().trim().isEmpty()) {
                showMessage("Semua nilai perbandingan alternatif harus diisi.", true); field.requestFocusInWindow(); validInput = false; break; 
            }
            Object val = field.getValue(); double nilai;
            if (val instanceof Number) nilai = ((Number) val).doubleValue();
            else {
                 try { nilai = Double.parseDouble(field.getText().replace(",", ".")); }
                 catch (NumberFormatException e){
                    showMessage("Input nilai tidak valid untuk " + pasangan.alternatif1Kode + " vs " + pasangan.alternatif2Kode, true);
                    field.requestFocusInWindow(); return new ArrayList<>();
                 }
            }
            if (nilai <= 0) {
                showMessage("Nilai perbandingan harus > 0 untuk " + pasangan.alternatif1Kode + " vs " + pasangan.alternatif2Kode, true);
                field.requestFocusInWindow(); validInput = false; break;
            }
            perbandinganList.add(new PenilaianAlternatif(pasangan.alternatif1Id, pasangan.alternatif2Id, nilai));
        }
        return validInput ? perbandinganList : new ArrayList<>();
    }
    
    public void displayMatriksPerbandinganAlternatif(double[][] matriks, List<Karyawan> karyawanList, Kriteria kriteriaAcuan) {
        if (matriks == null || karyawanList == null || matriks.length != karyawanList.size()) {
            updateMatriksPerbandinganAlternatifHeader(karyawanList != null ? karyawanList : new ArrayList<>(), kriteriaAcuan); return;
        }
        updateMatriksPerbandinganAlternatifHeader(karyawanList, kriteriaAcuan);
        for (int i = 0; i < matriks.length; i++) {
            for (int j = 0; j < matriks[i].length; j++) modelMatriksPerbandinganAlternatif.setValueAt(dfMatrix.format(matriks[i][j]), i, j + 1);
        }
    }
    
    public double[][] getMatriksPerbandinganAlternatifFromTable() {
        if (modelMatriksPerbandinganAlternatif == null || modelMatriksPerbandinganAlternatif.getRowCount() == 0) return null;
        int n = modelMatriksPerbandinganAlternatif.getRowCount(); double[][] matriks = new double[n][n];
        try {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Object valObj = modelMatriksPerbandinganAlternatif.getValueAt(i, j + 1);
                    if (valObj != null && !valObj.toString().isEmpty()) matriks[i][j] = Double.parseDouble(valObj.toString().replace(",","."));
                    else { showMessage("Nilai kosong di matriks perbandingan tidak diizinkan.", true); return null; }
                }
            }
        } catch (NumberFormatException e){ showMessage("Format angka pada matriks perbandingan tidak valid: " + e.getMessage(), true); return null; }
        return matriks;
    }

    public void displayMatriksNormalisasiAlternatif(double[][] normalizedMatrix, List<Karyawan> karyawanList, Kriteria kriteriaAcuan) {
        if (normalizedMatrix == null || karyawanList == null || normalizedMatrix.length != karyawanList.size()) {
             modelMatriksNormalisasiAlternatif.setRowCount(0); modelMatriksNormalisasiAlternatif.setColumnCount(0);
             return;
        }
        String title = "2. Matriks Normalisasi Alternatif";
        if(kriteriaAcuan != null) title += " (Kriteria: " + kriteriaAcuan.getKodeKriteria() + ")";
        Component parent = tabelMatriksNormalisasiAlternatif.getParent();
        if (parent != null) parent = parent.getParent();
        if (parent != null && parent instanceof JPanel) {
            JPanel parentPanel = (JPanel) parent;
            if (parentPanel.getBorder() instanceof TitledBorder) {
                 ((TitledBorder) parentPanel.getBorder()).setTitle(title);
                 parentPanel.repaint();
            }
        }
        Object[] columnHeaders = new Object[karyawanList.size() + 1];
        columnHeaders[0] = " ";
        for(int i=0; i < karyawanList.size(); i++) columnHeaders[i+1] = karyawanList.get(i).getKodeKaryawan();
        modelMatriksNormalisasiAlternatif.setColumnIdentifiers(columnHeaders);
        modelMatriksNormalisasiAlternatif.setRowCount(0);
        for (int i = 0; i < normalizedMatrix.length; i++) {
            Object[] rowData = new Object[normalizedMatrix.length + 1];
            rowData[0] = karyawanList.get(i).getKodeKaryawan();
            for (int j = 0; j < normalizedMatrix[i].length; j++) rowData[j+1] = dfMatrix.format(normalizedMatrix[i][j]);
            modelMatriksNormalisasiAlternatif.addRow(rowData);
        }
         if (tabelMatriksNormalisasiAlternatif.getColumnModel().getColumnCount() > 0) {
            tabelMatriksNormalisasiAlternatif.getColumnModel().getColumn(0).setPreferredWidth(70);
            tabelMatriksNormalisasiAlternatif.getColumnModel().getColumn(0).setMaxWidth(90);
            for(int i=1; i < tabelMatriksNormalisasiAlternatif.getColumnModel().getColumnCount(); i++) tabelMatriksNormalisasiAlternatif.getColumnModel().getColumn(i).setPreferredWidth(60);
        }
    }

    public void displayHasilBobotLokalAlternatif(List<Karyawan> karyawanList, double[] localPriorityVector, Kriteria kriteriaAcuan) {
        panelHasilBobotLokalList.removeAll(); 
        listLabelBobotLokalKaryawan.clear();
        listFieldBobotLokalKaryawan.clear();
        String title = "3. Hasil Bobot Lokal & Konsistensi";
        if(kriteriaAcuan != null) title += " (Kriteria: " + kriteriaAcuan.getKodeKriteria() + ")";
        if(borderHasilAkhir != null) {
            borderHasilAkhir.setTitle(title);
            Component parentContainer = panelHasilBobotLokalList.getParent(); 
            if (parentContainer != null) parentContainer = parentContainer.getParent(); 
            if (parentContainer instanceof JPanel) ((JPanel)parentContainer).repaint();
        }
        if (karyawanList != null && localPriorityVector != null && karyawanList.size() == localPriorityVector.length) {
            panelHasilBobotLokalList.setLayout(new GridBagLayout());
            GridBagConstraints gbcBobot = new GridBagConstraints();
            gbcBobot.anchor = GridBagConstraints.WEST;
            gbcBobot.insets = new Insets(2,2,2,10);
            gbcBobot.gridy = 0;
            for (int i = 0; i < karyawanList.size(); i++) {
                Karyawan k = karyawanList.get(i);
                gbcBobot.gridx = 0; gbcBobot.weightx = 0.7; gbcBobot.fill = GridBagConstraints.NONE;
                String kodeLabelBobot = kriteriaAcuan != null ? kriteriaAcuan.getKodeKriteria() + (i + 1) : k.getKodeKaryawan();
                JLabel lblBobotDesc = new JLabel(kodeLabelBobot + " ("+k.getNamaLengkap()+"):");
                lblBobotDesc.setFont(new Font("Arial", Font.PLAIN, 11));
                listLabelBobotLokalKaryawan.add(lblBobotDesc);
                panelHasilBobotLokalList.add(lblBobotDesc, gbcBobot);
                gbcBobot.gridx = 1; gbcBobot.weightx = 0.3; gbcBobot.fill = GridBagConstraints.HORIZONTAL;
                JTextField fieldBobotVal = createReadOnlyTextField();
                fieldBobotVal.setText(dfBobotLokal.format(localPriorityVector[i]));
                fieldBobotVal.setColumns(5);
                listFieldBobotLokalKaryawan.add(fieldBobotVal);
                panelHasilBobotLokalList.add(fieldBobotVal, gbcBobot);
                gbcBobot.gridy++;
            }
            gbcBobot.gridy++; gbcBobot.weighty = 1.0;
            panelHasilBobotLokalList.add(new JLabel(), gbcBobot);
        }
        panelHasilBobotLokalList.revalidate();
        panelHasilBobotLokalList.repaint();
    }

    public void displayNilaiKonsistensiLokal(double lambdaMax, double ci, double cr) {
        txtLambdaMaxLokal.setText(dfKonsistensi.format(lambdaMax));
        txtCILokal.setText(dfKonsistensi.format(ci));
        txtCRLokal.setText(dfKonsistensi.format(cr));
        if (cr <= 0.10) {
            lblStatusKonsistensiLokal.setText("Status: KONSISTEN (CR <= 0.10)");
            lblStatusKonsistensiLokal.setForeground(new Color(0, 128, 0));
        } else {
            lblStatusKonsistensiLokal.setText("Status: TIDAK KONSISTEN (CR > 0.10)");
            lblStatusKonsistensiLokal.setForeground(Color.RED);
        }
    }
    
    public void enableSimpanBobotLokalButton(boolean enable) {
        if (btnSimpanBobotLokal != null) {
            btnSimpanBobotLokal.setEnabled(enable);
        }
    }

    public double[] getHasilBobotLokalFromDisplay() {
        if (listFieldBobotLokalKaryawan.isEmpty()) return null;
        double[] bobot = new double[listFieldBobotLokalKaryawan.size()];
        try {
            for (int i = 0; i < listFieldBobotLokalKaryawan.size(); i++) {
                bobot[i] = Double.parseDouble(listFieldBobotLokalKaryawan.get(i).getText().replace(",","."));
            }
            return bobot;
        } catch (NumberFormatException e) {
            showMessage("Format bobot lokal di tampilan tidak valid.", true);
            return null;
        }
    }
    
    public void showMessage(String message, boolean isError) {
        if (isError) JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        else JOptionPane.showMessageDialog(this, message, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public AlternatifController getController() {
        return this.controller;
    }
    
    public void refreshPanelData(){
        if(controller != null){
            controller.loadInitialData();
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
            } catch (Exception e) { e.printStackTrace(); }
            JFrame frame = new JFrame("Panel Penilaian Alternatif");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            PanelAlternatif panel = new PanelAlternatif();
            frame.getContentPane().add(panel);
            frame.setMinimumSize(new Dimension(1150, 750)); 
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
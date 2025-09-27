package spkfarrosahp.View;
import spkfarrosahp.Controller.SeleksiController;
import spkfarrosahp.Model.HasilSeleksi;
import spkfarrosahp.Config.UserSession;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class PanelSeleksi extends JPanel {

    // Warna
    private static final Color WARNA_BACKGROUND_PANEL = new Color(245, 248, 251);
    private static final Color WARNA_PANEL_BAGIAN = Color.WHITE;
    private static final Color WARNA_BORDER = new Color(220, 225, 230);
    // private static final Color WARNA_HEADER_PANEL_UTAMA = new Color(230, 235, 240); // Tidak dipakai
    // private static final Color WARNA_JUDUL_TEXT = new Color(50, 50, 90); // Tidak dipakai
    private static final Color WARNA_SUBJUDUL_TEXT = new Color(80, 80, 110);
    private static final Color WARNA_TOMBOL_HITUNG_BG = new Color(0, 123, 255); 
    private static final Color WARNA_TOMBOL_HITUNG_FG = Color.WHITE;
    private static final Color WARNA_TOMBOL_SIMPAN_BG = new Color(40, 167, 69); 
    private static final Color WARNA_TOMBOL_SIMPAN_FG = Color.WHITE;
    private static final Color WARNA_TOMBOL_REFRESH_BG = new Color(23, 162, 184); 
    private static final Color WARNA_TOMBOL_REFRESH_FG = Color.WHITE;


    private JTable tabelNilaiAlternatifPerKriteria;
    private DefaultTableModel modelTabelNilaiAlternatifPerKriteria;
    private JTable tabelBobotKriteria;
    private DefaultTableModel modelTabelBobotKriteria;

    private JButton btnHitungSeleksi;
    private JButton btnSimpanHasil;
    private JButton btnRefreshDataSeleksi; 

    private JTable tabelDetailPerhitungan;
    private DefaultTableModel modelTabelDetailPerhitungan;
    private JTable tabelHasilPerangkingan;
    private DefaultTableModel modelTabelHasilPerangkingan;

    private SeleksiController controller;

    public PanelSeleksi() {
        setLayout(new BorderLayout(0, 10));
        setBackground(WARNA_BACKGROUND_PANEL);
        setBorder(new EmptyBorder(10, 15, 15, 15));

        initComponents();
        this.controller = new SeleksiController(this);
    }

    private void initComponents() {
        JPanel panelKonten = new JPanel(new BorderLayout(0,10));
        panelKonten.setOpaque(false);
        add(panelKonten, BorderLayout.CENTER);

        JPanel panelAreaAtas = createPanelAreaAtas();
        panelKonten.add(panelAreaAtas, BorderLayout.NORTH);

        JPanel panelAreaTengah = createPanelAreaTengah(); // Di sini tombol Refresh akan ditambahkan
        panelKonten.add(panelAreaTengah, BorderLayout.CENTER);
        
        JPanel panelAreaBawah = createPanelAreaBawah();
        panelKonten.add(panelAreaBawah, BorderLayout.SOUTH);
    }

    private JPanel createPanelAreaAtas() {
        JPanel panelAtas = new JPanel(new GridLayout(1, 2, 15, 0)); 
        panelAtas.setOpaque(false);
        panelAtas.setBorder(new EmptyBorder(0,0,10,0)); // Dulu 10,0,10,0

        // Panel Kiri Atas: Tabel Nilai Alternatif per Kriteria
        JPanel panelNilaiAlternatif = new JPanel(new BorderLayout());
        panelNilaiAlternatif.setBackground(WARNA_PANEL_BAGIAN);
        panelNilaiAlternatif.setBorder(createTitledBorder("Nilai Alternatif (Karyawan) per Kriteria"));
        modelTabelNilaiAlternatifPerKriteria = new DefaultTableModel(null, new String[]{"Kode Karyawan", "Nama Karyawan"}) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelNilaiAlternatifPerKriteria = new JTable(modelTabelNilaiAlternatifPerKriteria);
        styleTable(tabelNilaiAlternatifPerKriteria, null);
        JScrollPane scrollNilaiAlternatif = new JScrollPane(tabelNilaiAlternatifPerKriteria);
        scrollNilaiAlternatif.setPreferredSize(new Dimension(450, 160)); 
        panelNilaiAlternatif.add(scrollNilaiAlternatif, BorderLayout.CENTER);
        panelAtas.add(panelNilaiAlternatif);

        // Panel Kanan Atas: Tabel Bobot Global Kriteria
        JPanel panelBobotKriteria = new JPanel(new BorderLayout());
        panelBobotKriteria.setBackground(WARNA_PANEL_BAGIAN);
        panelBobotKriteria.setBorder(createTitledBorder("Bobot Global Kriteria"));
        modelTabelBobotKriteria = new DefaultTableModel(
                new Object[]{"Kode Kriteria", "Nama Kriteria", "Bobot Global"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelBobotKriteria = new JTable(modelTabelBobotKriteria);
        styleTable(tabelBobotKriteria, new int[]{100, 200, 100});
        JScrollPane scrollBobotKriteria = new JScrollPane(tabelBobotKriteria);
        scrollBobotKriteria.setPreferredSize(new Dimension(450, 160)); 
        panelBobotKriteria.add(scrollBobotKriteria, BorderLayout.CENTER);
        panelAtas.add(panelBobotKriteria);

        
        
        return panelAtas; // Mengembalikan panelAtas yang berisi dua tabel
    }

    private JPanel createPanelAreaTengah() {
        JPanel panelTengah = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); 
        panelTengah.setOpaque(false);

        // --- TOMBOL REFRESH DATA SELEKSI DITAMBAHKAN DI SINI ---
        btnRefreshDataSeleksi = new JButton("Refresh Data");
        styleButton(btnRefreshDataSeleksi, WARNA_TOMBOL_REFRESH_BG, WARNA_TOMBOL_REFRESH_FG, new Dimension(180, 40)); // Ukuran disesuaikan
        btnRefreshDataSeleksi.addActionListener(e -> {
            if(controller != null) controller.loadInitialDataForView();
        });
        panelTengah.add(btnRefreshDataSeleksi); // Tambahkan ke panel tengah
        // --- AKHIR PENAMBAHAN TOMBOL REFRESH ---

        btnHitungSeleksi = new JButton("Hitung Proses Seleksi");
        styleButton(btnHitungSeleksi, WARNA_TOMBOL_HITUNG_BG, WARNA_TOMBOL_HITUNG_FG, new Dimension(220, 40));
        btnHitungSeleksi.addActionListener(e -> {
            if(controller != null) controller.hitungProsesSeleksi();
        });

        btnSimpanHasil = new JButton("Simpan Hasil Seleksi");
        styleButton(btnSimpanHasil, WARNA_TOMBOL_SIMPAN_BG, WARNA_TOMBOL_SIMPAN_FG, new Dimension(220, 40));
        btnSimpanHasil.setEnabled(false); 
        btnSimpanHasil.addActionListener(e -> {
            if(controller != null) controller.simpanHasilSeleksi();
        });

        panelTengah.add(btnHitungSeleksi);
        panelTengah.add(btnSimpanHasil);
        return panelTengah;
    }

    
    private JPanel createPanelAreaBawah() {
        JPanel panelBawah = new JPanel(new GridLayout(1, 2, 15, 0)); 
        panelBawah.setOpaque(false);
        panelBawah.setBorder(new EmptyBorder(10,0,0,0));

        JPanel panelDetailPerhitungan = new JPanel(new BorderLayout());
        panelDetailPerhitungan.setBackground(WARNA_PANEL_BAGIAN);
        panelDetailPerhitungan.setBorder(createTitledBorder("Detail Perhitungan SPK"));
        modelTabelDetailPerhitungan = new DefaultTableModel(null, new String[]{"Kode Karyawan", "Nama Karyawan"}) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelDetailPerhitungan = new JTable(modelTabelDetailPerhitungan);
        styleTable(tabelDetailPerhitungan, null); 
        JScrollPane scrollDetail = new JScrollPane(tabelDetailPerhitungan);
        scrollDetail.setPreferredSize(new Dimension(450, 200)); 
        panelDetailPerhitungan.add(scrollDetail, BorderLayout.CENTER);
        panelBawah.add(panelDetailPerhitungan);

        JPanel panelHasilPerangkingan = new JPanel(new BorderLayout());
        panelHasilPerangkingan.setBackground(WARNA_PANEL_BAGIAN);
        panelHasilPerangkingan.setBorder(createTitledBorder("Hasil Akhir Perangkingan"));
        modelTabelHasilPerangkingan = new DefaultTableModel(
                new Object[]{"Peringkat", "Kode Karyawan", "Nama Karyawan", "Nilai Total SPK"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelHasilPerangkingan = new JTable(modelTabelHasilPerangkingan);
        styleTable(tabelHasilPerangkingan, new int[]{70, 100, 200, 120});
        JScrollPane scrollPerangkingan = new JScrollPane(tabelHasilPerangkingan);
        scrollPerangkingan.setPreferredSize(new Dimension(450, 200));
        panelHasilPerangkingan.add(scrollPerangkingan, BorderLayout.CENTER);
        panelBawah.add(panelHasilPerangkingan);
        
        return panelBawah;
    }
    
    private TitledBorder createTitledBorder(String title){
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(WARNA_BORDER, 1), title);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titledBorder.setTitleColor(WARNA_SUBJUDUL_TEXT);
        return titledBorder;
    }

    private void styleButton(JButton button, Color bg, Color fg, Dimension size) {
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

    public void displayNilaiAlternatifPerKriteria(String[] columnNames, List<Object[]> data) {
        if (modelTabelNilaiAlternatifPerKriteria == null) return;
        modelTabelNilaiAlternatifPerKriteria.setColumnIdentifiers(columnNames);
        modelTabelNilaiAlternatifPerKriteria.setRowCount(0);
        if (data != null) {
            for (Object[] row : data) {
                modelTabelNilaiAlternatifPerKriteria.addRow(row);
            }
        }
        if (tabelNilaiAlternatifPerKriteria.getColumnCount() > 1) {
            tabelNilaiAlternatifPerKriteria.getColumnModel().getColumn(0).setPreferredWidth(100);
            tabelNilaiAlternatifPerKriteria.getColumnModel().getColumn(1).setPreferredWidth(180);
            for (int i = 2; i < tabelNilaiAlternatifPerKriteria.getColumnCount(); i++) {
                tabelNilaiAlternatifPerKriteria.getColumnModel().getColumn(i).setPreferredWidth(80);
            }
            tabelNilaiAlternatifPerKriteria.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        } else {
            tabelNilaiAlternatifPerKriteria.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
    }

    public void displayBobotKriteria(List<Object[]> dataBobotKriteria) {
        if (modelTabelBobotKriteria == null) return;
        modelTabelBobotKriteria.setRowCount(0);
        if (dataBobotKriteria != null) {
            for (Object[] row : dataBobotKriteria) {
                modelTabelBobotKriteria.addRow(row);
            }
        }
    }

    public void displayDetailPerhitungan(String[] columnNames, List<Object[]> dataDetail) {
        if (modelTabelDetailPerhitungan == null) return;
        modelTabelDetailPerhitungan.setColumnIdentifiers(columnNames);
        modelTabelDetailPerhitungan.setRowCount(0);
        if (dataDetail != null) {
            for (Object[] row : dataDetail) {
                modelTabelDetailPerhitungan.addRow(row);
            }
        }
         if (tabelDetailPerhitungan.getColumnCount() > 1) {
            tabelDetailPerhitungan.getColumnModel().getColumn(0).setPreferredWidth(100); 
            tabelDetailPerhitungan.getColumnModel().getColumn(1).setPreferredWidth(180); 
            for (int i = 2; i < tabelDetailPerhitungan.getColumnCount(); i++) {
                tabelDetailPerhitungan.getColumnModel().getColumn(i).setPreferredWidth(100); 
            }
            tabelDetailPerhitungan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        } else {
            tabelDetailPerhitungan.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
    }

    public void displayHasilPerangkingan(List<HasilSeleksi> dataPerangkingan) {
        if (modelTabelHasilPerangkingan == null) return;
        modelTabelHasilPerangkingan.setRowCount(0);
        if (dataPerangkingan != null) {
            for (HasilSeleksi hasil : dataPerangkingan) {
                modelTabelHasilPerangkingan.addRow(new Object[]{
                    hasil.getPeringkat(),
                    hasil.getKodeKaryawan(),
                    hasil.getNamaKaryawan(),
                    new DecimalFormat("#.###").format(hasil.getNilaiTotalSpk())
                });
            }
        }
    }
    
    public void enableSimpanButton(boolean enable) {
        if (btnSimpanHasil != null) {
            btnSimpanHasil.setEnabled(enable);
        }
    }

    public void showMessage(String message, boolean isError) {
        if (isError) JOptionPane.showMessageDialog(this, message, "Error Proses Seleksi", JOptionPane.ERROR_MESSAGE);
        else JOptionPane.showMessageDialog(this, message, "Informasi Proses Seleksi", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public SeleksiController getController(){
        return this.controller;
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
            JFrame frame = new JFrame("Panel Proses Seleksi & Perangkingan");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            PanelSeleksi panel = new PanelSeleksi();
            frame.getContentPane().add(panel);
            frame.setMinimumSize(new Dimension(1000, 750));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
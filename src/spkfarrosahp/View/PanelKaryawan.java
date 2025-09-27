package spkfarrosahp.View;

import spkfarrosahp.Config.KoneksiDatabase;
import spkfarrosahp.Controller.KaryawanController; // Import KaryawanController
import spkfarrosahp.Model.Karyawan; // Import Karyawan model
import spkfarrosahp.Config.UserSession; // <-- IMPORT UserSession
import spkfarrosahp.View.LoginForm;    // <-- IMPORT LoginForm
import spkfarrosahp.Model.Admin;   
import com.toedter.calendar.JDateChooser;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.border.Border;

public class PanelKaryawan extends JPanel {

    // Warna (tetap sama)
    private static final Color WARNA_BACKGROUND_PANEL = new Color(245, 248, 251);
    private static final Color WARNA_PANEL_INPUT_BG = Color.WHITE;
    private static final Color WARNA_BORDER = new Color(220, 225, 230);
    private static final Color WARNA_BORDER_FIELD = new Color(220, 220, 220);
    private static final Color WARNA_HEADER_TABLE = new Color(235, 239, 242);
    private static final Color WARNA_TOMBOL_AKSI_BIRU_BG = new Color(0, 123, 255);
    private static final Color WARNA_TOMBOL_AKSI_BIRU_FG = Color.WHITE;
    private static final Color WARNA_READ_ONLY_FIELD_BG = new Color(238, 238, 238);
    private static final Color WARNA_JUDUL_TEXT = new Color(50, 50, 90);


    private JComboBox<String> cmbJabatan;
    private JTextField txtKodeKaryawan;
    private JTextField txtNamaLengkap;
    private JDateChooser dateChooserTanggalLahir;
    private JTextArea txtAlamat;
    private JTextField txtTelepon;

    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnBatal;

    private JTextField txtSearch;
    private JTable tabelKaryawan;
    private DefaultTableModel modelTabelKaryawan; // Akan diinisialisasi di initComponents

    private KaryawanController controller;
    private SimpleDateFormat sdfUntukTabel = new SimpleDateFormat("dd-MM-yyyy");

    public PanelKaryawan() {
        setLayout(new BorderLayout(0, 15));
        setBackground(WARNA_BACKGROUND_PANEL);
        setBorder(new EmptyBorder(10, 15, 15, 15));

        initComponents(); // Inisialisasi komponen UI dulu, termasuk modelTabelKaryawan
        
        // --- PERUBAHAN URUTAN INISIALISASI CONTROLLER ---
        this.controller = new KaryawanController(this); // Controller dibuat SETELAH UI siap
        // controller.loadData(); // loadData sudah dipanggil di konstruktor KaryawanController
        // --- AKHIR PERUBAHAN ---
        
        generateKodeKaryawan(); // Panggil setelah cmbJabatan diinisialisasi
    }

    private void initComponents() {
        // ... (Isi method initComponents() tetap sama seperti kode terakhir yang berhasil compile) ...
        // Pastikan di dalam method ini, modelTabelKaryawan diinisialisasi:
        // String[] kolom = {"Kode", "Nama Lengkap", "Jabatan", "Tgl Lahir", "Alamat", "Telepon"};
        // modelTabelKaryawan = new DefaultTableModel(null, kolom) {
        //     @Override public boolean isCellEditable(int row, int column) { return false; }
        // };
        // tabelKaryawan = new JTable(modelTabelKaryawan);
        // ... dan seterusnya ...

        // == Panel Atas (Form Input dan Tombol Aksi) ==
        JPanel panelFormDanTombol = new JPanel(new BorderLayout(15,0));
        panelFormDanTombol.setOpaque(false);
        panelFormDanTombol.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 2, 2, new Color(200,200,220)),
                new EmptyBorder(15,15,15,15)
        ));
        panelFormDanTombol.setBackground(WARNA_PANEL_INPUT_BG);

        JPanel panelFormInput = new JPanel(new GridBagLayout());
        panelFormInput.setOpaque(false);
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.anchor = GridBagConstraints.WEST;
        gbcForm.insets = new Insets(5, 5, 5, 15);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;

        // Baris 1: Jabatan
        gbcForm.gridx = 0; gbcForm.gridy = 0; gbcForm.weightx = 0.2;
        panelFormInput.add(new JLabel("Jabatan:"), gbcForm);
        gbcForm.gridx = 1; gbcForm.weightx = 0.8;
        cmbJabatan = new JComboBox<>(new String[]{"Screen Printing", "Printing Support"});
        cmbJabatan.setFont(new Font("Arial", Font.PLAIN, 13));
        cmbJabatan.setBackground(Color.WHITE);
        cmbJabatan.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) generateKodeKaryawan();
        });
        panelFormInput.add(cmbJabatan, gbcForm);

        // Baris 2: Kode Karyawan
        gbcForm.gridx = 0; gbcForm.gridy++; gbcForm.weightx = 0.2;
        panelFormInput.add(new JLabel("Kode Karyawan:"), gbcForm);
        gbcForm.gridx = 1; gbcForm.weightx = 0.8;
        txtKodeKaryawan = new JTextField(15);
        txtKodeKaryawan.setEditable(false);
        txtKodeKaryawan.setBackground(WARNA_READ_ONLY_FIELD_BG);
        txtKodeKaryawan.setFont(new Font("Arial", Font.BOLD, 13));
        txtKodeKaryawan.setBorder(createFieldBorder());
        panelFormInput.add(txtKodeKaryawan, gbcForm);
        
        gbcForm.gridx = 1; gbcForm.gridy++; 
        JLabel lblKodeInfo = new JLabel("<html><i>Otomatis (misal: SP001 / PS001)</i></html>");
        lblKodeInfo.setFont(new Font("Arial", Font.ITALIC, 10));
        lblKodeInfo.setForeground(Color.GRAY);
        gbcForm.insets = new Insets(0, 5, 5, 15);
        panelFormInput.add(lblKodeInfo, gbcForm);
        gbcForm.insets = new Insets(5, 5, 5, 15);

        gbcForm.gridx = 0; gbcForm.gridy++; gbcForm.weightx = 0.2;
        panelFormInput.add(new JLabel("Nama Lengkap:"), gbcForm);
        gbcForm.gridx = 1; gbcForm.weightx = 0.8;
        txtNamaLengkap = new JTextField(15);
        txtNamaLengkap.setFont(new Font("Arial", Font.PLAIN, 13));
        txtNamaLengkap.setBorder(createFieldBorder());
        panelFormInput.add(txtNamaLengkap, gbcForm);

        gbcForm.gridx = 0; gbcForm.gridy++; gbcForm.weightx = 0.2;
        panelFormInput.add(new JLabel("Tanggal Lahir:"), gbcForm);
        gbcForm.gridx = 1; gbcForm.weightx = 0.8;
        dateChooserTanggalLahir = new JDateChooser();
        dateChooserTanggalLahir.setDateFormatString("dd MMMM yyyy");
        dateChooserTanggalLahir.setFont(new Font("Arial", Font.PLAIN, 13));
        ((JTextField) dateChooserTanggalLahir.getDateEditor().getUiComponent()).setBorder(createFieldBorder());
        ((JTextField) dateChooserTanggalLahir.getDateEditor().getUiComponent()).setBackground(Color.WHITE);
        dateChooserTanggalLahir.getCalendarButton().setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelFormInput.add(dateChooserTanggalLahir, gbcForm);

        gbcForm.gridx = 0; gbcForm.gridy++; gbcForm.anchor = GridBagConstraints.NORTHWEST; gbcForm.weightx = 0.2;
        panelFormInput.add(new JLabel("Alamat:"), gbcForm);
        gbcForm.gridx = 1; gbcForm.weightx = 0.8; gbcForm.fill = GridBagConstraints.BOTH; gbcForm.weighty = 0.5;
        txtAlamat = new JTextArea(3, 15);
        txtAlamat.setFont(new Font("Arial", Font.PLAIN, 13));
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);
        scrollAlamat.getViewport().setBackground(Color.WHITE);
        scrollAlamat.setBorder(createFieldBorder());
        panelFormInput.add(scrollAlamat, gbcForm);
        gbcForm.fill = GridBagConstraints.HORIZONTAL; gbcForm.weighty = 0.0; gbcForm.anchor = GridBagConstraints.WEST;

        gbcForm.gridx = 0; gbcForm.gridy++; gbcForm.weightx = 0.2;
        panelFormInput.add(new JLabel("Telepon:"), gbcForm);
        gbcForm.gridx = 1; gbcForm.weightx = 0.8;
        txtTelepon = new JTextField(15);
        txtTelepon.setFont(new Font("Arial", Font.PLAIN, 13));
        txtTelepon.setBorder(createFieldBorder());
        ((AbstractDocument) txtTelepon.getDocument()).setDocumentFilter(new DocumentFilter() {
            Pattern regEx = Pattern.compile("\\d*");
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                if (regEx.matcher(text).matches()) super.replace(fb, offset, length, text, attrs);
            }
        });
        panelFormInput.add(txtTelepon, gbcForm);

        panelFormDanTombol.add(panelFormInput, BorderLayout.CENTER);

        JPanel panelTombolAksi = new JPanel();
        panelTombolAksi.setLayout(new BoxLayout(panelTombolAksi, BoxLayout.Y_AXIS));
        panelTombolAksi.setOpaque(false);
        panelTombolAksi.setBorder(new EmptyBorder(0, 15, 0, 0));

        Dimension buttonSize = new Dimension(100, 36);

        btnSimpan = createActionButton("Simpan", WARNA_TOMBOL_AKSI_BIRU_BG, WARNA_TOMBOL_AKSI_BIRU_FG, buttonSize);
        btnUbah = createActionButton("Ubah", WARNA_TOMBOL_AKSI_BIRU_BG, WARNA_TOMBOL_AKSI_BIRU_FG, buttonSize);
        btnHapus = createActionButton("Hapus", WARNA_TOMBOL_AKSI_BIRU_BG, WARNA_TOMBOL_AKSI_BIRU_FG, buttonSize);
        btnBatal = createActionButton("Batal", WARNA_TOMBOL_AKSI_BIRU_BG, WARNA_TOMBOL_AKSI_BIRU_FG, buttonSize);

        panelTombolAksi.add(btnSimpan);
        panelTombolAksi.add(Box.createRigidArea(new Dimension(0, 10)));
        panelTombolAksi.add(btnUbah);
        panelTombolAksi.add(Box.createRigidArea(new Dimension(0, 10)));
        panelTombolAksi.add(btnHapus);
        panelTombolAksi.add(Box.createRigidArea(new Dimension(0, 10)));
        panelTombolAksi.add(btnBatal);
        panelTombolAksi.add(Box.createVerticalGlue());

        panelFormDanTombol.add(panelTombolAksi, BorderLayout.EAST);
        add(panelFormDanTombol, BorderLayout.NORTH);

        JPanel panelBawah = new JPanel(new BorderLayout(0, 10));
        panelBawah.setOpaque(false);
        panelBawah.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 2, 2, new Color(200,200,220)),
                new EmptyBorder(15,15,15,15)
        ));
        panelBawah.setBackground(WARNA_PANEL_INPUT_BG);

        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSearch.setOpaque(false);
        txtSearch = new JTextField(30);
        txtSearch.putClientProperty("JTextField.placeholderText", "Cari Kode atau Nama Karyawan...");
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 13));
        txtSearch.setBorder(createFieldBorder());
        txtSearch.addActionListener(e -> controller.cariKaryawan(txtSearch.getText().trim()));
        JButton btnCari = new JButton("Cari");
        btnCari.addActionListener(e -> controller.cariKaryawan(txtSearch.getText().trim()));
        panelSearch.add(new JLabel("Cari:"));
        panelSearch.add(txtSearch);
        panelSearch.add(btnCari);
        panelBawah.add(panelSearch, BorderLayout.NORTH);

        String[] kolom = {"Kode", "Nama Lengkap", "Jabatan", "Tgl Lahir", "Alamat", "Telepon"};
        modelTabelKaryawan = new DefaultTableModel(null, kolom) { // Inisialisasi di sini
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelKaryawan = new JTable(modelTabelKaryawan); // Gunakan model yang sudah diinisialisasi
        tabelKaryawan.setFont(new Font("Arial", Font.PLAIN, 12));
        JTableHeader headerTabel = tabelKaryawan.getTableHeader();
        headerTabel.setFont(new Font("Arial", Font.BOLD, 13));
        headerTabel.setBackground(WARNA_HEADER_TABLE);
        headerTabel.setForeground(new Color(60,60,60));
        headerTabel.setBorder(new LineBorder(new Color(210,210,210)));
        headerTabel.setReorderingAllowed(false);
        tabelKaryawan.setRowHeight(28);
        tabelKaryawan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelKaryawan.setGridColor(new Color(225,225,225));
        tabelKaryawan.setShowGrid(true);
        tabelKaryawan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int selectedRow = tabelKaryawan.getSelectedRow();
                    if (selectedRow != -1) {
                        String kodeKaryawanTerpilih = modelTabelKaryawan.getValueAt(selectedRow, 0).toString();
                        controller.isiFormDariTabel(kodeKaryawanTerpilih);
                    }
                }
            }
        });
        JScrollPane scrollTabel = new JScrollPane(tabelKaryawan);
        scrollTabel.getViewport().setBackground(Color.WHITE);
        scrollTabel.setBorder(new LineBorder(WARNA_BORDER));
        panelBawah.add(scrollTabel, BorderLayout.CENTER);

        add(panelBawah, BorderLayout.CENTER);

        btnSimpan.addActionListener(e -> controller.simpanKaryawan());
        btnUbah.addActionListener(e -> controller.updateKaryawan());
        btnHapus.addActionListener(e -> controller.hapusKaryawan());
        btnBatal.addActionListener(e -> clearFieldsAndSelection());
    }

    private Border createFieldBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(WARNA_BORDER_FIELD),
            new EmptyBorder(6, 8, 6, 8)
        );
    }
    
    private JButton createActionButton(String text, Color backgroundColor, Color foregroundColor, Dimension preferredSize) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(9, 20, 9, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (preferredSize != null) {
            button.setPreferredSize(preferredSize);
            button.setMaximumSize(preferredSize);
            button.setMinimumSize(preferredSize);
        }
        return button;
    }

    public void generateKodeKaryawan() {
        // Pastikan controller sudah diinisialisasi
        if (controller != null && cmbJabatan != null && txtKodeKaryawan != null) {
            String jabatanTerpilih = (String) cmbJabatan.getSelectedItem();
            txtKodeKaryawan.setText(controller.generateNewKodeKaryawan(jabatanTerpilih));
        } else if (txtKodeKaryawan != null) {
            // Fallback jika controller atau cmbJabatan belum siap
            txtKodeKaryawan.setText("");
        }
    }

    public void displayDataInTable(List<Karyawan> daftarKaryawan) {
        if (modelTabelKaryawan == null) { // Antisipasi jika dipanggil sebelum initComponents
            System.err.println("modelTabelKaryawan belum diinisialisasi di PanelKaryawan.displayDataInTable");
            return;
        }
        modelTabelKaryawan.setRowCount(0);
        if (daftarKaryawan != null) {
            for (Karyawan k : daftarKaryawan) {
                Object[] rowData = {
                    k.getKodeKaryawan(),
                    k.getNamaLengkap(),
                    k.getJabatan(),
                    k.getTanggalLahir() != null ? sdfUntukTabel.format(k.getTanggalLahir()) : "",
                    k.getAlamat(),
                    k.getTelepon()
                };
                modelTabelKaryawan.addRow(rowData);
            }
        }
    }

    public void showMessage(String message, boolean isError) {
        if (isError) {
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, message, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public String getKodeKaryawan() { return txtKodeKaryawan != null ? txtKodeKaryawan.getText() : ""; }
    public String getNamaLengkap() { return txtNamaLengkap != null ? txtNamaLengkap.getText().trim() : ""; }
    public String getJabatan() { return cmbJabatan != null ? (String) cmbJabatan.getSelectedItem() : ""; }
    public Date getTanggalLahir() { return dateChooserTanggalLahir != null ? dateChooserTanggalLahir.getDate() : null; }
    public String getAlamat() { return txtAlamat != null ? txtAlamat.getText().trim() : ""; }
    public String getTelepon() { return txtTelepon != null ? txtTelepon.getText().trim() : ""; }

    public void requestTeleponFocus() {
        if (txtTelepon != null) txtTelepon.requestFocusInWindow();
    }

    public void requestKodeKaryawanFocus() {
        if (txtNamaLengkap != null) txtNamaLengkap.requestFocusInWindow();
    }
    
    public void clearFieldsAndSelection() {
        if (txtNamaLengkap != null) txtNamaLengkap.setText("");
        if (cmbJabatan != null && cmbJabatan.getItemCount() > 0) {
            cmbJabatan.setSelectedIndex(0); // Ini akan trigger generateKodeKaryawan jika listener aktif
        } else if (cmbJabatan != null) {
             generateKodeKaryawan();
        } else { // Jika cmbJabatan null
            if (txtKodeKaryawan != null) txtKodeKaryawan.setText(controller.generateNewKodeKaryawan(null));
        }
        
        if (dateChooserTanggalLahir != null) dateChooserTanggalLahir.setDate(null);
        if (txtAlamat != null) txtAlamat.setText("");
        if (txtTelepon != null) txtTelepon.setText("");
        if(tabelKaryawan != null) tabelKaryawan.clearSelection();
        setTombolUpdateMode(false);
        if(txtNamaLengkap != null) txtNamaLengkap.requestFocusInWindow();
    }

    public String getSelectedKodeKaryawanFromTable() {
        if (tabelKaryawan == null || modelTabelKaryawan == null) return null;
        int selectedRow = tabelKaryawan.getSelectedRow();
        if (selectedRow != -1) {
            return modelTabelKaryawan.getValueAt(selectedRow, 0).toString();
        }
        return null;
    }

    public void setFormData(String kode, String nama, String jabatan, Date tglLahir, String alamat, String telepon) {
        if (txtKodeKaryawan != null) txtKodeKaryawan.setText(kode);
        if (txtNamaLengkap != null) txtNamaLengkap.setText(nama);
        if (cmbJabatan != null) cmbJabatan.setSelectedItem(jabatan);
        if (dateChooserTanggalLahir != null) dateChooserTanggalLahir.setDate(tglLahir);
        if (txtAlamat != null) txtAlamat.setText(alamat);
        if (txtTelepon != null) txtTelepon.setText(telepon);
    }

    public void setTombolUpdateMode(boolean isUpdate) {
        if (btnSimpan != null) btnSimpan.setEnabled(!isUpdate);
        if (btnUbah != null) btnUbah.setEnabled(isUpdate);
        if (btnHapus != null) btnHapus.setEnabled(isUpdate);
    }

    private void batalInput() {
       clearFieldsAndSelection();
       // generateKodeKaryawan sudah dipanggil via cmbJabatan.setSelectedIndex(0)
    }
    
    private void simpanData() { if(controller != null) controller.simpanKaryawan(); }
    private void ubahData() { if(controller != null) controller.updateKaryawan(); }
    private void hapusData() { if(controller != null) controller.hapusKaryawan(); }
    public KaryawanController getController() {
    return this.controller;
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
            
            JFrame frame = new JFrame("Panel Manajemen Karyawan");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new PanelKaryawan());
            frame.setMinimumSize(new Dimension(900, 700));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
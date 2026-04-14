package ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import model.DiagnosisResult;
import model.PatientInput;
import service.RuleBasedDiagnosis;
import service.WeightedDiagnosis;

public class MainFrame extends JFrame {

    // input numerik utama
    private final JSpinner spTemp = new JSpinner(new SpinnerNumberModel(36.5, 30.0, 45.0, 0.1));
    private final JSpinner spWater = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 20.0, 0.5));
    private final JSpinner spUrineCount = new JSpinner(new SpinnerNumberModel(0, 0, 30, 1));
    private final JSpinner spBloodSugar = new JSpinner(new SpinnerNumberModel(100, 50, 600, 1));
    private final JSpinner spSys = new JSpinner(new SpinnerNumberModel(120, 70, 250, 1));
    private final JSpinner spDia = new JSpinner(new SpinnerNumberModel(80, 40, 150, 1));

    // checkbox gejala infeksi
    private final JCheckBox cbChills = new JCheckBox("Menggigil ringan");
    private final JCheckBox cbFeverSuddenHigh = new JCheckBox("Demam tinggi mendadak");
    private final JCheckBox cbFeverFluctuate = new JCheckBox("Demam naik turun 2-7 hari");

    private final JCheckBox cbCoughDry = new JCheckBox("Batuk kering");
    private final JCheckBox cbCoughPhlegm = new JCheckBox("Berdahak ringan");
    private final JCheckBox cbRunnyNose = new JCheckBox("Pilek / hidung berair / tersumbat");
    private final JCheckBox cbSoreThroat = new JCheckBox("Sakit tenggorokan");

    private final JCheckBox cbJointPain = new JCheckBox("Nyeri otot / tulang / sendi");
    private final JCheckBox cbRash = new JCheckBox("Ruam / bintik merah");
    private final JCheckBox cbTourniquetPos = new JCheckBox("Uji torniket positif");
    private final JCheckBox cbNauseaVomiting = new JCheckBox("Mual / muntah");
    private final JCheckBox cbAppetiteDown = new JCheckBox("Nafsu makan turun");

    // checkbox gejala non-infeksi
    private final JCheckBox cbThirsty = new JCheckBox("Sering haus");
    private final JCheckBox cbFrequentUrination = new JCheckBox("Sering buang air kecil");
    private final JCheckBox cbNightUrination = new JCheckBox("Sering BAK malam hari");
    private final JCheckBox cbFatigue = new JCheckBox("Mudah lelah");
    private final JCheckBox cbWoundSlowHeal = new JCheckBox("Luka sulit sembuh");
    private final JCheckBox cbRecurrentInfection = new JCheckBox("Infeksi berulang");

    private final JCheckBox cbHeadache = new JCheckBox("Sakit kepala");
    private final JCheckBox cbThrobbingBackHead = new JCheckBox("Berdenyut di belakang kepala");
    private final JCheckBox cbDizziness = new JCheckBox("Pusing / terasa berputar");
    private final JCheckBox cbBlurryVision = new JCheckBox("Penglihatan kabur");
    private final JCheckBox cbNosebleed = new JCheckBox("Mimisan");
    private final JCheckBox cbFastHeartRate = new JCheckBox("Detak jantung meningkat");

    // area output hasil diagnosa
    private final JTextArea output = new JTextArea();

    // font ui
    private final Font fontNormal = new Font("SansSerif", Font.PLAIN, 16);
    private final Font fontBold = new Font("SansSerif", Font.BOLD, 17);

    public MainFrame() {
        setTitle("Sistem Pakar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 760);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // atur gaya komponen
        applyStyle();

        // header di bagian atas
        add(createHeader(), BorderLayout.NORTH);

        // isi utama di tengah
        add(createMainContent(), BorderLayout.CENTER);

        // tombol aksi dipindah keluar dari scroll
        add(createButtonsPanel(), BorderLayout.SOUTH);
    }

    // mengatur tampilan spinner dan checkbox
    private void applyStyle() {
        styleSpinner(spTemp);
        styleSpinner(spWater);
        styleSpinner(spUrineCount);
        styleSpinner(spBloodSugar);
        styleSpinner(spSys);
        styleSpinner(spDia);

        for (JCheckBox cb : allChecks()) {
            cb.setFont(fontNormal);
        }
    }

    // styling input angka
    private void styleSpinner(JSpinner sp) {
        sp.setFont(fontNormal);
        sp.setPreferredSize(new Dimension(145, 34));

        JComponent editor = sp.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JFormattedTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setFont(fontNormal);
            tf.setHorizontalAlignment(JTextField.CENTER);
            tf.setColumns(8);
        }
    }

    // header aplikasi
    private JComponent createHeader() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(new EmptyBorder(10, 12, 0, 12));

        JLabel title = new JLabel("Sistem Pakar Diagnosa Penyakit");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel subtitle = new JLabel("Pendekatan berbasis aturan dan sistem bobot untuk infeksi dan non-infeksi.");
        subtitle.setFont(fontNormal);

        panel.add(title);
        panel.add(subtitle);
        return panel;
    }

    // panel utama yang berisi input dan output
    private JComponent createMainContent() {
        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createInputPanel(),
                createOutputPanel()
        );
        split.setResizeWeight(0.62);
        split.setDividerLocation(620);
        split.setContinuousLayout(true);
        split.setOneTouchExpandable(true);
        return split;
    }

    // panel input utama dengan scroll
    private JComponent createInputPanel() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(new EmptyBorder(10, 10, 10, 10));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(createGeneralPanel());
        container.add(Box.createVerticalStrut(10));
        container.add(createInfluenzaPanel());
        container.add(Box.createVerticalStrut(10));
        container.add(createDbdPanel());
        container.add(Box.createVerticalStrut(10));
        container.add(createDiabetesPanel());
        container.add(Box.createVerticalStrut(10));
        container.add(createHypertensionPanel());

        container.setPreferredSize(new Dimension(920, 1400));

        JScrollPane scroll = new JScrollPane(container);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);

        return scroll;
    }

    // bagian data umum
    private JPanel createGeneralPanel() {
        JPanel panel = sectionPanel("Data Umum");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(fieldRow("Suhu tubuh (°C)", spTemp));
        panel.add(checkboxRow(cbFeverSuddenHigh));
        panel.add(checkboxRow(cbFeverFluctuate));
        panel.add(checkboxRow(cbChills));
        return panel;
    }

    // bagian gejala influenza
    private JPanel createInfluenzaPanel() {
        JPanel panel = sectionPanel("Infeksi - Influenza");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(checkboxRow(cbCoughDry));
        panel.add(checkboxRow(cbCoughPhlegm));
        panel.add(checkboxRow(cbRunnyNose));
        panel.add(checkboxRow(cbSoreThroat));
        return panel;
    }

    // bagian gejala demam berdarah
    private JPanel createDbdPanel() {
        JPanel panel = sectionPanel("Infeksi - Demam Berdarah");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(checkboxRow(cbJointPain));
        panel.add(checkboxRow(cbRash));
        panel.add(checkboxRow(cbTourniquetPos));
        panel.add(checkboxRow(cbNauseaVomiting));
        panel.add(checkboxRow(cbAppetiteDown));
        return panel;
    }

    // bagian gejala diabetes
    private JPanel createDiabetesPanel() {
        JPanel panel = sectionPanel("Non-Infeksi - Diabetes");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(checkboxRow(cbThirsty));
        panel.add(fieldRow("Minum per hari (liter)", spWater));
        panel.add(checkboxRow(cbFrequentUrination));
        panel.add(fieldRow("Buang air kecil per hari", spUrineCount));
        panel.add(checkboxRow(cbNightUrination));
        panel.add(checkboxRow(cbFatigue));
        panel.add(fieldRow("Kadar gula darah (mg/dL)", spBloodSugar));
        panel.add(checkboxRow(cbWoundSlowHeal));
        panel.add(checkboxRow(cbRecurrentInfection));
        return panel;
    }

    // bagian gejala hipertensi
    private JPanel createHypertensionPanel() {
        JPanel panel = sectionPanel("Non-Infeksi - Hipertensi");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(checkboxRow(cbHeadache));
        panel.add(checkboxRow(cbThrobbingBackHead));
        panel.add(checkboxRow(cbDizziness));
        panel.add(checkboxRow(cbBlurryVision));
        panel.add(fieldRow("Tekanan darah sistolik", spSys));
        panel.add(fieldRow("Tekanan darah diastolik", spDia));
        panel.add(checkboxRow(cbNosebleed));
        panel.add(checkboxRow(cbFastHeartRate));
        return panel;
    }

    // tombol aksi ditempatkan di luar scroll
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

        JButton btnRule = new JButton("Rule-Based");
        JButton btnWeight = new JButton("Weighted");
        JButton btnClear = new JButton("Clear");

        btnRule.setFont(fontBold);
        btnWeight.setFont(fontBold);
        btnClear.setFont(fontBold);

        btnRule.addActionListener(e -> diagnoseRule());
        btnWeight.addActionListener(e -> diagnoseWeight());
        btnClear.addActionListener(e -> clearForm());

        panel.add(btnRule);
        panel.add(btnWeight);
        panel.add(btnClear);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    // panel output hasil
    private JComponent createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 14));
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        output.setText("Hasil diagnosa akan tampil di sini.\n");

        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createTitledBorder("Output"));

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // membuat satu bagian panel dengan judul
    private JPanel sectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 13)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return panel;
    }

    // baris untuk input angka
    private JPanel fieldRow(String label, JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = new JLabel(label);
        l.setFont(fontNormal);
        l.setPreferredSize(new Dimension(260, 24));

        panel.add(l);
        panel.add(component);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        return panel;
    }

    // baris untuk checkbox
    private JPanel checkboxRow(JCheckBox checkbox) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkbox.setFont(fontNormal);
        panel.add(checkbox);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        return panel;
    }

    // ambil input dari ui ke object
    private PatientInput getInput() {
        PatientInput p = new PatientInput();

        p.temperature = ((Number) spTemp.getValue()).doubleValue();
        p.waterLiters = ((Number) spWater.getValue()).doubleValue();
        p.urinationCount = ((Number) spUrineCount.getValue()).intValue();
        p.bloodSugar = ((Number) spBloodSugar.getValue()).intValue();
        p.bpSys = ((Number) spSys.getValue()).intValue();
        p.bpDia = ((Number) spDia.getValue()).intValue();

        p.chills = cbChills.isSelected();
        p.feverSuddenHigh = cbFeverSuddenHigh.isSelected();
        p.feverFluctuate = cbFeverFluctuate.isSelected();

        p.coughDry = cbCoughDry.isSelected();
        p.coughPhlegm = cbCoughPhlegm.isSelected();
        p.runnyNose = cbRunnyNose.isSelected();
        p.soreThroat = cbSoreThroat.isSelected();

        p.jointPain = cbJointPain.isSelected();
        p.rash = cbRash.isSelected();
        p.tourniquetPositive = cbTourniquetPos.isSelected();
        p.nauseaVomiting = cbNauseaVomiting.isSelected();
        p.appetiteDown = cbAppetiteDown.isSelected();

        p.thirsty = cbThirsty.isSelected();
        p.frequentUrination = cbFrequentUrination.isSelected();
        p.nightUrination = cbNightUrination.isSelected();
        p.fatigue = cbFatigue.isSelected();
        p.woundSlowHeal = cbWoundSlowHeal.isSelected();
        p.recurrentInfection = cbRecurrentInfection.isSelected();

        p.headache = cbHeadache.isSelected();
        p.throbbingBackHead = cbThrobbingBackHead.isSelected();
        p.dizziness = cbDizziness.isSelected();
        p.blurryVision = cbBlurryVision.isSelected();
        p.nosebleed = cbNosebleed.isSelected();
        p.fastHeartRate = cbFastHeartRate.isSelected();

        return p;
    }

    // diagnosa dengan aturan
    private void diagnoseRule() {
        PatientInput input = getInput();
        DiagnosisResult result = RuleBasedDiagnosis.diagnose(input);
        output.setText(buildReport("DIAGNOSA BERBASIS ATURAN", input, result));
    }

    // diagnosa dengan bobot
    private void diagnoseWeight() {
        PatientInput input = getInput();
        DiagnosisResult result = WeightedDiagnosis.diagnose(input);
        output.setText(buildReport("DIAGNOSA SISTEM BOBOT", input, result));
    }

    // format hasil diagnosa
    private String buildReport(String title, PatientInput input, DiagnosisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append("\n");
        sb.append("============================================================\n");
        sb.append(input.fullSummary()).append("\n");
        sb.append("Hasil diagnosa:\n");
        sb.append("- penyakit  : ").append(result.disease).append("\n");
        sb.append("- kategori  : ").append(result.category).append("\n");
        sb.append("- keyakinan : ").append(result.confidence).append("\n");
        sb.append("- detail    : ").append(result.detail).append("\n");

        if (result.scores != null && !result.scores.isEmpty()) {
            sb.append("\nSkor tiap penyakit:\n");
            for (Map.Entry<String, Double> e : result.scores.entrySet()) {
                sb.append(String.format("- %-15s : %.2f%%%n", e.getKey(), e.getValue()));
            }
        }

        return sb.toString();
    }

    // format persen
    private String formatScores(Map<String, Double> scores) {
        if (scores == null || scores.isEmpty()) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Double> e : scores.entrySet()) {
            if (!first) sb.append(" | ");
            sb.append(e.getKey()).append("=").append(String.format("%.1f%%", e.getValue()));
            first = false;
        }
        return sb.toString();
    }

    // hapus isi form
    private void clearForm() {
        spTemp.setValue(36.5);
        spWater.setValue(0.0);
        spUrineCount.setValue(0);
        spBloodSugar.setValue(100);
        spSys.setValue(120);
        spDia.setValue(80);

        for (JCheckBox cb : allChecks()) {
            cb.setSelected(false);
        }

        output.setText("Form dibersihkan.\n");
    }

    // kumpulan checkbox untuk styling
    private List<JCheckBox> allChecks() {
        List<JCheckBox> list = new ArrayList<>();
        list.add(cbChills);
        list.add(cbFeverSuddenHigh);
        list.add(cbFeverFluctuate);
        list.add(cbCoughDry);
        list.add(cbCoughPhlegm);
        list.add(cbRunnyNose);
        list.add(cbSoreThroat);
        list.add(cbJointPain);
        list.add(cbRash);
        list.add(cbTourniquetPos);
        list.add(cbNauseaVomiting);
        list.add(cbAppetiteDown);
        list.add(cbThirsty);
        list.add(cbFrequentUrination);
        list.add(cbNightUrination);
        list.add(cbFatigue);
        list.add(cbWoundSlowHeal);
        list.add(cbRecurrentInfection);
        list.add(cbHeadache);
        list.add(cbThrobbingBackHead);
        list.add(cbDizziness);
        list.add(cbBlurryVision);
        list.add(cbNosebleed);
        list.add(cbFastHeartRate);
        return list;
    }
}
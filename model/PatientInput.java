package model;

import java.util.ArrayList;
import java.util.List;

public class PatientInput {

    // data numerik pasien
    public double temperature;
    public double waterLiters;
    public int urinationCount;
    public int bloodSugar;
    public int bpSys;
    public int bpDia;

    // gejala umum demam
    public boolean chills;
    public boolean feverSuddenHigh;
    public boolean feverFluctuate;

    // gejala influenza
    public boolean coughDry;
    public boolean coughPhlegm;
    public boolean runnyNose;
    public boolean soreThroat;

    // gejala demam berdarah
    public boolean jointPain;
    public boolean rash;
    public boolean tourniquetPositive;
    public boolean nauseaVomiting;
    public boolean appetiteDown;

    // gejala diabetes
    public boolean thirsty;
    public boolean frequentUrination;
    public boolean nightUrination;
    public boolean fatigue;
    public boolean woundSlowHeal;
    public boolean recurrentInfection;

    // gejala hipertensi
    public boolean headache;
    public boolean throbbingBackHead;
    public boolean dizziness;
    public boolean blurryVision;
    public boolean nosebleed;
    public boolean fastHeartRate;

    // ringkasan gejala yang dipilih user
    public String summary() {
        List<String> items = new ArrayList<>();

        if (chills) items.add("menggigil ringan");
        if (feverSuddenHigh) items.add("demam tinggi mendadak");
        if (feverFluctuate) items.add("pola demam naik turun 2-7 hari");

        if (coughDry) items.add("batuk kering");
        if (coughPhlegm) items.add("berdahak ringan");
        if (runnyNose) items.add("pilek / hidung berair / tersumbat");
        if (soreThroat) items.add("sakit tenggorokan");

        if (jointPain) items.add("nyeri otot / tulang / sendi");
        if (rash) items.add("ruam / bintik merah");
        if (tourniquetPositive) items.add("uji torniket positif");
        if (nauseaVomiting) items.add("mual / muntah");
        if (appetiteDown) items.add("nafsu makan turun");

        if (thirsty) items.add("sering haus");
        if (frequentUrination) items.add("sering buang air kecil");
        if (nightUrination) items.add("sering BAK malam hari");
        if (fatigue) items.add("mudah lelah");
        if (woundSlowHeal) items.add("luka sulit sembuh");
        if (recurrentInfection) items.add("infeksi berulang");

        if (headache) items.add("sakit kepala");
        if (throbbingBackHead) items.add("berdenyut di belakang kepala");
        if (dizziness) items.add("pusing / terasa berputar");
        if (blurryVision) items.add("penglihatan kabur");
        if (nosebleed) items.add("mimisan");
        if (fastHeartRate) items.add("detak jantung meningkat");

        // jika tidak ada gejala
        if (items.isEmpty()) {
            return "-";
        }

        // gabungkan semua gejala menjadi satu string
        return String.join(", ", items);
    }

    // ringkasan lengkap data pasien (angka + gejala)
    public String fullSummary() {
        StringBuilder sb = new StringBuilder();

        // bagian data numerik
        sb.append("Data angka:\n");
        sb.append(String.format("- suhu tubuh           : %.1f c%n", temperature));
        sb.append(String.format("- minum per hari       : %.1f liter%n", waterLiters));
        sb.append(String.format("- buang air kecil      : %d kali/hari%n", urinationCount));
        sb.append(String.format("- gula darah           : %d mg/dl%n", bloodSugar));
        sb.append(String.format("- tekanan darah        : %d/%d mmhg%n", bpSys, bpDia));

        // bagian gejala
        sb.append("\nGejala yang dicentang:\n");
        sb.append("- ").append(summary()).append("\n");

        return sb.toString();
    }
}
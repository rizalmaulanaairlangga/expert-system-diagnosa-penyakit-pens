package service;

import model.DiagnosisResult;
import model.PatientInput;

public class WeightedDiagnosis {

    // method utama untuk melakukan diagnosa berbasis bobot
    public static DiagnosisResult diagnose(PatientInput in) {
        DiagnosisResult res = new DiagnosisResult();

        // hitung skor masing-masing penyakit
        double influenzaScore = scoreInfluenza(in);
        double dbdScore = scoreDbd(in);
        double diabetesScore = scoreDiabetes(in);
        double hipertensiScore = scoreHipertensi(in);

        // simpan skor ke dalam map hasil
        res.scores.put("Influenza", influenzaScore);
        res.scores.put("Demam Berdarah", dbdScore);
        res.scores.put("Diabetes", diabetesScore);
        res.scores.put("Hipertensi", hipertensiScore);

        // cari penyakit dengan skor tertinggi
        String bestDisease = "Tidak diketahui";
        double bestScore = -1.0;

        for (String key : res.scores.keySet()) {
            double value = res.scores.get(key);
            if (value > bestScore) {
                bestScore = value;
                bestDisease = key;
            }
        }

        // buat detail persentase semua penyakit
        res.detail =
                "Influenza     : " + formatPercent(influenzaScore) + "\n" +
                "Demam Berdarah: " + formatPercent(dbdScore) + "\n" +
                "Diabetes      : " + formatPercent(diabetesScore) + "\n" +
                "Hipertensi    : " + formatPercent(hipertensiScore) + "\n";

        // jika skor tertinggi masih rendah, dianggap belum cukup data
        if (bestScore < 45.0) {
            res.disease = "Belum cukup data";
            res.category = "-";
            res.confidence = formatPercent(bestScore);
            res.detail = res.detail + "\nskor tertinggi masih di bawah ambang 45%.";
            return res;
        }

        // tentukan hasil akhir berdasarkan skor tertinggi
        res.disease = bestDisease;
        res.category = (bestDisease.equals("Influenza") || bestDisease.equals("Demam Berdarah"))
                ? "Infeksi"
                : "Non-Infeksi";
        res.confidence = formatPercent(bestScore);
        res.detail = res.detail + "\ndiagnosis diambil dari skor tertinggi.";
        return res;
    }

    // hitung skor influenza berdasarkan gejala
    private static double scoreInfluenza(PatientInput in) {
        double score = 0.0;
        double max = 10.0;

        if (in.temperature >= 37.5 && in.temperature <= 39.0) score += 3;
        if (in.chills) score += 1;
        if (in.coughDry) score += 2;
        if (in.coughPhlegm) score += 1;
        if (in.runnyNose) score += 2;
        if (in.soreThroat) score += 1;

        // konversi ke persen
        return (score / max) * 100.0;
    }

    // hitung skor demam berdarah
    private static double scoreDbd(PatientInput in) {
        double score = 0.0;
        double max = 13.0;

        if (in.temperature > 39.0) score += 3;
        if (in.feverSuddenHigh) score += 1;
        if (in.feverFluctuate) score += 1;
        if (in.jointPain) score += 2;
        if (in.rash) score += 2;
        if (in.tourniquetPositive) score += 2;
        if (in.nauseaVomiting) score += 1;
        if (in.appetiteDown) score += 1;

        return (score / max) * 100.0;
    }

    // hitung skor diabetes
    private static double scoreDiabetes(PatientInput in) {
        double score = 0.0;
        double max = 13.0;

        if (in.thirsty || in.waterLiters > 3.0) score += 3;
        if (in.frequentUrination) score += 2;
        if (in.urinationCount > 8) score += 2;
        if (in.nightUrination) score += 1;
        if (in.fatigue) score += 1;
        if (in.bloodSugar > 200) score += 3;
        if (in.woundSlowHeal) score += 1;
        if (in.recurrentInfection) score += 1;

        return (score / max) * 100.0;
    }

    // hitung skor hipertensi
    private static double scoreHipertensi(PatientInput in) {
        double score = 0.0;
        double max = 9.0;

        if (in.bpSys >= 140 && in.bpDia >= 90) score += 3;
        if (in.headache) score += 1;
        if (in.throbbingBackHead) score += 1;
        if (in.dizziness) score += 1;
        if (in.blurryVision) score += 1;
        if (in.nosebleed) score += 1;
        if (in.fastHeartRate) score += 1;

        return (score / max) * 100.0;
    }

    // format nilai menjadi persen
    private static String formatPercent(double value) {
        return String.format("%.2f%%", value);
    }
}
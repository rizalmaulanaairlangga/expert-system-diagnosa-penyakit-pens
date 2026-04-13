package service;

import java.util.ArrayList;
import java.util.List;
import model.DiagnosisResult;
import model.PatientInput;

public class RuleBasedDiagnosis {

    // method utama untuk menentukan diagnosa berdasarkan aturan
    public static DiagnosisResult diagnose(PatientInput in) {
        // evaluasi tiap penyakit
        Candidate flu = evaluateInfluenza(in);
        Candidate dbd = evaluateDbd(in);
        Candidate diabetes = evaluateDiabetes(in);
        Candidate hipertensi = evaluateHipertensi(in);

        // pilih kandidat dengan skor tertinggi
        Candidate best = flu;
        if (dbd.score > best.score) best = dbd;
        if (diabetes.score > best.score) best = diabetes;
        if (hipertensi.score > best.score) best = hipertensi;

        DiagnosisResult res = new DiagnosisResult();

        // jika tidak memenuhi aturan utama
        if (!best.positive) {
            res.disease = "Tidak diketahui";
            res.category = "-";
            res.confidence = "Rendah";
            res.detail = "Gejala belum cukup untuk memenuhi aturan utama.";
            return res;
        }

        // hasil akhir diagnosa
        res.disease = best.disease;
        res.category = best.category;
        res.confidence = best.score >= 6 ? "Tinggi" : "Sedang";
        res.detail = "Aturan cocok: " + String.join(", ", best.matches);
        return res;
    }

    // evaluasi aturan untuk influenza
    private static Candidate evaluateInfluenza(PatientInput in) {
        int score = 0;
        List<String> matches = new ArrayList<>();

        if (in.temperature >= 37.5 && in.temperature <= 39.0) {
            score += 2;
            matches.add("suhu 37.5-39 c");
        }
        if (in.chills) {
            score += 1;
            matches.add("menggigil ringan");
        }
        if (in.coughDry) {
            score += 2;
            matches.add("batuk kering");
        }
        if (in.coughPhlegm) {
            score += 1;
            matches.add("berdahak ringan");
        }
        if (in.runnyNose) {
            score += 2;
            matches.add("pilek");
        }
        if (in.soreThroat) {
            score += 2;
            matches.add("sakit tenggorokan");
        }

        // aturan utama influenza
        boolean positive = score >= 5 && (in.coughDry || in.runnyNose || in.soreThroat);
        return new Candidate("Influenza", "Infeksi", score, positive, matches);
    }

    // evaluasi aturan demam berdarah
    private static Candidate evaluateDbd(PatientInput in) {
        int score = 0;
        List<String> matches = new ArrayList<>();

        if (in.temperature > 39.0) {
            score += 2;
            matches.add("suhu > 39 c");
        }
        if (in.feverSuddenHigh) {
            score += 1;
            matches.add("demam tinggi mendadak");
        }
        if (in.feverFluctuate) {
            score += 1;
            matches.add("demam naik turun 2-7 hari");
        }
        if (in.jointPain) {
            score += 2;
            matches.add("nyeri otot / tulang / sendi");
        }
        if (in.rash) {
            score += 2;
            matches.add("ruam / bintik merah");
        }
        if (in.tourniquetPositive) {
            score += 2;
            matches.add("uji torniket positif");
        }
        if (in.nauseaVomiting) {
            score += 1;
            matches.add("mual / muntah");
        }
        if (in.appetiteDown) {
            score += 1;
            matches.add("nafsu makan turun");
        }

        // aturan utama dbd
        boolean positive = score >= 5 && (in.temperature > 39.0) && (in.rash || in.tourniquetPositive);
        return new Candidate("Demam Berdarah", "Infeksi", score, positive, matches);
    }

    // evaluasi aturan diabetes
    private static Candidate evaluateDiabetes(PatientInput in) {
        int score = 0;
        List<String> matches = new ArrayList<>();

        if (in.thirsty || in.waterLiters > 3.0) {
            score += 3;
            matches.add("sering haus / minum > 3 liter");
        }
        if (in.frequentUrination || in.urinationCount > 8 || in.nightUrination) {
            score += 3;
            matches.add("sering buang air kecil");
        }
        if (in.fatigue) {
            score += 1;
            matches.add("mudah lelah");
        }
        if (in.bloodSugar > 200) {
            score += 3;
            matches.add("gula darah > 200 mg/dl");
        }
        if (in.woundSlowHeal || in.recurrentInfection) {
            score += 1;
            matches.add("luka sulit sembuh / infeksi berulang");
        }

        // aturan utama diabetes
        boolean positive = score >= 6 && in.bloodSugar > 200;
        return new Candidate("Diabetes", "Non-Infeksi", score, positive, matches);
    }

    // evaluasi aturan hipertensi
    private static Candidate evaluateHipertensi(PatientInput in) {
        int score = 0;
        List<String> matches = new ArrayList<>();

        if (in.bpSys >= 140 && in.bpDia >= 90) {
            score += 3;
            matches.add("tekanan darah >= 140/90");
        }
        if (in.headache) {
            score += 1;
            matches.add("sakit kepala");
        }
        if (in.throbbingBackHead) {
            score += 1;
            matches.add("berdenyut di belakang kepala");
        }
        if (in.dizziness) {
            score += 1;
            matches.add("pusing / terasa berputar");
        }
        if (in.blurryVision) {
            score += 1;
            matches.add("penglihatan kabur");
        }
        if (in.nosebleed) {
            score += 1;
            matches.add("mimisan");
        }
        if (in.fastHeartRate) {
            score += 1;
            matches.add("detak jantung meningkat");
        }

        // aturan utama hipertensi
        boolean positive = score >= 4 && in.bpSys >= 140 && in.bpDia >= 90;
        return new Candidate("Hipertensi", "Non-Infeksi", score, positive, matches);
    }

    // class untuk menyimpan kandidat diagnosa
    private static class Candidate {
        final String disease;
        final String category;
        final int score;
        final boolean positive;
        final List<String> matches;

        Candidate(String disease, String category, int score, boolean positive, List<String> matches) {
            this.disease = disease;
            this.category = category;
            this.score = score;
            this.positive = positive;
            this.matches = matches;
        }
    }
}
package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class DiagnosisResult {

    // hasil utama diagnosa
    public String disease = "-";
    public String category = "-";
    public String confidence = "-";
    public String detail = "-";

    // menyimpan skor tiap penyakit (digunakan pada weighted)
    public Map<String, Double> scores = new LinkedHashMap<>();
}
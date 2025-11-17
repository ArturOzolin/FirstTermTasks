package com.mipt.arturozolin.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TextFileAnalyzer {

  public static class AnalysisResult {
    private final long lineCount;
    private final long wordCount;
    private final long charCount;
    private final Map<Character, Long> charFrequency;

    public AnalysisResult(long lineCount, long wordCount, long charCount, Map<Character, Long> charFrequency) {
      this.lineCount = lineCount;
      this.wordCount = wordCount;
      this.charCount = charCount;
      this.charFrequency = charFrequency;
    }

    public long lineCount() {
      return lineCount;
    }

    public long wordCount() {
      return wordCount;
    }

    public long charCount() {
      return charCount;
    }

    public Map<Character, Long> charFrequency() {
      return charFrequency;
    }

    @Override
    public String toString() {
      return String.format("AnalysisResult{lines=%d, words=%d, chars=%d, charFreq=%s}",
              lineCount, wordCount, charCount, charFrequency);
    }
  }
  public AnalysisResult analyzeFile(String filePath) throws IOException {
    long lineCount = 0;
    long wordCount = 0;
    long charCount = 0;
    Map<Character, Long> charFrequency = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(
            new FileReader(filePath, StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        ++lineCount;
        charCount += line.length();
        wordCount += line.trim().isEmpty() ? 0 : line.trim().split("\\s+").length;
        for (char c : line.toCharArray()) {
          charFrequency.put(c, charFrequency.getOrDefault(c, 0L) + 1);
        }
      }
    }
    return new AnalysisResult(lineCount, wordCount, charCount, charFrequency);
  }
  public void saveAnalysisResult(AnalysisResult result, String outputPath) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(
            new FileWriter(outputPath, StandardCharsets.UTF_8))) {
      writer.write(String.format("Количество строк: %,d", result.lineCount()));
      writer.newLine();
      writer.write(String.format("Количество слов: %,d", result.wordCount()));
      writer.newLine();
      writer.write(String.format("Количество символов: %,d", result.charCount()));
      writer.newLine();
      writer.write("Частота символов:");
      writer.newLine();
      for (Map.Entry<Character, Long> entry : result.charFrequency().entrySet()) {
        writer.write(String.format("%c: %,d", entry.getKey(), entry.getValue()));
        writer.newLine();
      }
      writer.flush();
    }
  }
}

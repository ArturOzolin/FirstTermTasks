package com.mipt.arturozolin.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.util.Map;

class TextFileAnalyzerTest {

  @TempDir
  Path tempDir;

  @Test
  void testAnalyzeFile() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Path testFile = tempDir.resolve("test.txt");
    List<String> lines = Arrays.asList("Hello world!", "This is a test.", "Java IO");
    Files.write(testFile, lines);

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    assertEquals(3, result.lineCount(), "Количество строк должно быть 3");
    assertEquals(8, result.wordCount(), "Количество слов должно быть 8");
    assertEquals(34, result.charCount(), "Количество символов должно быть 34");
    assertTrue(result.charFrequency().containsKey('H'), "Должен быть символ 'H' в частоте");
  }

  @Test
  void testSaveAnalysisResult() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    TextFileAnalyzer.AnalysisResult result = new TextFileAnalyzer.AnalysisResult(2, 5, 20, Map.of('a', 5L, 'b', 2L));

    Path outputFile = tempDir.resolve("analysis.txt");
    analyzer.saveAnalysisResult(result, outputFile.toString());

    assertTrue(Files.exists(outputFile), "Файл должен быть создан");
    assertTrue(Files.size(outputFile) > 0, "Файл не должен быть пустым");

    List<String> savedLines = Files.readAllLines(outputFile);
    String content = String.join("\n", savedLines);

    assertTrue(content.contains("Количество строк: 2"));
    assertTrue(content.contains("Количество слов: 5"));
    assertTrue(content.contains("Количество символов: 20"));
    assertTrue(content.contains("a: 5"));
    assertTrue(content.contains("b: 2"));
  }
}

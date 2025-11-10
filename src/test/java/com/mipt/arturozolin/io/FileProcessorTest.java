package com.mipt.arturozolin.io;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessorTest {

  @Test
  void testSplitAndMergeFile() throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = Files.createTempFile("test", ".dat");
    byte[] testData = new byte[1500];
    new Random().nextBytes(testData);
    Files.write(testFile, testData);

    String outputDir = Files.createTempDirectory("parts").toString();
    List<Path> parts = processor.splitFile(testFile.toString(), outputDir, 500);

    assertEquals(3, parts.size());

    long totalSize = Files.size(testFile);
    long partSize = Files.size(parts.get(0));
    assertTrue(partSize <= 500);
    assertTrue(totalSize > 0);

    Path mergedFile = Files.createTempFile("merged", ".dat");
    processor.mergeFiles(parts, mergedFile.toString());

    assertArrayEquals(Files.readAllBytes(testFile), Files.readAllBytes(mergedFile));
  }

  @Test
  void testEmptyFileSplit() throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = Files.createTempFile("empty", ".dat");
    Files.write(testFile, new byte[0]);

    String outputDir = Files.createTempDirectory("parts").toString();
    List<Path> parts = processor.splitFile(testFile.toString(), outputDir, 500);

    assertEquals(0, parts.size());

    Path mergedFile = Files.createTempFile("merged", ".dat");
    processor.mergeFiles(parts, mergedFile.toString());

    assertEquals(0, Files.size(mergedFile));
  }

  @Test
  void testSmallFileSplit() throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = Files.createTempFile("small", ".dat");
    byte[] smallData = new byte[100];
    new Random().nextBytes(smallData);
    Files.write(testFile, smallData);

    String outputDir = Files.createTempDirectory("parts").toString();
    List<Path> parts = processor.splitFile(testFile.toString(), outputDir, 500);

    assertEquals(1, parts.size());

    Path mergedFile = Files.createTempFile("merged", ".dat");
    processor.mergeFiles(parts, mergedFile.toString());

    assertArrayEquals(Files.readAllBytes(testFile), Files.readAllBytes(mergedFile));
  }

  @Test
  void testMergeFilesInWrongOrder() throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = Files.createTempFile("test", ".dat");
    byte[] testData = new byte[1500];
    new Random().nextBytes(testData);
    Files.write(testFile, testData);

    String outputDir = Files.createTempDirectory("parts").toString();
    List<Path> parts = processor.splitFile(testFile.toString(), outputDir, 500);

    Collections.reverse(parts);

    Path mergedFile = Files.createTempFile("merged", ".dat");
    processor.mergeFiles(parts, mergedFile.toString());

    assertNotEquals(Files.readAllBytes(testFile), Files.readAllBytes(mergedFile));
  }

  @Test
  void testLargePartSize() throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = Files.createTempFile("test", ".dat");
    byte[] testData = new byte[1500];
    new Random().nextBytes(testData);
    Files.write(testFile, testData);

    String outputDir = Files.createTempDirectory("parts").toString();
    List<Path> parts = processor.splitFile(testFile.toString(), outputDir, 2000);

    assertEquals(1, parts.size());

    Path mergedFile = Files.createTempFile("merged", ".dat");
    processor.mergeFiles(parts, mergedFile.toString());

    assertArrayEquals(Files.readAllBytes(testFile), Files.readAllBytes(mergedFile));
  }

  @Test
  void testExceptionOnNonExistentFile() {
    FileProcessor processor = new FileProcessor();

    assertThrows(IOException.class, () -> {
      processor.splitFile("nonexistent_file.txt", "outputDir", 500);
    });
  }
}

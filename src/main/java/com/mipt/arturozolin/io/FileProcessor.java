package com.mipt.arturozolin.io;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.*;

public class FileProcessor {
  public List<Path> splitFile(String sourcePath, String outputDir, int partSize) throws IOException {
    File sourceFile = new File(sourcePath);
    List<Path> partPaths = new ArrayList<>();
    try (RandomAccessFile source = new RandomAccessFile(sourceFile, "r");
         FileChannel sourceChannel = source.getChannel()) {
      long fileSize = sourceFile.length();
      long partsCount = (fileSize + partSize - 1) / partSize;
      for (int i = 0; i < partsCount; i++) {
        String partName = sourceFile.getName() + ".part" + (i + 1);
        Path partPath = Paths.get(outputDir, partName);
        partPaths.add(partPath);

        try (FileChannel outputChannel = new FileOutputStream(partPath.toFile()).getChannel()) {
          long position = i * partSize;
          long remaining = fileSize - position;
          long transferSize = Math.min(partSize, remaining);

          ByteBuffer buffer = ByteBuffer.allocate((int) transferSize);
          sourceChannel.position(position);
          sourceChannel.read(buffer);
          buffer.flip();
          outputChannel.write(buffer);
        }
      }
    }
    return partPaths;
  }

  public void mergeFiles(List<Path> partPaths, String outputPath) throws IOException {
    try (FileChannel outputChannel = new FileOutputStream(outputPath).getChannel()) {
      for (Path partPath : partPaths) {
        try (FileChannel inputChannel = new FileInputStream(partPath.toFile()).getChannel()) {
          inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        }
      }
    }
  }
}

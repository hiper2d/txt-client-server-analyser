package com.hiper2d.visor;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileCountVisor extends SimpleFileVisitor<Path> {
	@Getter
	private int fileCount;

	@Override
	public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
		String filename = filePath.getFileName().toString();
		if (filename.endsWith(".txt")) {
			fileCount++;
		}
		return FileVisitResult.CONTINUE;
	}
}

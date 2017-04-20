package com.hiper2d.visor;

import com.hiper2d.TxtAnalyzer;
import com.hiper2d.domain.analyze.Directory;
import com.hiper2d.domain.analyze.TxtFile;
import lombok.Getter;
import reactor.core.publisher.EmitterProcessor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Optional;

public class FileAnalyzeVisor extends SimpleFileVisitor<Path> {
	@Getter
	private Directory root;
	private Directory currentDir;
	private TxtAnalyzer analyzer;
	private EmitterProcessor<String> stream;
	private int directoryLevel;

	public FileAnalyzeVisor(EmitterProcessor<String> stream) {
		analyzer = new TxtAnalyzer();
		this.stream = stream;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		String dirname = dir.getFileName().toString();
		if (root == null) {
			createRootDirectory(dirname);
		} else {
			createSubdirectory(dirname);
		}
		return super.preVisitDirectory(dir, attrs);
	}

	@Override
	public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
		String filename = filePath.getFileName().toString();
		if (!filename.endsWith(".txt")) {
			return FileVisitResult.CONTINUE;
		}
		TxtFile builtFile = buildTxtFile(filePath, filename);
		currentDir.addFile(builtFile);
		stream.onNext("Scanning a file: " + filename);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		directoryLevel--;
		currentDir = currentDir.getParent();
		return super.postVisitDirectory(dir, exc);
	}

	private void createSubdirectory(String dirname) {
		int dirIndex = currentDir.getSubdirectories().size() + 1;
		Directory newNode = Directory.builder().name(dirname).parent(currentDir).level(++directoryLevel).index(dirIndex).build();
		currentDir.addSubdirectory(newNode);
		currentDir = newNode;
	}

	private void createRootDirectory(String dirname) {
		root = Directory.builder().name(dirname).build();
		currentDir = root;
	}

	private TxtFile buildTxtFile(Path filePath, String filename) throws IOException {
		int fileIndex = currentDir.getFiles().size() + 1;
		TxtFile.TxtFileBuilder txtFileBuilder = TxtFile.builder()
				.name(filename)
				.index(fileIndex)
				.path(filePath);
		addBigWords(filePath, txtFileBuilder);
		return txtFileBuilder.build();
	}

	private void addBigWords(Path filePath, TxtFile.TxtFileBuilder txtFileBuilder) throws IOException {
		Optional<Map<String, Integer>> bigWordsFound = analyzer.analyzeFile(filePath);
		bigWordsFound.ifPresent(bigWords -> txtFileBuilder.big(true).bigWords(bigWords));
	}
}

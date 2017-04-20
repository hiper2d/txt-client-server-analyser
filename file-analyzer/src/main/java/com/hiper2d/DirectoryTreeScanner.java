package com.hiper2d;

import com.hiper2d.domain.analyze.Directory;
import com.hiper2d.visor.FileAnalyzeVisor;
import com.hiper2d.visor.FileCountVisor;
import reactor.core.publisher.EmitterProcessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DirectoryTreeScanner {
	public static Directory scan(String relativePath, EmitterProcessor<String> stream) throws IOException {
		FileAnalyzeVisor visor = new FileAnalyzeVisor(stream);
		Files.walkFileTree(Paths.get(relativePath), visor);
		return visor.getRoot();
	}

	public static int getFilesCount(String relativePath) throws IOException {
		FileCountVisor visor = new FileCountVisor();
		Files.walkFileTree(Paths.get(relativePath), visor);
		return visor.getFileCount();
	}
}

package com.hiper2d.visor;

import com.hiper2d.BaseScanTest;
import com.hiper2d.domain.analyze.Directory;
import com.hiper2d.domain.analyze.TxtFile;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class FileAnalyzeVisorTest extends BaseScanTest {
	private FileAnalyzeVisor visor;
	private int fileIndex;

	@Before
	public void setup() throws IOException {
		visor = new FileAnalyzeVisor(stream);
		Files.walkFileTree(Paths.get(PATH_DATA), visor);
	}

	@Test
	public void filesHasCorrectIndexes() {
		Directory root = visor.getRoot();
		checkFileIndexesInDirectory(root);
		root.getSubdirectories().forEach(this::checkFileIndexesInDirectory);
	}

	@Test
	public void scannedDirectoriesHasLevels() throws IOException {
		Directory root = visor.getRoot();
		assertEquals(0, root.getLevel());
		checkLevelsInSubdirectories(root);
	}

	private void checkFileIndexesInDirectory(Directory root) {
		fileIndex = 1;
		root.getFiles().stream()
				.sorted(Comparator.comparingInt(TxtFile::getIndex))
				.forEach(file -> assertEquals(fileIndex++, file.getIndex()));
	}

	private void checkLevelsInSubdirectories(Directory root) {
		root.getSubdirectories().forEach(firstLevel -> {
			assertEquals(1, firstLevel.getLevel());
			firstLevel.getSubdirectories().forEach(secondLevel -> {
				assertEquals(2, secondLevel.getLevel());
			});
		});
	}
}
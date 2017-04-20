package com.hiper2d;

import com.hiper2d.domain.analyze.Directory;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DirectoryTreeScannerTest extends BaseScanTest {
	@Test
	public void countFiles() throws Exception {
		int fileCount = DirectoryTreeScanner.getFilesCount(PATH_DATA);
		assertEquals("There should be 6 files in data directory",6, fileCount);
	}

	@Test
	public void scanDataDirectory() throws IOException, URISyntaxException {
		Directory data = DirectoryTreeScanner.scan(PATH_DATA, stream);
		assertNotNull("Scan should return some root", data);
		assertEquals("There should be 3 files", 3, data.getFiles().size());
		assertEquals("There should be 2 subdirectory", 2, data.getSubdirectories().size());
	}

	@Test
	public void ignoreNonTextFiles() {

	}
}
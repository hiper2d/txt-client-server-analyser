package com.hiper2d.visor;

import com.hiper2d.BaseScanTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class FileCountVisorTest extends BaseScanTest {
	private FileCountVisor visor;

	@Before
	public void setup() {
		visor = new FileCountVisor();
	}

	@Test
	public void countTxtFiles() throws IOException {
		Files.walkFileTree(Paths.get(PATH_DATA), visor);
		assertEquals(6, visor.getFileCount());
	}
}
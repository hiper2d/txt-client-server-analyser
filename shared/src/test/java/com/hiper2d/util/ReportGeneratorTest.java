package com.hiper2d.util;

import com.hiper2d.domain.analyze.Directory;
import com.hiper2d.domain.analyze.TxtFile;
import com.hiper2d.domain.scan.ScanReport;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

public class ReportGeneratorTest {
	private static final String BIG_FILES_SCAN_OUTPUT =
			"<data>\n" +
				"\t<file #1 b> <#words> <the 1000>\n" +
				"\t<sub-directory #1 sub>\n" +
					"\t\t<file #1 b> <#words> <the 1000>";

	private static final String SMALL_FILES_SCAN_OUTPUT =
			"<data>\n" +
				"\t<file #1 a> <#words>\n" +
				"\t<sub-directory #1 sub>\n" +
					"\t\t<file #1 a> <#words>";

	private Directory root;

	@Before
	public void setup() {
		root = generateDirectoryStructure();
	}

	@Test
	public void generateCorrectReportWithReIndexedFiles() throws IOException, URISyntaxException {
		ScanReport scanReport = ReportGenerator.generate(root);
		System.out.println(scanReport.getBigFilesRoot());
		System.out.println(scanReport.getSmallFilesRoot());
		assertEquals(BIG_FILES_SCAN_OUTPUT, scanReport.getBigFilesRoot().toString());
		assertEquals(SMALL_FILES_SCAN_OUTPUT, scanReport.getSmallFilesRoot().toString());
	}

	private Directory generateDirectoryStructure() {
		Set<TxtFile> files = new TreeSet<>(Arrays.asList(
				TxtFile.builder().name("a").index(1).build(),
				TxtFile.builder().name("b").index(2).big(true).bigWord("the", 1000).build()
		));
		Directory root = Directory.builder().name("data").files(files).build();
		Directory sub = Directory.builder().name("sub").parent(root).files(files).index(1).level(1).build();
		root.addSubdirectory(sub);
		return root;
	}
}
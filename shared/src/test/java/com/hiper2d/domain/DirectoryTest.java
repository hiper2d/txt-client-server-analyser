package com.hiper2d.domain;

import com.hiper2d.domain.analyze.Directory;
import com.hiper2d.domain.analyze.TxtFile;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

public class DirectoryTest extends DirectoryBaseTest {
	protected final String COMPLEX_SCAN_OUTPUT =
			"<data>\n" +
				"\t<file #1 a> <#words>\n" +
				"\t<file #2 b> <#words> <the 1000>\n" +
				"\t<sub-directory #1 sub1>\n" +
					"\t\t<file #1 a> <#words>\n" +
					"\t\t<file #2 b> <#words> <the 1000>\n" +
					"\t\t<sub-directory #1 sub1sub1>\n" +
						"\t\t\t<file #1 a> <#words>\n" +
						"\t\t\t<file #2 b> <#words> <the 1000>\n" +
				"\t<sub-directory #2 sub2>\n" +
					"\t\t<file #1 a> <#words>\n" +
					"\t\t<file #2 b> <#words> <the 1000>";

	@Test
	public void validToString() throws Exception {
		Directory root = generateDirectoryTreeWithFiles();
		assertEquals(COMPLEX_SCAN_OUTPUT, root.toString());
	}
}
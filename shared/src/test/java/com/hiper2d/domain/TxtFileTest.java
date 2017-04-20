package com.hiper2d.domain;

import com.hiper2d.domain.analyze.TxtFile;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TxtFileTest {
	@Test
	public void validToStringWithWords() {
		TxtFile txtFile = TxtFile.builder()
				.index(1)
				.name("Some file.txt")
				.big(true)
				.bigWord("one", 100)
				.bigWord("two", 200)
				.bigWord("three", 300)
				.build();
		assertTrue(txtFile.toString().matches("<file #1 Some file\\.txt> <#words> <one 100> <two 200> <three 300>"));
	}

	@Test
	public void validToStringWithoutWords() {
		TxtFile txtFile = TxtFile.builder()
				.index(1)
				.name("Some file.txt")
				.build();
		assertTrue(txtFile.toString().matches("<file #1 Some file\\.txt> <#words>"));
	}
}
package com.hiper2d;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TxtAnalyzerTest extends BaseScanTest {
	@Test
	public void countWords() throws IOException {
		TxtAnalyzer analyzer = new TxtAnalyzer();
		Optional<Map<String, Integer>> result = analyzer.analyzeFile(Paths.get(PATH_ANDROMEDA));
		assertFalse("Should be empty because there is less than 1000 words in '" + PATH_ANDROMEDA + "'", result.isPresent());

		TxtAnalyzer lowLimitsAnalyzer = new TxtAnalyzer(9, 10);
		result = lowLimitsAnalyzer.analyzeFile(Paths.get(PATH_ELEVEN));
		assertEquals(11, result.get().get("the").intValue());
	}

	@Test
	public void ignoreEmptyLines() throws IOException {
		TxtAnalyzer lowLimitsAnalyzer = new TxtAnalyzer(0, 0);
		Optional<Map<String, Integer>> result = lowLimitsAnalyzer.analyzeFile(Paths.get(PATH_ELEVEN));
		assertEquals(2, result.get().entrySet().size());
	}
}
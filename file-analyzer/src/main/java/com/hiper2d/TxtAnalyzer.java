package com.hiper2d;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TxtAnalyzer {
	private static final int SAME_WORDS_COUNT_LIMIT = 50;
	private static final int WORDS_COUNT_LIMIT = 1000;

	private int wordsCount;
	private final int sameWordsCountLimit;
	private final int wordsCountLimit;

	public TxtAnalyzer() {
		sameWordsCountLimit = SAME_WORDS_COUNT_LIMIT;
		wordsCountLimit = WORDS_COUNT_LIMIT;
	}

	public Optional<Map<String, Integer>> analyzeFile(Path filePath) throws IOException {
		wordsCount = 0;
		Map<String, Integer> words = countDifferentWords(filePath);
		return Optional.ofNullable((wordsCount > wordsCountLimit) ? extractFrequentWords(words) : null);
	}

	private Map<String, Integer> extractFrequentWords(Map<String, Integer> words) {
		return words.entrySet().stream()
				.filter(e -> e.getValue() > sameWordsCountLimit)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private Map<String, Integer> countDifferentWords(Path filePath) throws IOException {
		Map<String, Integer> words = new HashMap<>();
		Files.newBufferedReader(filePath).lines()
				.filter(line -> !line.isEmpty())
				.map(line -> line.split("[\\s]+"))
				.flatMap(Arrays::stream)
				.forEach(w -> {
					words.putIfAbsent(w, 0);
					words.compute(w, (k, v) -> v += 1);
					wordsCount++;
				});
		return words;
	}
}

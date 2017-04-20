package com.hiper2d.domain.analyze;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiConsumer;

@Getter
@Builder
public class TxtFile implements Comparable<TxtFile> {
	@NonNull
	private String name;
	@JsonIgnore
	private Path path;
	@Setter
	private int index;
	private boolean big;
	@Singular
	private Map<String, Integer> bigWords;

	@Override
	public int compareTo(TxtFile o) {
		return new Integer(index).compareTo(o.getIndex());
	}

	@Override
	public String toString() {
		StringBuilder builder = stringifyFileNameAndIndex();
		if (big && bigWords.entrySet().size() > 0) {
			bigWords.forEach(stringifyWords(builder));
		}
		return builder.toString();
	}

	@JsonIgnore
	public boolean isSmall() {
		return !big;
	}

	private StringBuilder stringifyFileNameAndIndex() {
		StringBuilder builder = new StringBuilder();
		builder.append("<file #")
				.append(index)
				.append(" ")
				.append(name)
				.append("> <#words>");
		return builder;
	}

	private BiConsumer<String, Integer> stringifyWords(StringBuilder builder) {
		return (key, value) -> builder.append(" <")
				.append(key).append(" ")
				.append(value)
				.append(">");
	}
}

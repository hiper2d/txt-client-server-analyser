package com.hiper2d.domain.analyze;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

@Getter
@Builder
public class Directory implements Comparable<Directory> {
	@NonNull
	private String name;
	private Set<TxtFile> files;
	private Set<Directory> subdirectories;
	@JsonIgnore
	private Directory parent;
	private int level;
	private int index;

	public boolean addSubdirectory(Directory directory) {
		return getSubdirectories().add(directory);
	}

	public boolean addFile(TxtFile file) {
		return getFiles().add(file);
	}

	public Set<TxtFile> getFiles() {
		if (files == null) {
			files = new TreeSet<>();
		}
		return files;
	}

	public Set<Directory> getSubdirectories() {
		if (subdirectories == null) {
			subdirectories = new TreeSet<>();
		}
		return subdirectories;
	}

	@Override
	public int compareTo(Directory o) {
		return new Integer(index).compareTo(o.getIndex());
	}

	/*@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		appendDirectory(builder);
		appendFiles(builder);
		appendSubdirectories(builder);
		return builder.toString();
	}*/

	private void appendDirectory(StringBuilder builder) {
		if (!isRoot()) {
			builder.append("\n");
		}
		IntStream.range(0, level).forEach(appendTabConsumer(builder));
		builder.append("<");
		if (!isRoot()) {
			builder.append("sub-directory #").append(index).append(" ");
		}
		builder.append(name).append(">");
	}

	private void appendFiles(StringBuilder builder) {
		getFiles().stream().sorted(TxtFile::compareTo).forEach(file -> {
			builder.append("\n");
			IntStream.rangeClosed(0, level).forEach(appendTabConsumer(builder));
			builder.append(file);
		});
	}

	private void appendSubdirectories(StringBuilder builder) {
		getSubdirectories().stream().sorted(Directory::compareTo).forEach(builder::append);
	}

	private IntConsumer appendTabConsumer(StringBuilder builder) {
		return i -> builder.append("\t");
	}

	private boolean isRoot() {
		return level == 0;
	}
}

package com.hiper2d.domain;

import com.hiper2d.domain.analyze.Directory;
import com.hiper2d.domain.analyze.TxtFile;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public abstract class DirectoryBaseTest {
	protected Directory generateDirectoryTreeWithFiles() {
		Set<TxtFile> files = new TreeSet<>(Arrays.asList(
				TxtFile.builder().name("a").index(1).build(),
				TxtFile.builder().name("b").index(2).big(true).bigWord("the", 1000).build()
		));
		Directory root = Directory.builder().name("data").files(files).build();

		Directory sub1 = Directory.builder().name("sub1").parent(root).files(files).index(1).level(1).build();
		Directory sub2 = Directory.builder().name("sub2").parent(root).files(files).index(2).level(1).build();
		root.addSubdirectory(sub1);
		root.addSubdirectory(sub2);

		Directory sub1sub1 = Directory.builder().name("sub1sub1").parent(sub1).files(files).index(1).level(2).build();
		sub1.addSubdirectory(sub1sub1);
		return root;
	}
}

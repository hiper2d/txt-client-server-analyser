package com.hiper2d.util;

import com.hiper2d.domain.analyze.Directory;
import com.hiper2d.domain.analyze.TxtFile;
import com.hiper2d.domain.scan.ScanReport;

import java.util.function.Function;
import java.util.function.Predicate;

public class ReportGenerator {

	public static ScanReport generate(Directory root) {
		Directory bigFilesRoot = copyDir(root, TxtFile::isBig);
		Directory smallFilesRoot = copyDir(root, TxtFile::isSmall);
		return new ScanReport(bigFilesRoot, smallFilesRoot);
	}

	private static Directory copyDir(Directory originalDir, Predicate<TxtFile> fileFilterPredicate) {
		return copyDir(originalDir, null, fileFilterPredicate);
	}

	private static Directory copyDir(Directory originalDir, Directory copyDirParent, Predicate<TxtFile> fileFilterPredicate) {
		Directory copyDir = copyDirWithFilteredFiles(originalDir, copyDirParent, fileFilterPredicate);
		originalDir.getSubdirectories().forEach(sub -> copyDir(sub, copyDir, fileFilterPredicate));
		return copyDir;
	}

	private static Directory copyDirWithFilteredFiles(Directory originalDir, Directory copyDirParent, Predicate<TxtFile> fileFilterPredicate) {
		Directory copyDir = Directory.builder()
				.level(originalDir.getLevel())
				.name(originalDir.getName())
				.index(originalDir.getIndex())
				.parent(copyDirParent)
				.build();
		copyFiles(originalDir, fileFilterPredicate, copyDir);
		saveCopiedSubdirectoryToParent(copyDirParent, copyDir);
		return copyDir;
	}

	private static void copyFiles(Directory originalDir, Predicate<TxtFile> fileFilterPredicate, Directory copyDir) {
		int[] indexKeeper = { 1 };
		originalDir.getFiles().stream()
				.filter(fileFilterPredicate)
				.map(getReindexFunction(indexKeeper))
				.forEach(copyDir::addFile);
	}

	private static void saveCopiedSubdirectoryToParent(Directory copyDirParent, Directory copyDir) {
		if (copyDirParent != null) {
			copyDirParent.addSubdirectory(copyDir);
		}
	}

	private static Function<TxtFile, TxtFile> getReindexFunction(int[] indexKeepHack) {
		return file -> {
			file.setIndex(indexKeepHack[0]++);
			return file;
		};
	}
}

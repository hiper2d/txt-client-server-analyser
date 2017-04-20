package com.hiper2d.service.impl;

import com.hiper2d.DirectoryTreeScanner;
import com.hiper2d.domain.analyze.Directory;
import com.hiper2d.exception.ServerException;
import com.hiper2d.service.ScanService;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Server;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import reactor.core.publisher.EmitterProcessor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

@Async
@Log4j2
@Service
public class ScanServiceImpl implements ScanService {
	public ListenableFuture<Directory> scan(String path, EmitterProcessor<String> stream) {
		Directory root;
		int filesCount;
		stream.onNext("Started scanning process");
		try {
			filesCount = DirectoryTreeScanner.getFilesCount(path);
			stream.onNext("Found " + filesCount + " file(s) in " + Paths.get(path).normalize().toAbsolutePath());
			root = DirectoryTreeScanner.scan(path, stream);
		} catch (IOException e) {
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
			throw new ServerException(e);
		} finally {
			stream.onComplete();
			stream.blockLast();
		}
		return new AsyncResult<>(root);
	}
}

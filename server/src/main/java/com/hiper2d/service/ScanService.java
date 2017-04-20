package com.hiper2d.service;

import com.hiper2d.domain.analyze.Directory;
import com.hiper2d.domain.scan.ScanResult;
import org.springframework.util.concurrent.ListenableFuture;
import reactor.core.publisher.EmitterProcessor;

import java.io.IOException;
import java.net.URISyntaxException;


public interface ScanService {
	ListenableFuture<Directory> scan(String path, EmitterProcessor<String> stream);
}

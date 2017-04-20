package com.hiper2d;

import org.junit.Before;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

public abstract class BaseScanTest {
	protected static final String PATH_DATA = "src/test/resources/data";

	static final String PATH_ANDROMEDA = PATH_DATA + "/Andromeda.txt";
	static final String PATH_ELEVEN = PATH_DATA + "/11.txt";

	protected EmitterProcessor<String> stream;

	@Before
	public void setupStreams() {
		stream = EmitterProcessor.<String>create().connect();
		Flux<String> flux = stream.doOnNext(s -> {});
		flux.subscribe();
	}
}

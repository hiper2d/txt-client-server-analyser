package com.hiper2d.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiper2d.domain.analyze.Directory;
import com.hiper2d.domain.scan.ScanResult;
import com.hiper2d.util.ReportGenerator;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JacksonTest extends DirectoryBaseTest {

	@Test
	public void serializeAndDeserialize() throws IOException {
		Directory root = generateDirectoryTreeWithFiles();
		ScanResult res = ScanResult.builder().message("Done").report(ReportGenerator.generate(root)).completed(true).build();

		ObjectMapper mapper = new ObjectMapper();
		byte[] json = mapper.writeValueAsBytes(res);
		ScanResult back = mapper.readValue(json, ScanResult.class);

		assertEquals(res.getMessage(), back.getMessage());
		assertEquals(res.isCompleted(), back.isCompleted());
		assertEquals(res.isError(), back.isError());

		assertEquals(res.getReport().getSmallFilesRoot().toString(), back.getReport().getSmallFilesRoot().toString());
		assertEquals(res.getReport().getBigFilesRoot().toString(), back.getReport().getBigFilesRoot().toString());
	}
}

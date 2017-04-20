package com.hiper2d.domain.scan;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonSerialize
@JsonDeserialize
public class ScanResult {
	private String message;
	private ScanReport report;
	private boolean error;
	private boolean completed;
}

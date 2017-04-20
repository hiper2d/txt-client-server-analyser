package com.hiper2d.domain.scan;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hiper2d.domain.analyze.Directory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
public class ScanReport {
	Directory bigFilesRoot;
	Directory smallFilesRoot;
}

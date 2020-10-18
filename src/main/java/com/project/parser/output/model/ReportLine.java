package com.project.parser.output.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by konstantin on 18.10.2020.
 */
@Builder
@Getter
public class ReportLine {
    @CsvBindByName(column = "className")
    private final String className;
    @CsvBindByName(column = "moduleName")
    private final String moduleName;
    @CsvBindByName(column = "methodName")
    private final String methodName;
    @CsvBindByName(column = "fromEndpoint")
    private final String fromEndpoint;
    @CsvBindByName(column = "toView")
    private final String toView;
    @CsvBindByName(column = "httpMethod")
    private final String httpMethod;
    @CsvBindByName(column = "parameters")
    private final String parameters;
}

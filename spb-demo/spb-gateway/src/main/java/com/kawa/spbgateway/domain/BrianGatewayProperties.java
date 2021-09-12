package com.kawa.spbgateway.domain;

import com.kawa.spbgateway.route.BrianRouteDefinition;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BrianGatewayProperties {
    private String url;
    private List<BrianRouteDefinition> routes =new ArrayList<>();
}

package com.kawa.spbgateway.route;

import lombok.Data;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Validated
@Data
public class BrianRouteDefinition extends RouteDefinition {
    private List<String> apiKeys = new ArrayList<>();
    private List<String> auths = new ArrayList<>();

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPredicates(), getFilters(), getUri(), getMetadata(), getOrder(),
                this.apiKeys, this.auths);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + getId() +
                ", uri=" + getUri() +
                ", predicates=" + getPredicates() +
                ", filters=" + getFilters() +
                ", metadata=" + getMetadata() +
                ", order=" + getOrder() +
                ", apiKeys=" + apiKeys +
                ", auths=" + auths +
                '}';
    }
}

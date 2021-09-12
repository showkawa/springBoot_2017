package com.kawa.spbgateway.property;


import org.springframework.core.env.PropertySource;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * RefreshRoutePropertySource
 * add a prefix for an existing property source
 */
public class RefreshRoutePropertySource extends PropertySource {

    private PropertySource innerPropertySource;
    private String prefix;

    public RefreshRoutePropertySource(String prefix, PropertySource origin) {
        super("RefreshRoutePropertySource-" + origin.getName());
        this.innerPropertySource = origin;
        this.prefix = prefix;
    }

    @Override
    public Object getProperty(String name) {
        Assert.notNull(name, "name can not be null!");
        var target = prefix + ".";
        if (name.startsWith(target)) {
            return innerPropertySource.getProperty(name.replace(target, ""));
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RefreshRoutePropertySource that = (RefreshRoutePropertySource) o;
        return innerPropertySource.equals(that.innerPropertySource) && prefix.equals(that.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), innerPropertySource, prefix);
    }
}

package com.kawa.script;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface PluginRegistry<T extends Plugin<S>, S> extends Iterable<T> {


    public static <S, T extends Plugin<S>> PluginRegistry<T, S> empty() {
        return of(Collections.emptyList());
    }

    @SafeVarargs
    public static <S, T extends Plugin<S>> PluginRegistry<T, S> of(T... plugins) {
        return of(Arrays.asList(plugins));
    }

    public static <S, T extends Plugin<S>> PluginRegistry<T, S> of(List<? extends T> plugins) {
        return of(plugins);
    }

    Optional<T> getPluginFor(S delimiter);

    List<T> getPluginsFor(S delimiter);

    int countPlugins();

    boolean contains(T plugin);

    boolean hasPluginFor(S delimiter);

    List<T> getPlugins();
}

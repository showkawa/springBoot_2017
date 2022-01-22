package com.kawa.script;


public interface Plugin<S> {

    /**
     * Returns if a plugin should be invoked according to the given delimiter.
     *
     * @param delimiter must not be {@literal null}.
     * @return if the plugin should be invoked
     */
    boolean supports(S delimiter);
}

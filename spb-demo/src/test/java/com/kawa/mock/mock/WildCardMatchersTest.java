package com.kawa.mock.mock;

import com.kawa.mock.BrianService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class WildCardMatchersTest {

    @Mock
    private BrianService brianService;


    @After
    public void destory() {
        reset(brianService);
    }

    @Test
    public void How_To_Use_WildCard_Matchers() {
        when(brianService.mt(anyInt(), anyString(), anyCollection(), isA(Serializable.class))).thenReturn("pass");
        String result = brianService.mt(0, "brian", Collections.emptyList(), "-345678987653456");
        assertThat(result, equalTo("pass"));
        result = brianService.mt(0, "kawa", Collections.emptyList(), "-9999999999999999999");
        assertThat(result, equalTo("pass"));
    }

    @Test
    public void How_To_Use_WildCard_Matchers2() {
        doNothing().when(brianService).mtVoid(anyInt(), anyString(), anyCollection(), isA(Serializable.class));
        brianService.mtVoid(0, "brian", Collections.emptyList(), "-345678987653456");
        verify(brianService, times(1)).mtVoid(0, "brian", Collections.emptyList(), "-345678987653456");
    }


    @Test
    public void How_To_Use_WildCard_Matchers_WithSpec() {
        when(brianService.mt(anyInt(), eq("brian"), anyCollection(), isA(Serializable.class))).thenReturn("pass");
        when(brianService.mt(anyInt(), eq("kawa"), anyCollection(), isA(Serializable.class))).thenReturn("block");
        String result = brianService.mt(0, "brian", Collections.emptyList(), "-345678987653456");
        assertThat(result, equalTo("pass"));
        result = brianService.mt(0, "kawa", Collections.emptyList(), "-9999999999999999999");
        assertThat(result, equalTo("block"));
    }


}








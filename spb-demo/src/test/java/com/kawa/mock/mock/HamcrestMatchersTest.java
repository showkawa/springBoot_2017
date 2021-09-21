package com.kawa.mock.mock;

import com.kawa.mock.BrianService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.reset;


@RunWith(MockitoJUnitRunner.class)
public class HamcrestMatchersTest {

    @Mock
    private BrianService brianService;


    @After
    public void destory() {
        reset(brianService);
    }

    @Test
    public void How_To_Use_Hamcrest_Matchers() {
        int r = 15;

        assertThat(r, equalTo(15));
        assertThat(r, not(equalTo(16)));
        assertThat(r, is(15));
        assertThat(r, is(not(17)));

        // or
        assertThat(r, either(equalTo(15)).or(equalTo(16)));

        // and
        assertThat(r, both(not(19)).and(not(20)));

        // any match
        assertThat(r, anyOf(is(15), is(16), is(17), not(18)));

        // all match
        assertThat(r, allOf(is(15), not(is(16)), not(equalTo(17)), not(18)));
    }


}








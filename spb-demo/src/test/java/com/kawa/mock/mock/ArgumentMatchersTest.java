package com.kawa.mock.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ArgumentMatchersTest {

    @Mock
    private Style style;

    @Test
    public void How_To_Use_Argument_Matchers() {
        // eq() isA() any()

        when(style.style(isA(Person.class))).thenReturn("ok");
        assertThat(style.style(new Brian1()), equalTo("ok"));
        assertThat(style.style(new Brian2()), equalTo("ok"));
        reset(style);

        when(style.style(any(Brian1.class))).thenReturn("yes");
        assertThat(style.style(new Brian2()), equalTo("yes"));
    }

    static class Style {
        String style(Person p) {
            return p.style();
        }
    }

    interface Person {
        String style();
    }

    class Brian1 implements Person {
        @Override
        public String style() {
            throw new RuntimeException();
        }
    }

    class Brian2 extends Brian1 {

        @Override
        public String style() {
            throw new RuntimeException();
        }
    }

}








package com.kawa.mock.mock;

import com.kawa.mock.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpyingTest {
    /**
     * Spy 可以部分Mock
     */
    @Spy
    private UserService userService;

    @Test
    public void How_To_Spy() {
        int brian = userService.getAge("brian");
        assertThat(brian, equalTo(18));

        when(userService.getAge("brian")).thenReturn(28);
        assertThat(userService.getAge("brian"), equalTo(28));
    }

}

package com.kawa.mock.mock;


import com.kawa.mock.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StubbingTest {

    @Mock
    private List<String> mList;

    @Mock
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void destory() {
        reset(this.mList);
    }

    @Test
    public void How_To_Stubbing() {
        when(mList.get(0)).thenReturn("brian");
        assertThat(mList.get(0), equalTo("brian"));

        // exception test
        when(mList.get(anyInt())).thenThrow(new RuntimeException());
        try {
            mList.get(9);
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(RuntimeException.class));
        }
    }

    @Test
    public void How_To_Stubbing_Void_Method() {
        doNothing().when(mList).clear();
        mList.clear();
        verify(mList, times(1)).clear();

        // exception test
        doThrow(RuntimeException.class).when(mList).clear();
        try {
            mList.clear();
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(RuntimeException.class));
        }
    }


    @Test
    public void How_To_Stubbing_DoReturn() {
        // when(mList.get(0)).thenReturn("brian");
        doReturn("brian").when(mList).get(0);
        assertThat(mList.get(0), equalTo("brian"));
    }

    @Test
    public void How_To_Iterate_Stubbing_DoReturn() {
        when(mList.size()).thenReturn(1, 2, 3, 4);

        assertThat(mList.size(), equalTo(1));
        assertThat(mList.size(), equalTo(2));
        assertThat(mList.size(), equalTo(3));
        assertThat(mList.size(), equalTo(4));

        assertThat(mList.size(), equalTo(4));
        assertThat(mList.size(), equalTo(4));
    }

    @Test
    public void How_To_Stubbing_With_Answer() {
        when(mList.get(anyInt())).thenAnswer((Answer<String>) invocation -> {
            Integer argument = invocation.getArgument(0);
            return String.valueOf(argument * 12);
        });

        assertThat(mList.get(0), equalTo("0"));
        assertThat(mList.get(1), equalTo("12"));
    }

    @Test
    public void How_To_Stubbing_With_RealCall() {
        System.out.println(userService.getClass());

        when(userService.getAge(anyString())).thenCallRealMethod();
        assertThat(userService.getAge("brian"), equalTo(18));
    }

}

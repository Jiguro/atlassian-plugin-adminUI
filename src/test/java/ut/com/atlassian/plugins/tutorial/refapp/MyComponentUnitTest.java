package ut.com.atlassian.plugins.tutorial.refapp;

import com.atlassian.plugins.tutorial.refapp.api.MyPluginComponent;
import com.atlassian.plugins.tutorial.refapp.impl.MyPluginComponentImpl;
import org.junit.Ignore;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest {

    @Test
    public void testMyName() {
        final MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }

    @Ignore
    @Test
    public void testAdd() {
        final MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("Result", 8, component.addNumbers(8, 8));
    }
}
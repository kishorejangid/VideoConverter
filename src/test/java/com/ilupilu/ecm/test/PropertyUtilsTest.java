package com.ilupilu.ecm.test;

import com.ilupilu.ecm.utils.PropertyUtils;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Kishore on 12-08-2014.
 */
public class PropertyUtilsTest {
    @Test
    public void getShouldReturnValue()
    {
        assertNotNull("Failed to get appName from app.properties.", PropertyUtils.get("appName"));
    }
}

package com.upreal.utils;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by Kyosukke on 15/01/2016.
 */
public class SoapGlobalManagerTest extends TestCase {

    private SoapGlobalManager gm;

    public void setUp() throws Exception {
        super.setUp();

        gm = new SoapGlobalManager();
    }

    public void tearDown() throws Exception {

    }

    public void testCallService() throws Exception {
        Object o;

        o = gm.callService(null, null);

        Assert.assertNull(o);
    }
}
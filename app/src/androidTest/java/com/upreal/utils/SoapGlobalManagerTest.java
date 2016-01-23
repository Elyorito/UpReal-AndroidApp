package com.upreal.utils;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Kyosukke on 15/01/2016.
 */
public class SoapGlobalManagerTest extends TestCase {

    private SoapGlobalManager gm;

    private final String FAKE_SERVICE = "fake_service";

    public void setUp() throws Exception {
        super.setUp();

        gm = new SoapGlobalManager();
    }

    public void tearDown() throws Exception {

    }

    public void testCallServiceWithNullParams() throws Exception {
        Object o;

        o = gm.callService(null, null);

        Assert.assertNull(o);
    }

    public void testCallServiceWithFakeParams() throws Exception {
        Object o;

        o = gm.callService(FAKE_SERVICE, new SoapObject());

        Assert.assertNull(o);
    }
}
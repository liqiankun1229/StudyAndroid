// IFileAidlInterface.aidl
package com.lqk.activity;

// Declare any non-default types here with import statements
/**
 *
 */
interface IFileAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString);

    void client2Server(in ParcelFileDescriptor pfd);

}
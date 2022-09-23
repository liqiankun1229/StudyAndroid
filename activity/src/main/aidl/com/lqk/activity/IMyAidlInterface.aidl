// IMyAidlInterface.aidl
package com.lqk.activity;

// Declare any non-default types here with import statements
import com.lqk.activity.bean.Person;

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString);

    void addPerson(in Person person);

    List<Person> getPersonList();

    void addBytes(in byte [] bytes);

}

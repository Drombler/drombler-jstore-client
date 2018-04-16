
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.jstore.client.sample.impl;

import org.drombler.jstore.client.sample.Sample;

/**
 * Here you would e.g. write to a file/ db, call a WebService ...
 *
 * @author puce
 */
public class SampleServiceClient {

    public void saveSample(Sample sample) {
        System.out.println("Save sample: " + sample.getName());
    }

    public Sample getSample(String name) {
        System.out.println("Get sample from service for name: " + name);
        return new Sample(name);
    }
}

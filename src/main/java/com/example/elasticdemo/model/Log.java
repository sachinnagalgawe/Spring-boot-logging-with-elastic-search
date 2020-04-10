/* ***************************************************************************
 * Copyright 2020 VMware, Inc.  All rights reserved.
 * -- VMware Confidential
 * ***************************************************************************
 * $Author$ $Id$ $DateTime$
 * ***************************************************************************/

package com.example.elasticdemo.model;

import org.springframework.data.annotation.Id;

/**
 * <code>Log</code> is model used for elastic logger Log <br>
 * 
 * @author Ambikeshwar Singh
 */
public class Log {

    private Metadata metadata;

    private Payload payload;

    /**
     * 
     */
    public Log() {
    }

    /**
     * @return the metadata
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * @return the payload
     */
    public Payload getPayload() {
        return payload;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * @param payload the payload to set
     */
    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    /**
     * @return String
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Log [metadata=" + metadata + ", payload=" + payload + "]";
    }

}

/* ***************************************************************************
 * Copyright 2020 VMware, Inc.  All rights reserved.
 * -- VMware Confidential
 * ***************************************************************************
 * $Author$ $Id$ $DateTime$
 * ***************************************************************************/

package com.example.elasticdemo.logging.model;

/** 
 * <code>ElasticLog</code> is model used for elastic logger ElasticLog <br>
 * 
 * @author Ambikeshwar Singh
 */
public class ElasticLog {

    private Metadata metadata;
    
    private Payload payload;

    /**
     * 
     */
    public ElasticLog() {}

    /**
     * @param metadata
     * @param payload
     */
    public ElasticLog(Metadata metadata, Payload payload) {
        super();
        this.metadata = metadata;
        this.payload = payload;
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

}

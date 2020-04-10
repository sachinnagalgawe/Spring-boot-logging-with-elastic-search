/* ***************************************************************************
 * Copyright 2020 VMware, Inc.  All rights reserved.
 * -- VMware Confidential
 * ***************************************************************************
 * $Author$ $Id$ $DateTime$
 * ***************************************************************************/

package com.example.elasticdemo.model;

/**
 * <code>Metadata</code> is model used to store the metadata for the log. <br>
 * 
 * @author Ambikeshwar Singh
 */
public class Metadata {

    private String payloadType;

    private String category;

    private String eventName;

    /**
     * 
     */
    public Metadata() {
    }

    /**
     * @return the payloadType
     */
    public String getPayloadType() {
        return payloadType;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return the eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @param payloadType the payloadType to set
     */
    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @param eventName the eventName to set
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * @return string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Metadata [payloadType=" + payloadType + ", category=" + category + ", eventName=" + eventName + "]";
    }
}

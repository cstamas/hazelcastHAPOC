package com.lambdazen.hazelcast;

import java.io.Serializable;

import com.hazelcast.core.PartitionAware;

public final class ServiceKey implements PartitionAware, Serializable {
	public final String serviceKey;
    public final String partitionKey;

    public ServiceKey(String serviceKey, String partitionKey) {
    	this.serviceKey = serviceKey;
        this.partitionKey = partitionKey;
    }

    @Override
    public Object getPartitionKey() {
        return partitionKey;
    }

	public String getServiceKey() {
		return serviceKey;
	}
}

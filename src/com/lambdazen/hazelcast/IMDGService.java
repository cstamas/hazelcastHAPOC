package com.lambdazen.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class IMDGService {	
	private static HazelcastInstance instance;
	
	public static void init(){		
		instance = Hazelcast.newHazelcastInstance();
	}
	
	public static HazelcastInstance getInstance(){
		return instance;
	}	
}

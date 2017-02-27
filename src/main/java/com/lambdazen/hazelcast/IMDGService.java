package com.lambdazen.hazelcast;

import java.util.List;
import java.util.stream.Collectors;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;
import com.hazelcast.core.Partition;
import com.hazelcast.core.PartitionService;

public class IMDGService {	
	private HazelcastInstance instance;
	public IMap<ServiceKey, String> serviceMap;
	public PartitionService pService;
	
	public IMDGService(){		
		instance = Hazelcast.newHazelcastInstance();
		
		serviceMap = instance.getMap("services");
		pService = instance.getPartitionService();
	}
	
	public HazelcastInstance getInstance(){
		return instance;
	}	
	
	public int addService(ServiceKey key, String value){
		serviceMap.put(key, value);
		return pService.getPartition(key).getPartitionId(); 
	}
	
	public String lookupService(ServiceKey key){
		if (serviceMap.containsKey(key)){
			Partition sPartition = pService.getPartition(key);
			Member owner = sPartition.getOwner();
			if (owner != null){
				return owner.getSocketAddress().toString() + ", partition:"+sPartition.getPartitionId();
			}
		}
		
		return null;
	}
	
	public List<String> listServices(){
		return serviceMap.localKeySet().stream().map(s -> s.getServiceKey()).collect(Collectors.toList());
	}
	
	public String info(){
		return instance.toString() + "partition:" + pService.getPartitions().stream().map(s -> s.getPartitionId()).collect(Collectors.toList());
	}
}

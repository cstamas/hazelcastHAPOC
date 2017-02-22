package com.lambdazen.hazelcast;

import java.util.stream.Collectors;

import com.hazelcast.core.MigrationEvent;
import com.hazelcast.core.MigrationListener;

public class ServiceMigrationListener implements MigrationListener {
	public void migrationStarted(MigrationEvent migrationEvent) {
		//System.out.println("migration started:"+migrationEvent);
		if (migrationEvent.getOldOwner().localMember()){
			int migratedPartitionId = migrationEvent.getPartitionId();
			System.out.println("partition " + migratedPartitionId + " has been moved out to the other member, the affected services are:");
			System.out.println(CommandExecutor.imdgService.serviceMap.localKeySet().stream().filter(s -> (CommandExecutor.imdgService.pService.getPartition(s).getPartitionId() == migratedPartitionId)).map(p -> p.getServiceKey()).collect(Collectors.toList()));
		}
	}

	public void migrationCompleted(MigrationEvent migrationEvent) {
	    //System.out.println("migration ended:"+migrationEvent);
		if (migrationEvent.getNewOwner().localMember()){
			int migratedPartitionId = migrationEvent.getPartitionId();
			System.out.println("partition " + migratedPartitionId + " has been moved into this member, the affected services are:");
			System.out.println(CommandExecutor.imdgService.serviceMap.localKeySet().stream().filter(s -> (CommandExecutor.imdgService.pService.getPartition(s).getPartitionId() == migratedPartitionId)).map(p -> p.getServiceKey()).collect(Collectors.toList()));
		}
	}
	
	public void migrationFailed(MigrationEvent migrationEvent){
		System.out.println("migration failed:"+migrationEvent);
	}
}

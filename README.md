# Overview

This is a proof-of-concept on how Hazelcast can be used to achieve high availability in a cluster of servers. The POC supports the notions of servers, services and partitions. The servers are simulated by command-line shells. The services are simply numbers assigned to a partition. 

Servers can hold one or more partitions and when one server fails, all its partitions are moved to other servers automatically. In the POC, there are only two partitions: one hosting odd-numbered services and the other hosting even-numbered services. 

In a real HA system, the services could be stateful such as in-memory databases or caches. The cluster supports lookup functionality allowing clients to access the appropriate server hosting the service. The automatic failover functionality allows the stateful services to restore themselves after a short downtime in case of server failure. 

# Usage

## Installation

You can build this POC by running these commands; 
```
mvn clean install
cd src/dist/bin
./hc_av_poc.sh
```

This launches a HA server with command-line options to manage "services" which, in the context of the PoC, are simply numbers assigned to the servers in the cluster. 

```
<bunch of Hazelcast messages>
Welcome to Hazelcast High Availability POC environment!

Please enter your command:
```

To set up the cluster, you can launch another shell window and run hv_av_poc. The two "servers" should now automatically connect with each other using Hazelcast. 

## info

With two windows launched, you can run the `info` command which should show something like: 

```
HazelcastInstance{name='_hzInstance_1_hchapoc', node=Address[192.168.1.10]:5701}partition:[0, 1]
```

The port number is the only thing different between the two instances in the cluster. 

## add

The `add` command lets you add numbered services to the servers. 

In either window, you can run these commands: 

```
add 1
add 2
add 3
add 4
add 5
```

## list

All odd numbered services are assigned to one server and even numbered services to the other. You can confirm this by running the `list` command on both shells.

One of the shells should show [1, 3, 5] and the other one [2, 4].

## exit and failover

You can simulate the shutdown of one of the servers in the cluster by running the `exit` command on the server with the odd-numbered servers. 

The other server should show a message like this after around 10 seconds: 
```
partition 1 has been moved into this member, the affected services are:
[5, 3, 1]
```

## failback to primary server

You can relaunch the second server to initiate the failback of one of the partitions. The servers should reconfigure themselves with the odd and even numbered services. 

## lookup

Clients can lookup the server hosting a service using the lookup command. For example `lookup 1` could return something like:
```
service 1 is hosted at /192.168.1.10:5702, partition:1
```

In a real HA implementation, clients can use the lookup functionality to identify the server where a services, such as an in-memory database or cache, is hosted. The client can then query that service remotely. 


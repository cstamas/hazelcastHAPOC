package com.lambdazen.hazelcast;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CommandExecutor {
        public static IMDGService imdgService = new IMDGService();

        public static String usage(){
                return "Valid commands: \n"+
                   " list - list all services deployed in this server \n"+
                           " add <service_key> - add a new service with service key in the server identified by the partition_key \n" +
                   " lookup <service_key> - find out which server hosts the service identified by the service key \n" +
                           " info - get the current instance information \n"+
                           " exit - exit the POC environment \n";
        }

        public static String getPartitionKey(String key){
                return (((key.hashCode()%2) == 0)?"partition1":"partition2");
        }

        public static void addService(String key){
                String partitionKey = getPartitionKey(key);
                ServiceKey serviceKey = new ServiceKey(key, partitionKey);
                String owner = imdgService.lookupService(serviceKey);
                if (owner == null){
                        int imdgPartitionKey = imdgService.addService(serviceKey, key);
                        System.out.println("service " + key + " has been sucessfully added with partition key:"+imdgPartitionKey);
                }else{
                        System.out.println("service " + key + " has already been hosted at " + owner);
                }
        }

        public static void listService(){
                List<String> localServices = imdgService.listServices();
                System.out.println("local server hosted services:"+localServices.toString());
        }

        public static void lookupService(String key){
                String partitionKey = getPartitionKey(key);
                ServiceKey serviceKey = new ServiceKey(key, partitionKey);
                String owner = imdgService.lookupService(serviceKey);
                if (owner == null){
                        System.out.println("service " + key + " has not been added yet, please use add command to add the service first");
                }else{
                        System.out.println("service " + key + " is hosted at " + owner);
                }
        }

        public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);
        try {
                Thread.sleep(3000);
                System.out.println("Welcome to hazelcast high availability POC environment!");
            while (true) {
                System.out.println("\nPlease enter your command:\n");
                String line = scanner.nextLine();
                if (line != null && line.trim().length() > 0){
                        line = line.trim();
                        String[] lineSplits = line.split("\\s+");
                        if ("help".equals(line)){
                                System.out.println(usage());
                        }else if (lineSplits[0].equals("add") && lineSplits.length == 2){
                                addService(lineSplits[1]);
                        }else if (lineSplits[0].equals("list") && lineSplits.length == 1){
                                listService();
                        }else if (lineSplits[0].equals("lookup") && lineSplits.length == 2){
                                lookupService(lineSplits[1]);
                        }else if (lineSplits[0].equals("info") && lineSplits.length == 1){
                                System.out.println(imdgService.info());
                        }else if (lineSplits[0].equals("exit") && lineSplits.length == 1){
                                System.out.println("Thanks for trying the POC!");
                                System.exit(0);
                        }
                }
            }
        } catch(Throwable th) {
            System.out.println("erro: "+th.getMessage());
            th.printStackTrace();
        } finally{
                scanner.close();
        }

        }

}

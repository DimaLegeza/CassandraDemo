package org.cassandrademo.example;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hom.EntityManagerImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: legezadm
 * @since: 21.07.13
 */
public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context-config.xml");
        CassandraHelper cassandraHelper = (CassandraHelper) applicationContext.getBean("cassandraHelper");
        Cluster cluster = (Cluster) applicationContext.getBean("cluster");
        Keyspace keyspace = (Keyspace) applicationContext.getBean("keyspace");
        EntityManagerImpl entityManager = (EntityManagerImpl) applicationContext.getBean("entityManager");
        System.out.println("established connection");
//        cassandraHelper.clusterPrepare(KEYSPACE, cluster);
        cassandraHelper.persistUser(entityManager);
        cassandraHelper.persistState(entityManager);
        cassandraHelper.retrieveState(entityManager);
        cassandraHelper.retrieveState(entityManager);
        cassandraHelper.retrieveUser(entityManager);
        cassandraHelper.getUserById(keyspace);
        cassandraHelper.mutateState(keyspace);
        cassandraHelper.sliceQuery(keyspace);

    }
}

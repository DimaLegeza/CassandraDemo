package org.cassandrademo.example.pojo;

import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hom.EntityManagerImpl;

/**
 * @author: legezadm
 * @since: 21.07.13
 */
public class EntityManagerProvider {

    private static EntityManagerImpl entityManager;

    public static EntityManagerImpl provide(Keyspace keyspace) {
        if (entityManager == null) {
            entityManager = new EntityManagerImpl(keyspace, "org.cassandrademo.example.pojo");
        }
        return entityManager;
    }
}

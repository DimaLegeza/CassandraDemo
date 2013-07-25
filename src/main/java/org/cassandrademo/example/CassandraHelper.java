package org.cassandrademo.example;

import me.prettyprint.cassandra.model.*;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHost;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;
import me.prettyprint.hom.EntityManagerImpl;
import org.cassandrademo.example.pojo.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: legezadm
 * @since: 21.07.13
 */
@Repository
public class CassandraHelper {

    public Cluster establishConnection(List<String> seedHosts, String clusterName) {
        Cluster cluster = null;
        for (String seedHost: seedHosts) {
            cluster = HFactory.getOrCreateCluster(clusterName, seedHost);
            if (cluster.getConnectionManager().getActivePools().size() == 0) {
                CassandraHost cassandraHost = new CassandraHost(seedHost);
                cluster.getConnectionManager().removeCassandraHost(cassandraHost);
            } else {
                break;
            }
        }
        return cluster;
    }

    public Keyspace retrieveKeySpace(String keySpace, Cluster cluster,
                                     ConfigurableConsistencyLevel consistencyLevel, Map<String, String> creds) {
        return HFactory.createKeyspace(keySpace, cluster, consistencyLevel, FailoverPolicy.FAIL_FAST, creds);
    }

    public void clusterPrepare(String keyspaceName, Cluster cluster) {
        StringSerializer stringSerializer = new StringSerializer();
        BasicColumnFamilyDefinition stateFamilyDefinition = new BasicColumnFamilyDefinition();
        stateFamilyDefinition.setKeyspaceName(keyspaceName);
        stateFamilyDefinition.setName("State");
        stateFamilyDefinition.setDefaultValidationClass(ComparatorType.UTF8TYPE.getClassName());
        stateFamilyDefinition.setComparatorType(ComparatorType.UTF8TYPE);
        stateFamilyDefinition.setKeyValidationClass(ComparatorType.UTF8TYPE.getClassName());
        BasicColumnDefinition stateColumnDefinition = new BasicColumnDefinition();
        stateColumnDefinition.setName(stringSerializer.toByteBuffer(StateEnum.STATE_NAME.getValue()));
        stateColumnDefinition.setValidationClass(ComparatorType.UTF8TYPE.getClassName());
        stateFamilyDefinition.addColumnDefinition(stateColumnDefinition);
        BasicColumnDefinition zipColumnDefinition = new BasicColumnDefinition();
        zipColumnDefinition.setName(stringSerializer.toByteBuffer(StateEnum.ZIP_CODE.getValue()));
        zipColumnDefinition.setValidationClass(ComparatorType.UTF8TYPE.getClassName());
        stateFamilyDefinition.addColumnDefinition(zipColumnDefinition);

        BasicColumnFamilyDefinition userFamilyDefinition = new BasicColumnFamilyDefinition();
        userFamilyDefinition.setKeyspaceName(keyspaceName);
        userFamilyDefinition.setName("User");
        userFamilyDefinition.setDefaultValidationClass(ComparatorType.UTF8TYPE.getClassName());
        userFamilyDefinition.setComparatorType(ComparatorType.UTF8TYPE);
        userFamilyDefinition.setKeyValidationClass(ComparatorType.UTF8TYPE.getClassName());
        BasicColumnDefinition nameColumnDefinition = new BasicColumnDefinition();
        nameColumnDefinition.setName(stringSerializer.toByteBuffer(UserEnum.USER_NAME.getValue()));
        nameColumnDefinition.setValidationClass(ComparatorType.UTF8TYPE.getClassName());
        userFamilyDefinition.addColumnDefinition(nameColumnDefinition);
        BasicColumnDefinition surnameColumnDefinition = new BasicColumnDefinition();
        surnameColumnDefinition.setName(stringSerializer.toByteBuffer(UserEnum.USER_SURNAME.getValue()));
        surnameColumnDefinition.setValidationClass(ComparatorType.UTF8TYPE.getClassName());
        userFamilyDefinition.addColumnDefinition(surnameColumnDefinition);
        BasicColumnDefinition phoneColumnDefinition = new BasicColumnDefinition();
        phoneColumnDefinition.setName(stringSerializer.toByteBuffer(UserEnum.USER_PHONE.getValue()));
        phoneColumnDefinition.setValidationClass(ComparatorType.UTF8TYPE.getClassName());
        userFamilyDefinition.addColumnDefinition(phoneColumnDefinition);

        BasicColumnFamilyDefinition userInStateFamilyDefinition = new BasicColumnFamilyDefinition();
        userInStateFamilyDefinition.setKeyspaceName(keyspaceName);
        userInStateFamilyDefinition.setName("UserState");
        userInStateFamilyDefinition.setDefaultValidationClass(ComparatorType.UTF8TYPE.getClassName());
        userInStateFamilyDefinition.setComparatorType(ComparatorType.UTF8TYPE);
        userInStateFamilyDefinition.setKeyValidationClass(ComparatorType.UTF8TYPE.getClassName());
        BasicColumnDefinition userIdColumnDefinition = new BasicColumnDefinition();
        userIdColumnDefinition.setName(stringSerializer.toByteBuffer(UserInStateEnum.USER_ID.getValue()));
        userIdColumnDefinition.setValidationClass(ComparatorType.UTF8TYPE.getClassName());
        userIdColumnDefinition.setIndexType(ColumnIndexType.KEYS);
        userInStateFamilyDefinition.addColumnDefinition(userIdColumnDefinition);
        BasicColumnDefinition stateIdColumnDefinition = new BasicColumnDefinition();
        stateIdColumnDefinition.setName(stringSerializer.toByteBuffer(UserInStateEnum.STATE_ID.getValue()));
        stateIdColumnDefinition.setValidationClass(ComparatorType.UTF8TYPE.getClassName());
        stateIdColumnDefinition.setIndexType(ColumnIndexType.KEYS);
        userInStateFamilyDefinition.addColumnDefinition(stateIdColumnDefinition);


        cluster.addColumnFamily(new ThriftCfDef(stateFamilyDefinition));
        cluster.addColumnFamily(new ThriftCfDef(userFamilyDefinition));
        cluster.addColumnFamily(new ThriftCfDef(userInStateFamilyDefinition));

    }
    //-----------------------------------------DATA PERSISTANCE WITH EM or WITH QUERY-----------------------------------
    public void persistUser(EntityManagerImpl entityManager) {
        User user = new User("2", "alex", "raccoon", "");
        entityManager.save(user);
    }

    public void mutateState(Keyspace keyspace) {
        Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
        HColumn<String, String> stateName = HFactory.createColumn("stateName", "DC", StringSerializer.get(), StringSerializer.get());
        HColumn<String, String> zipCode = HFactory.createColumn("zipCode", "05632", StringSerializer.get(), StringSerializer.get());
        mutator.addInsertion("5", "State", stateName);
        mutator.addInsertion("5", "State", zipCode);
        mutator.execute();
    }

    public void persistState(EntityManagerImpl entityManager) {
        State state = new State("4", "CO", "07641");
        entityManager.save(state);
    }

    //---------------------------------------------RETRIEVALS WITH ID OR WITH EM----------------------------------------
    public void retrieveUser(EntityManagerImpl entityManager) {
        User user = entityManager.load(User.class, "1");
        System.out.println(user.getName());
    }

    public void retrieveState(EntityManagerImpl entityManager) {
        State state = entityManager.load(State.class, "4");
        System.out.println(state.getStateName() + " - " + state.getZipCode());
    }

    public void getUserById(Keyspace keyspace) {
        String query = "Select * from State where key=%s";
        StringSerializer se = new StringSerializer();
        CqlQuery<String, String, String> cqlQuery = new CqlQuery<String, String, String>(keyspace, se, se, se);
        cqlQuery.setQuery(String.format(query,"5"));
        CqlRows<String, String, String> rows = cqlQuery.execute().get();
        for (Row<String, String, String> row: rows) {
            for (HColumn<String, String> column: row.getColumnSlice().getColumns()) {
                System.out.println(column.getName() + " - " + column.getValue());
            }
        }
    }

    //-------------------------------SLICERS (actually same stuff like CQL, but without one)----------------------------
    public void sliceQuery(Keyspace keyspace) {
        StringSerializer se = new StringSerializer();
        SliceQuery<String, String, String> sliceQuery = HFactory.createSliceQuery(keyspace, se, se, se);
        sliceQuery.setColumnFamily("State").setRange("", "", false, 5).setKey("5");
        for (HColumn<String, String> column: sliceQuery.execute().get().getColumns()) {
            System.out.println(column.getName() + " - " + column.getValue());
        }
    }

}

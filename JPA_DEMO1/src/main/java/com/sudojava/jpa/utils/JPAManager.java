package com.sudojava.jpa.utils;

import com.sudojava.jpa.domain.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Created by luoliwen on 17/4/18.
 */
public class JPAManager<T extends Person> {

    /**
     * 本类实例
     */
    private static JPAManager manager;
    /**
     * 实体管理类的工厂对象
     */
    private EntityManagerFactory factory;
    /**
     * 实体管理类
     */
    private EntityManager entityManager;
    /**
     * 事务处理
     */
    private EntityTransaction transaction;

    private JPAManager() {
        factory = Persistence.createEntityManagerFactory("jpa");
        entityManager = factory.createEntityManager();
    }

    /**
     * 采用单例模式提供JPA访问类的实例
     * @return
     */
    public static synchronized JPAManager getInstance() {
        if (manager == null) {
            manager = new JPAManager();
        }
        return manager;
    }

    /**
     * 关闭实体类和工厂类
     */
    public void close(){
        if (entityManager!=null){
            entityManager.close();
        }
        if (factory!=null){
            factory.close();
        }
    }


    /**
     * 保存 Java对象
     * @param t
     * @return
     * @throws RuntimeException
     */
    public T saveObject(T t) throws RuntimeException {

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            t = entityManager.contains(t) ? t : entityManager.merge(t);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("保存失败！");
        } finally {
            close();
        }
        return t;
    }


    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        Person person = new Person();
        person.setName("X");
        person.setAddress("XX");
        person.setAge(23);
        JPAManager.getInstance().saveObject(person);


    }
}

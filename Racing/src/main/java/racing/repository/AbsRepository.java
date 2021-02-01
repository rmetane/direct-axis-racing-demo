package racing.repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class AbsRepository<T> implements IRepository<T> {
	private Class<T> type;
	private static EntityManagerFactory emf;
	
	public static EntityManagerFactory getEntityManagerFactory() {
		if(emf == null || !emf.isOpen()) {
			emf = Persistence.createEntityManagerFactory("da.db.racing");
		}		
		return emf;
	}
	
	public void close() {
		if(emf != null && emf.isOpen()) {
			emf.close();
		}
	}
	
    public static EntityManager getEntityManager() {    	  	
    	return getEntityManagerFactory().createEntityManager();
    }

    @SuppressWarnings("unchecked")
    public AbsRepository() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public Optional<T> create(T t) {
        EntityManager entityManager = getEntityManager();
    	
		try {
			entityManager.getTransaction().begin();
	    	entityManager.persist(t);
	    	entityManager.getTransaction().commit();
	    	entityManager.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
		}
		finally {
			entityManager.close();
		}
		
		if(t == null) {
			return Optional.empty();
		}

		return Optional.of(t);
    }
    
    @Override
    public List<T> createAll(List<T> tList) {
        final EntityManager entityManager = getEntityManager();
    	
		try {
			entityManager.getTransaction().begin();
	    	tList.stream().forEach(t -> entityManager.persist(t));
	    	entityManager.getTransaction().commit();
	    	entityManager.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
		}
		finally {
			entityManager.close();
		}
		
		return tList;
    }

    @Override
    public Optional<T> delete(Object id) {
    	Optional<T> optionalT = find(id);
    	
    	if(!optionalT.isPresent()) {
    		return optionalT;
    	}
    	
    	EntityManager entityManager = getEntityManager();
    	
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(entityManager.merge(optionalT.get()));
	    	entityManager.getTransaction().commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
		}
		finally {
			entityManager.close();
		}
		
		return optionalT;
    }

    @Override
    public Optional<T> find(Object id) {
    	T t = null;
        EntityManager entityManager = getEntityManager();
    	
		try {
			entityManager.getTransaction().begin();
			t = getEntityManager().find(type, id);
	    	entityManager.getTransaction().commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();			
		}
		finally {
			entityManager.close();
		}
		
		if(t == null) {
			return Optional.empty();
		}

		return Optional.of(t);
    }

    @Override
    public Optional<T> update(T t) {
    	EntityManager entityManager = getEntityManager();
    	
		try {
			entityManager.getTransaction().begin();
			t = entityManager.merge(t);
	    	entityManager.getTransaction().commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
		}
		finally {
			entityManager.close();
		}
		
		if(t == null) {
			return Optional.empty();
		}

		return Optional.of(t);  
    }
    
	@Override
	public List<T> findAll() {
    	List<T> items;
    	EntityManager entityManager = getEntityManager();
    	
		try {
			String queryName = type.getSimpleName() + ".findAll";
			items = entityManager.createNamedQuery(queryName,type).getResultList();
		}
		catch (Exception e) {
			e.printStackTrace();
			items = new ArrayList<T>();
		}
		finally {
			entityManager.close();
		}
		
		return items;
	}
}
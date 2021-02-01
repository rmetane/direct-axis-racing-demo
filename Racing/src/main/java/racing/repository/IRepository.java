package racing.repository;

import java.io.Closeable;
import java.util.List;
import java.util.Optional;

public interface IRepository<T> extends Closeable {	
    Optional<T> create(T t);
    
    List<T> createAll(List<T> tList);

    Optional<T> delete(Object id);

    Optional<T> find(Object id);
    
    List<T> findAll();

    Optional<T> update(T t);   
}
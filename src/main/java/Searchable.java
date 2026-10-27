import java.sql.SQLException;
import java.util.List;

/** Common contract for classes that search records. */
public interface Searchable<T> {
    List<T> search(String keyword) throws SQLException;
}

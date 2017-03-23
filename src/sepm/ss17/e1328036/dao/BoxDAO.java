package sepm.ss17.e1328036.dao;

import sepm.ss17.e1328036.dto.Box;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by evgen on 18.03.2017.
 */
public interface BoxDAO {

    void save(Box box);
    Box search(int bid);
    void delete(int bid);
    void updatePrice(int bid, float newPrice);
    void addImages(int bid, List<FileInputStream> images);
    List<InputStream> getImages(int bid);
}

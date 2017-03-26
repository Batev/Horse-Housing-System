package sepm.ss17.e1328036.service;

import sepm.ss17.e1328036.dto.Box;

import java.util.List;

/**
 * Created by evgen on 23.03.2017.
 */
public interface Service {

    List<Box> getAllBoxes() throws ServiceException;
    List<Box> getBoxesByPrice(float priceFrom, float priceTo) throws ServiceException;
    List<Box> getBoxesBySize(float sizeFrom, float sizeTo) throws ServiceException;
    List<Box> getBoxesById(int bid) throws ServiceException;
    void updateBoxSize(int bid, float newSize) throws ServiceException;
    void updateBoxPrice(int bid, float newPrice) throws ServiceException;
    void deleteBox(int bid) throws ServiceException;
    void addBox(Box box) throws ServiceException;
}
package seng202.team0.repository;

import seng202.team0.exceptions.DuplicateEntryException;
import seng202.team0.models.Wine;

import java.util.List;

public class WineDAO implements DAOInterface<Wine> {
    @Override
    public List getAll() {
        return null;
    }

    @Override
    public Wine getOne(int id) {
        return null;
    }

    @Override
    public int add(Wine toAdd) throws DuplicateEntryException {
        return 0;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(Wine toUpdate) {

    }
}

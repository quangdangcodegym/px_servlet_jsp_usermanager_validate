package com.phuxuan.dao;

import java.sql.SQLException;
import java.util.List;

import com.phuxuan.model.Country;



public interface ICountryDAO {
	public void insertCountry(Country country) throws SQLException;

    public Country selectCountry(int id);

    public List<Country> selectAllCountry();

    public boolean deleteCountry(int id) throws SQLException;

    public boolean updateCountry(Country country) throws SQLException;

}

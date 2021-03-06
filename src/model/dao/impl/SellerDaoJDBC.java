package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		
		PreparedStatement st = null;


		try {
			st = conn.prepareStatement("INSERT INTO seller "
					+ "(Name,Email,BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1,obj.getName());
			st.setString(2,obj.getEmail());
			st.setDate(3,new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4,obj.getBaseSalary());
			st.setInt(5,obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected >0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
							obj.setId(id);
				}
				rs.close();
			}else {
				throw new DbException("Could not insert");
			}
			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;


		try {
			st = conn.prepareStatement("UPDATE seller "
					+ "SET Name = ?,Email=? ,BirthDate=?, BaseSalary=?, DepartmentId=? "
					+ "WHERE Id =?");
			st.setString(1,obj.getName());
			st.setString(2,obj.getEmail());
			st.setDate(3,new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4,obj.getBaseSalary());
			st.setInt(5,obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
				
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select seller.*, department.Name as Depname "
					+ "FROM seller INNER JOIN department ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller sell = instantiateSeller(rs,dep);
				return sell;
			}
			return null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		
		Seller obj = new Seller();
		new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		Department dep = new Department(rs.getInt("DepartmentId"),rs.getString("DepName"));
		return dep;
	}

	public List<Seller> findAll(){
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select seller.*, department.Name as Depname "
					+ "FROM seller INNER JOIN department ON seller.DepartmentId = department.Id "
					+ "ORDER By Name");
			
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			
			//Para nao duplicar os departamentos
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				//verifica se o departametno já está no map
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs,dep);
				list.add(obj);
				
			}
			return list;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new DbException(e.getMessage());
			}finally {
				DB.closeStatement(st);
				DB.closeResultSet(rs);
			}
		
	}
	
	public List<Seller> findByDepartment(Department department){
		
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select seller.*, department.Name as Depname "
					+ "FROM seller INNER JOIN department ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER By Name");
			
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			
			//Para nao duplicar os departamentos
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {
				
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				//verifica se o departametno já está no map
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instantiateSeller(rs,dep);
				list.add(obj);
				
			}
			return list;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new DbException(e.getMessage());
			}finally {
				DB.closeStatement(st);
				DB.closeResultSet(rs);
			}
	}

}

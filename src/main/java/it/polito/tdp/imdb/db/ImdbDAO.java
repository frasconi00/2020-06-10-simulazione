package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getGenres() {
		String sql="SELECT DISTINCT genre "
				+ "FROM movies_genres";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				result.add(res.getString("genre"));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void getVertici(String genre,Map<Integer, Actor> idMap) {
		String sql="SELECT * "
				+ "FROM actors a1 "
				+ "WHERE a1.id IN ( "
				+ "SELECT r.actor_id "
				+ "FROM movies m, roles r, movies_genres mg "
				+ "WHERE m.id=r.movie_id AND m.id=mg.movie_id "
				+ "AND mg.genre=? "
				+ ")";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genre);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				if(!idMap.containsKey(res.getInt("id"))) {
					
					Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
							res.getString("gender"));
					idMap.put(actor.getId(), actor);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Adiacenza> getAdiacenze(String genre, Map<Integer,Actor> idMap) {
		String sql="SELECT r1.actor_id, r2.actor_id, COUNT(DISTINCT m.id) AS peso "
				+ "FROM movies m, movies_genres mg, roles r1, roles r2 "
				+ "WHERE m.id=mg.movie_id AND m.id=r1.movie_id AND m.id=r2.movie_id "
				+ "AND r1.actor_id < r2.actor_id "
				+ "AND mg.genre=? "
				+ "GROUP BY r1.actor_id, r2.actor_id";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genre);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				
				Actor a1 = idMap.get(res.getInt("r1.actor_id"));
				Actor a2 = idMap.get(res.getInt("r2.actor_id"));
				int peso = res.getInt("peso");
				
				Adiacenza a = new Adiacenza(a1, a2, peso);
				
				result.add(a);
				
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	
}

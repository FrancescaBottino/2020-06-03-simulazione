package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public void listAllPlayers(Map<Integer, Player> idMap){
		
		String sql = "SELECT * FROM Players";
	
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(!idMap.containsKey(res.getInt("PlayerID"))) {
					
					Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
					idMap.put(player.getPlayerID(), player);
					
				}
				
				
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Player> getVertici(double numero, Map<Integer, Player> idMap){
		
		
		String sql= "SELECT PlayerID "
				+ "FROM Actions "
				+ "GROUP BY PlayerID "
				+ "HAVING AVG(Goals) > ? ";
		
		Connection conn = DBConnect.getConnection();
		List<Player> result = new ArrayList<Player>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, numero);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("PlayerID"))) {
					
					result.add(idMap.get(res.getInt("PlayerID")));
					
					
				}
				
				
			}
			conn.close();
			return result;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
	}
	
	public List<Adiacenza> getAllAdiacenze(Map<Integer, Player> idMap){
		
		
		String sql = "SELECT a1.PlayerID as p1, a2.PlayerID as p2, (SUM(a1.TimePlayed) - SUM(a2.TimePlayed)) as peso "
				+ "FROM Actions a1, Actions a2 "
				+ "WHERE a1.PlayerID > a2.PlayerID "
				+ "AND a1.TeamID != a2.TeamID  "
				+ "AND a1.starts = a2.starts "
				+ "AND a1.starts = ? "
				+ "AND a1.MatchID = a2.MatchID "
				+ "GROUP BY a1.PlayerID, a2.PlayerID ";
		
		List<Adiacenza> result = new ArrayList<>();
		
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, 1);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("p1")) && idMap.containsKey(res.getInt("p2"))) {
					
					
					result.add(new Adiacenza(idMap.get(res.getInt("p1")), idMap.get(res.getInt("p2")), res.getInt("peso")));
					
					
					
				}
				
				
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
	}
}

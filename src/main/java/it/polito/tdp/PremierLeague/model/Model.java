package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private List<Team> allNodes;
	private Graph<Team,DefaultWeightedEdge> graph;
	private Map<Team,Integer> classifica;
	
	
	public Model() {
		this.dao=new PremierLeagueDAO();
		this.allNodes=new ArrayList<>();
		this.classifica=new HashMap<>();
	}
	
	private void loadNodes() {
		if(this.allNodes.isEmpty()) {
			this.allNodes=this.dao.listAllTeams();
		}
	}
	
	public void buildGraph() {
		this.loadNodes();
		this.graph=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.graph,this.allNodes);
//		System.out.println("v: "+this.graph.vertexSet().size());
		this.getClassifica();
/*		for(Team t:this.classifica.keySet()) {
			System.out.println("\n"+this.classifica.get(t)); 
		} */
        for(Team t1:this.classifica.keySet()) {
        	for(Team t2:this.classifica.keySet()) {
            	if(this.classifica.get(t1)-this.classifica.get(t2)>0 && !this.graph.containsEdge(t1,t2)) {
            		Graphs.addEdge(this.graph,t1,t2,this.classifica.get(t1)-this.classifica.get(t2));
            	}
            }
        }
//      System.out.println("e: "+this.graph.edgeSet().size());
			
	}
	
	private void getClassifica(){
		List<ResultOfTeam> risultati= new ArrayList<>(this.dao.listAllResultOfTeamHome());
		risultati.addAll(this.dao.listAllResultOfTeamAway());
		for(ResultOfTeam c:risultati) {
			if(c.getScore()!=0) {
				c.setScore(c.getScore()*3);
			}else {
				c.setScore(1);
			}
		}
		int score=0;
		for(ResultOfTeam c1:risultati) {
			if(this.classifica.containsKey(c1.getTeam())==false) {
			  for(ResultOfTeam c2:risultati) {
				 if(c1.getTeam().getTeamID()==c2.getTeam().getTeamID()) {
					 score+=c2.getScore();
				}
				
			}
			  this.classifica.put(c1.getTeam(),score);
			  score=0;
		  }
		}
	}

	public Integer getVerticiSize() {
		return this.graph.vertexSet().size();
	}

	public Integer getArchiSize() {
		return this.graph.edgeSet().size();
	}
	
    public List<Team> getSquadre(){
    	return this.allNodes;
    }
    
    public List<ResultOfTeam> getPeggiori(Team t){
    	List<Team> successori=Graphs.successorListOf(this.graph,t);
    	List<ResultOfTeam> peggiori=new ArrayList<>();
    	for(Team team:successori) {
    		peggiori.add(new ResultOfTeam(team,this.classifica.get(t)-this.classifica.get(team)));
    	}
    	Collections.sort(peggiori);
    	return peggiori;
    }
	
    public List<ResultOfTeam> getMigliori(Team t){
    	List<Team> predecessori=Graphs.predecessorListOf(this.graph,t);
    	List<ResultOfTeam> migliori=new ArrayList<>();
    	for(Team team:predecessori) {
    		migliori.add(new ResultOfTeam(team,this.classifica.get(team)-this.classifica.get(t)));
    	}
    	Collections.sort(migliori);
    	return migliori;
    }
}
	
	
	
	
	
	
	
	
	


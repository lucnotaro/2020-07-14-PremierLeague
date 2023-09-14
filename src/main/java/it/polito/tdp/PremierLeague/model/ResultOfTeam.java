package it.polito.tdp.PremierLeague.model;

import java.util.Objects;

public class ResultOfTeam implements Comparable<ResultOfTeam> {
	
	private Team team;
	private Integer score;
	
	public ResultOfTeam(Team team, Integer score) {
		super();
		this.team = team;
		this.score = score;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return team + " (" + score+")";
	}
	@Override
	public int hashCode() {
		return Objects.hash(score, team);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResultOfTeam other = (ResultOfTeam) obj;
		return Objects.equals(score, other.score) && Objects.equals(team, other.team);
	}
	@Override
	public int compareTo(ResultOfTeam o) {
		return this.getScore()-o.getScore();
	} 
	
	
}

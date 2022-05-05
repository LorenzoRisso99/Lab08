package it.polito.tdp.extflightdelays.model;


import java.util.ArrayList;

import java.util.List;


import org.jgrapht.Graphs;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;


public class Model {
	
	ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
	SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	
	public void creaGrafo(int distanzaMedia) {
		
		List<Airport> airports = dao.loadAllAirports();
		
		Graphs.addAllVertices(this.grafo,airports);
		
		List<Rotta> l = dao.getRotte(distanzaMedia);

		for (Rotta rotta : l) {
			DefaultWeightedEdge edge = grafo.getEdge(rotta.getA1(), rotta.getA2());
			if (edge == null) {
				Graphs.addEdge(grafo, rotta.getA1(), rotta.getA2(), rotta.getWeight());
			} else {
				double peso = grafo.getEdgeWeight(edge);
				double newPeso = (peso + rotta.getWeight()) / 2;
				grafo.setEdgeWeight(edge, newPeso);
			}
		}
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public List<Rotta> getRotte() {
		List<Rotta> rotte = new ArrayList<Rotta>();
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			rotte.add(new Rotta(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), this.grafo.getEdgeWeight(e)));
		}
		return rotte;
	}

}

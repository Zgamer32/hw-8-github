package cs311.hw8.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph<V, E> implements IGraph<V, E> {
	
	/**
	 * Boolean flag for determining if the graph is directed
	 */
	//I decided to make the flag be for beign undirected as it prevents programmer error
	//as there is a larger change of me missing many ! in my if statements as opposed
	//to missing it once in the method for returning the flags state.
	private boolean undirected;
	
	/**
	 * List containing all vertices
	 */
	private ArrayList<IGraph.Vertex<V>> vertices= new ArrayList<IGraph.Vertex<V>>();
	
	/**
	 * List containing all edges
	 */
	private ArrayList<IGraph.Edge<E>> edges=new ArrayList<IGraph.Edge<E>>();

	/**
	 * Constructor that creates a directed or undirected graph with no edges or vertices
	 */
	public Graph(boolean directed) 
	{
		undirected=!directed;
	}

	/**
	 * Makes the graph directed. It is up to the user to remove all edges that would cause conflicts with the graph.
	 */
	@Override
	public void setDirectedGraph() 
	{
		undirected=false;
	}
	/**
	 * Makes the graph undirected. It will remove duplicate edges and in cases where the data doesn't match. It 
	 * sets the edge data to null. Older edges will remain while the younger ones will be the ones removed.
	 */
	@Override
	public void setUndirectedGraph() 
	{
		undirected=false;
		ArrayList<IGraph.Edge<E>> newEdges=new ArrayList<IGraph.Edge<E>>();
		//New list of edges with duplicated removed
		newEdges.add(edges.get(0));
		//For all edges
		for(int i=1;i<edges.size();i++)
		{
			boolean duplicateEdge=false;
			int addressOfDuplicate=0;
			//For all new edges
			for(int j=0;j<newEdges.size();j++)
			{
				//Check if duplicate
				if(edges.get(i).getVertexName2()==newEdges.get(j).getVertexName1()&&edges.get(i).getVertexName1()==newEdges.get(j).getVertexName2())
				{
					duplicateEdge=true;
					addressOfDuplicate=j;
				}
			}
			//If duplicate compare values
			if(duplicateEdge)
			{
				//if not equal set data to null and continue
				if(edges.get(i).getEdgeData()!=newEdges.get(addressOfDuplicate).getEdgeData())
				{
					newEdges.set(addressOfDuplicate, new cs311.hw8.graph.IGraph.Edge<E>(newEdges.get(addressOfDuplicate).getVertexName1(),newEdges.get(addressOfDuplicate).getVertexName2(),null));
				}
				//If above isn't run then they are equal so we just don't add the edge
			}
			//Then the edge isn't a duplicate and needs to be added
			newEdges.add(edges.get(i));
		}
	}
	/**
	 * Returns boolean value of if the graph is directed.
	 * 
	 * @return True if graph is directed.
	 */
	@Override
	public boolean isDirectedGraph() 
	{
		return !undirected;
	}
	
	/**
	 * Adds a vertex with null data and no edges. First checks if there is already
	 * a vertex with the same name.
	 * 
	 * @param the name of the vertex
	 * 
	 * @throws cs311.hw8.graph.IGraph.DuplicateVertexException
	 */
	@Override
	public void addVertex(String vertexName) throws cs311.hw8.graph.IGraph.DuplicateVertexException 
	{
		//If no data is given then add a vertex with null data
		//Why have duplicate code, just use the full method but with null being given.
		this.addVertex(vertexName, null);
	}
	
	/**
	 * Adds a vertex with vertexName and vertexData but no edges. Checks if there is a duplicate vertex by name.
	 * @param vertexName Name of new Vertex
	 * @param vertexData Data of new Vertex
	 * @throws cs311.hw8.graph.IGraph.DuplicateVertexException
	 */
	@Override
	public void addVertex(String vertexName, V vertexData) throws cs311.hw8.graph.IGraph.DuplicateVertexException 
	{
		//If you are able to get the vertex than the vertex exists already
		//I couldn't think of any other ways to check for vertices other than iterating through my list
		//So I figured why write it in multiple places. The get method contains the code for searching
		//and doesn't do anything other than return the vertex or null so it works well for a find method
		if(this.getVertex(vertexName)!=null) throw new cs311.hw8.graph.IGraph.DuplicateVertexException();
		vertices.add(new Vertex<V>(vertexName, vertexData));
		
	}

	/**
	 * Adds edge with null data between two vertices. Checks if there is an edge between them checking both
	 * orders for the vertices if the graph is undirected and (x,y)=(y,x). First checks that there are two 
	 * vertices with the given names.
	 * @param vertex1 First vertex to connect from.
	 * @param vertex2 Second vertex to connect from. This is where the direction head in a directed graph.
	 * @throws cs311.hw8.graph.IGraph.DuplicateEdgeException
	 * @throws cs311.hw8.graph.IGraph.NoSuchVertexException
	 */
	@Override
	public void addEdge(String vertex1, String vertex2) throws cs311.hw8.graph.IGraph.DuplicateEdgeException, cs311.hw8.graph.IGraph.NoSuchVertexException 
	{
		//Same reasons as for the dataless vertex method. It doesnt take any longer and
		//keeps line count down. Also reduces the chance that i forget to fix both methods should something change.
		this.addEdge(vertex1, vertex2, null);
	}
	
	/**
	 * Adds a edge between the two vertices going from vertex1 to vertex 2 with data edgeData. Checks first to see if the two
	 * vertices exist. Then checks if the edge already exists. From both directions if the graph is undirected. 
	 * @param vertex1	First vertex and origin of edge
	 * @param vertex2	Second vertex and destination of edge
	 * @param edgeData	Data of edge ie. weight
	 * @throws cs311.hw8.graph.IGraph.DuplicateEdgeException
	 * @throws cs311.hw8.graph.IGraph.NoSuchVertexException
	 */
	@Override
	public void addEdge(String vertex1, String vertex2, E edgeData) throws cs311.hw8.graph.IGraph.DuplicateEdgeException, cs311.hw8.graph.IGraph.NoSuchVertexException 
	{
		//As stated before, this method performs the search just as well and will reduce
		//the chance of error as the code is only in one place so any changes are all
		//within the same method. If I can get it from the get method it exists.
		if(this.getVertex(vertex1)==null) throw new cs311.hw8.graph.IGraph.NoSuchVertexException();
		if(this.getVertex(vertex2)==null) throw new cs311.hw8.graph.IGraph.NoSuchVertexException();		
		
		//Same principle as above except now with vertices. Funny story I had this check correct earlier than
		//the two above, where I called the get method but without checking for null. Why I didn't notice it
		//untill adding in more comments I don't know.
		if(this.getEdge(vertex1, vertex2)!=null) throw new cs311.hw8.graph.IGraph.DuplicateEdgeException();
		
		//If the graph isn't directed
		if(undirected)
		{
			//Perform an additional check for the vertexes in reverse
			if(this.getEdge(vertex2, vertex1)!=null) throw new cs311.hw8.graph.IGraph.DuplicateEdgeException();
		}
		//At this point if an exception hasn't been thrown then it is safe to add.
		edges.add(new Edge<E>(vertex1,vertex2,edgeData));
	}
	
	/**
	 * Returns type <V> data from vertex vertexName if vertex exists.
	 * @param vertexName Name of vertex
	 * @return	<V> data in vertex
	 * @throws cs311.hw8.graph.IGraph.NoSuchVertexException
	 */
	@Override
	public V getVertexData(String vertexName) throws cs311.hw8.graph.IGraph.NoSuchVertexException 
	{
		//Use the get method for the same reasons
		Vertex<V> vertex=this.getVertex(vertexName);
		
		//If the method returns null than the vertex doesn't exists
		if(vertex==null) throw new cs311.hw8.graph.IGraph.NoSuchVertexException();
		
		//If the exception was thrown than this wont be run. Otherise return the data.
		return vertex.getVertexData();
	}
	
	/**
	 * Changes data at given vertex by setting the index of the vertex containing the given name to 
	 * a new vertex with the new data.
	 * @param vertexName Name of vertex to change
	 * @param vertexData Data to change vertex to
	 * @throws cs311.hw8.graph.IGraph.NoSuchVertexException
	 */
	@Override
	public void setVertexData(String vertexName, V vertexData) throws cs311.hw8.graph.IGraph.NoSuchVertexException 
	{
		//In this case I put the code in two places, as for this method I needed the location of the vertex also
	
		//So I iterate through all the vertices
		for(int i=0;i<vertices.size();i++)
		{
			//and if the name is equal than this is the vertex as names must be unique
			if(vertexName==vertices.get(i).getVertexName())
			{
				//So I create a new vertex as was stated in the discussion board
				vertices.set(i, new Vertex<V>(vertexName,vertexData));
				
				//And then return to stop the loop and exit the method
				return;
			}
		}
		//If we make it here than the vertex was never found and the return statment not ran
		//So we throw the exception
		throw new cs311.hw8.graph.IGraph.NoSuchVertexException();
		
	}

	/**
	 * Returns the data at the edge going from vertex 1 to vertex 2 or  either direction if the graph is undirected.
	 * @param vertex1	Source Vertex
	 * @param vertex2	Destinatiopn vertex
	 * @return	Data of edge between vertices
	 * @throws cs311.hw8.graph.IGraph.NoSuchVertexException
	 * @throws cs311.hw8.graph.IGraph.NoSuchEdgeException
	 */
	@Override
	public E getEdgeData(String vertex1, String vertex2) throws cs311.hw8.graph.IGraph.NoSuchVertexException, cs311.hw8.graph.IGraph.NoSuchEdgeException 
	{
		//First we need to check if the vertices exist as the getEdgemethod wont tell us if it can't find
		//it due to missing vertices or if the edge simply doesn't exists
		if(this.getVertex(vertex1)==null||this.getVertex(vertex2)==null) throw new cs311.hw8.graph.IGraph.NoSuchVertexException();

		//Like so many methods before it. This too uses the get method
		//But we just do like we did with the get vertexData method but with edges
		//and an additional check at the top
		Edge<E> edge=this.getEdge(vertex1,vertex2);
		if(edge==null) throw new cs311.hw8.graph.IGraph.NoSuchEdgeException();
		return edge.getEdgeData();
	}

	/**
	 * Sets the data of the edge between the two vertices to the given data.
	 * @param vertex1 First vertex, source in directed graphs
	 * @param vertex2 Second vertex, destination in directed graphs
	 * @param edgeData New data for the edge
	 * @throws cs311.hw8.graph.IGraph.NoSuchVertexException
	 * @throws cs311.hw8.graph.IGraph.NoSuchEdgeException
	 */
	@Override
	public void setEdgeData(String vertex1, String vertex2, E edgeData)	throws cs311.hw8.graph.IGraph.NoSuchVertexException, cs311.hw8.graph.IGraph.NoSuchEdgeException 
	{
		//Check for valid vertex just like in the get edgeData method
		if(this.getVertex(vertex1)==null||this.getVertex(vertex2)==null) throw new cs311.hw8.graph.IGraph.NoSuchVertexException();
		
		//Just like in the setVertexData, we have to have the duplicate code here so
		//we can get the index of the edge
		
		//For each edge
		for(int i=0;i<edges.size();i++)
		{
			//If the vertex names are equal than this is the edge we want
			if(edges.get(i).getVertexName1()==vertex1 && edges.get(i).getVertexName2()==vertex2)
			{
				//So we set the edge at the address to a new edge with the vertices and data given
				//as stated in the discussion board. Then we return to exit the loop and method. 
				edges.set(i,new Edge<E>(vertex1,vertex2,edgeData));
				return;
			}
			//If we didn't find it above, and the graph is undirected, the edge might be listed as the other direction.
			if(undirected&&edges.get(i).getVertexName1()==vertex2 && edges.get(i).getVertexName2()==vertex1)
			{
				//If we did find it it as being listed going the other direction we need to change its data still
				//I decided that it would be best to preserve the direction of the edge despite
				//the graph being undirected in this case as it could affect something later.
				//We still return to exit the loop and the method
				edges.set(i,new Edge<E>(vertex2,vertex1,edgeData));
				return;
			}
		}
		
		//If we didn't exit the method yet than the edge wasn't found
		throw new cs311.hw8.graph.IGraph.NoSuchEdgeException();
	}

	/**
	 * returns the vertex of the given name if it exists in the graph
	 * returns null otherwise
	 * @param VertexName
	 * @return The verte of the given name, or null if it doens't exist.
	 */
	@Override
	public cs311.hw8.graph.IGraph.Vertex<V> getVertex(String VertexName) 
	{
		//I couldn't think of another way to find the vertex and since we weren't given any time
		//restrictions so I just iterated through the list.
		for(int i=0;i<vertices.size();i++)
		{
			//Since we were told that vertices must have unique names I just used ==
			//and since my list is vertices i can just return the vertex at the index.
			//This ease of finding and adding is why I went with an array list. It also
			//made it easy to perform the list returns for edges and vertices
			if(VertexName==vertices.get(i).getVertexName()) return vertices.get(i);
		}
		//If we did hit the return in the loop than the vertex isn't in the graph so I decided
		//to return null as this method doesn't throw any exceptions.
		return null;
	}
	
	/**
	 *
	 * @param vertexName1
	 * @param vertexName2
	 * @return Edge if it exists or null if it doesn't
	 */
	@Override
	public cs311.hw8.graph.IGraph.Edge<E> getEdge(String vertexName1, String vertexName2) 
	{
		//
		if(undirected)
		{
			for(int i=0;i<edges.size();i++)
			{
				if(vertexName1==edges.get(i).getVertexName1()&&vertexName2==edges.get(i).getVertexName2())
				{
					return edges.get(i);
				}
				if(vertexName2==edges.get(i).getVertexName1()&&vertexName1==edges.get(i).getVertexName2())
				{
					return edges.get(i);
				}
			}
		}
		else
		{
			for(int i=0;i<edges.size();i++)
			{
				if(vertexName1==edges.get(i).getVertexName1()&&vertexName2==edges.get(i).getVertexName2())
				{
					return edges.get(i);
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @return List of all vertices in the graph
	 */
	@Override
	public List<cs311.hw8.graph.IGraph.Vertex<V>> getVertices() {
		return vertices;
	}

	/**
	 * 
	 * @return List of all edges in the graph
	 */
	@Override
	public List<cs311.hw8.graph.IGraph.Edge<E>> getEdges() {
		return edges;
	}

	/**
	 * Finds all neighbors of the given vertex and returns them in a list
	 * @param vertex Vertex name to find
	 * @return List of all vertices that this vertex conneccts to.
	 */
	@Override
	public List<cs311.hw8.graph.IGraph.Vertex<V>> getNeighbors(String vertex) {
		//The list of neighbors to be returned
		List<cs311.hw8.graph.IGraph.Vertex<V>> neighbors=new ArrayList<cs311.hw8.graph.IGraph.Vertex<V>>();
		
		//For all the edges
		for(int i=0;i<edges.size();i++)
		{
			
			//Check if the edge starts from vertex and if so add the destination of the edge to the graph
			if(edges.get(i).getVertexName1()==vertex) neighbors.add(this.getVertex(edges.get(i).getVertexName2()));
			
			//If the graph is undirected we need to check it going the other direction as edge (a,b)= edge(b,a)
			if(undirected)if(edges.get(i).getVertexName2()==vertex) neighbors.add(this.getVertex(edges.get(i).getVertexName1()));
			
		}
		
		return neighbors;
	}

}

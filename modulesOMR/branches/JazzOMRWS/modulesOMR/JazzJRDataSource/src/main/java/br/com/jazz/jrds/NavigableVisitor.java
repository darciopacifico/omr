package br.com.jazz.jrds;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Inter
 * @author darciopa
 */
public interface NavigableVisitor {

	Iterator createIterator(JazzJRNavigableField jazzJRNavigableField, Collection coll);
	
	void setParameters(Map<String , Object> params);
	
}

laraImport("weaver.JoinPointsBase");
laraImport("lara._JavaTypes");

//import lara.Check;
//import clava.Clava;
//import clava._ClavaJavaTypes;



JoinPoints.prototype.toJoinPoint = function(node) {
	return _JavaTypes.getType("pt.up.fe.specs.smali.weaver.CxxJoinpoints").createFromLara(node);

/*
	Java.type("")
	var cxxJps = _ClavaJavaTypes.getCxxJoinPoints();
	return cxxJps.createFromLara(node);
	*/
}


/**
 * 
 * @return {$jp[]} the children of the given node
 */
JoinPoints.prototype._all_children = function($jp) {
	println("ASDASDASDSAD")
	return $jp.jpChildren;
}


/**
 * 
 * @return {$jp[]} the descendants of the given node
 */
JoinPoints.prototype._all_descendants = function($jp) {
	return $jp.jpDescendants;
}

/**
 * 
 * @return {$jp[]} all the nodes that are inside the scope of a given node
 */
JoinPoints.prototype._all_scope_nodes = function($jp) {
	throw "No scopes in Smali";
}

